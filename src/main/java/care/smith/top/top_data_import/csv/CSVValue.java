package care.smith.top.top_data_import.csv;

import care.smith.top.model.DataType;
import care.smith.top.top_data_import.Util;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CSVValue {

  private CSVHead head;
  private String value;

  public CSVValue(CSVHead head, String value) {
    this.head = head;
    if (value != null && !value.isBlank()) this.value = value.trim();
  }

  public CSVValue(String name, String value, DataType dataType, String codeSystem, String code) {
    this(new CSVHead(name, dataType, codeSystem, code), value);
  }

  public CSVValue(String name, String value, DataType dataType) {
    this(new CSVHead(name, dataType), value);
  }

  public String getName() {
    return head.getName();
  }

  public DataType getDataType() {
    return head.getDataType();
  }

  public String getValue() {
    return value;
  }

  public int getIndex() {
    return head.getIndex();
  }

  public String getCodeSystem() {
    return head.getCodeSystem();
  }

  public String getCode() {
    return head.getCode();
  }

  public String getUnit() {
    return head.getUnit();
  }

  public BigDecimal getNumberValue() {
    return (value == null || getDataType() != DataType.NUMBER) ? null : new BigDecimal(value);
  }

  public LocalDateTime getDateValue() {
    return (value == null || getDataType() != DataType.DATE_TIME) ? null : Util.parseDate(value);
  }

  public Boolean getBooleanValue() {
    return (value == null || getDataType() != DataType.BOOLEAN) ? null : Boolean.valueOf(value);
  }

  public String getStringValue() {
    return (getDataType() != DataType.STRING) ? null : value;
  }

  @Override
  public String toString() {
    return head + "|" + value;
  }
}
