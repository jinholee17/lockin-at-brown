package edu.brown.cs.student.main.server.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.Map;

/** Utility class providing methods for JSON serialization using Moshi library. */
public class Utils {
  /**
   * Serializes Map through Moshi Library
   *
   * @param map Map that stores the response
   * @return Serialized JSON
   */
  public static String toMoshiJson(Map<String, Object> map) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    return adapter.toJson(map);
  }
}
