package edu.brown.cs.student.main.server.DataSource;

public class Location {
  private String name;
  private double longitude;
  private double latitude;
  private String yelpID;

  /**
   * Type class for a location, to store data parsed from the json
   * @param name
   * @param longitude
   * @param latitude
   * @param yelpID
   */
  public Location(String name, double longitude, double latitude, String yelpID) {
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
    this.yelpID = yelpID;
  }

  /**
   * Returns name of the location
   * @return name of the location
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the longitude of the location
   * @return Longitude of the location
   */
  public double getLongitude() {
    return this.longitude;
  }

  /**
   * Returns the latitude of the location
   * @return Latitude of the location
   */
  public double getLatitude() {
    return this.latitude;
  }

  /**
   * Returns the YelpID of the location
   * @return YelpID for the location
   */
  public String getYelpID() {
    return this.yelpID;
  }
}
