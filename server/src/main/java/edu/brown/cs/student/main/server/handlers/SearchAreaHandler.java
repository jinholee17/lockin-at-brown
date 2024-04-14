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

/** get all redlining data given search area-descriptions */
public class SearchAreaHandler implements Route {
  GeoJSONData geoJSONData;

  public SearchAreaHandler(GeoJSONData geoJSONData) {
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
      String areaDescription = request.queryParams("area-desc");

      if (areaDescription == null || areaDescription.isEmpty()) {
        responseMap.put("response_type", "error");
        responseMap.put("error message", "No area description input");
        responseMap.put("the parameter is", "area-desc=[value]");
        return Utils.toMoshiJson(responseMap);
      }

      GeoJsonObject object = this.geoJSONData.returnParseGeoJson();
      // Filterns data based on user description
      List<Feature> filteredList = new ArrayList<>();
      List<Feature> areas = object.features;
      for (Feature feature : areas) {
        if (feature != null && feature.properties != null) {
          if (feature.properties.area_description_data.toString().contains(areaDescription)) {
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
    // Returns filtered data
    return Utils.toMoshiJson(responseMap);
  }
}
