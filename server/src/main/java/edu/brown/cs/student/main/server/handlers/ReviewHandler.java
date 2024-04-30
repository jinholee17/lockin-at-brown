package edu.brown.cs.student.main.server.handlers;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ReviewHandler implements Route {
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    // try {
    // String uid = request.queryParams("uid");

    //     // // Remove the user from the database
    //     // System.out.println("clearing words for user: " + uid);
    //     // this.storageHandler.clearUser(uid);

    //     // responseMap.put("response_type", "success");
    //     // } catch (Exception e) {
    //     // // error likely occurred in the storage handler
    //     // e.printStackTrace();
    //     // responseMap.put("response_type", "failure");
    //     // responseMap.put("error", e.getMessage());
    //     // }

    return false;
    // return Utils.toMoshiJson(responseMap);

  }
}
