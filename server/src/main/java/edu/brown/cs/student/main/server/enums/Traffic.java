package edu.brown.cs.student.main.server.enums;

public enum Traffic {
  BARELY,
  LIGHT,
  MODERATE,
  HEAVY;

  public static Traffic convertTraffic(String traffic) {
    return switch (traffic) {
      case "Barely any traffic" -> BARELY;
      case "Light traffic" -> LIGHT;
      case "Moderate traffic" -> MODERATE;
      default -> HEAVY;
    };
  }
}
