package edu.brown.cs.student.main.server.Storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** Provided in gear up, for storing information */
public interface StorageInterface {

  void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data);

  void deleteWord(String uid, String word);

  List<Map<String, Object>> getCollection(String uid, String collection_id)
      throws InterruptedException, ExecutionException;

  void clearUser(String uid) throws InterruptedException, ExecutionException;

  void clearPins(String uid) throws InterruptedException, ExecutionException;
}
