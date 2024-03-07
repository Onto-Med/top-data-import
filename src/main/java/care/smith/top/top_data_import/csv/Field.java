package care.smith.top.top_data_import.csv;

import care.smith.top.model.DataType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class Field {

  private String value;
  private DataType dataType;

  public Field(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(String value) {
    if (dataType == DataType.STRING || value == null || value.isBlank()) return;
    if (isNumber(value)) {
      if (dataType == null) dataType = DataType.NUMBER;
      else if (dataType != DataType.NUMBER) dataType = DataType.STRING;
    } else if (isDate(value)) {
      if (dataType == null) dataType = DataType.DATE_TIME;
      else if (dataType != DataType.DATE_TIME) dataType = DataType.STRING;
    } else if (isBoolean(value)) {
      if (dataType == null) dataType = DataType.BOOLEAN;
      else if (dataType != DataType.BOOLEAN) dataType = DataType.STRING;
    } else dataType = DataType.STRING;
  }

  private boolean isNumber(String v) {
    try {
      Double.parseDouble(v);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private boolean isBoolean(String v) {
    return "true".equalsIgnoreCase(v) || "false".equalsIgnoreCase(v);
  }

  private boolean isDate(String v) {
    try {
      parseDate(v);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }

  private static DateTimeFormatter parseFormatter =
      new DateTimeFormatterBuilder()
          .appendPattern(
              "[yyyy-MM-dd'T'HH:mm:ss][yyyy-MM-dd'T'HH:mm][yyyy-MM-dd][dd.MM.yyyy][dd.MM.yy]")
          .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
          .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
          .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
          .toFormatter();

  public static LocalDateTime parseDate(String dateTime) {
    return LocalDateTime.parse(dateTime, parseFormatter);
  }

  @Override
  public String toString() {
    return value + ":" + dataType;
  }
}
