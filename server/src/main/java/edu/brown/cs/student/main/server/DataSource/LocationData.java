package edu.brown.cs.student.main.server.DataSource;

import java.util.List;

/**
 * Class for location data, which is a list of locations
 */
public class LocationData {
  private List<Location> locations;

  /**
   * Returns the list of locations
   * @return the list of location data
   */
  public List<Location> getLocations() {
    return this.locations;
  }
}
