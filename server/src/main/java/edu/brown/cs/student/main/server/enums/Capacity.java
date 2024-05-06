package edu.brown.cs.student.main.server.enums;

/**
 * Enum class responsible for representing the group capacity for a study spot
 */
public enum Capacity {
  ALONE,
  SMALL,
  MEDIUM,
  BIG;

  /**
   * Method that converts a string value from CSV file to the ENUM
   * @param capacity
   * @return
   */
  public static Capacity convertCapacity(String capacity) {
    return switch (capacity) {
      case "1 person (Studying alone)" -> ALONE;
      case "2-4 people" -> SMALL;
      case "4-8 people" -> MEDIUM;
      default -> BIG;
    };
  }
}
