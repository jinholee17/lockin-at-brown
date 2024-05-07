package edu.brown.cs.student.main.server.Enums;

/** Enum class responsible for representing traffic at a study spot */
public enum Traffic {
  BARELY,
  LIGHT,
  MODERATE,
  HEAVY;

  /**
   * Method that converts a string value from CSV file to the ENUM
   *
   * @param traffic
   * @return
   */
  public static Traffic convertTraffic(String traffic) {
    return switch (traffic) {
      case "Barely any traffic" -> BARELY;
      case "Light traffic" -> LIGHT;
      case "Moderate traffic" -> MODERATE;
      default -> HEAVY;
    };
  }
}
