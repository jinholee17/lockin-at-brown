package edu.brown.cs.student.main.server.DataSource;

import edu.brown.cs.student.main.server.Parser.GeoJsonObject;
import edu.brown.cs.student.main.server.Parser.GeoJsonParser;

/** Class to create geoJsonmock data */
public class GeoJSONMock implements GeoJSONData {
  String filePath;

  /**
   * Constructor for processing mock data
   *
   * @param filePath the mock data source
   */
  public GeoJSONMock(String filePath) {
    this.filePath = filePath;
  }

  public GeoJsonObject returnParseGeoJson() {
    GeoJsonParser parser = new GeoJsonParser();
    GeoJsonObject parsedJSON = parser.createGeoJson(filePath, false);
    return new GeoJsonObject(parsedJSON); // return defensive copy instead
  }
}
