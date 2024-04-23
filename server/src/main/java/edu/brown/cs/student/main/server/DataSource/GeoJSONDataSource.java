package edu.brown.cs.student.main.server.DataSource;

import edu.brown.cs.student.main.server.Parser.GeoJsonObject;
import edu.brown.cs.student.main.server.Parser.GeoJsonParser;

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
