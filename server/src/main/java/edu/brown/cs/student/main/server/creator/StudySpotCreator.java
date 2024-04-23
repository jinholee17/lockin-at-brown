package edu.brown.cs.student.main.server.creator;

import edu.brown.cs.student.main.server.Parser.StudySpot;
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
    switch (rowLength) {
      case 10:
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
                row.get(9));
        return studySpot;
      case 9:
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
                row.get(8));
        return studySpot;
      case 8:
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
                row.get(7));
        return studySpot;
      default:
        accessibility = new ArrayList<String>();
        accessibility.add(row.get(5));
        studySpot =
            new StudySpot(
                row.get(0),
                row.get(1),
                row.get(2),
                row.get(3),
                row.get(4),
                accessibility,
                row.get(6));
        return studySpot;
    }
  }
}
