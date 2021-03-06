/*
 * Copyright (C) 2019 Seerbit
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.seerbit.service.impl;

import com.google.gson.JsonObject;
import com.seerbit.Client;
import com.seerbit.httpclient.impl.HttpClientImpl;
import com.seerbit.service.Request;
import com.seerbit.service.Service;
import com.seerbit.util.Utility;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Seerbit
 */
public class ServiceMerchantImpl 
        implements Service, Request {

    private final HttpClientImpl httpClient;
    
    protected boolean requiresToken;
    protected Client client;
    protected JsonObject response;
    protected String token;

    /**
     *
     * @param client
     */
    public ServiceMerchantImpl(Client client) {
        Utility.doClientNonNull(client);
        this.httpClient = new HttpClientImpl();
        this.client = client;
        this.response = new JsonObject();
    }

    /**
     *
     * @return client
     */
    @Override
    public Client getClient() {
        return client;
    }

    /**
     *
     * @return requiresToken
     */
    @Override
    public boolean isTokenRequired() {
        return requiresToken;
    }

    /**
     *
     * @param requiresToken
     */
    @Override
    public void setRequiresToken(boolean requiresToken) {
        this.requiresToken = requiresToken;
    }

    /**
     *
     * @param endpoint
     * @param payload
     * @param token
     * @return json
     */
    @Override
    public JsonObject postRequest(String endpoint, Map<String, Object> payload, String token) {
        String message = "Set a field named \"apiBase\" in the client configuration";
        Objects.requireNonNull(client.getConfig().get("apiBase"), message);
        String endpointURL = client.getConfig().get("apiBase").toString().concat(endpoint);
        JsonObject json = httpClient.post(this, endpointURL, payload, token);
        return json;
    }

    /**
     *
     * @param endpoint
     * @param payload
     * @param token
     * @return json
     */
    @Override
    public JsonObject putRequest(String endpoint, Map<String, Object> payload, String token) {
        String message = "Set a field named \"apiBase\" in the client configuration";
        Objects.requireNonNull(client.getConfig().get("apiBase"), message);
        String endpointURL = client.getConfig().get("apiBase").toString().concat(endpoint);
        JsonObject json = httpClient.put(this, endpointURL, payload, token);
        return json;
    }

    /**
     *
     * @param endpoint
     * @param token
     * @return json
     */
    @Override
    public JsonObject getRequest(String endpoint, String token) {
        String message = "Set a field named \"apiBase\" in the client configuration";
        Objects.requireNonNull(client.getConfig().get("apiBase"), message);
        String endpointURL = client.getConfig().get("apiBase").toString().concat(endpoint);
        JsonObject json = httpClient.get(this, endpointURL, token);
        return json;
    }
}
