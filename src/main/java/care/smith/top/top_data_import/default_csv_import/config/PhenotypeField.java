package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.top_data_import.default_csv_import.exception.IllegalFieldException;

public enum PhenotypeField {
  PHENOTYPE_ID(
      "phenotype_id",
      "phenotypeid",
      "observationid",
      "conditionid",
      "medicationid",
      "procedureid",
      "id"),
  ENCOUNTER_ID("encounter_id", "encounterid"),
  SUBJECT_ID("subject_id", "subjectid", "patientid"),
  DATE_TIME("date_time", "datetime", "timestamp", "createdat"),
  START_DATE_TIME("start_date_time", "startdatetime", "starttimestamp", "start", "periodstart"),
  END_DATE_TIME("end_date_time", "enddatetime", "endtimestamp", "end", "periodend"),
  CODE_SYSTEM("code_system", "codesystem", "terminology"),
  CODE("code", "code"),
  UNIT(
      "unit",
      "unit",
      "unitofmeasurement",
      "unitofmeasure",
      "measuringunit",
      "measurementunit",
      "measureunit"),
  NUMBER_VALUE("number_value", "numbervalue", "decimalvalue"),
  TEXT_VALUE("text_value", "textvalue", "stringvalue"),
  DATE_TIME_VALUE("date_time_value", "datetimevalue", "timestampvalue"),
  BOOLEAN_VALUE("boolean_value", "booleanvalue");

  public static final String TABLE_NAME = "phenotype";

  private String fieldName;
  private String[] allowedFieldNames;

  private PhenotypeField(String fieldName, String... allowedFieldNames) {
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

  public static PhenotypeField getField(String fieldName) throws IllegalFieldException {
    for (PhenotypeField field : PhenotypeField.values())
      if (Util.isAllowedName(fieldName, field.getAllowedFieldNames())) return field;
    throw new IllegalFieldException(fieldName, TABLE_NAME);
  }
}
