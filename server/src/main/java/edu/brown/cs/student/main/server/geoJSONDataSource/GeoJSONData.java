package edu.brown.cs.student.main.server.geoJSONDataSource;

import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonObject;

/** Flexible interface provided to red-line data related handlers */
public interface GeoJSONData {
  GeoJsonObject returnParseGeoJson();
}
