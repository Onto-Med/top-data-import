package care.smith.top.top_data_import;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class Util {

  public static boolean isNumber(String s) {
    try {
      Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public static boolean isBoolean(String s) {
    return "true".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s);
  }

  public static boolean isDate(String s) {
    try {
      parseDate(s);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }

  private static DateTimeFormatter parseFormatter =
      new DateTimeFormatterBuilder()
          .appendPattern(
              "[yyyy-MM-dd'T'HH:mm:ss.SSS][yyyy-MM-dd'T'HH:mm:ss.SS][yyyy-MM-dd'T'HH:mm:ss.S][yyyy-MM-dd'T'HH:mm:ss][yyyy-MM-dd'T'HH:mm][yyyy-MM-dd][dd.MM.yyyy][dd.MM.yy]")
          .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
          .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
          .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
          .toFormatter();

  public static LocalDateTime parseDate(String dateTime) {
    return LocalDateTime.parse(dateTime, parseFormatter);
  }
}
