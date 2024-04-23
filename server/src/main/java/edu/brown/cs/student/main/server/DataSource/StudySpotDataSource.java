package edu.brown.cs.student.main.server.DataSource;

import edu.brown.cs.student.main.server.Parser.StudySpot;
import edu.brown.cs.student.main.server.Parser.StudySpotParser;
import edu.brown.cs.student.main.server.creator.CreatorFromRow;
import edu.brown.cs.student.main.server.creator.StudySpotCreator;
import edu.brown.cs.student.main.server.enums.Capacity;
import edu.brown.cs.student.main.server.enums.Traffic;
import edu.brown.cs.student.main.server.enums.Volume;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class StudySpotDataSource {
  public List<StudySpot> parsed;
  public CreatorFromRow<StudySpot> studySpotCreator;

  public StudySpotDataSource() throws IOException {
    String path =
        "/Users/julie_chung/Desktop/cs32/term-project-jlee812-asun59-mzheng37-hchung33/server/data/data.csv";
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
      if (score > maxScore) {
        maxScore = score;
        maxIndex = i;
      }
    }

    return this.parsed.get(maxIndex);
  }

  public List<StudySpot> getParsed() {
    return this.parsed;
  }
}
