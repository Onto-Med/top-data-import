package care.smith.top.top_data_import.default_csv_import.exception;

public class UnmappedFieldException extends Exception {

  private static final long serialVersionUID = 1L;

  public UnmappedFieldException(String fieldName, String tableName) {
    super("The field '" + fieldName + "' in the table '" + tableName + "' is not mapped!");
  }
}
