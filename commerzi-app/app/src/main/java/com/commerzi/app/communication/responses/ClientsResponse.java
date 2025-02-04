package com.commerzi.app.communication.responses;

import com.commerzi.app.Client;

import java.util.List;

public class ClientsResponse {
    public final List<Client> clients;
    public final String message;

    public ClientsResponse(List<Client> clients, String message) {
        this.clients = clients;
        this.message = message;
    }
}
