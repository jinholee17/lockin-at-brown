package edu.brown.cs.student.main.server.creator;

import com.google.gson.Gson;
import edu.brown.cs.student.main.server.DataSource.Location;
import edu.brown.cs.student.main.server.DataSource.LocationData;
import edu.brown.cs.student.main.server.Parser.StudySpot;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudySpotCreator implements CreatorFromRow<StudySpot> {
  List<List<String>> rows;

  public StudySpotCreator() {
    this.rows = new ArrayList<List<String>>();
  }

  @Override
  public StudySpot create(List<String> row) {
    int rowLength = row.size();
    StudySpot studySpot;
    ArrayList<String> accessibility;
    System.out.println(row);
    System.out.println(row.size());
    switch (rowLength) {
      case 11:
        accessibility = new ArrayList<String>();
        accessibility.add(row.get(5));
        accessibility.add(row.get(6));
        accessibility.add(row.get(7));
        accessibility.add(row.get(8));
        studySpot =
            new StudySpot(
                row.get(0),
                row.get(1),
                row.get(2),
                row.get(3),
                row.get(4),
                accessibility,
                row.get(9),
                this.getCoords(row.get(0)).getLongitude(),
                this.getCoords(row.get(0)).getLatitude(),
                row.get(10));
        return studySpot;
      case 10:
        accessibility = new ArrayList<String>();
        accessibility.add(row.get(5));
        accessibility.add(row.get(6));
        accessibility.add(row.get(7));
        studySpot =
            new StudySpot(
                row.get(0),
                row.get(1),
                row.get(2),
                row.get(3),
                row.get(4),
                accessibility,
                row.get(8),
                this.getCoords(row.get(0)).getLongitude(),
                this.getCoords(row.get(0)).getLatitude(),
                row.get(9));
        return studySpot;
      case 9:
        accessibility = new ArrayList<String>();
        accessibility.add(row.get(5));
        accessibility.add(row.get(6));
        studySpot =
            new StudySpot(
                row.get(0),
                row.get(1),
                row.get(2),
                row.get(3),
                row.get(4),
                accessibility,
                row.get(7),
                this.getCoords(row.get(0)).getLongitude(),
                this.getCoords(row.get(0)).getLatitude(),
                row.get(8));
        return studySpot;
      default:
        accessibility = new ArrayList<String>();
        accessibility.add(row.get(5));
        System.out.println(row);
        studySpot =
            new StudySpot(
                row.get(0),
                row.get(1),
                row.get(2),
                row.get(3),
                row.get(4),
                accessibility,
                row.get(6),
                this.getCoords(row.get(0)).getLongitude(),
                this.getCoords(row.get(0)).getLatitude(),
                row.get(7));
        return studySpot;
    }
  }

  /**
   * Returns a Location object with coordinates of that location stored
   * @param locationName
   * @return
   */
  public Location getCoords(String locationName) {
    String workingDirectory = System.getProperty("user.dir");
    String path = Paths.get(workingDirectory, "data", "locationcoords.json").toString();
    try (FileReader reader = new FileReader(path)) {
      // Parse JSON data into a custom class or map
      Gson gson = new Gson();
      LocationData locationData = gson.fromJson(reader, LocationData.class);

      // Search for a location by name
      for (Location location : locationData.getLocations()) {
        if (location.getName().equals(locationName)) {
          return location;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.err.println("Location name " + locationName + " not found in coordinates data");
    return null;
  }
}
