package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.DataSource.*;
import edu.brown.cs.student.main.server.Handlers.*;
import edu.brown.cs.student.main.server.Storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.Storage.StorageInterface;
import java.io.IOException;
import spark.Filter;
import spark.Spark;

/** Top Level class for our project, utilizes spark to create and maintain our server. */
public class Server {
  public static void setUpServer() {
    int port = 3232;
    Spark.port(port);

    after(
        (Filter)
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            });

    StorageInterface firebaseUtils;

    try {
      firebaseUtils = new FirebaseUtilities();
      StudySpotDataSource studySpotDataSource = new StudySpotDataSource();
      ReviewDatasource reviewDatasource = new ReviewDatasource();

      // add end points
      Spark.get("delete-word", new DeleteWordHandler(firebaseUtils));
      Spark.get("list-words", new ListWordsHandler(firebaseUtils));
      Spark.get("add-word", new AddWordHandler(firebaseUtils));
      Spark.get("clear-user", new ClearUserHandler(firebaseUtils));
      Spark.get("search-study", new SearchStudyHandler(studySpotDataSource));
      Spark.get("study-review", new ReviewHandler(reviewDatasource));

      Spark.notFound(
          (request, response) -> {
            response.status(404); // Not Found
            System.out.println("ERROR");
            return "404 Not Found - The requested endpoint does not exist.";
          });
      Spark.init();
      Spark.awaitInitialization();

      System.out.println("Server started at http://localhost:" + port);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(
          "Error: Could not initialize Firebase. Likely due to firebase_config.json not being found. Exiting.");
      System.exit(1);
    }
  }

  /**
   * Runs Server.
   *
   * @param args none
   */
  public static void main(String[] args) {
    setUpServer();
  }
}
