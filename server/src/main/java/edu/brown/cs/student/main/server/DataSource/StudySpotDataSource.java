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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class StudySpotDataSource {
  public List<StudySpot> parsed;
  public CreatorFromRow<StudySpot> studySpotCreator;

  public StudySpotDataSource() throws IOException {
    String workingDirectory = System.getProperty("user.dir");
    String path = Paths.get(workingDirectory, "data", "data.csv").toString();
    this.parse(path);
  }

  public void parse(String path) throws IOException {
    this.studySpotCreator = new StudySpotCreator();
    FileReader reader = new FileReader(path);
    StudySpotParser<StudySpot> parser = new StudySpotParser<>(reader, this.studySpotCreator);
    this.parsed = parser.parse();
  }

  private static void addToBestThree(List<StudySpot> bestThree, StudySpot s, int score) {
    bestThree.add(s);
  }

  /**
   * Point Algorithm (Rough Draft)
   *
   * @param volume
   * @param traffic
   * @param capacity
   * @return
   */
  public List<StudySpot> match(
      Volume volume,
      Traffic traffic,
      Capacity capacity,
      Double lon,
      Double lat,
      String accessible,
      String whiteboard,
      String aes) {

    int maxIndex = 0;
    double maxScore = 0;
    List<Double> scores = new ArrayList<>();
    List<Integer> indices = new ArrayList<>();
    List<StudySpot> topThreeSpots = new ArrayList<>();

    for (int i = 0; i < this.parsed.size(); i++) {
      StudySpot s = this.parsed.get(i);
      double score = 0;
      if (volume != null && volume.equals(s.volume)) {
        score += 1;
      }
      if (traffic != null && traffic.equals(s.traffic)) {
        score += 1;
      }
      if (capacity != null && capacity.equals(s.capacity)) {
        score += 1;
      }
      if (aes != null) {
        if(aes.equals("yes"))
        {
          if(s.aesthetic.equals("good")){
            score += 1;
          }
          if(s.aesthetic.equals("medium")){
            score += 0.5;
          }
          if(s.aesthetic.equals("bad")){
            score -= 1;
          }
        }
      }
      if (accessible != null) {
        if (accessible.equals("yes") || accessible.equals("true")) {
          score += s.accessibility;
        }
      }
      if (whiteboard != null) {
        if (whiteboard.equals("yes") || whiteboard.equals("true")) {
          if (s.whiteboard) {
            score += 1;
          }
        }
      }
      // subtracts by 'how close' a study spot is by weight of 3*difference
      if (lon != null && lat != null) {
        score -= getClose(lon, lat, s.longitude, s.latitude);
      }
      if (s.time.equals(this.getTime())) {
        score += 1;
      }
      //      if (score > maxScore) {
      //        maxScore = score;
      //        maxIndex = i;
      //      }
      scores.add(score);
      indices.add(i);
    }
    indices.sort(Comparator.comparingDouble(scores::get));
    List<Integer> topThreeIndices = indices.subList(Math.max(0, indices.size() - 3), indices.size());
    for (Integer index : topThreeIndices) {
      topThreeSpots.add(this.parsed.get(index));
    }
    //    System.out.println(maxScore);
    return topThreeSpots;
  }

  public Time getTime() {
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

  /**
   * Subtracts the difference of coordinates- can be adjusted based on how much you want location to
   * be a weight in the algorithm. If a place is close by, it will return < 0.01. If a place is far,
   * it will return > 0.4
   *
   * @param mylon
   * @param mylat
   * @param lon
   * @param lat
   * @return
   */
  public double getClose(Double mylon, Double mylat, Double lon, Double lat) {
    return Math.abs(3 * (mylon - lon + (mylat - lat)));
  }

  public List<StudySpot> getParsed() {
    return this.parsed;
  }
}
