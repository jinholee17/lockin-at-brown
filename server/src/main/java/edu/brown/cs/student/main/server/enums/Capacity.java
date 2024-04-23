package edu.brown.cs.student.main.server.enums;

public enum Capacity {
  ALONE,
  SMALL,
  MEDIUM,
  BIG;

  public static Capacity convertCapacity(String capacity) {
    return switch (capacity) {
      case "1 person (Studying alone)" -> ALONE;
      case "2-4 people" -> SMALL;
      case "4-8 people" -> MEDIUM;
      default -> BIG;
    };
  }
}
