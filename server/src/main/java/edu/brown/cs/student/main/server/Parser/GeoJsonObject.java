package edu.brown.cs.student.main.server.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the GeoJSON data - it contains a list of features, and each feature
 * contains a type, a geometry (a huge list of coordinates), and a list of properties. Each list of
 * properties contains a name, a holc grade, and a hashmap of area description data, along other
 * properties.
 */
public class GeoJsonObject {
  public String type;
  public List<Feature> features;

  public static class Feature {
    public String type;
    public Geometry geometry;
    public Properties properties;

    /**
     * Normal constructor
     *
     * @param type type
     * @param geometry geometry
     * @param properties properties
     */
    public Feature(String type, Geometry geometry, Properties properties) {
      this.type = type;
      this.geometry = geometry;
      this.properties = properties;
    }

    /**
     * Copy constructor
     *
     * @param f - object to copy from
     */
    public Feature(Feature f) {
      this.type = f.type;
      if (f.geometry != null) {
        this.geometry = new Geometry(f.geometry);
      }
      this.properties = new Properties(f.properties);
    }
  }

  public static class Geometry {
    public String type;
    public List<List<List<List<Double>>>> coordinates;

    /**
     * normal constructor
     *
     * @param type type
     * @param cord the coordinate array
     */
    public Geometry(String type, List<List<List<List<Double>>>> cord) {
      this.type = type;
      this.coordinates = cord;
    }

    /*
     * copy constructor
     * @param g to make copy from
     */
    public Geometry(Geometry g) {
      this.type = g.type;
      this.coordinates = new ArrayList<>(g.coordinates);
    }
  }

  public static class Properties {
    public String city;
    public String holc_grade;
    public Map<String, String> area_description_data;

    /**
     * normal constructor
     *
     * @param city city
     * @param holc_grade grade
     * @param area_description_data map holding descriptions
     */
    public Properties(String city, String holc_grade, Map<String, String> area_description_data) {
      this.city = city;
      this.holc_grade = holc_grade;
      this.area_description_data = area_description_data;
    }

    /**
     * copy constructor
     *
     * @param p to make copy from
     */
    public Properties(Properties p) {
      this.city = p.city;
      this.holc_grade = p.holc_grade;
      this.area_description_data = new HashMap<>(p.area_description_data);
    }
  }

  /**
   * copy constructor for defensive programming
   *
   * @param o to make copy from
   */
  public GeoJsonObject(GeoJsonObject o) {
    this.type = o.type;
    this.features = new ArrayList<>();
    for (Feature feature : o.features) {
      if (feature != null) {
        this.features.add(new Feature(feature));
      }
    }
  }
}
