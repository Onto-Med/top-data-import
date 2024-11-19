package care.smith.top.top_data_import.default_csv_import;

import java.util.stream.Stream;

public enum TableField {
  SUBJECT_SUBJECT_ID("subject_id", Table.SUBJECT, "subjectid", "patientid", "id"),
  SUBJECT_BIRTH_DATE("birth_date", Table.SUBJECT, "birthdate", "dateofbirth"),
  SUBJECT_SEX("sex", Table.SUBJECT, "sex", "gender"),

  ENCOUNTER_ENCOUNTER_ID("encounter_id", Table.ENCOUNTER, "encounterid", "id"),
  ENCOUNTER_SUBJECT_ID("subject_id", Table.ENCOUNTER, "subjectid", "patientid"),
  ENCOUNTER_TYPE("type", Table.ENCOUNTER, "type"),
  ENCOUNTER_START_DATE_TIME(
      "start_date_time",
      Table.ENCOUNTER,
      "startdatetime",
      "starttimestamp",
      "start",
      "periodstart"),
  ENCOUNTER_END_DATE_TIME(
      "end_date_time", Table.ENCOUNTER, "enddatetime", "endtimestamp", "end", "periodend"),

  PHENOTYPE_PHENOTYPE_ID(
      "phenotype_id",
      Table.PHENOTYPE,
      "phenotypeid",
      "observationid",
      "conditionid",
      "medicationid",
      "procedureid",
      "id"),
  PHENOTYPE_ENCOUNTER_ID("encounter_id", Table.PHENOTYPE, "encounterid"),
  PHENOTYPE_SUBJECT_ID("subject_id", Table.PHENOTYPE, "subjectid", "patientid"),
  PHENOTYPE_DATE_TIME("date_time", Table.PHENOTYPE, "datetime", "timestamp", "createdat"),
  PHENOTYPE_START_DATE_TIME(
      "start_date_time",
      Table.PHENOTYPE,
      "startdatetime",
      "starttimestamp",
      "start",
      "periodstart"),
  PHENOTYPE_END_DATE_TIME(
      "end_date_time", Table.PHENOTYPE, "enddatetime", "endtimestamp", "end", "periodend"),
  PHENOTYPE_CODE_SYSTEM("code_system", Table.PHENOTYPE, "codesystem", "terminology"),
  PHENOTYPE_CODE("code", Table.PHENOTYPE, "code"),
  PHENOTYPE_UNIT(
      "unit",
      Table.PHENOTYPE,
      "unit",
      "unitofmeasurement",
      "unitofmeasure",
      "measuringunit",
      "measurementunit",
      "measureunit"),
  PHENOTYPE_NUMBER_VALUE("number_value", Table.PHENOTYPE, "numbervalue", "decimalvalue"),
  PHENOTYPE_TEXT_VALUE("text_value", Table.PHENOTYPE, "textvalue", "stringvalue"),
  PHENOTYPE_DATE_TIME_VALUE("date_time_value", Table.PHENOTYPE, "datetimevalue", "timestampvalue"),
  PHENOTYPE_BOOLEAN_VALUE("boolean_value", Table.PHENOTYPE, "booleanvalue");

  private String fieldName;
  private Table table;
  private String[] allowedFieldNames;

  private TableField(String fieldName, Table table, String... allowedFieldNames) {
    this.fieldName = fieldName;
    this.table = table;
    this.allowedFieldNames = allowedFieldNames;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getTableName() {
    return table.getName();
  }

  public String getPropertyName() {
    return getTableName() + "." + getFieldName();
  }

  public boolean hasAllowedName(String fieldName) {
    return Stream.of(allowedFieldNames).anyMatch(fieldName.replace("_", "").toLowerCase()::equals);
  }

  public static TableField getSubjectField(String fieldName) throws IllegalFieldException {
    if (SUBJECT_SUBJECT_ID.hasAllowedName(fieldName)) return SUBJECT_SUBJECT_ID;
    if (SUBJECT_BIRTH_DATE.hasAllowedName(fieldName)) return SUBJECT_BIRTH_DATE;
    if (SUBJECT_SEX.hasAllowedName(fieldName)) return SUBJECT_SEX;
    throw new IllegalFieldException(fieldName, Table.SUBJECT.getName());
  }

  public static TableField getEncounterField(String fieldName) throws IllegalFieldException {
    if (ENCOUNTER_ENCOUNTER_ID.hasAllowedName(fieldName)) return ENCOUNTER_ENCOUNTER_ID;
    if (ENCOUNTER_SUBJECT_ID.hasAllowedName(fieldName)) return ENCOUNTER_SUBJECT_ID;
    if (ENCOUNTER_TYPE.hasAllowedName(fieldName)) return ENCOUNTER_TYPE;
    if (ENCOUNTER_START_DATE_TIME.hasAllowedName(fieldName)) return ENCOUNTER_START_DATE_TIME;
    if (ENCOUNTER_END_DATE_TIME.hasAllowedName(fieldName)) return ENCOUNTER_END_DATE_TIME;
    throw new IllegalFieldException(fieldName, Table.ENCOUNTER.getName());
  }

  public static TableField getPhenotypeField(String fieldName) throws IllegalFieldException {
    if (PHENOTYPE_PHENOTYPE_ID.hasAllowedName(fieldName)) return PHENOTYPE_PHENOTYPE_ID;
    if (PHENOTYPE_ENCOUNTER_ID.hasAllowedName(fieldName)) return PHENOTYPE_ENCOUNTER_ID;
    if (PHENOTYPE_SUBJECT_ID.hasAllowedName(fieldName)) return PHENOTYPE_SUBJECT_ID;
    if (PHENOTYPE_DATE_TIME.hasAllowedName(fieldName)) return PHENOTYPE_DATE_TIME;
    if (PHENOTYPE_START_DATE_TIME.hasAllowedName(fieldName)) return PHENOTYPE_START_DATE_TIME;
    if (PHENOTYPE_END_DATE_TIME.hasAllowedName(fieldName)) return PHENOTYPE_END_DATE_TIME;
    if (PHENOTYPE_CODE_SYSTEM.hasAllowedName(fieldName)) return PHENOTYPE_CODE_SYSTEM;
    if (PHENOTYPE_CODE.hasAllowedName(fieldName)) return PHENOTYPE_CODE;
    if (PHENOTYPE_UNIT.hasAllowedName(fieldName)) return PHENOTYPE_UNIT;
    if (PHENOTYPE_NUMBER_VALUE.hasAllowedName(fieldName)) return PHENOTYPE_NUMBER_VALUE;
    if (PHENOTYPE_TEXT_VALUE.hasAllowedName(fieldName)) return PHENOTYPE_TEXT_VALUE;
    if (PHENOTYPE_DATE_TIME_VALUE.hasAllowedName(fieldName)) return PHENOTYPE_DATE_TIME_VALUE;
    if (PHENOTYPE_BOOLEAN_VALUE.hasAllowedName(fieldName)) return PHENOTYPE_BOOLEAN_VALUE;
    throw new IllegalFieldException(fieldName, Table.PHENOTYPE.getName());
  }
}
