package edu.brown.cs.student.main.server.Parser;

import edu.brown.cs.student.main.server.enums.Capacity;
import edu.brown.cs.student.main.server.enums.Time;
import edu.brown.cs.student.main.server.enums.Traffic;
import edu.brown.cs.student.main.server.enums.Volume;
import java.util.List;

public class StudySpot {
  public String name;
  public Time time;
  public Volume volume;
  public Traffic traffic;
  public Capacity capacity;
  public boolean whiteboard;
  public int accessibility;

  public StudySpot(
      String name,
      String time,
      String volume,
      String traffic,
      String capacity,
      List<String> accessibility,
      String whiteboard) {
    this.name = name;
    this.time = Time.convertTime(time);
    this.volume = Volume.convertVolume(volume);
    this.traffic = Traffic.convertTraffic(traffic);
    this.whiteboard = whiteboard.equals("Yes");
    this.capacity = Capacity.convertCapacity(capacity);
    this.accessibility = accessibility.size();
  }
}
