package com.commerzi.app.route.actualRoute;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.customers.Coordinates;
import com.commerzi.app.customers.Customer;
import com.commerzi.app.route.actualRoute.maps.GPSRoute;
import com.commerzi.app.route.actualRoute.maps.Geometry;
import com.commerzi.app.route.plannedRoute.PlannedRoute;
import com.commerzi.app.route.visit.EVisitStatus;
import com.commerzi.app.route.visit.Visit;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";
    private MapView mapView;

    private TextView tvCustomerName;
    private TextView tvContactInfo;
    private Button btn_valider;
    private Button btn_passer;
    private List<Visit> visits;

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

        btn_valider.setOnClickListener(v -> validateVisit());
        btn_passer.setOnClickListener(v -> skipVisit());

        Log.d(TAG, "onCreate: ");
        Log.d(TAG, "MapView: " + mapView);

        Configuration.getInstance().setUserAgentValue(getPackageName());

        mapView = findViewById(R.id.mapview);

        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        GeoPoint francePoint = new GeoPoint(46.603354, 1.888334);  // Coordonnées géographiques de la France
        mapView.getController().setCenter(francePoint);
        mapView.getController().setZoom(7.0);

        Log.d(TAG, "onCreate: MapView set up");

        plannedRoute = (PlannedRoute) getIntent().getParcelableExtra("route");

        if (plannedRoute != null) {
            Toast.makeText(this, "Not null", Toast.LENGTH_SHORT).show();
            getActualRoute();
        } else {
            Toast.makeText(this, "Planned route is null", Toast.LENGTH_SHORT).show();
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
                    updateUserLocation(location);
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(@NonNull String provider) {}

                @Override
                public void onProviderDisabled(@NonNull String provider) {}
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);

        } else {
            Log.d(TAG, "onCreate: Permission not granted");
            // Request permission if not granted
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateVisit() {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        Visit current = visits.get(0);
        current.setStatus(EVisitStatus.VISITED);
//        communicator.updateVisit(current, new CommunicatorCallback<>(
//                response -> {
//                    visits.remove(0);
//                    displayVisit();
//                },
//                error -> {
//                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "validateVisit: " + error.message);
//                }
//        ));
    }

    private void skipVisit() {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        Visit current = visits.get(0);
        current.setStatus(EVisitStatus.SKIPPED);

//        communicator.updateVisit(current, new CommunicatorCallback<>(
//                response -> {
//                    visits.remove(0);
//                    displayVisit();
//                },
//                error -> {
//                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
//                    Log.e(TAG, "skipVisit: " + error.message);
//                }
//        ));
    }

    private void getActualRoute() {
        Communicator communicator = Communicator.getInstance(getApplicationContext());

        PlannedRoute clone = new PlannedRoute(plannedRoute.getName(), plannedRoute.getCustomersAndProspects());
        clone.setId(plannedRoute.getId());
        clone.setStartingPoint(plannedRoute.getStartingPoint());
        clone.setEndingPoint(plannedRoute.getEndingPoint());
        clone.setTotalDistance(plannedRoute.getTotalDistance());
        communicator.createActualRoute(clone, new CommunicatorCallback<>(
                    response -> {
                        routeAndGpsDtoData = response.actualRoute;
                        Log.d(TAG, "getActualRoute: " + routeAndGpsDtoData);
                        displayVisit();
                        displayRoute();
                    },
                    error -> {
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "getActualRoute: " + error.message);
                        Log.d(TAG, "getActualRoute: " + error.actualRoute);
                    }
                )
        );
    }

    private void displayVisit() {
        visits = routeAndGpsDtoData.getRoute().getVisits();

        Visit current;
        Customer customer;
        for (Visit visit : visits) {
            while (visit.getStatus() == EVisitStatus.VISITED
                    || visit.getStatus() == EVisitStatus.SKIPPED) {
                visits.remove(visit);
            }

            current = visit;
            customer = current.getCustomer();

            tvCustomerName.setText(
                    getString(R.string.prochaine_visite)
                    + " " + customer.getName()
                    + "\n" + customer.getType()
                    + "\n" + customer.getAddress()
                    + " - " + customer.getCity()
            );
            tvContactInfo.setText(customer.getContact().getCleanInfos());
        }


    }

    private void displayRoute() {
        ActualRoute route = routeAndGpsDtoData.getRoute();

        int visitNumber = route.getVisits().size();
        Visit visit;
        Coordinates coordinates;
        Customer customer;
        for (int i = 0; i < visitNumber; i++) {
            visit = route.getVisits().get(i);
            customer = visit.getCustomer();
            coordinates = route.getCoordinates().get(i);

            // Ajout d'un marqueur pour chaque visite
            Marker marker = new Marker(mapView);
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
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Reprise de la carte
        mapView.onResume();
    }

    private void updateUserLocation(Location location) {
        Log.d(TAG, "updateUserLocation: " + location.getLatitude() + ", " + location.getLongitude());
        if (userMarker != null) {
            mapView.getOverlays().remove(userMarker);
        }

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

        if (routeAndGpsDtoData != null) {
            // draw a line between visited clients and between the user and the last client visited
            List<GeoPoint> geoPoints = new ArrayList<>();
            for (Visit visit : routeAndGpsDtoData.getRoute().getVisits()) {
                Customer customer = visit.getCustomer();
                Coordinates coordinates = customer.getGpsCoordinates();
                geoPoints.add(new GeoPoint(coordinates.getLatitude(), coordinates.getLongitude()));
            }

            // Add the user's location to the list of points
            geoPoints.add(userGeoPoint);

            // Create a polyline from the list of points
            org.osmdroid.views.overlay.Polyline polyline = new org.osmdroid.views.overlay.Polyline();
            polyline.setPoints(geoPoints);
            mapView.getOverlayManager().add(polyline);

        }

        mapView.getOverlays().add(userMarker);
        mapView.invalidate(); // Redraw the map
    }

}
