package care.smith.top.top_data_import.default_csv_import;

import care.smith.top.top_data_import.default_csv_import.config.DefaultConfig;
import care.smith.top.top_data_import.default_csv_import.config.PhenotypeField;
import care.smith.top.top_data_import.default_csv_import.exception.UnmappedFieldException;
import java.nio.file.Path;
import java.util.Map;

public class PhenotypeCSVImport extends DefaultCSVImport {

  public PhenotypeCSVImport(Path path, DefaultConfig config, DefaultDB db)
      throws UnmappedFieldException {
    super(path, config, db);
    checkHeader(config.getPhenotypeCSVFields(), PhenotypeField.TABLE_NAME);
  }

  @Override
  protected void write(Map<String, String> record) {
    db.insertPhenotype(record);
  }
}
