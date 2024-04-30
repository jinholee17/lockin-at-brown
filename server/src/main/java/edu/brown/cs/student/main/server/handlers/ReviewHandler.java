package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.DataSource.*;
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
    Map<String, Object> responseMap = new HashMap<>();
    // try {
    //   URL requestURL = new URL(
    //     "https",
    //     "mybusiness.googleapis.com",
    //     "/v4/accounts/"
    //     + accound Id + "/locations/" + locationId + "/reviews");

    // HttpURLConnection clientConnection = connect(requestURL);
    // Moshi moshi = new Moshi.Builder().build();
    // // Serialize output
    // Type listType = Types.newParameterizedType(List.class, List.class);
    // JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

    // List<List<String>> body =
    //       adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    // clientConnection.disconnect();
    // System.out.print(body)

    // responseMap.put("response_type", "success");
    // } catch (Exception e) {
    // // error likely occurred in the storage handler
    // e.printStackTrace();
    // responseMap.put("response_type", "failure");
    // responseMap.put("error", e.getMessage());
    // }

    try {
      if (request.queryParams("locationId") != null && request.queryParams("accoundId") != null) {
        System.out.print(request.queryParams("locationId"));
        System.out.print(request.queryParams("accoundId"));
        // TODO: call API
      } else {
       throw new Exception("Invalid Query");
      }
    } catch (Exception e) {
      errorJson.put("error", e.getMessage());
      return adapterError.toJson(errorJson);
    }

    return Utils.toMoshiJson(responseMap);
  }
}
