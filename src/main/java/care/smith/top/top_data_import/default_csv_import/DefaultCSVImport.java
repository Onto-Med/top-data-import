package care.smith.top.top_data_import.default_csv_import;

import care.smith.top.top_data_import.default_csv_import.config.DefaultConfig;
import care.smith.top.top_data_import.default_csv_import.config.Field;
import care.smith.top.top_data_import.default_csv_import.config.PhenotypeField;
import care.smith.top.top_data_import.default_csv_import.exception.UnmappedFieldException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultCSVImport {

  protected final Logger LOGGER = LoggerFactory.getLogger(DefaultCSVImport.class);
  protected DefaultConfig config;
  protected DefaultDB db;
  private CSVReader csvReader;
  private String[] header;

  public DefaultCSVImport(Path path, DefaultConfig config, DefaultDB db) {
    this.config = config;
    this.db = db;
    try {
      CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
      csvReader = new CSVReaderBuilder(Files.newBufferedReader(path)).withCSVParser(parser).build();
      header = csvReader.readNext();
    } catch (IOException | CsvValidationException e) {
      LOGGER.warn(e.getMessage(), e);
    }
  }

  protected void checkHeader(EnumMap<? extends Field, String> fields, String tableName)
      throws UnmappedFieldException {
    for (String head : header)
      if (!fields.values().contains(head.trim()))
        throw new UnmappedFieldException(head.trim(), tableName);
  }

  public void write() {
    if (header == null) return;
    try {
      String[] values;
      while ((values = csvReader.readNext()) != null) write(toMap(values));
      csvReader.close();
    } catch (IOException | CsvValidationException e) {
      LOGGER.warn(e.getMessage(), e);
    }
  }

  private Map<String, String> toMap(String[] values) {
    Map<String, String> record = new HashMap<>();
    for (int i = 0; i < header.length; i++) record.put(header[i].trim(), values[i].trim());
    return record;
  }

  protected abstract void write(Map<String, String> record);

  public static void main(String[] args) throws UnmappedFieldException {
    DefaultConfig c = new DefaultConfig("jdbc:h2:file:./test_files/test_db", "user", "pw");
    c.setPhenotypeField(PhenotypeField.DATE_TIME, "time_stamp");
    c.setPhenotypeField(PhenotypeField.UNIT, "unit_of_measure");

    DefaultDB db = new DefaultDB(c).initialise();

    SubjectCSVImport sbj = new SubjectCSVImport(Paths.get("test_files/subjects.csv"), c, db);
    sbj.write();
    db.printSubjectTable();

    EncounterCSVImport enc = new EncounterCSVImport(Paths.get("test_files/encounters.csv"), c, db);
    enc.write();
    db.printEncounterTable();

    PhenotypeCSVImport phe = new PhenotypeCSVImport(Paths.get("test_files/phenotypes.csv"), c, db);
    phe.write();
    db.printPhenotypeTable();
  }
}
