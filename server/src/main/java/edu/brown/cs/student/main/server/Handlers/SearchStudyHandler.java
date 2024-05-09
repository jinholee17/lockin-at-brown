package edu.brown.cs.student.main.server.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.DataSource.StudySpotDataSource;
import edu.brown.cs.student.main.server.Parser.StudySpot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler that is responsible for returning top 3 study spots based on the user's inquiry */
public class SearchStudyHandler implements Route {
  private final StudySpotDataSource studySpotDataSource;

  public SearchStudyHandler(StudySpotDataSource studySpotDataSource) {
    this.studySpotDataSource = studySpotDataSource;
  }

  /**
   * Given parameters: volume, traffic, capacity, accessible, whiteboard,aesthetics,lon,lat Returns
   * 3 study spots
   *
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  @Override
  public Object handle(Request request, Response response) throws IOException {
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshiReturn = new Moshi.Builder().build();
    JsonAdapter<Map<String, List<StudySpot>>> adapterReturn =
        moshiReturn.adapter(
            Types.newParameterizedType(Map.class, String.class, List.class, StudySpot.class));
    try {
      // collect parameters from the request
      String vol = request.queryParams("volume");
      String tra = request.queryParams("traffic");
      String cap = request.queryParams("capacity");
      // accessible and whiteboard can be 'yes' or 'true', doesn't matter elsewise
      String acc = request.queryParams("accessible");
      String whi = request.queryParams("whiteboard");
      String aes = request.queryParams("aesthetics");
      // user's location longitude and latitude
      String checklon = request.queryParams("lon");
      String checkLat = request.queryParams("lat");

      Double lon = null;
      Double lat = null;

      if (checklon != null) {
        lon = Double.parseDouble(request.queryParams("lon"));
      }
      if (checkLat != null) {
        lat = Double.parseDouble(request.queryParams("lat"));
      }

      // Stores returned top 3 study spots
      List<StudySpot> studySpots =
          this.studySpotDataSource.match(vol, tra, cap, lon, lat, acc, whi, aes);

      Map<String, List<String>> allSpots = new HashMap<>();

      this.matchDescription(studySpots, allSpots);

      responseMap.put("Result", allSpots);
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("Response Type", "Failure");
      responseMap.put("error", e.getMessage());
    }
    // Returns filtered data
    return Utils.toMoshiJson(responseMap);
  }

  /**
   * Helper method that gets the descriptions of the matched studySpots
   *
   * @param studySpots List that stores the best three matches
   * @param allSpots List of descriptions for each match
   */
  private void matchDescription(List<StudySpot> studySpots, Map<String, List<String>> allSpots) {
    for (int i = 0; i < studySpots.size(); i++) {
      List<String> descriptions = new ArrayList<>();
      descriptions.add(studySpots.get(i).volume.toString().toLowerCase());
      descriptions.add(studySpots.get(i).capacity.toString().toLowerCase());
      descriptions.add(studySpots.get(i).traffic.toString().toLowerCase());

      String descriptString = descriptions.toString();

      List<Double> coordinates = new ArrayList<>();
      coordinates.add(studySpots.get(i).longitude);
      coordinates.add(studySpots.get(i).latitude);
      String coordsString = coordinates.toString();

      List<String> result = new ArrayList<>();

      result.add(descriptString);
      result.add(coordsString);

      allSpots.put(studySpots.get(i).name, result);
    }
  }
}
