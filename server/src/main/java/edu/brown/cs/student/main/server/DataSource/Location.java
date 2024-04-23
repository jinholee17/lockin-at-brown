package edu.brown.cs.student.main.server.DataSource;

public class Location {
  private String name;
  private double longitude;
  private double latitude;

  public Location(String name, double longitude, double latitude) {
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public String getName() {
    return this.name;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public double getLatitude() {
    return this.latitude;
  }
}
