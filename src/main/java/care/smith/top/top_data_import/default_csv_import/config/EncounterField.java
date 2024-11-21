package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.top_data_import.default_csv_import.exception.IllegalFieldException;

public enum EncounterField {
  ENCOUNTER_ID("encounter_id", "encounterid", "id"),
  SUBJECT_ID("subject_id", "subjectid", "patientid"),
  TYPE("type", "type"),
  START_DATE_TIME("start_date_time", "startdatetime", "starttimestamp", "start", "periodstart"),
  END_DATE_TIME("end_date_time", "enddatetime", "endtimestamp", "end", "periodend");

  public static final String TABLE_NAME = "encounter";

  private String fieldName;
  private String[] allowedFieldNames;

  private EncounterField(String fieldName, String... allowedFieldNames) {
    this.fieldName = fieldName;
    this.allowedFieldNames = allowedFieldNames;
  }

  public String getFieldName() {
    return fieldName;
  }

  private String[] getAllowedFieldNames() {
    return allowedFieldNames;
  }

  public String getPropertyName() {
    return TABLE_NAME + "." + getFieldName();
  }

  public static EncounterField getField(String fieldName) throws IllegalFieldException {
    for (EncounterField field : EncounterField.values())
      if (Util.isAllowedName(fieldName, field.getAllowedFieldNames())) return field;
    throw new IllegalFieldException(fieldName, TABLE_NAME);
  }
}
