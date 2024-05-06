package edu.brown.cs.student.main.server.Parser;

import edu.brown.cs.student.main.server.creator.CreatorFromRow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class that parses a study spot
 * @param <T>
 */
public class StudySpotParser<T> {
  BufferedReader reader;
  CreatorFromRow<T> toCreate;
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  /**
   * Constructor for the StudySpotParser
   * @param reader BufferedReader for reading the CSV file
   * @param toCreate Creator object for StudySpot
   */
  public StudySpotParser(Reader reader, CreatorFromRow<T> toCreate) {
    this.reader = new BufferedReader(reader);
    this.toCreate = toCreate;
  }

  /**
   * Given a file which is read into the buffered reader, this parses it into
   * a list of a generic object
   * @return
   * @throws IOException
   */
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

  /**
   * Removes whitespaces and quotes from a string
   * @param arg
   * @return
   */
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
