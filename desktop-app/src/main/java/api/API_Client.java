package main.java.api;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Map;
import java.util.TreeMap;

public class API_Client {
    private final String apiURL = "http://api.awes-projects.com";
    private String accessToken = null;

    public void authorize(Integer userID, String password) throws UnirestException {
        TreeMap params = new TreeMap<String, Object>();
        params.put("userID", userID);
        params.put("password", password);
        this.accessToken = new JSONObject(query("/users/getAccessToken", "POST", params)).get("accessToken").toString();
        System.out.println(this.accessToken);
    }

    public void authorizeUsingCardUID(String cardUID) throws UnirestException {
        TreeMap params = new TreeMap<String, Object>();
        params.put("cardUID", cardUID);
        this.accessToken = new JSONObject(query("/users/getAccessTokenUsingCardUID", "POST", params)).get("accessToken").toString();
        System.out.println(this.accessToken);
    }

    public String query(String method, String httpRequestMethod, Map parameters) throws UnirestException {
        return query(method, httpRequestMethod, parameters, false);
    }

    public String query(String method, String httpRequestMethod, Map parameters, boolean authorizationRequired) throws UnirestException {
        if(authorizationRequired && accessToken == null) {
            throw new RuntimeException("API call is not authorized");
        }

        com.mashape.unirest.http.HttpResponse<JsonNode> response = null;
        if(httpRequestMethod.toLowerCase().equals("post")) {
            HttpRequestWithBody request = Unirest.post(this.apiURL + method);
            if(authorizationRequired) {
                request.header("Access-Token", this.accessToken);
            }
            request.fields(parameters);
            response = request.asJson();
        } else if(httpRequestMethod.toLowerCase().equals("get")) {
            GetRequest request = Unirest.get(this.apiURL + method);
            if(authorizationRequired) {
                request.header("Access-Token", this.accessToken);
            }
            request.queryString(parameters);
            response = request.asJson();
        } else {
            throw new RuntimeException("\"httpRequestMethod\" is invalid");
        }

        if(response.getStatus() != 200) {
            throw new RuntimeException(response.getBody().getObject().get("errorMessage").toString());
        }

        return response.getBody().toString();
    }

    public JSONArray getDocuments() throws UnirestException {
        return getDocuments(false, null);
    }

    public JSONArray getDocuments(boolean availableOnly) throws UnirestException {
        return getDocuments(availableOnly, null);
    }

    public JSONArray getDocuments(Integer type) throws UnirestException {
        return getDocuments(false, type);
    }

    public JSONArray getDocuments(boolean availableOnly, Integer type) throws UnirestException {
        TreeMap params = new TreeMap<String, Object>();
        if(availableOnly) {
            params.put("availableOnly", 1);
        }
        if(type != null) {
            params.put("type", type);
        }
        System.out.println(query("/documents/getDocuments", "GET", params));
        return new JSONArray(query("/documents/getDocuments", "GET", params));
    }
}
