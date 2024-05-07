package edu.brown.cs.student.main.server.Parser;

import edu.brown.cs.student.main.server.Enums.Time;
import java.util.List;

/** Class that represents a study spot. */
public class StudySpot {
  public String name;
  public Time time;
  public String volume;
  public String traffic;
  public String capacity;
  public boolean whiteboard;
  public int accessibility;
  public double longitude;
  public double latitude;
  public String aesthetic;

  /**
   * Constructor for StudySpot
   *
   * @param name name of the StudySpot
   * @param time time the StudySpot information was recorded
   * @param volume volume at the StudySpot
   * @param traffic traffic at the StudySpot
   * @param capacity group capacity for the StudySpot
   * @param accessibility accessibility features at the StudySpot
   * @param whiteboard whiteboard presence at the StudySpot
   * @param longitude longitude of the StudySpot
   * @param latitude latitude of the StudySpot
   * @param aesthetic aesthetic value at the StudySpot
   */
  public StudySpot(
      String name,
      String time,
      String volume,
      String traffic,
      String capacity,
      List<String> accessibility,
      String whiteboard,
      double longitude,
      double latitude,
      String aesthetic) {
    this.name = name;
    this.time = Time.convertTime(time);
    this.volume = volume;
    this.traffic = traffic;
    this.whiteboard = whiteboard.equals("Yes");
    this.capacity = capacity;
    this.accessibility = accessibility.size();
    this.longitude = longitude;
    this.latitude = latitude;
    this.aesthetic = aesthetic;
  }
}
