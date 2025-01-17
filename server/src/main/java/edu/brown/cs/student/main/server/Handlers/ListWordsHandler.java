package edu.brown.cs.student.main.server.Handlers;

import edu.brown.cs.student.main.server.Storage.StorageInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler that is responsible for storing list of words from users (preferences) */
public class ListWordsHandler implements Route {

  public StorageInterface storageHandler;

  public ListWordsHandler(StorageInterface storageHandler) {
    this.storageHandler = storageHandler;
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
      String uid = request.queryParams("uid");

      // get all the words for the user
      List<Map<String, Object>> vals = this.storageHandler.getCollection(uid, "words");

      // convert the key,value map to just a list of the words.
      List<String> words = vals.stream().map(word -> word.get("word").toString()).toList();
      responseMap.put("response_type", "success");
      responseMap.put("words", words);
    } catch (Exception e) {
      // error likely occurred in the storage handler
      e.printStackTrace();
      responseMap.put("response_type", "failure");
      responseMap.put("error", e.getMessage());
    }
    return Utils.toMoshiJson(responseMap);
  }
}
