package care.smith.top.top_data_import.default_csv_import;

import care.smith.top.top_data_import.default_csv_import.config.DefaultConfig;
import care.smith.top.top_data_import.default_csv_import.config.SubjectField;
import care.smith.top.top_data_import.default_csv_import.exception.UnmappedFieldException;
import java.nio.file.Path;
import java.util.Map;

public class SubjectCSVImport extends DefaultCSVImport {

  public SubjectCSVImport(Path path, DefaultConfig config, DefaultDB db)
      throws UnmappedFieldException {
    super(path, config, db);
    checkHeader(config.getSubjectCSVFields(), SubjectField.TABLE_NAME);
  }

  @Override
  protected void write(Map<String, String> record) {
    db.insertSubject(record);
  }
}
