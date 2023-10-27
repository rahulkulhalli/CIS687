package org.syr.cis687.utils;

import org.syr.cis687.models.ETA;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static ETA makePostRequest(String url) {

        ETA eta = new ETA();

        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and parse the response
                try (JsonReader reader = Json.createReader(connection.getInputStream())) {
                    JsonObject jsonResponse = reader.readObject();

                    JsonArray rows = jsonResponse.getJsonArray("rows");
                    for (int i = 0; i < rows.size(); i++) {
                        JsonObject row = rows.getJsonObject(i);
                        JsonArray elements = row.getJsonArray("elements");
                        for (int j = 0; j < elements.size(); j++) {
                            JsonObject element = elements.getJsonObject(j);
                            if ("OK".equals(element.getString("status"))) {
                                // Extract the distance.
                                JsonObject distance = element.getJsonObject("distance");
                                eta.setEstimatedDistance(distance.getString("text"));

                                // Extract the duration.
                                JsonObject duration = element.getJsonObject("duration");
                                eta.setEstimatedTime(duration.getString("text"));

                                // Single element only.
                                break;
                            }
                        }
                    }
                }
            } else {
                System.out.println("HTTP POST request failed with response code: " + responseCode);
            }
            return eta;

        } catch (IOException e) {
            return null;
        }
    }
}
