package care.smith.top.top_data_import.default_csv_import;

import care.smith.top.top_data_import.default_csv_import.config.DefaultConfig;
import java.nio.file.Path;
import java.util.Map;

public class EncounterCSVImport extends DefaultCSVImport {

  public EncounterCSVImport(Path path, DefaultConfig config, DefaultDB db) {
    super(path, config, db);
  }

  @Override
  protected void write(Map<String, String> record) {
    db.insertEncounter(record);
  }
}
