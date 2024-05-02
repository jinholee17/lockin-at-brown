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
   * getter methods for all the location fields
   * @return
   */
  public String getName() {
    return this.name;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public String getYelpID() {
    return this.yelpID;
  }
}
