package edu.brown.cs.student.main.server.creator;

import java.util.List;

public interface CreatorFromRow<T> {
  T create(List<String> row);
}
