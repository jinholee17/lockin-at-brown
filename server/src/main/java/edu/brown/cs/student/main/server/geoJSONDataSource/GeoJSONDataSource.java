package edu.brown.cs.student.main.server.geoJSONDataSource;

import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonObject;
import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonParser;

/** The data source providing Redlining datasource */
public class GeoJSONDataSource implements GeoJSONData {

  /** returns the parsed red-lining json data */
  public GeoJsonObject returnParseGeoJson() {
    GeoJsonParser parser = new GeoJsonParser();
    String filePath = "./data/provided_fullDownload.geojson";
    GeoJsonObject parsedJSON = parser.createGeoJson(filePath, true);
    return new GeoJsonObject(parsedJSON); // return defensive copy instead
  }
}
