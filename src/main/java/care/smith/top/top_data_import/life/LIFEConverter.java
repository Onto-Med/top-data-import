package care.smith.top.top_data_import.life;

import care.smith.top.top_data_import.Config;
import care.smith.top.top_data_import.csv.CSVConverter;
import care.smith.top.top_data_import.csv.CSVField;
import care.smith.top.top_data_import.csv.CSVRecord;
import care.smith.top.top_data_import.db.DB;
import java.nio.file.Path;

public class LIFEConverter extends CSVConverter {

  private DB db;
  private Config config;

  public LIFEConverter(Path path, DB db, Config config) {
    super(path);
    this.db = db;
    this.config = config;
  }

  @Override
  protected void convert(CSVRecord csvRecord) {
    System.out.println(csvRecord);

    String codeSystem = csvRecord.get(0).getName().split("_")[0];

    String idName = codeSystem + "_" + config.getColId();
    String dateName = codeSystem + "_" + config.getColDate();
    String sexName = codeSystem + "_" + config.getColSex();
    String bdName = codeSystem + "_" + config.getColBirthDate();

    CSVField id = csvRecord.get(idName);
    CSVField date = csvRecord.get(dateName);
    CSVField sex = csvRecord.get(sexName);
    CSVField bd = csvRecord.get(bdName);

    db.insertSbj(id, bd, sex);

    for (CSVField phe : csvRecord.getOthers(idName, dateName, sexName, bdName)) {
      String code = phe.getName().split("_")[1];
      db.insertPhe(id, phe.code(codeSystem, code), date);
    }
  }
}
