package care.smith.top.top_data_import.csv;

import care.smith.top.model.DataType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class CSVHead {

  private String name;
  private DataType dataType;
  private String codeSystem;
  private String code;
  private String unit;
  private int index;

  public CSVHead(String name) {
    if (name == null || name.isBlank())
      throw new IllegalArgumentException("The CSV header fields must not be empty!");
    this.name = name.trim();
  }

  public CSVHead(String name, DataType dataType, String codeSystem, String code) {
    this(name);
    this.dataType = dataType;
    this.codeSystem = codeSystem;
    this.code = code;
  }

  public CSVHead(String name, DataType dataType) {
    this(name);
    this.dataType = dataType;
  }

  public String getName() {
    return name;
  }

  public CSVHead name(String name) {
    this.name = name;
    return this;
  }

  public DataType getDataType() {
    return dataType;
  }

  public CSVHead dataType(DataType dataType) {
    this.dataType = dataType;
    return this;
  }

  public String getCodeSystem() {
    return codeSystem;
  }

  public CSVHead codeSystem(String codeSystem) {
    this.codeSystem = codeSystem;
    return this;
  }

  public String getCode() {
    return code;
  }

  public CSVHead code(String code) {
    this.code = code;
    return this;
  }

  public CSVHead code(String[] code) {
    return code(code[0], code[1]);
  }

  public CSVHead code(String codeSystem, String code) {
    this.codeSystem = codeSystem;
    this.code = code;
    return this;
  }

  public String getUnit() {
    return unit;
  }

  public CSVHead unit(String unit) {
    this.unit = unit;
    return this;
  }

  public int getIndex() {
    return index;
  }

  public CSVHead index(int index) {
    this.index = index;
    return this;
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
              "[yyyy-MM-dd'T'HH:mm:ss.SSS][yyyy-MM-dd'T'HH:mm:ss.SS][yyyy-MM-dd'T'HH:mm:ss.S][yyyy-MM-dd'T'HH:mm:ss][yyyy-MM-dd'T'HH:mm][yyyy-MM-dd][dd.MM.yyyy][dd.MM.yy]")
          .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
          .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
          .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
          .toFormatter();

  public static LocalDateTime parseDate(String dateTime) {
    return LocalDateTime.parse(dateTime, parseFormatter);
  }

  @Override
  public String toString() {
    return name + ":" + dataType + ":" + codeSystem + ":" + code + ":" + index;
  }
}
