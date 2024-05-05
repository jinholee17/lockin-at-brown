package edu.brown.cs.student.backendUnitTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.main.server.DataSource.Location;
import edu.brown.cs.student.main.server.DataSource.StudySpotDataSource;
import edu.brown.cs.student.main.server.Parser.StudySpot;
import edu.brown.cs.student.main.server.creator.StudySpotCreator;
import edu.brown.cs.student.main.server.enums.Time;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import org.junit.jupiter.api.Test;

public class backendUnitTests {

  /**
   * StudySpotDataSource correctly parses ./data/data.csv into study spots
   * @throws IOException
   */
  @Test
  public void studySpotParsingTest() throws IOException {
    StudySpotDataSource source = new StudySpotDataSource();
    String workingDirectory = System.getProperty("user.dir");
    String path = Paths.get(workingDirectory, "data", "data.csv").toString();
    source.parse(path);
    List<StudySpot> parsed = source.getParsed();

    assertEquals(parsed.get(0).name, "85 waterman basement tables");
    assertEquals(parsed.get(0).volume, "Quiet");
    assertEquals(parsed.get(0).time, Time.EARLY_AFTERNOON);
    assertEquals(parsed.get(71).name,"watson institute first floor classrooms");
    assertEquals(parsed.get(71).time, Time.EVENING);
    assertEquals(parsed.size(), 72);
  }

  /**
   * Tests that StudySpotCreator.java correctly parses data/locationcoords.json
   * to correctly retrieve coordinates for a study spot
   */
  @Test
  public void locationCoordTest() throws FileNotFoundException {
    StudySpotCreator creator = new StudySpotCreator();
    Location erc = creator.getCoords("erc");
    assertEquals(erc.getLatitude(),41.826633629712795);
    assertEquals(erc.getLongitude(),-71.39810534483652);

    Location newwatson = creator.getCoords("new watson");
    assertEquals(newwatson.getLatitude(),41.82423800342244);
    assertEquals(newwatson.getLongitude(),-71.39898933126929);

    List<Location> locData = creator.getLocationData().getLocations();
    assertEquals(locData.get(0).getName(),"new watson");
    assertEquals(locData.get(1).getName(),"the hay");
    assertEquals(locData.size(),70);
  }

  /**
   * Tests that correct time is calculated for study spot data source
   * @throws IOException
   */
  @Test
  public void getTimeTest() throws IOException {
    StudySpotDataSource source = new StudySpotDataSource();
    Time time = source.getTime();

    Calendar calendar = Calendar.getInstance();
    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    if (time.equals(Time.MORNING))
    {
      assertTrue(currentHour >= 7 && currentHour < 12);
    }
    else if (time.equals(Time.EARLY_AFTERNOON))
    {
      assertTrue(currentHour >= 12 && currentHour < 16);
    }
    else if (time.equals(Time.LATE_AFTERNOON))
    {
      assertTrue(currentHour >= 16 && currentHour < 20);
    }
    else {
      assertTrue(time.equals(Time.EVENING));
    }
  }

  /**
   * Tests the getClose method in StudySpotDataSource to ensure
   * it is returning the correct difference between lon/lat of 2 coords
   */
  @Test
  public void getCloseTest() throws IOException {
    StudySpotDataSource source = new StudySpotDataSource();
    assertEquals(source.getClose(41.82464875822009,-71.3965891283718,0.0,0.0),59.143880740303416);
    assertEquals(source.getClose(41.82464875822009,-71.3965891283718,41.83025426633095,-71.40281532445142),0.0012413759375391464);
  }

  /**
   * Tests for the matching algorithm, ensuring that the correct
   * study spots are being returned
   */
  @Test
  public void matchTest() throws IOException {
    StudySpotDataSource source = new StudySpotDataSource();
    boolean erc = false;
    List<StudySpot> results = source.match("Conversational","Heavy Traffic","4-8 people",-71.39810534483652, 41.826633629712795, "yes","Yes","yes");
    for (StudySpot spot : results){
      System.out.println(spot.name);
      if (spot.name.equals("erc")){
        erc = true;
      }
    }
    assertTrue(erc);
  }
}
