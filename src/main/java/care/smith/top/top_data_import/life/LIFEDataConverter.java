package care.smith.top.top_data_import.life;

import care.smith.top.top_data_import.Config;
import care.smith.top.top_data_import.csv.CSVConverter;
import care.smith.top.top_data_import.csv.CSVRecord;
import care.smith.top.top_data_import.csv.CSVValue;
import care.smith.top.top_data_import.db.DB;
import java.nio.file.Path;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LIFEDataConverter extends CSVConverter {
  private final Logger LOGGER = LoggerFactory.getLogger(LIFEDataConverter.class);
  private DB db;
  private Config config;
  private String codeSystem;

  public LIFEDataConverter(Path path, DB db, Config config) {
    super(path);
    this.db = db;
    this.config = config;
    Arrays.stream(getHeader()).forEach(h -> h.code(h.getName().split("_")));
    codeSystem = getHeader()[0].getCodeSystem();
  }

  @Override
  protected void convert(CSVRecord csvRecord) {
    LOGGER.debug(csvRecord.toString());

    String idName = codeSystem + "_" + config.getColId();
    String dateName = codeSystem + "_" + config.getColDate();
    String sexName = codeSystem + "_" + config.getColSex();
    String bdName = codeSystem + "_" + config.getColBirthDate();

    CSVValue id = csvRecord.get(idName);
    CSVValue date = csvRecord.get(dateName);
    CSVValue sex = csvRecord.get(sexName);
    CSVValue bd = csvRecord.get(bdName);

    db.insertSbj(id, bd, sex);

    Arrays.stream(csvRecord.getOthers(idName, dateName, sexName, bdName))
        .forEach(phe -> db.insertPhe(id, phe, date));
  }
}
