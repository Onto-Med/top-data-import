package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.top_data_import.default_csv_import.exception.IllegalFieldException;

public enum SubjectField {
  SUBJECT_ID("subject_id", "subjectid", "patientid", "id"),
  BIRTH_DATE("birth_date", "birthdate", "dateofbirth"),
  SEX("sex", "sex", "gender");

  public static final String TABLE_NAME = "subject";

  private String fieldName;
  private String[] allowedFieldNames;

  private SubjectField(String fieldName, String... allowedFieldNames) {
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

  public static SubjectField getField(String fieldName) throws IllegalFieldException {
    for (SubjectField field : SubjectField.values())
      if (Util.isAllowedName(fieldName, field.getAllowedFieldNames())) return field;
    throw new IllegalFieldException(fieldName, TABLE_NAME);
  }
}
