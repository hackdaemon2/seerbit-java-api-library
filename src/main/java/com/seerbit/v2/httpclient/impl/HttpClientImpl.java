/*
 * Copyright (C) 2020 Seerbit
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
package com.seerbit.v2.httpclient.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.seerbit.v2.Client;
import com.seerbit.v2.NumericConstants;
import com.seerbit.v2.config.Config;
import com.seerbit.v2.exception.ConnectionException;
import com.seerbit.v2.exception.SeerbitException;
import com.seerbit.v2.httpclient.HttpClient;
import com.seerbit.v2.service.Service;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static com.seerbit.v2.enums.HttpHeaderEnum.CONTENT_TYPE;

/**
 * @author Seerbit
 */
public class HttpClientImpl implements HttpClient, NumericConstants {

	private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

	private final Gson GSON;

	private CloseableHttpResponse response;
	private JsonObject json;
	private int statusCode;

	/**
	 * default constructor
	 */
	public HttpClientImpl() {
		statusCode = -20;
		GSON = new Gson();
		json = new JsonObject();
	}

	/**
	 * @param service A non-optional class, the Service
	 * @param url     A non-optional String, the request url
	 * @param params  A non-optional Map, the payload
	 * @param token   A nullable String, the auth token
	 *
	 * @return json
	 */
	@Override
	public JsonObject post(Service service, String url, Map<String, Object> params, String token) {
		HttpPost postRequest;
		String contentTypeParam;
		String contentTypeValue;
		Client client;
		Config config;
		String requestBody;
		StringEntity entity;
		String authenticationString;
		String responseBody;
		Object[] result;

		try {
			contentTypeParam = CONTENT_TYPE.getParam();
			contentTypeValue = CONTENT_TYPE.getValue();
			client = service.getClient();
			config = client.getConfig();
			requestBody = GSON.toJson(params);
			postRequest = new HttpPost(url);
			postRequest.setHeader(contentTypeParam, contentTypeValue);
			postRequest.setHeader("Request-Timeout", String.valueOf(config.getTimeout()));
			entity = new StringEntity(requestBody);
			postRequest.setEntity(entity);

			if (service.isTokenRequired()) {

				if (Objects.isNull(token) || token.length() < MIN_SIZE) {
					throw new ConnectionException("Please provide an authentication token.");
				} else {
					authenticationString = String.format("Bearer %s", token);
					postRequest.setHeader("Authorization", authenticationString);
				}

			}

			System.out.println("Request headers to Seerbit: " + Arrays.toString(postRequest.getAllHeaders()));
			result = this.request(postRequest);
			response = (CloseableHttpResponse) result[0];
			statusCode = Integer.parseInt(String.valueOf(result[1]));
			responseBody = EntityUtils.toString(response.getEntity());
			System.out.println(String.format("JsonResponse: %s", responseBody));

			json = JsonParser.parseString(responseBody).getAsJsonObject();

			if (statusCode < HTTP_STATUS_200 || statusCode > HTTP_STATUS_299 || Objects.isNull(response)) {
				SeerbitException.handleException(json);
			}

		} catch (IOException | ParseException exception) {
			System.out.println(exception.getMessage());
		}

		return json;
	}

	/**
	 * @param service A non-optional class, the Service
	 * @param url     A non-optional String, the request url
	 * @param params  A non-optional Map, the payload
	 * @param token   A nullable String, the auth token
	 *
	 * @return json
	 */
	@Override
	public JsonObject put(Service service, String url, Map<String, Object> params, String token) {
		HttpPut putRequest;
		String contentTypeParam;
		String contentTypeValue;
		Client client;
		Config config;
		String requestBody;
		StringEntity entity;
		Object[] result;
		String responseBody;

		try {
			contentTypeParam = CONTENT_TYPE.getParam();
			contentTypeValue = CONTENT_TYPE.getValue();
			client = service.getClient();
			config = client.getConfig();
			requestBody = GSON.toJson(params);
			putRequest = new HttpPut(url);
			putRequest.setHeader(contentTypeParam, contentTypeValue);
			putRequest.setHeader("Request-Timeout", String.valueOf(config.getTimeout()));
			entity = new StringEntity(requestBody);
			putRequest.setEntity(entity);

			if (service.isTokenRequired()) {

				if (Objects.isNull(token) || token.length() < MIN_SIZE) {
					throw new ConnectionException("Please provide an authentication token.");
				} else {
					String authenticationString = String.format("Bearer %s", token);
					putRequest.setHeader("Authorization", authenticationString);
				}

			}

			System.out.println("Request headers to Seerbit: " + Arrays.toString(putRequest.getAllHeaders()));
			result = this.request(putRequest);
			response = (CloseableHttpResponse) result[0];
			statusCode = Integer.parseInt(String.valueOf(result[1]));
			responseBody = EntityUtils.toString(response.getEntity());
			System.out.println(String.format("JsonResponse: %s", responseBody));
			json = JsonParser.parseString(responseBody).getAsJsonObject();

			if (statusCode < HTTP_STATUS_200 || statusCode > HTTP_STATUS_299 || Objects.nonNull(response)) {
				SeerbitException.handleException(json);
			}

		} catch (IOException | ParseException exception) {
			System.out.println(exception.getMessage());
		}

		return json;
	}

	/**
	 * @param service A non-optional class, the Service
	 * @param url     A non-optional String, the request url
	 * @param token   A nullable String, the auth token
	 *
	 * @return json
	 */
	@Override
	public JsonObject get(Service service, String url, String token) {
		HttpGet getRequest;
		Client client;
		Config config;
		Object[] result;
		String authenticationString;
		String responseBody;

		try {
			client = service.getClient();
			config = client.getConfig();
			getRequest = new HttpGet(url);
			getRequest.setHeader("Request-Timeout", String.valueOf(config.getTimeout()));

			if (service.isTokenRequired()) {

				if (Objects.isNull(token) || token.length() < MIN_SIZE) {
					throw new ConnectionException("Please provide an authentication token.");
				} else {
					authenticationString = String.format("Bearer %s", token);
					getRequest.setHeader("Authorization", authenticationString);
				}

			}

			System.out.println("Request headers to Seerbit: " + Arrays.toString(getRequest.getAllHeaders()));
			result = this.request(getRequest);
			response = (CloseableHttpResponse) result[0];
			statusCode = Integer.parseInt(String.valueOf(result[1]));
			responseBody = EntityUtils.toString(response.getEntity());
			System.out.println(String.format("JsonResponse: %s", responseBody));

			json = JsonParser.parseString(responseBody).getAsJsonObject();

			if (statusCode < HTTP_STATUS_200 || statusCode > HTTP_STATUS_299 || Objects.nonNull(response)) {
				SeerbitException.handleException(json);
			}

		} catch (IOException | ParseException exception) {
			System.out.println(exception.getMessage());
		}

		return json;
	}

	/**
	 * @param postRequest A non-optional class, the HttpPost request
	 *
	 * @return json
	 */
	private Object[] request(final HttpPost postRequest) {

		try {
			response = HTTP_CLIENT.execute(postRequest);
			statusCode = response.getStatusLine().getStatusCode();
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}

		return new Object[]{response, statusCode};
	}

	/**
	 * @param putRequest A non-optional class, the HttpPut request
	 *
	 * @return json
	 */
	private Object[] request(final HttpPut putRequest) {

		try {
			response = HTTP_CLIENT.execute(putRequest);
			statusCode = response.getStatusLine().getStatusCode();
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}

		return new Object[]{response, statusCode};
	}

	/**
	 * @param getRequest A non-optional class, the HttpGet request
	 *
	 * @return json
	 */
	private Object[] request(final HttpGet getRequest) {

		try {
			response = HTTP_CLIENT.execute(getRequest);
			statusCode = response.getStatusLine().getStatusCode();
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}

		return new Object[]{response, statusCode};
	}

}