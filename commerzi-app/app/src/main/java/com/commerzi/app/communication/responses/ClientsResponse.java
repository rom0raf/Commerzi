package com.commerzi.app.communication.responses;

import com.commerzi.app.Client;

import java.util.ArrayList;

public class ClientsResponse {
    public final ArrayList<Client> clients;
    public final String message;

    public ClientsResponse(ArrayList<Client> clients, String message) {
        this.clients = clients;
        this.message = message;
    }
}
