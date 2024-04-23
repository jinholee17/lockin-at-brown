package edu.brown.cs.student.main.server.DataSource;

import edu.brown.cs.student.main.server.Parser.GeoJsonObject;

/** Flexible interface provided to red-line data related handlers */
public interface GeoJSONData {
  GeoJsonObject returnParseGeoJson();
}
