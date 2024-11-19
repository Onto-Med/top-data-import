package care.smith.top.top_data_import.default_csv_import;

public class IllegalFieldException extends Exception {

  private static final long serialVersionUID = 1L;

  public IllegalFieldException(String field, String table) {
    super("The field '" + field + "' is not allowed in the table '" + table + "'!");
  }
}
