package edu.brown.cs.student.main.server.DataSource;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ReviewDatasource {

  private static final String API_KEY =
      "Gh5GhOIvNTND-kLWc3QsFMJXLcTBdbaJwZ2mofgThV7Rufb_T5ygzW9lywLzlxtDLrUXggOmYXF1C9wrUYurDrfUw3eZ2dgC0-LLLL01_mNBDVvHbZtADHFrtQQxZnYx";

  public double getReviews(String businessID) throws Exception {
    System.out.print(businessID);
    URL url = new URL("https://api.yelp.com/v3/businesses/" + businessID); // Entery URL
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("GET");
    // Set the authorization header with the Bearer scheme
    connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
    connection.setRequestProperty("Accept", "application/json");

    // Check for a successful response (HTTP status 200)
    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      // Read the response
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder responseContent = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        responseContent.append(line);
      }
      reader.close();

      // Create Moshi instance to parse JSON
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<Map<String, Object>> jsonAdapter =
          moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

      // Parse the JSON into a Map
      Map<String, Object> jsonMap = jsonAdapter.fromJson(responseContent.toString());

      if (jsonMap != null && jsonMap.containsKey("rating")) {
        // Assuming the "rating" is a double value
        System.out.print(jsonMap.get("rating"));
        return (double) jsonMap.get("rating");
      } else {
        throw new Exception("Rating not found in the response");
      }
    } else {
      throw new Exception("Failed to retrieve data: " + connection.getResponseMessage());
    }
  }
}