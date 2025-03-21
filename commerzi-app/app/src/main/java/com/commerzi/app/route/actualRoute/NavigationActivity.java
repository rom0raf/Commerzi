package com.commerzi.app.route.actualRoute;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.ActualRouteNavigationResponse;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.customers.Coordinates;
import com.commerzi.app.customers.Customer;
import com.commerzi.app.dto.UpdateLocationDTO;
import com.commerzi.app.dto.UpdateVisitDTO;
import com.commerzi.app.route.plannedRoute.PlannedRoute;
import com.commerzi.app.route.utils.ERouteStatus;
import com.commerzi.app.route.visit.EVisitStatus;
import com.commerzi.app.route.visit.Visit;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";
    private MapView mapView;

    private TextView tvCustomerName;
    private TextView tvContactInfo;
    private Button btn_valider;
    private Button btn_passer;
    private Button btn_pause;
    private LinearLayout buttons;
    private List<Visit> visits;

    private int visitIndex = 0;

    private List<Coordinates> coordinatesToSend;

    private PlannedRoute plannedRoute;
    private RouteAndGpsDto routeAndGpsDtoData;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker userMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actual_route);

        mapView = findViewById(R.id.mapview);
        tvCustomerName = findViewById(R.id.tvCustomerNameNavigation);
        tvContactInfo = findViewById(R.id.tvContactInfoNavigation);
        btn_valider = findViewById(R.id.btn_valider);
        btn_passer = findViewById(R.id.btn_passer);
        btn_pause = findViewById(R.id.btn_pause);
        buttons = findViewById(R.id.buttons);


        btn_valider.setOnClickListener(v -> validateVisit());
        btn_passer.setOnClickListener(v -> skipVisit());
        btn_pause.setOnLongClickListener(v -> {
            pauseRoute(routeAndGpsDtoData.getRoute());
            return true;
        });

        coordinatesToSend = new ArrayList<>();

        Configuration.getInstance().setUserAgentValue(getPackageName());

        mapView = findViewById(R.id.mapview);

        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        GeoPoint francePoint = new GeoPoint(46.603354, 1.888334);  // Coordonnées géographiques de la France
        mapView.getController().setCenter(francePoint);
        mapView.getController().setZoom(7.0);

        Log.d(TAG, "onCreate: MapView set up");

        Intent routeIntent = getIntent();

        Parcelable routeParcelable = routeIntent.getParcelableExtra("route");

        if (routeParcelable instanceof PlannedRoute) {
            System.out.println("PlannedRoute reçue");
            plannedRoute = (PlannedRoute) routeParcelable;
            handlePlannedRoute();
        } else if (routeParcelable instanceof ActualRoute) {
            System.out.println("ActualRoute reçue");
            ActualRoute actualRoute = (ActualRoute) routeParcelable;
            routeAndGpsDtoData = new RouteAndGpsDto(actualRoute, null);
            handleActualRoute(actualRoute);
        } else {
            Toast.makeText(this, "Aucune route valide reçue", Toast.LENGTH_SHORT).show();
            finish();
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }


        // Check permissions and request location updates
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: Permission granted");
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.d(TAG, "Location: " + location.getLatitude() + ", " + location.getLongitude() + " Accuracy: " + location.getAccuracy());

                    if (routeAndGpsDtoData == null || routeAndGpsDtoData.getRoute() == null) {
                        return;
                    }

                    if (routeAndGpsDtoData.getRoute().getStatus() != ERouteStatus.COMPLETED) {
                        updateUserLocation(location);
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);

        } else {
            Log.d(TAG, "onCreate: Permission not granted");
            // Request permission if not granted
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePlannedRoute() {
        if (routeAndGpsDtoData != null && routeAndGpsDtoData.getRoute().getStatus() == ERouteStatus.IN_PROGRESS) {
            getActualRouteById(routeAndGpsDtoData.getRoute().getId());
        } else {
            getActualRoute();
        }
    }

    private void handleActualRoute(ActualRoute actualRoute) {
        visits = actualRoute.getVisits();
        displayVisit();
        System.out.println("ActualRoute reçue");
        System.out.println(actualRoute);
        displayRoute(actualRoute);
    }

    private void validateVisit() {

        Communicator communicator = Communicator.getInstance(getApplicationContext());

        System.out.println("Route: " + routeAndGpsDtoData);

        communicator.confirmVisit(routeAndGpsDtoData.getRoute(), new CommunicatorCallback<ActualRouteNavigationResponse>(
                response -> {
                    ActualRoute route = response.actualRoute.getRoute();
                    routeAndGpsDtoData.setRoute(route);
                    visits = route.getVisits();
                    visitIndex++;
                    Toast.makeText(this, "Visite validée", Toast.LENGTH_SHORT).show();
                    displayVisit();
                },
                error -> {
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "validateVisit: " + error.message);
                }
        ));
    }

    private void skipVisit() {
        Communicator communicator = Communicator.getInstance(getApplicationContext());

        UpdateVisitDTO updateVisitDTO = new UpdateVisitDTO(routeAndGpsDtoData.getRoute(), visitIndex, EVisitStatus.SKIPPED);

        communicator.skipVisit(routeAndGpsDtoData.getRoute(), new CommunicatorCallback<ActualRouteNavigationResponse>(
                response -> {
                    ActualRoute route = response.actualRoute.getRoute();
                    routeAndGpsDtoData.setRoute(route);
                    visits = route.getVisits();
                    visitIndex++;
                    Toast.makeText(this, "Visite passée", Toast.LENGTH_SHORT).show();
                    displayVisit();
                },
                error -> {
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "skipVisit: " + error.message);
                }
        ));
    }

    private void pauseRoute(ActualRoute route) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());

        communicator.pauseRoute(route, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(this, "Itinéraire mis en pause", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "pauseRoute: " + error.message);
                }
        ));
    }

    private void getActualRouteById(String actualRouteId) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());

        communicator.getActualRouteById(actualRouteId, new CommunicatorCallback<>(
                response -> {
                    routeAndGpsDtoData = response.actualRoute;
                    System.out.println("Route reçue");
                    Log.d(TAG, "getActualRouteById: " + routeAndGpsDtoData);
                    visitIndex = getVisitIndex(routeAndGpsDtoData.getRoute().getVisits());
                    visits = routeAndGpsDtoData.getRoute().getVisits();
                    displayVisit();
                    ActualRoute route = routeAndGpsDtoData.getRoute();
                    btn_pause.setOnLongClickListener(v -> {
                        pauseRoute(route);
                        return true;
                    });
                    displayRoute(route);
                },
                error -> {
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "getActualRouteById: " + error.message);
                    Log.d(TAG, "getActualRouteById: " + error.actualRoute);
                    finish();
                }
        ));
    }

    private void getActualRoute() {
        Communicator communicator = Communicator.getInstance(getApplicationContext());

        PlannedRoute clone = new PlannedRoute(plannedRoute.getName(), plannedRoute.getCustomers());
        clone.setId(plannedRoute.getId());
        clone.setStartingPoint(plannedRoute.getStartingPoint());
        clone.setEndingPoint(plannedRoute.getEndingPoint());
        clone.setTotalDistance(plannedRoute.getTotalDistance());
        communicator.createActualRoute(clone, new CommunicatorCallback<>(
                response -> {
                    routeAndGpsDtoData = response.actualRoute;
                    Log.d(TAG, "getActualRoute: " + routeAndGpsDtoData);
                    visitIndex = getVisitIndex(routeAndGpsDtoData.getRoute().getVisits());
                    visits = routeAndGpsDtoData.getRoute().getVisits();
                    displayVisit();
                    ActualRoute route = routeAndGpsDtoData.getRoute();
                    btn_pause.setOnLongClickListener(v -> {
                        pauseRoute(route);
                        return true;
                    });
                    displayRoute(route);
                },
                error -> {
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "getActualRoute: " + error.message);
                    Log.d(TAG, "getActualRoute: " + error.actualRoute);
                }
        ));
    }

    private int getVisitIndex(List<Visit> visits) {
        for (int i = 0; i < visits.size(); i++) {
            if (visits.get(i).getStatus() == EVisitStatus.NOT_VISITED) {
                System.out.println("Visit index: " + i);
                return i;
            }
        }
        return -1;
    }

    private void displayVisit() {
        visits.forEach(System.out::println);

        visitIndex = getVisitIndex(visits);

        Visit current;
        Customer customer;

        for (int i = 0; i < visits.size(); i++) {
            if (i == visitIndex) {
                current = visits.get(i);
                customer = current.getCustomer();
                tvCustomerName.setText(customer.getName());
                tvContactInfo.setText(
                        customer.getType()
                                + "\n" +
                        customer.getAddress() + " - " + customer.getCity()
                                + "\n" +
                                customer.getContact().getCleanInfos()
                );
                break;
            }
        }

        if (visitIndex == visits.size()) {
            routeAndGpsDtoData.getRoute().setStatus(ERouteStatus.COMPLETED);
        }

        if (routeAndGpsDtoData != null && routeAndGpsDtoData.getRoute().getStatus() == ERouteStatus.COMPLETED) {
            tvCustomerName.setText("Toutes les visites ont été effectuées");
            tvContactInfo.setText("");

            // bouton pour terminer la route, revenir à la liste des routes
            Button btnTerminer = new Button(this);
            btnTerminer.setText("Terminer le parcours");

            btnTerminer.setOnLongClickListener(v -> {
                Communicator communicator = Communicator.getInstance(getApplicationContext());
                communicator.finishRoute(routeAndGpsDtoData.getRoute(), new CommunicatorCallback<>(
                        response -> {
                            Toast.makeText(this, "Parcours terminé", Toast.LENGTH_SHORT).show();
                        },
                        error -> {
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "finishRoute: " + error.message);
                        }
                ));

                finish();
                return true;
            });

            buttons.removeView(btn_valider);
            buttons.removeView(btn_passer);
            buttons.removeView(btn_pause);

            buttons.addView(btnTerminer);

            sendNotification("Parcours terminé", "Toutes les visites ont été effectuées.");

        }

    }

    private void displayRoute(ActualRoute route) {

        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(route.getCoordinates().get(0).getLatitude(), route.getCoordinates().get(0).getLongitude()));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(getString(R.string.depart));
        marker.setIcon(getResources().getDrawable(R.drawable.ic_home_scaled, null));
        mapView.getOverlays().add(marker);


        int visitNumber = route.getVisits().size();
        Visit visit;
        Coordinates coordinates;
        Customer customer;
        for (int i = 0; i < visitNumber; i++) {
            visit = route.getVisits().get(i);
            customer = visit.getCustomer();
            coordinates = customer.getGpsCoordinates();

            // Ajout d'un marqueur pour chaque visite
            marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(coordinates.getLatitude(), coordinates.getLongitude()));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(customer.getName() + "\n" + customer.getType()
                    + "\n" + customer.getAddress() + " - " + customer.getCity()
            );
            mapView.getOverlays().add(marker);

            Log.d(TAG, "displayRoute: " + customer.getName());
        }

        Log.d(TAG, "displayRoute: added");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        mapView.onPause();

        if (routeAndGpsDtoData == null || routeAndGpsDtoData.getRoute() == null) {
            return;
        }

        if (routeAndGpsDtoData.getRoute().getStatus() == ERouteStatus.COMPLETED) {
            return;
        }

        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.pauseRoute(routeAndGpsDtoData.getRoute(), new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(this, R.string.paused, Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "pauseRoute: " + error.message);
                }
        ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reprise de la carte
        mapView.onResume();

        if (routeAndGpsDtoData == null || routeAndGpsDtoData.getRoute() == null) {
            return;
        }

        if (routeAndGpsDtoData.getRoute().getStatus() == ERouteStatus.COMPLETED) {
            return;
        }

        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.resumeRoute(routeAndGpsDtoData.getRoute(), new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(this, R.string.reprise, Toast.LENGTH_SHORT).show();
                    routeAndGpsDtoData.setRoute(response.routes.get(0));
                    displayRoute(routeAndGpsDtoData.getRoute());
                    displayVisit();
                    displayItinerary(routeAndGpsDtoData.getRoute());
                },
                error -> {
                    Toast.makeText(this, "Erreur: " + error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void displayItinerary(ActualRoute route) {
        if (route == null || route.getCoordinates() == null || route.getCoordinates().isEmpty()) {
            return;
        }

        Polyline polyline = new Polyline();
        polyline.setTitle("Itinerary");

        for (Coordinates coordinates : route.getCoordinates()) {
            GeoPoint geoPoint = new GeoPoint(coordinates.getLatitude(), coordinates.getLongitude());
            polyline.addPoint(geoPoint);
        }

        mapView.getOverlays().add(polyline);
        mapView.invalidate(); // Redraw the map
    }

    private void updateUserLocation(Location location) {
        Log.d(TAG, "updateUserLocation: " + location.getLatitude() + ", " + location.getLongitude());
        if (userMarker != null) {
            mapView.getOverlays().remove(userMarker);
        }

        coordinatesToSend.add(new Coordinates(location.getLatitude(), location.getLongitude()));

        if (routeAndGpsDtoData != null) {
            routeAndGpsDtoData.getRoute().getCoordinates().addAll(coordinatesToSend);
        }

        if (routeAndGpsDtoData!= null) {
            displayItinerary(routeAndGpsDtoData.getRoute());
        }

        sendUserLocation();

        // Create a new marker at the user's location
        GeoPoint userGeoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        userMarker = new Marker(mapView);
        userMarker.setPosition(userGeoPoint);
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        // Set a custom color for the marker
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.user_marker); // Use a custom icon if you like

        if (drawable != null) {
            userMarker.setIcon(drawable);
        } else {
            userMarker.setIcon(getResources().getDrawable(R.drawable.user_marker, null)); // A default icon
        }

        if (location.hasAccuracy()) {
            userMarker.setSubDescription("Accuracy: " + location.getAccuracy() + "m");
        }

        mapView.getOverlays().add(userMarker);
        mapView.invalidate(); // Redraw the map
    }

    private void sendUserLocation() {
        if (routeAndGpsDtoData == null) {
            return;
        }

        String routeId = routeAndGpsDtoData.getRoute().getId();
        UpdateLocationDTO updateLocationDTO = new UpdateLocationDTO(routeId, coordinatesToSend);

        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.sendUserLocation(updateLocationDTO, new CommunicatorCallback<>(
                response -> {
                    coordinatesToSend.clear();

                    displayNearbyCustomers(response.customers);

                },
                error -> {
                    Toast.makeText(this, "message: " + error.message, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "sendUserLocation: " + error.message);
                }
        ));
    }

    private void displayNearbyCustomers(List<Customer> customers) {
        // display customers that are not in the route (visits list)
        // display a marker for each customer with a different icon
        // display a notification for each customer

        for (Customer customer : customers) {
            if (visits.stream().anyMatch(visit -> visit.getCustomer().getId().equals(customer.getId()))) {
                continue;
            }

            Coordinates coordinates = customer.getGpsCoordinates();
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(coordinates.getLatitude(), coordinates.getLongitude()));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getResources().getDrawable(R.drawable.customer, null));
            marker.setTitle(customer.getName() + "\n" + customer.getType()
                    + "\n" + customer.getAddress() + " - " + customer.getCity()
            );
            mapView.getOverlays().add(marker);

            sendNotification(
                    getString(R.string.proximite),
                    getString(R.string.proche) + customer.getContact().getCleanInfos()
            );
        }

    }

    private void sendNotification(String title, String message) {
        String channelId = "route_completion_channel";
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Route Completion";
            String description = "Notifications for route completion";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_route)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // notificationId is a unique int for each notification that you must define
        int notificationId = 1;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

}
