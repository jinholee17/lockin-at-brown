package edu.brown.cs.student.main.server.geoJSONDataSource;

import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonObject;
import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonParser;

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
