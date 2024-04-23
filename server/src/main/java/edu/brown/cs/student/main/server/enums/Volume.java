package edu.brown.cs.student.main.server.enums;

public enum Volume {
  TOTAL_SILENCE,
  QUIET,
  CONVERSATIONAL,
  LOUD;

  public static Volume convertVolume(String volume) {
    return switch (volume) {
      case "Total Silence" -> TOTAL_SILENCE;
      case "Quiet" -> QUIET;
      case "Conversational" -> CONVERSATIONAL;
      default -> LOUD;
    };
  }
}
