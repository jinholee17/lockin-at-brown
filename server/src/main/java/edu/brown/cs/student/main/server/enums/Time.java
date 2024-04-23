package edu.brown.cs.student.main.server.enums;

public enum Time {
  MORNING,
  EARLY_AFTERNOON,
  LATE_AFTERNOON,
  EVENING,

  NIGHT;

  public static Time convertTime(String time) {
    return switch (time) {
      case "7 am - 12 pm" -> MORNING;
      case "12 pm - 4 pm" -> EARLY_AFTERNOON;
      case "4 pm - 8 pm" -> LATE_AFTERNOON;
      case "8 pm - 12 am" -> EVENING;
      default -> NIGHT;
    };
  }
}
