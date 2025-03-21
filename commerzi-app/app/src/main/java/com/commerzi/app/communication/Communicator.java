package com.commerzi.app.communication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.commerzi.app.auth.LoginActivity;
import com.commerzi.app.communication.responses.ActualRouteNavigationResponse;
import com.commerzi.app.communication.responses.ActualRouteResponse;
import com.commerzi.app.communication.responses.NearbyCustomersResponse;
import com.commerzi.app.communication.responses.PlannedRouteResponse;
import com.commerzi.app.customers.Customer;
import com.commerzi.app.R;
import com.commerzi.app.auth.User;
import com.commerzi.app.communication.responses.AuthResponse;
import com.commerzi.app.communication.responses.CustomerResponse;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.communication.responses.GenericMessageResponse;
import com.commerzi.app.dto.NearbyCustomersDTO;
import com.commerzi.app.dto.UpdateLocationDTO;
import com.commerzi.app.route.actualRoute.ActualRoute;
import com.commerzi.app.route.actualRoute.RouteAndGpsDto;
import com.commerzi.app.route.plannedRoute.PlannedRoute;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Communicator {

    private Context context;
    private RequestQueue queue;
    private CommunicatorProperties properties;
    private String sessionToken;

    private static Communicator instance;

    public static Communicator getInstance(Context context) {
        if (instance == null) {
            instance = new Communicator(context);
        } else {
            instance.updateContext(context);
        }
        return instance;
    }

    private Communicator(Context context) {
        this.updateContext(context);
        try {
            this.loadProperties();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void updateContext(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    private void loadProperties() throws IOException {
        this.properties = new CommunicatorProperties(this.context);
    }

    private String getBaseUrl() {
        return this.properties.getString(CommunicatorProperties.BASE_KEY) + ":" +
               this.properties.getString(CommunicatorProperties.PORT);
    }

    private String buildAuthFullUrl() {
        return this.getBaseUrl() + this.properties.getString(CommunicatorProperties.AUTH_URL);
    }

    private String buildUserFullUrl() {
        return this.getBaseUrl() + this.properties.getString(CommunicatorProperties.USER_URL);
    }

    private String buildCustomersBaseUrl() {
        return this.getBaseUrl() + this.properties.getString(CommunicatorProperties.CUSTOMER_URL);
    }

    private String buildCustomersUrlById(String id) {
        return this.buildCustomersBaseUrl() + id;
    }

    private String buildPlannedRoutesBaseUrl() {
        return this.getBaseUrl() + this.properties.getString(CommunicatorProperties.PLANNED_ROUTE_URL);
    }

    private String buildActualRoutesbaseUrl() {
        return this.getBaseUrl() + this.properties.getString(CommunicatorProperties.ACTUAL_ROUTE_URL);
    }

    private void request(int method, String url, JSONObject requestBody, boolean authenticated,
                         Response.Listener<String> listener,
                         @Nullable Response.ErrorListener errorListener) {
        // This is needed because "this" in getHeaders() is
        // the StringRequest "this" and not the actual Communicator
        String token = this.sessionToken;
        int timeout = 10_000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                timeout,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );

        Response.ErrorListener errorListenerWithUnauthorized = error -> {
            if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                this.handleUnauthorizedResponse();
            } else if (errorListener != null) {
                errorListener.onErrorResponse(error);
            }
        };


        StringRequest stringRequest = new StringRequest(method, url, listener, errorListenerWithUnauthorized) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                if (requestBody != null) {
                    return requestBody.toString().getBytes();
                }
                return super.getBody();
            }

            @Override
            public Response<String> parseNetworkResponse(NetworkResponse response) {
                return Response.success(new String(new String(response.data).getBytes(StandardCharsets.UTF_8)), HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public String getBodyContentType() {
                if (requestBody != null) {
                    return "application/json; charset=utf-8";
                }
                return super.getBodyContentType();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();

                if (requestBody != null) {
                    headers.put("Content-Type", "application/json");
                }

                if (authenticated) {
                    headers.put("X-Commerzi-Auth", token);
                }

                return headers;
            }
        };

        stringRequest.setRetryPolicy(retryPolicy);

        this.queue.add(stringRequest);
    }

    private void handleUnauthorizedResponse() {
        Toast.makeText(context, context.getString(R.string.session_expired), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void login(String email, String password, CommunicatorCallback<AuthResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", email);
            requestBody.put("password", password);

            this.request(Request.Method.POST, this.buildAuthFullUrl(), requestBody, false,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = !jsonResponse.getString("userId").isEmpty();

                        if (success) {
                            this.sessionToken = jsonResponse.getString("session");
                            User user = User.fromJSONObject(jsonResponse);

                            AuthResponse authResponse = new AuthResponse("", user);
                            callback.onSuccess(authResponse);
                        } else {
                            AuthResponse authResponse = new AuthResponse(context.getString(R.string.credentials_error), null);
                            callback.onFailure(authResponse);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AuthResponse authResponse = new AuthResponse(context.getString(R.string.unexpected_error), null);
                        callback.onFailure(authResponse);
                    }
                },
                error -> {
                    AuthResponse authResponse = new AuthResponse(context.getString(R.string.login_error_message), null);
                    callback.onFailure(authResponse);
                }
            );
        } catch (Exception e) {
            AuthResponse authResponse = new AuthResponse(context.getString(R.string.request_creation_error), null);
            callback.onFailure(authResponse);
        }
    }

    public void signup(User user, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", user.getEmail());
            requestBody.put("password", user.getPassword());
            requestBody.put("firstName", user.getFirstName());
            requestBody.put("lastName", user.getLastName());
            requestBody.put("address", user.getAddress());
            requestBody.put("city", user.getCity());

            this.request(Request.Method.POST, this.buildUserFullUrl(), requestBody, false,
                response -> callback.onSuccess(
                    new GenericMessageResponse(
                        context.getString(R.string.account_creation_success)
                    )
                ),
                error -> {
                    GenericMessageResponse signupResponse;
                    if (error != null && error.networkResponse != null) {
                        signupResponse = new GenericMessageResponse(new String(error.networkResponse.data));
                    } else {
                        signupResponse = new GenericMessageResponse(context.getString(R.string.unexpected_error));
                    }
                    callback.onFailure(signupResponse);
                }
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }

    public void createCustomer(Customer customer, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", customer.getName());
            jsonBody.put("address", customer.getAddress());
            jsonBody.put("city", customer.getCity());
            jsonBody.put("description", customer.getDescription());
            jsonBody.put("type", customer.getType());

            JSONObject contact = new JSONObject();
            contact.put("firstName", customer.getContact().getFirstName());
            contact.put("lastName", customer.getContact().getLastName());
            contact.put("phoneNumber", customer.getContact().getPhoneNumber());

            jsonBody.put("contact", contact);

            this.request(Request.Method.POST, this.buildCustomersBaseUrl(), jsonBody, true,
                response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.customer_creation_success))),
                error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.unexpected_error)))
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }

    public void updateCustomer(Customer customer, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", customer.getName());
            jsonBody.put("address", customer.getAddress());
            jsonBody.put("city", customer.getCity());
            jsonBody.put("description", customer.getDescription());
            jsonBody.put("type", customer.getType());

            JSONObject contact = new JSONObject();
            contact.put("firstName", customer.getContact().getFirstName());
            contact.put("lastName", customer.getContact().getLastName());
            contact.put("phoneNumber", customer.getContact().getPhoneNumber());

            jsonBody.put("contact", contact);

            this.request(Request.Method.PUT, this.buildCustomersUrlById(customer.getId()), jsonBody, true,
                response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.customer_update_success))),
                error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.update_error)))
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }

    public void getCustomers(CommunicatorCallback<CustomerResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.GET, this.buildCustomersBaseUrl(), null, true,
            response -> {
                ArrayList<Customer> customerList = new ArrayList<>();
                try {
                    JSONArray jsonResponse = new JSONArray(response);


                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject customerJson = jsonResponse.getJSONObject(i);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Customer customer = gsonBuilder.create().fromJson(customerJson.toString(), Customer.class);
                        customerList.add(customer);
                    }

                    callback.onSuccess(new CustomerResponse(customerList, null));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new CustomerResponse(null, context.getString(R.string.data_processing_error)));
                }
            },
            error -> callback.onFailure(new CustomerResponse(null, context.getString(R.string.customers_fetching_error)))
        );
    }

    public void deleteCustomer(Customer customer, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.DELETE, this.buildCustomersUrlById(customer.getId()), null, true,
            response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.customer_delete_success))),
            error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.delete_error)))
        );
    }

    public void updateProfile(User user, CommunicatorCallback<GenericMessageResponse> callback){
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }
                
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("firstName", user.getFirstName());
            jsonBody.put("lastName", user.getLastName());
            jsonBody.put("email", user.getEmail());
            jsonBody.put("address", user.getAddress());
            jsonBody.put("city", user.getCity());
            jsonBody.put("password", user.getPassword());
            jsonBody.put("session", this.sessionToken);

            this.request(Request.Method.PUT, this.buildUserFullUrl(), jsonBody, true,
                response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.profile_update_success))),
                error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.error_during_update)))
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }

    public void getRoutes(CommunicatorCallback<PlannedRouteResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.GET, this.buildPlannedRoutesBaseUrl(), null, true,
                response -> {
                    ArrayList<PlannedRoute> routes = new ArrayList<>();
                    try {
                        JSONArray jsonResponse = new JSONArray(response);

                        for (int i = 0; i < jsonResponse.length(); i++) {
                            JSONObject routesJson = jsonResponse.getJSONObject(i);
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            PlannedRoute route = gsonBuilder.create().fromJson(routesJson.toString(), PlannedRoute.class);

                            routes.add(route);
                        }

                        callback.onSuccess(new PlannedRouteResponse(routes, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(new PlannedRouteResponse(null, context.getString(R.string.data_processing_error)));
                    }
                },
                error -> callback.onFailure(new PlannedRouteResponse(null, context.getString(R.string.routes_fetching_error)))
        );
    }

    public void createRoute(String name, PlannedRoute route, CommunicatorCallback<GenericMessageResponse> callback){
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            JSONObject jsonBody = new JSONObject();
            JSONArray ids = new JSONArray();
            route.getCustomers().forEach(
                    customer -> ids.put(customer.getId())
            );

            jsonBody.put("customersId", ids);

            String encodedName = URLEncoder.encode(name);

            this.request(Request.Method.POST, this.buildPlannedRoutesBaseUrl() + encodedName, jsonBody, true,
                    response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.route_creation_success))),
                    error -> {
                        // convert response.networkResponse.data to string
                        String errorMessage = error.networkResponse != null ? new String(error.networkResponse.data) : context.getString(R.string.error_during_update);

                        callback.onFailure(new GenericMessageResponse(errorMessage));
                    }
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }

    public void getPlannedRouteById(String id, CommunicatorCallback<PlannedRouteResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.GET, this.buildPlannedRoutesBaseUrl() + id, null, true,
            response -> {
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    PlannedRoute route = gsonBuilder.create().fromJson(response, PlannedRoute.class);

                
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new PlannedRouteResponse(null, context.getString(R.string.data_processing_error)));
                }
            },
            error -> callback.onFailure(new PlannedRouteResponse(null, context.getString(R.string.route_fetching_error)))
        );
    }

    public void updateRoute(PlannedRoute route, CommunicatorCallback<GenericMessageResponse> callback){
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            String body = gsonBuilder.create().toJson(route);
            Log.d("Oui", "updateRoute: " + body);

            String encodedName = URLEncoder.encode(route.getName(), "UTF-8");

            this.request(Request.Method.PUT, this.buildPlannedRoutesBaseUrl() + encodedName, new JSONObject(body), true,
                response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.route_updated_success))),
                error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.error_during_update)))
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }

    public void deletePlannedRoute(PlannedRoute route, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.DELETE, this.buildPlannedRoutesBaseUrl() + route.getId(), null, true,
            response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.route_delete_success))),
            error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.delete_error)))
        );
    }

    public void createActualRoute(PlannedRoute plannedRoute, CommunicatorCallback<ActualRouteNavigationResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            Log.d("Route", "Planned route : " + plannedRoute.toString());
            GsonBuilder gBuilder = new GsonBuilder();
            String body = gBuilder.create().toJson(plannedRoute);
            JSONObject jsonBody = new JSONObject(body);

            this.request(Request.Method.POST, this.buildActualRoutesbaseUrl(), jsonBody, true,
                response -> {
                    Log.d("Route", "Response : " + response);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    RouteAndGpsDto actualRoute = gsonBuilder.create().fromJson(response, RouteAndGpsDto.class);

                    actualRoute.getRoute().setRouteId(plannedRoute.getId());
                    callback.onSuccess(new ActualRouteNavigationResponse(actualRoute, null));
                },
                error -> callback.onFailure(new ActualRouteNavigationResponse(null, context.getString(R.string.actual_route_error)))
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new ActualRouteNavigationResponse(null, context.getString(R.string.request_creation_error)));
        }
    }

    public void getActualRouteById(String actualRouteId, CommunicatorCallback<ActualRouteNavigationResponse> callback) {

        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            this.request(Request.Method.GET, this.buildActualRoutesbaseUrl() + actualRouteId, null, true,
                response -> {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    RouteAndGpsDto actualRoute = gsonBuilder.create().fromJson(response, RouteAndGpsDto.class);

                    callback.onSuccess(new ActualRouteNavigationResponse(actualRoute, null));
                },
                error -> callback.onFailure(new ActualRouteNavigationResponse(null, context.getString(R.string.actual_route_error)))
            );

        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new ActualRouteNavigationResponse(null, context.getString(R.string.request_creation_error)));
        }

    }

    public void sendUserLocation(UpdateLocationDTO locationDTO, CommunicatorCallback<NearbyCustomersResponse> callback) {
        
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }
        
        try {
            GsonBuilder gBuilder = new GsonBuilder();
            String body = gBuilder.create().toJson(locationDTO);
            System.out.println(body);
            JSONObject jsonBody = new JSONObject(body);


            this.request(Request.Method.POST, this.buildActualRoutesbaseUrl() + locationDTO.getRouteId(), jsonBody, true,
                response -> {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    NearbyCustomersDTO nearCustomers = gsonBuilder.create().fromJson(response, NearbyCustomersDTO.class);
                    callback.onSuccess(new NearbyCustomersResponse(nearCustomers.getCustomers(), null));
                },
                error -> callback.onFailure(new NearbyCustomersResponse(null, context.getString(R.string.actual_route_error)))
            );
            
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new NearbyCustomersResponse(null, context.getString(R.string.request_creation_error)));
        }
        
    }

    
    public void skipVisit(ActualRoute toUpdate, CommunicatorCallback<ActualRouteNavigationResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            GsonBuilder gBuilder = new GsonBuilder();
            String body = gBuilder.create().toJson(toUpdate);
            JSONObject jsonBody = new JSONObject(body);

            this.request(Request.Method.PUT, this.buildActualRoutesbaseUrl() + "skip", jsonBody, true,
                response -> {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    ActualRoute actualRoute = gsonBuilder.create().fromJson(response, ActualRoute.class);
                    RouteAndGpsDto routeAndGpsDto = new RouteAndGpsDto();
                    routeAndGpsDto.setRoute(actualRoute);

                    callback.onSuccess(new ActualRouteNavigationResponse(routeAndGpsDto, null));
                },
                error -> {
                    String errorMessage = error.networkResponse != null ? new String(error.networkResponse.data) : context.getString(R.string.error_during_update);

                    System.out.println(errorMessage);

                    error.printStackTrace();
                    callback.onFailure(new ActualRouteNavigationResponse(null, context.getString(R.string.error_during_update)));
                }
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new ActualRouteNavigationResponse(null, context.getString(R.string.request_creation_error)));
        }
    }


    public void confirmVisit(ActualRoute toUpdate, CommunicatorCallback<ActualRouteNavigationResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            GsonBuilder gBuilder = new GsonBuilder();
            String body = gBuilder.create().toJson(toUpdate);
            JSONObject jsonBody = new JSONObject(body);

            this.request(Request.Method.PUT, this.buildActualRoutesbaseUrl() + "confirm", jsonBody, true,
                response -> {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    ActualRoute actualRoute = gsonBuilder.create().fromJson(response, ActualRoute.class);
                    RouteAndGpsDto routeAndGpsDto = new RouteAndGpsDto();
                    routeAndGpsDto.setRoute(actualRoute);

                    callback.onSuccess(new ActualRouteNavigationResponse(routeAndGpsDto, null));
                },
                error -> {
                    String errorMessage = error.networkResponse != null ? new String(error.networkResponse.data) : context.getString(R.string.error_during_update);

                    System.out.println(errorMessage);
                }
            );

        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new ActualRouteNavigationResponse(null, context.getString(R.string.request_creation_error)));
        }
    }

    public void deleteActualRoute(ActualRoute route, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.DELETE, this.buildActualRoutesbaseUrl() + route.getId(), null, true,
            response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.route_delete_success))),
            error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.delete_error)))
        );
    }

    public void getActualRoutes(CommunicatorCallback<ActualRouteResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.GET, this.buildActualRoutesbaseUrl(), null, true,
                response -> {
                    ArrayList<ActualRoute> routes = new ArrayList<>();
                    try {
                        JSONArray jsonResponse = new JSONArray(response);

                        for (int i = 0; i < jsonResponse.length(); i++) {
                            JSONObject routesJson = jsonResponse.getJSONObject(i);

                            GsonBuilder gsonBuilder = new GsonBuilder();
                            ActualRoute route = gsonBuilder.create().fromJson(routesJson.toString(), ActualRoute.class);

                            routes.add(route);
                        }

                        callback.onSuccess(new ActualRouteResponse(routes, null));
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailure(new ActualRouteResponse(null, context.getString(R.string.data_processing_error)));
                    }
                },
                error -> callback.onFailure(new ActualRouteResponse(null, context.getString(R.string.routes_fetching_error)))
        );
    }

    public void pauseRoute(ActualRoute route, CommunicatorCallback<ActualRouteResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.POST, this.buildActualRoutesbaseUrl() + "pause/" + route.getId(), null, true,
            response -> {
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    ActualRoute actualRoute = gsonBuilder.create().fromJson(response, ActualRoute.class);

                    ArrayList<ActualRoute> routes = new ArrayList<>();
                    routes.add(actualRoute);

                    callback.onSuccess(new ActualRouteResponse(routes, null));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new ActualRouteResponse(null, context.getString(R.string.data_processing_error)));
                }
            },
            error -> callback.onFailure(new ActualRouteResponse(null, context.getString(R.string.error_during_update)))
        );
    }

    public void resumeRoute(ActualRoute route, CommunicatorCallback<ActualRouteResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.POST, this.buildActualRoutesbaseUrl() + "resume/" + route.getId(), null, true,
            response -> {
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    ActualRoute actualRoute = gsonBuilder.create().fromJson(response, ActualRoute.class);

                    ArrayList<ActualRoute> routes = new ArrayList<>();
                    routes.add(actualRoute);

                    callback.onSuccess(new ActualRouteResponse(routes, null));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new ActualRouteResponse(null, context.getString(R.string.data_processing_error)));
                }
            },
            error -> callback.onFailure(new ActualRouteResponse(null, context.getString(R.string.error_during_update)))
        );
    }

    public void finishRoute(ActualRoute route, CommunicatorCallback<ActualRouteResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.POST, this.buildActualRoutesbaseUrl() + "finish/" + route.getId(), null, true,
            response -> {
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    ActualRoute actualRoute = gsonBuilder.create().fromJson(response, ActualRoute.class);

                    ArrayList<ActualRoute> routes = new ArrayList<>();
                    routes.add(actualRoute);

                    callback.onSuccess(new ActualRouteResponse(routes, null));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new ActualRouteResponse(null, context.getString(R.string.data_processing_error)));
                }
            },
            error -> callback.onFailure(new ActualRouteResponse(null, context.getString(R.string.error_during_update)))
        );
    }
}
