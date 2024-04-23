package edu.brown.cs.student.main.server.DataSource;

import edu.brown.cs.student.main.server.Parser.StudySpot;
import edu.brown.cs.student.main.server.Parser.StudySpotParser;
import edu.brown.cs.student.main.server.creator.CreatorFromRow;
import edu.brown.cs.student.main.server.creator.StudySpotCreator;
import edu.brown.cs.student.main.server.enums.Capacity;
import edu.brown.cs.student.main.server.enums.Time;
import edu.brown.cs.student.main.server.enums.Traffic;
import edu.brown.cs.student.main.server.enums.Volume;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.nio.file.Paths;
import java.util.List;

public class StudySpotDataSource {
  public List<StudySpot> parsed;
  public CreatorFromRow<StudySpot> studySpotCreator;

  public StudySpotDataSource() throws IOException {
    String workingDirectory = System.getProperty("user.dir");
    String path =
        Paths.get(workingDirectory, "data", "data.csv").toString();
    this.parse(path);
  }

  public void parse(String path) throws IOException {
    this.studySpotCreator = new StudySpotCreator();
    FileReader reader = new FileReader(path);
    StudySpotParser<StudySpot> parser = new StudySpotParser<>(reader, this.studySpotCreator);
    this.parsed = parser.parse();
  }

  /**
   * Point Algorithm (Rough Draft)
   *
   * @param volume
   * @param traffic
   * @param capacity
   * @return
   */
  public StudySpot match(Volume volume, Traffic traffic, Capacity capacity) {
    int maxIndex = 0;
    int maxScore = 0;

    // TODO: Account for current date and time

    for (int i = 0; i < this.parsed.size(); i++) {
      StudySpot s = this.parsed.get(i);
      int score = 0;
      if (volume != null && volume.equals(s.volume)) {
        score += 1;
      }
      if (traffic != null && traffic.equals(s.traffic)) {
        score += 1;
      }
      if (capacity != null && capacity.equals(s.capacity)) {
        score += 1;
      }
      score += s.accessibility;
      if (s.whiteboard) {
        score += 1;
      }
      if (s.time.equals(this.getTime())){
        score += 1;
      }
      if (score > maxScore) {
        maxScore = score;
        maxIndex = i;
      }
    }

    return this.parsed.get(maxIndex);
  }

  public Time getTime(){
    Calendar calendar = Calendar.getInstance();
    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
    Time timePeriod;
    if (currentHour >= 7 && currentHour < 12) {
      timePeriod = Time.MORNING;
    } else if (currentHour >= 12 && currentHour < 16) {
      timePeriod = Time.EARLY_AFTERNOON;
    } else if (currentHour >= 16 && currentHour < 20) {
      timePeriod = Time.LATE_AFTERNOON;
    } else {
      timePeriod = Time.EVENING;
    }
    return timePeriod;
  }
  public List<StudySpot> getParsed() {
    return this.parsed;
  }
}
