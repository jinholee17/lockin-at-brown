package edu.brown.cs.student.main.server.Handlers;

import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.DataSource.*;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler that is responsible for dealing with review calls */
public class ReviewHandler implements Route {

  private final ReviewDatasource reviewDatasource;

  public ReviewHandler(ReviewDatasource reviewDatasource) {
    this.reviewDatasource = reviewDatasource;
  }

  /**
   * Invoked when a request is made on this route's corresponding path e.g. '/hello'
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return The content to be set in the response
   */
  @Override
  public Object handle(Request request, Response response) {
    // Serialize the error message to a JSON string
    Moshi moshi = new Moshi.Builder().build();
    Map<String, String> errorJson = new HashMap<>();
    Moshi moshiError = new Moshi.Builder().build();
    JsonAdapter<Map<String, String>> adapterError =
        moshiError.adapter(Types.newParameterizedType(Map.class, String.class, String.class));

    // Serializes successful output
    JsonAdapter<Map<String, Object>> adapterResponse =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

    Map<String, Object> responseMap = new HashMap<>();
    try {
      if (request.queryParams("name") != null) {
        String businessID = getID(request.queryParams("name"));
        double starRating = reviewDatasource.getReviews(businessID);

        // Serialize the output
        // Fetch reviews based on query parameters
        responseMap.put("Success", starRating);
        return adapterResponse.toJson(responseMap);

      } else {
        throw new Exception("Invalid Query");
      }
    } catch (Exception e) {
      errorJson.put("error", e.getMessage());
      return adapterError.toJson(errorJson);
    }
  }

  /**
   * Helper method that deals with getting the yelp ID from out data source that is passed into the
   * get review helper method implemented in datasource
   */
  public String getID(String locationName) {
    String workingDirectory = System.getProperty("user.dir");
    String path = Paths.get(workingDirectory, "data", "locationcoords.json").toString();
    try (FileReader reader = new FileReader(path)) {
      // Parse JSON data into a custom class or map
      Gson gson = new Gson();
      LocationData locationData = gson.fromJson(reader, LocationData.class);

      // Search for a location by name
      for (Location location : locationData.getLocations()) {
        if (location.getName().equals(locationName)) {
          return location.getYelpID();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
