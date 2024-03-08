package care.smith.top.top_data_import.csv;

import care.smith.top.model.DataType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class CSVField {

  private String name;
  private DataType dataType;
  private String value;
  private int index;

  public CSVField(String name) {
    if (name == null || name.isBlank())
      throw new IllegalArgumentException("The CSV header fields must not be empty!");
    this.name = name.trim();
  }

  public CSVField(CSVField field, String value, int index) {
    this.name = field.getName();
    this.dataType = field.getDataType();
    if (value != null) this.value = value.trim();
    this.index = index;
  }

  public String getName() {
    return name;
  }

  public DataType getDataType() {
    return dataType;
  }

  public String getValue() {
    return value;
  }

  public int getIndex() {
    return index;
  }

  public BigDecimal getNumberValue() {
    return new BigDecimal(value);
  }

  public LocalDateTime getDateValue() {
    return parseDate(value);
  }

  public boolean getBooleanValue() {
    return Boolean.valueOf(value);
  }

  public void setDataType(String val) {
    if (dataType == DataType.STRING || val == null || val.isBlank()) return;
    val = val.trim();
    if (isNumber(val)) {
      if (dataType == null) dataType = DataType.NUMBER;
      else if (dataType != DataType.NUMBER) dataType = DataType.STRING;
    } else if (isDate(val)) {
      if (dataType == null) dataType = DataType.DATE_TIME;
      else if (dataType != DataType.DATE_TIME) dataType = DataType.STRING;
    } else if (isBoolean(val)) {
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
    return name + ":" + value + ":" + dataType + ":" + index;
  }
}
