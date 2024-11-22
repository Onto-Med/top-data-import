package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.model.DataType;
import java.util.EnumMap;

public enum PhenotypeField implements Field {
  PHENOTYPE_ID("phenotype_id", "text NOT NULL", DataType.STRING),
  ENCOUNTER_ID("encounter_id", "text", DataType.STRING),
  SUBJECT_ID("subject_id", "text", DataType.STRING),
  CODE_SYSTEM("code_system", "text NOT NULL", DataType.STRING),
  CODE("code", "text NOT NULL", DataType.STRING),
  DATE_TIME("date_time", "timestamp", DataType.DATE_TIME),
  START_DATE_TIME("start_date_time", "timestamp", DataType.DATE_TIME),
  END_DATE_TIME("end_date_time", "timestamp", DataType.DATE_TIME),
  UNIT("unit", "text", DataType.STRING),
  NUMBER_VALUE("number_value", "numeric(20,3)", DataType.NUMBER),
  TEXT_VALUE("text_value", "text", DataType.STRING),
  DATE_TIME_VALUE("date_time_value", "timestamp", DataType.DATE_TIME),
  BOOLEAN_VALUE("boolean_value", "boolean", DataType.BOOLEAN);

  public static final String TABLE_NAME = "phenotype";

  private String fieldName;
  private String dbProperties;
  private DataType dataType;

  private PhenotypeField(String fieldName, String dbProperties, DataType dataType) {
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

  public static EnumMap<PhenotypeField, String> getFields() {
    EnumMap<PhenotypeField, String> fields = new EnumMap<>(PhenotypeField.class);
    for (PhenotypeField field : PhenotypeField.values()) fields.put(field, field.getFieldName());
    return fields;
  }
}
