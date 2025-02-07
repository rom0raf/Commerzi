package com.commerzi.app.communication;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.commerzi.app.Client;
import com.commerzi.app.R;
import com.commerzi.app.User;
import com.commerzi.app.communication.responses.AuthResponse;
import com.commerzi.app.communication.responses.ClientsResponse;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.communication.responses.GenericMessageResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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

    private void request(int method, String url, JSONObject requestBody, boolean authenticated,
                         Response.Listener<String> listener,
                         @Nullable Response.ErrorListener errorListener) {
        // This is needed because "this" in getHeaders() is
        // the StringRequest "this" and not the actual Communicator
        String token = this.sessionToken;
        StringRequest stringRequest = new StringRequest(method, url, listener, errorListener) {
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

        this.queue.add(stringRequest);
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
                        AuthResponse authResponse = new AuthResponse(context.getString(R.string.response_parsing_error), null);
                        callback.onFailure(authResponse);
                    }
                },
                error -> {
                    AuthResponse authResponse = new AuthResponse(context.getString(R.string.connection_error), null);
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

    public void createClient(Client client, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", client.getName());
            jsonBody.put("address", client.getAddress());
            jsonBody.put("city", client.getCity());
            jsonBody.put("description", client.getDescription());
            jsonBody.put("type", client.getType());

            JSONObject contact = new JSONObject();
            contact.put("firstName", client.getContactFirstName());
            contact.put("lastName", client.getContactLastName());
            contact.put("phoneNumber", client.getContactPhoneNumber());

            jsonBody.put("contact", contact);

            this.request(Request.Method.POST, this.buildCustomersBaseUrl(), jsonBody, true,
                response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.client_creation_success))),
                error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.unexpected_error)))
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }

    public void updateClient(Client client, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", client.getName());
            jsonBody.put("address", client.getAddress());
            jsonBody.put("city", client.getCity());
            jsonBody.put("description", client.getDescription());
            jsonBody.put("type", client.getType());

            JSONObject contact = new JSONObject();
            contact.put("firstName", client.getContactFirstName());
            contact.put("lastName", client.getContactLastName());
            contact.put("phoneNumber", client.getContactPhoneNumber());

            jsonBody.put("contact", contact);

            this.request(Request.Method.PUT, this.buildCustomersUrlById(client.getId()), jsonBody, true,
                response -> callback.onSuccess(new GenericMessageResponse("Client mis à jour")),
                error -> callback.onFailure(new GenericMessageResponse("Erreur lors de la mise à jour"))
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }

    public void getClients(CommunicatorCallback<ClientsResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.GET, this.buildCustomersBaseUrl(), null, true,
            response -> {
                ArrayList<Client> clientList = new ArrayList<>();
                try {
                    JSONArray jsonResponse = new JSONArray(response);


                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject clientJson = jsonResponse.getJSONObject(i);

                        String id = clientJson.optString("id", "Inconnu");
                        String name = clientJson.optString("name", "Inconnu");
                        String address = clientJson.optString("address", "Non spécifié");
                        String city = clientJson.optString("city", "Non spécifié");
                        String description = clientJson.optString("description", "Non spécifiée");
                        String type = clientJson.optString("type", "Inconnu");

                        JSONObject contact = clientJson.optJSONObject("contact");
                        String firstName = contact != null ? contact.optString("firstName", "Inconnu") : "Inconnu";
                        String lastName = contact != null ? contact.optString("lastName", "Inconnu") : "Inconnu";
                        String phoneNumber = contact != null ? contact.optString("phoneNumber", "Non spécifié") : "Non spécifié";

                        Client client = new Client(id, name, address, city, description, type, firstName, lastName, phoneNumber);
                        clientList.add(client);
                    }

                    callback.onSuccess(new ClientsResponse(clientList, null));
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFailure(new ClientsResponse(null, "Erreur de traitement des données"));
                }
            },
            error -> callback.onFailure(new ClientsResponse(null, "Impossible de charger les clients"))
        );
    }

    public void deleteClient(Client client, CommunicatorCallback<GenericMessageResponse> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback can't be null.");
        }

        this.request(Request.Method.DELETE, this.buildCustomersUrlById(client.getId()), null, true,
            response -> callback.onSuccess(new GenericMessageResponse("Entreprise supprimée")),
            error -> callback.onFailure(new GenericMessageResponse("Erreur lors de la suppression"))
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
                response -> callback.onSuccess(new GenericMessageResponse(context.getString(R.string.profile_updated))),
                error -> callback.onFailure(new GenericMessageResponse(context.getString(R.string.error_during_update)))
            );
        } catch(Exception e) {
            e.printStackTrace();
            callback.onFailure(new GenericMessageResponse(context.getString(R.string.request_creation_error)));
        }
    }
}
