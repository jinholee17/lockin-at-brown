package edu.brown.cs.student.main.server.Parser;

import edu.brown.cs.student.main.server.creator.CreatorFromRow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class StudySpotParser<T> {
  BufferedReader reader;
  CreatorFromRow<T> toCreate;
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  public StudySpotParser(Reader reader, CreatorFromRow<T> toCreate) {
    this.reader = new BufferedReader(reader);
    this.toCreate = toCreate;
  }

  public List<T> parse() throws IOException {
    List<T> createdList = new ArrayList<T>();
    String line = this.reader.readLine();
    while (line != null) {
      List<String> row = Arrays.asList(regexSplitCSVRow.split(line));
      row.replaceAll(StudySpotParser::postprocess);
      createdList.add(this.toCreate.create(row));
      line = this.reader.readLine();
    }
    return createdList;
  }

  public static String postprocess(String arg) {
    return arg
        // Remove extra spaces at beginning and end of the line
        .trim()
        // Remove a beginning quote, if present
        .replaceAll("^\"", "")
        // Remove an ending quote, if present
        .replaceAll("\"$", "")
        // Replace double-double-quotes with double-quotes
        .replaceAll("\"\"", "\"");
  }
}
