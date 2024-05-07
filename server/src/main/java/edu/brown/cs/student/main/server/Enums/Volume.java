package edu.brown.cs.student.main.server.Enums;

/** Enum class responsible for representing volume at a study spot */
public enum Volume {
  TOTAL_SILENCE,
  QUIET,
  CONVERSATIONAL,
  LOUD;

  /**
   * Method that converts a string value from CSV file to the ENUM
   *
   * @param volume
   * @return
   */
  public static Volume convertVolume(String volume) {
    return switch (volume) {
      case "Total Silence" -> TOTAL_SILENCE;
      case "Quiet" -> QUIET;
      case "Conversational" -> CONVERSATIONAL;
      default -> LOUD;
    };
  }
}
