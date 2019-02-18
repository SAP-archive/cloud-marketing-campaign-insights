package com.sap.cec.mkt.insight.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;

/** Client utility class **/
public class ClientUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ClientUtil.class);

    private ClientUtil() {
    }

    /**
     * Get data
     * 
     * @param destinationName
     * @param path
     * @param contextURL
     * @param payload
     * @return responseString @throws
     */
    public static String getData(String destinationName, String path, String contextURL) {
	String responseString = null;
	HttpResponse httpResponse = null;
	try {
	    Destination destination = DestinationAccessor.getDestination(destinationName);
	    HttpClient httpClient = HttpClientAccessor.getHttpClient(destinationName);
	    String uri = destination.getUri().toString();

	    String authString = destination.getPropertiesByName().get("User") + ":"
		    + destination.getPropertiesByName().get("Password");
	    String base64AuthString = Base64.getEncoder().encodeToString(authString.getBytes());

	    String url = uri + path + contextURL;
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + base64AuthString);
	    httpGet.setHeader("Accept", "application/json");
	    httpResponse = httpClient.execute(httpGet);
	    // Check GET response status code
	    int statusCode = httpResponse.getStatusLine().getStatusCode();
	    LOG.info(" StatusCode: ", statusCode);
	    responseString = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
	} catch (IOException e) {
	    LOG.error(" Exception while posting data to Hybris Marketing Cloud", e);
	}
	/*
	 * return new Gson().fromJson(responseString, new
	 * TypeToken<ArrayList<CampaignByTeamMember>>() { private static final long
	 * serialVersionUID = 1L; }.getType());
	 */
	return responseString;
    }

    /**
     * Post data
     * 
     * @param destinationName
     * @param path
     * @param contextURL
     * @param payload
     * @return responseString @throws
     */
    public static String postData(String destinationName, String path, String contextURL, String payload) {
	String responseString = null;
	HttpResponse httpResponse = null;
	Header[] cookies = null;
	String token = null;
	try {
	    Destination destination = DestinationAccessor.getDestination(destinationName);
	    HttpClient httpClient = HttpClientAccessor.getHttpClient(destinationName);
	    String uri = destination.getUri().toString();
	    String authString = destination.getPropertiesByName().get("User") + ":"
		    + destination.getPropertiesByName().get("Password");
	    String base64AuthString = Base64.getEncoder().encodeToString(authString.getBytes());

	    httpResponse = getCsrfTokenResponse(httpClient, uri, path, base64AuthString);
	    if (httpResponse != null) {
		cookies = httpResponse.getHeaders("Set-Cookie");
		Header header = httpResponse.getFirstHeader("x-csrf-token");
		if (header != null) {
		    token = header.getValue();
		}
	    }
	    String url = uri + path + contextURL;
	    HttpPost httpPost = new HttpPost(url);
	    httpPost.setHeaders(cookies);
	    httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + base64AuthString);
	    httpPost.setHeader("x-csrf-token", token);
	    httpPost.setHeader("Content-Type", "application/json");
	    httpPost.addHeader("Accept", "application/json");
	    httpPost.setEntity(new StringEntity(payload));
	    httpResponse = httpClient.execute(httpPost);
	    // Check POST response status code
	    int statusCode = httpResponse.getStatusLine().getStatusCode();
	    LOG.info(" StatusCode: ", statusCode);
	    responseString = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
	} catch (IOException e) {
	    LOG.error(" Exception while posting data to Hybris Marketing Cloud", e);
	}
	return responseString;
    }

    private static HttpResponse getCsrfTokenResponse(HttpClient httpClient, String uri, String path, String authString)
	    throws IOException {
	HttpResponse httpResponse = null;
	try {
	    // Execute HTTP request
	    String url = uri + path + "/$metadata";
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + authString);
	    httpGet.setHeader("x-csrf-token", "fetch");
	    httpGet.setHeader("Content-Type", "application/json");
	    httpResponse = httpClient.execute(httpGet);
	} catch (IOException e) {
	    LOG.error("GET CSRF token failed", e);
	}

	return httpResponse;
    }
}
