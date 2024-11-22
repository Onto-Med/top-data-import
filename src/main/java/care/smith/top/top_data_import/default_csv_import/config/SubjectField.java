package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.model.DataType;
import java.util.EnumMap;

public enum SubjectField implements Field {
  SUBJECT_ID("subject_id", "text NOT NULL", DataType.STRING),
  BIRTH_DATE("birth_date", "timestamp", DataType.DATE_TIME),
  SEX("sex", "text", DataType.STRING);

  public static final String TABLE_NAME = "subject";

  private String fieldName;
  private String dbProperties;
  private DataType dataType;

  private SubjectField(String fieldName, String dbProperties, DataType dataType) {
    this.fieldName = fieldName;
    this.dbProperties = dbProperties;
    this.dataType = dataType;
  }

  @Override
  public String getFieldName() {
    return fieldName;
  }

  @Override
  public String getDBProperties() {
    return dbProperties;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public String getPropertyName() {
    return TABLE_NAME + "." + getFieldName();
  }

  public static EnumMap<SubjectField, String> getFields() {
    EnumMap<SubjectField, String> fields = new EnumMap<>(SubjectField.class);
    for (SubjectField field : SubjectField.values()) fields.put(field, field.getFieldName());
    return fields;
  }
}
