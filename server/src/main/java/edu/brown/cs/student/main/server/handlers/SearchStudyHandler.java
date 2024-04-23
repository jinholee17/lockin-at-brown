package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.DataSource.StudySpotDataSource;
import edu.brown.cs.student.main.server.Parser.StudySpot;
import edu.brown.cs.student.main.server.enums.Capacity;
import edu.brown.cs.student.main.server.enums.Traffic;
import edu.brown.cs.student.main.server.enums.Volume;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchStudyHandler implements Route {
  private final StudySpotDataSource studySpotDataSource;

  public SearchStudyHandler(StudySpotDataSource studySpotDataSource) {
    this.studySpotDataSource = studySpotDataSource;
  }

  @Override
  public Object handle(Request request, Response response) throws IOException {
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshiReturn = new Moshi.Builder().build();
    JsonAdapter<Map<String, StudySpot>> adapterReturn =
        moshiReturn.adapter(Types.newParameterizedType(Map.class, String.class, StudySpot.class));
    try {
      // collect parameters from the request
      String vol = request.queryParams("volume");
      String tra = request.queryParams("traffic");
      String cap = request.queryParams("capacity");
      //accessible and whiteboard can be 'yes' or 'true', doesn't matter elsewise
      String acc = request.queryParams("accessible");
      String whi = request.queryParams("whiteboard");
      Double lon = Double.parseDouble(request.queryParams("lon"));
      Double lat = Double.parseDouble(request.queryParams("lat"));

      Volume volume = null;
      Traffic traffic = null;
      Capacity capacity = null;

      if (vol != null) {
        volume = Volume.convertVolume(vol);
      }

      if (tra != null) {
        traffic = Traffic.convertTraffic(tra);
      }

      if (cap != null) {
        capacity = Capacity.convertCapacity(cap);
      }

      StudySpot studySpot = this.studySpotDataSource.match(volume, traffic, capacity,lon,lat,acc,whi);
      responseMap.put("Result", studySpot);

    } catch (Exception e) {
      // error likely occurred in the storage handler
      e.printStackTrace();
      responseMap.put("Response Type", "Failure");
      responseMap.put("error", e.getMessage());
    }
    // Returns filtered data
    responseMap.put("Response Type", "Success");
    return Utils.toMoshiJson(responseMap);
  }
}
