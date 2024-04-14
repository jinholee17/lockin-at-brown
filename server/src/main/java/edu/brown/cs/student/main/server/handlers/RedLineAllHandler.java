package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.geoJSONDataSource.GeoJSONData;
import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonObject;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** get all red line data */
public class RedLineAllHandler implements Route {
  GeoJSONData geoJsonData;

  public RedLineAllHandler(GeoJSONData geoJsonData) {
    this.geoJsonData = geoJsonData;
  }

  /**
   * Invoked when a request is made on this route's corresponding path e.g. '/hello'
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return The content to be set in the response
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Gets datas
      GeoJsonObject object = this.geoJsonData.returnParseGeoJson();
      responseMap.put("success", object);
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }
    // Returns data
    return Utils.toMoshiJson(responseMap);
  }
}
