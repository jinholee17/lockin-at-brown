package edu.brown.cs.student.main.server.Storage;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseUtilities implements StorageInterface {

  /**
   * Class which interacts with firebase
   *
   * @throws IOException
   */
  public FirebaseUtilities() throws IOException {

    String workingDirectory = System.getProperty("user.dir");
    Path firebaseConfigPath =
        Paths.get(workingDirectory, "src", "main", "resources", "firebase_config.json");

    FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath.toString());

    FirebaseOptions options =
        new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

    FirebaseApp.initializeApp(options);
  }

  /**
   * Retrieves data from firestore
   *
   * @param uid
   * @param collection_id
   * @return
   * @throws InterruptedException
   * @throws ExecutionException
   * @throws IllegalArgumentException
   */
  @Override
  public List<Map<String, Object>> getCollection(String uid, String collection_id)
      throws InterruptedException, ExecutionException, IllegalArgumentException {
    if (uid == null || collection_id == null) {
      throw new IllegalArgumentException("getCollection: uid and/or collection_id cannot be null");
    }

    Firestore db = FirestoreClient.getFirestore();
    // 1: Make the data payload to add to your collection
    CollectionReference dataRef = db.collection("users").document(uid).collection(collection_id);

    // 2: Get pin documents
    QuerySnapshot dataQuery = dataRef.get().get();

    // 3: Get data from document queries
    List<Map<String, Object>> data = new ArrayList<>();

    for (QueryDocumentSnapshot doc : dataQuery.getDocuments()) {

      data.add(doc.getData());
    }

    return data;
  }

  /**
   * Adds document to firestore
   *
   * @param uid
   * @param collection_id
   * @param doc_id
   * @param data
   * @throws IllegalArgumentException
   */
  @Override
  public void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data)
      throws IllegalArgumentException {
    if (uid == null || collection_id == null || doc_id == null || data == null) {
      throw new IllegalArgumentException(
          "addDocument: uid, collection_id, doc_id, or data cannot be null");
    }

    Firestore db = FirestoreClient.getFirestore();
    // 1: Get a ref to the collection that you created
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collection_id);

    // 2: Write data to the collection ref
    collectionRef.document(doc_id).set(data);
  }

  /** clears the collections inside of a specific user. */
  @Override
  public void clearUser(String uid) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
    try {
      // removes all data for user 'uid'
      Firestore db = FirestoreClient.getFirestore();
      // 1: Get a ref to the user document
      DocumentReference userDoc = db.collection("users").document(uid);
      // 2: Delete the user document
      deleteDocument(userDoc);
    } catch (Exception e) {
      System.err.println("Error removing user : " + uid);
      System.err.println(e.getMessage());
    }
  }

  /**
   * Deletes a word from firestore
   *
   * @param uid
   * @param word
   * @throws IllegalArgumentException
   */
  @Override
  public void deleteWord(String uid, String word) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
    try {

      System.out.println("word: " + word);
      // removes all data for user 'uid'
      Firestore db = FirestoreClient.getFirestore();
      // 1: Get the words collection
      // List<Map<String, Object>> words = getCollection(uid, "words");
      CollectionReference words = db.collection("users").document(uid).collection("words");

      // get all documents in the collection words
      ApiFuture<QuerySnapshot> future = words.get();
      List<QueryDocumentSnapshot> documents = future.get().getDocuments();

      // delete document if document content is words
      for (QueryDocumentSnapshot doc : documents) {
        if (doc.getReference().toString().contains(word)) {
          doc.getReference().delete();
        }
      }
    } catch (Exception e) {
      System.err.println("Error removing pins : " + uid);
      System.err.println(e.getMessage());
    }
  }

  /** clears the collections inside of a specific user. */
  @Override
  public void clearPins(String uid) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
    try {
      // removes all data for user 'uid'
      Firestore db = FirestoreClient.getFirestore();
      // 1: Get a ref to the pins document
      CollectionReference pinsCollection = db.collection("users").document(uid).collection("pins");
      // 2: Delete the user document
      deleteCollection(pinsCollection);
    } catch (Exception e) {
      System.err.println("Error removing pins : " + uid);
      System.err.println(e.getMessage());
    }
  }

  /**
   * Deletes a document from firestore
   *
   * @param doc
   */
  private void deleteDocument(DocumentReference doc) {
    // for each subcollection, run deleteCollection()
    Iterable<CollectionReference> collections = doc.listCollections();
    for (CollectionReference collection : collections) {
      deleteCollection(collection);
    }
    // then delete the document
    doc.delete();
  }

  // recursively removes all the documents and collections inside a collection
  // https://firebase.google.com/docs/firestore/manage-data/delete-data#collections
  private void deleteCollection(CollectionReference collection) {
    try {

      // get all documents in the collection
      ApiFuture<QuerySnapshot> future = collection.get();
      List<QueryDocumentSnapshot> documents = future.get().getDocuments();

      // delete each document
      for (QueryDocumentSnapshot doc : documents) {
        doc.getReference().delete();
      }

      // NOTE: the query to documents may be arbitrarily large. A more robust
      // solution would involve batching the collection.get() call.
    } catch (Exception e) {
      System.err.println("Error deleting collection : " + e.getMessage());
    }
  }
}
