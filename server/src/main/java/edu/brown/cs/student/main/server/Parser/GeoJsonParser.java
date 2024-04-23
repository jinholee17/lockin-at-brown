package edu.brown.cs.student.main.server.Parser;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class GeoJsonParser {
  GeoJsonObject parsedJSON;

  /**
   * Parses JSON data from a JsonReader and converts it to the specified target type.
   *
   * @param source The JsonReader containing the JSON data.
   * @param targetType The Class representing the target data type to convert the JSON to.
   * @param <T> The generic type of the target data.
   * @return An instance of the target data type parsed from the JSON.
   * @throws IOException if there's an error reading or parsing the JSON data.
   */
  public static <T> T fromJsonGeneral(String source, Class<T> targetType) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<T> adapter = moshi.adapter(targetType);

    return adapter.fromJson(source);
  }

  /**
   * Reads the given data and parse the object
   *
   * @param filePath the fil
   * @param isFile if a fileReader or a stringReader should be used
   * @return the parsed object data
   */
  public GeoJsonObject createGeoJson(String filePath, Boolean isFile) {
    try {
      // ***************** READING THE FILE *****************
      Reader jsonReader;
      if (isFile) {
        jsonReader = new FileReader(filePath);
      } else {
        jsonReader = new StringReader(filePath);
      }
      BufferedReader br = new BufferedReader(jsonReader);
      String fileString = "";
      String line = br.readLine();
      while (line != null) {
        fileString = fileString + line;
        line = br.readLine();
      }
      jsonReader.close();

      // ****************** CREATING THE ADAPTER **********
      return fromJsonGeneral(fileString, GeoJsonObject.class);

    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }
}
