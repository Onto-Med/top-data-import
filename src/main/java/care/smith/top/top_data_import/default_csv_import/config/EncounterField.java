package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.model.DataType;
import java.util.EnumMap;

public enum EncounterField implements Field {
  ENCOUNTER_ID("encounter_id", "text NOT NULL", DataType.STRING),
  SUBJECT_ID("subject_id", "text", DataType.STRING),
  TYPE("type", "text", DataType.STRING),
  START_DATE_TIME("start_date_time", "timestamp", DataType.DATE_TIME),
  END_DATE_TIME("end_date_time", "timestamp", DataType.DATE_TIME);

  public static final String TABLE_NAME = "encounter";

  private String fieldName;
  private String dbProperties;
  private DataType dataType;

  private EncounterField(String fieldName, String dbProperties, DataType dataType) {
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

  public static EnumMap<EncounterField, String> getFields() {
    EnumMap<EncounterField, String> fields = new EnumMap<>(EncounterField.class);
    for (EncounterField field : EncounterField.values()) fields.put(field, field.getFieldName());
    return fields;
  }
}
