package care.smith.top.top_data_import.default_csv_import;

import care.smith.top.model.DataType;
import care.smith.top.top_data_import.Util;
import care.smith.top.top_data_import.db.TablePrinter;
import care.smith.top.top_data_import.default_csv_import.config.DefaultConfig;
import care.smith.top.top_data_import.default_csv_import.config.EncounterField;
import care.smith.top.top_data_import.default_csv_import.config.Field;
import care.smith.top.top_data_import.default_csv_import.config.PhenotypeField;
import care.smith.top.top_data_import.default_csv_import.config.SubjectField;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDB {

  private Connection con;
  private TablePrinter tablePrinter;
  private DefaultConfig config;

  private final Logger LOGGER = LoggerFactory.getLogger(DefaultDB.class);

  public DefaultDB(DefaultConfig config) {
    this.config = config;
    try {
      con = config.getDatabaseConnection();
      tablePrinter = new TablePrinter(con);
    } catch (SQLException e) {
      LOGGER.warn(e.getMessage(), e);
    }

    dropTables();
    createTables();
  }

  public void dropTables() {
    execute("DROP ALL OBJECTS");
  }

  public void createTables() {
    execute(
        getCreateTableStatement(
            SubjectField.TABLE_NAME, config.getSubjectFields(), SubjectField.SUBJECT_ID));
    execute(
        getCreateTableStatement(
            EncounterField.TABLE_NAME, config.getEncounterFields(), EncounterField.ENCOUNTER_ID));
    execute(
        getCreateTableStatement(
            PhenotypeField.TABLE_NAME, config.getPhenotypeFields(), PhenotypeField.PHENOTYPE_ID));
  }

  public void insertSubject(Map<String, String> record) {
    execute(getInsertStatement(SubjectField.TABLE_NAME, config.getSubjectCSVFields(), record));
  }

  public void insertEncounter(Map<String, String> record) {
    execute(getInsertStatement(EncounterField.TABLE_NAME, config.getEncounterCSVFields(), record));
  }

  public void insertPhenotype(Map<String, String> record) {
    execute(getInsertStatement(PhenotypeField.TABLE_NAME, config.getPhenotypeCSVFields(), record));
  }

  private void execute(String sql) {
    LOGGER.debug("execute sql statement:{}{}", System.lineSeparator(), sql);
    try {
      Statement stmt = con.createStatement();
      stmt.execute(sql);
      stmt.close();
    } catch (SQLException e) {
      LOGGER.warn(e.getMessage(), e);
    }
  }

  public void close() {
    try {
      con.close();
    } catch (SQLException e) {
      LOGGER.warn(e.getMessage(), e);
    }
  }

  public void printSubjectTable() {
    tablePrinter.print(SubjectField.TABLE_NAME);
  }

  public void printEncounterTable() {
    tablePrinter.print(EncounterField.TABLE_NAME);
  }

  public void printPhenotypeTable() {
    tablePrinter.print(PhenotypeField.TABLE_NAME);
  }

  private static final String LS = System.lineSeparator();

  private String getCreateTableStatement(String tableName, Set<? extends Field> fields, Field id) {
    StringBuffer sb = new StringBuffer("CREATE TABLE " + tableName + " (" + LS);
    for (Field field : fields)
      sb.append(String.format("  %s %s,%s", field.getFieldName(), field.getDBProperties(), LS));
    sb.append("  PRIMARY KEY (" + id.getFieldName() + ")" + LS);
    sb.append(")");
    return sb.toString();
  }

  private String getInsertStatement(
      String tableName, EnumMap<? extends Field, String> fields, Map<String, String> record) {
    List<String> values = new ArrayList<>();
    for (Field field : fields.keySet())
      values.add(getDBValue(record.get(fields.get(field)), field.getDataType()));
    return "INSERT INTO " + tableName + " VALUES (" + String.join(", ", values) + ")";
  }

  private String getDBValue(String value, DataType dataType) {
    if (value == null || value.isBlank()) return null;
    if (dataType == DataType.NUMBER) return value;
    if (dataType == DataType.STRING) return "'" + value + "'";
    if (dataType == DataType.DATE_TIME)
      return "'" + Util.parseDate(value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "'";
    if (dataType == DataType.BOOLEAN) return value;
    return null;
  }
}
