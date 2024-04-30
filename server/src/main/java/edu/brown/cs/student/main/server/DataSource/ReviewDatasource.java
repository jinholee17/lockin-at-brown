package edu.brown.cs.student.main.server.DataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReviewDatasource {

  private static final String API_KEY =
      "Gh5GhOIvNTND-kLWc3QsFMJXLcTBdbaJwZ2mofgThV7Rufb_T5ygzW9lywLzlxtDLrUXggOmYXF1C9wrUYurDrfUw3eZ2dgC0-LLLL01_mNBDVvHbZtADHFrtQQxZnYx";

  public String getReviews(String businessID) throws Exception {
    System.out.print(businessID);
    URL url =
        new URL(
            "https",
            "api.yelp.com",
            "/v3/private/businesses/" + businessID + "/reviews"); // Entery URL
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("GET");
    // Set the authorization header with the Bearer scheme
    connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
    connection.setRequestProperty("Accept", "application/json");

    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
      System.err.println(
          "Failed with status: "
              + connection.getResponseCode()
              + " - "
              + connection.getResponseMessage());
    }

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
      return responseContent.toString(); // Return the response content
    } else {
      throw new Exception("Failed to retrieve data: " + connection.getResponseMessage());
    }
  }
}
