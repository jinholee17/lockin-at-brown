package edu.brown.cs.student.main.server.Creator;

import java.util.List;

/**
 * CreatorFromRow interface that allows the class that implements it create any object from row.
 *
 * @param <T>
 */
public interface CreatorFromRow<T> {
  T create(List<String> row);
}
