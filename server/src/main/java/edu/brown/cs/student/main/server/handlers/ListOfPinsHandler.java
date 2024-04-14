package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import spark.Request;
import spark.Response;
import spark.Route;

/** list all pins given user */
public class ListOfPinsHandler implements Route {

  public StorageInterface storageHandler;

  public ListOfPinsHandler(StorageInterface storageHandler) {
    this.storageHandler = storageHandler;
  }

  /**
   * Invoked when a request is made on this route's corresponding path e.g. '/listPins'
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return The content to be set in the response
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    try {
      String uid = request.queryParams("uid");

      // Get all the pins for the user
      List<Map<String, Object>> vals = this.storageHandler.getCollection(uid, "pins");
      System.out.print(vals);
      List<String> pinCoord =
          vals.stream()
              .map(pin -> pin.get("pin"))
              .filter(Objects::nonNull) // Filter out null values
              .map(Object::toString) // Convert to string
              .toList();
      System.out.print(vals);
      responseMap.put("response_type", "success");
      responseMap.put("pins", pinCoord);
    } catch (Exception e) {
      // Error likely occurred in the storage handler
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }
}
