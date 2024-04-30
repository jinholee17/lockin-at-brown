package edu.brown.cs.student.main.server.handlers;

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

public class ReviewHandler implements Route {

  private final ReviewDatasource reviewDatasource;

  public ReviewHandler(ReviewDatasource reviewDatasource) {
    this.reviewDatasource = reviewDatasource;
  }

  @Override
  public Object handle(Request request, Response response) {

    // Serialize the error message to a JSON string
    Moshi moshi = new Moshi.Builder().build();
    Map<String, String> errorJson = new HashMap<>();
    Moshi moshiError = new Moshi.Builder().build();
    JsonAdapter<Map<String, String>> adapterError =
        moshiError.adapter(Types.newParameterizedType(Map.class, String.class, String.class));

    // Serialize successfull output
    JsonAdapter<Map<String, Object>> adapterResponse =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

    Map<String, Object> responseMap = new HashMap<>();

    //return false;
    // return Utils.toMoshiJson(responseMap);

    try {
      if (request.queryParams("name") != null) {
        // System.out.print(request.queryParams("locationId"));
        // System.out.print(request.queryParams("accoundId"));
        // TODO: call API
        String businessID = getID(request.queryParams("name"));
        System.out.println("hi :" + businessID);
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

  public String getID(String locationName){
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
    System.err.println("Location name " + locationName + " not found in ID data");
    return null;
  }

}
