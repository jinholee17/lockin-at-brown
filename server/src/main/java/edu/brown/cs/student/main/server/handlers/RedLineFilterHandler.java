package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.geoJSONDataSource.GeoJSONData;
import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonObject;
import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonObject.Feature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** get all redline data given filtering bounding box */
public class RedLineFilterHandler implements Route {

  GeoJSONData geoJSONData;

  public RedLineFilterHandler(GeoJSONData geoJSONData) {
    this.geoJSONData = geoJSONData;
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
    Map<String, Object> responseMap = new HashMap<>();

    try {
      // collect parameters from the request
      String maxLat = request.queryParams("max-lat");
      String maxLong = request.queryParams("max-long");
      String minLat = request.queryParams("min-lat");
      String minLong = request.queryParams("min-long");
      // Checks valid input in query
      if (maxLat == null
          || maxLong == null
          || minLat == null
          || minLong == null
          || maxLat.isEmpty()
          || maxLong.isEmpty()
          || minLat.isEmpty()
          || minLong.isEmpty()) {
        responseMap.put("response_type", "error");
        responseMap.put("error message", "No file name inputted as parameter");
        responseMap.put(
            "the parameter is",
            "max-lat=[value]&max-long=[value]&min-lat=[value]&min-long=[value]");
        return Utils.toMoshiJson(responseMap);
      }

      double maxLatD;
      double maxLongD;
      double minLatD;
      double minLongD;

      try {
        maxLatD = Double.parseDouble(maxLat);
        maxLongD = Double.parseDouble(maxLong);
        minLatD = Double.parseDouble(minLat);
        minLongD = Double.parseDouble(minLong);
      } catch (NumberFormatException e) {
        responseMap.put("response_type", "error");
        responseMap.put("error", "please make sure all parameters are numbers");
        return Utils.toMoshiJson(responseMap);
      }

      GeoJsonObject object = this.geoJSONData.returnParseGeoJson();
      // Filters redline data
      List<Feature> filteredList = new ArrayList<>();
      List<Feature> areas = object.features;
      for (Feature feature : areas) {
        if (feature != null && feature.geometry != null) {
          List<List<List<List<Double>>>> cList = feature.geometry.coordinates;
          List<Double> coordinates = cList.get(0).get(0).get(0);
          Double latitude = coordinates.get(0);
          Double longitude = coordinates.get(1);
          if (latitude <= maxLatD
              && latitude >= minLatD
              && longitude <= maxLongD
              && longitude >= minLongD) {
            filteredList.add(feature);
          }
        }
      }
      object.features = filteredList;
      responseMap.put("success", object);
    } catch (Exception e) {
      // error likely occurred in the storage handler
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }
    // Returns filtered data redline
    return Utils.toMoshiJson(responseMap);
  }
}
