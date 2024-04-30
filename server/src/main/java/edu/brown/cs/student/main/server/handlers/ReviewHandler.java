package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
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

    try {
      if (request.queryParams("businessID") != null) {
        // System.out.print(request.queryParams("locationId"));
        // System.out.print(request.queryParams("accoundId"));
        // TODO: call API
        double starRating = reviewDatasource.getReviews(request.queryParams("businessID"));

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
}
