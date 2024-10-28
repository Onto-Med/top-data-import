package care.smith.top.top_data_import.csv;

import care.smith.top.model.DataType;
import care.smith.top.top_data_import.Util;

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
    } else if (Util.isDate(val)) {
      if (dataType == null) dataType = DataType.DATE_TIME;
      else if (dataType != DataType.DATE_TIME) dataType = DataType.STRING;
    } else if (Util.isBoolean(val)) {
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

  @Override
  public String toString() {
    return name + ":" + dataType + ":" + codeSystem + ":" + code + ":" + index;
  }
}
