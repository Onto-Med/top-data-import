package care.smith.top.top_data_import.db;

import care.smith.top.model.DataType;
import care.smith.top.top_data_import.Config;
import care.smith.top.top_data_import.csv.CSVValue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DB {

  private Connection con;

  private static final String CREATE_SBJ =
      "CREATE TABLE subject (\r\n"
          + "    subject_id text NOT NULL,\r\n"
          + "    birth_date timestamp,\r\n"
          + "    sex        text,\r\n"
          + "    PRIMARY KEY (subject_id)\r\n"
          + ")";

  private static final String CREATE_PHE =
      "CREATE TABLE phenotype (\r\n"
          + "    subject_id      text NOT NULL,\r\n"
          + "    created_at      timestamp,\r\n"
          + "    code_system     text NOT NULL,\r\n"
          + "    code            text NOT NULL,\r\n"
          + "    unit            text,\r\n"
          + "    number_value    numeric(20,3),\r\n"
          + "    text_value      text,\r\n"
          + "    date_time_value timestamp,\r\n"
          + "    boolean_value   boolean\r\n"
          + ")";

  private TablePrinter tablePrinter;

  private Logger log = LoggerFactory.getLogger(DB.class);

  public DB(Config config) {
    try {
      con = DriverManager.getConnection(config.getDbUrl(), config.getDbUser(), config.getDbPw());
      tablePrinter = new TablePrinter(con);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    execute("DROP ALL OBJECTS");
    execute(CREATE_SBJ);
    execute(CREATE_PHE);
  }

  public void insertSbj(CSVValue idField, CSVValue bdField, CSVValue sexField) {
    insertSbj(idField.getValue(), getDateValue(bdField), getStringValue(sexField));
  }

  public void insertSbj(String id, LocalDateTime bd, String sex) {
    execute(
        "MERGE INTO subject VALUES ("
            + String.join(", ", quote(id), quote(format(bd)), quote(sex))
            + ")");
  }

  public void insertPhe(CSVValue sbjIdField, CSVValue pheField, CSVValue dateField) {
    insertPhe(
        sbjIdField.getValue(),
        getDateValue(dateField),
        pheField.getCodeSystem(),
        pheField.getCode(),
        pheField.getUnit(),
        pheField.getNumberValue(),
        pheField.getStringValue(),
        pheField.getDateValue(),
        pheField.getBooleanValue());
  }

  public void insertPhe(
      String sbjId,
      LocalDateTime date,
      String codeSystem,
      String code,
      String unit,
      Number numVal,
      String txtVal,
      LocalDateTime dateVal,
      Boolean boolVal) {
    execute(
        "INSERT INTO phenotype (subject_id, created_at, code_system, code, unit, number_value, text_value, date_time_value, boolean_value) VALUES ("
            + String.join(
                ", ",
                quote(sbjId),
                quote(format(date)),
                quote(codeSystem),
                quote(code),
                quote(unit),
                toString(numVal),
                quote(txtVal),
                quote(format(dateVal)),
                toString(boolVal))
            + ")");
  }

  private String quote(String s) {
    return (s == null) ? null : "'" + s + "'";
  }

  private String format(LocalDateTime dt) {
    return (dt == null) ? null : dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  private String toString(Object o) {
    return (o == null) ? null : o.toString();
  }

  private String getStringValue(CSVValue f) {
    return (f == null) ? null : f.getStringValue();
  }

  private LocalDateTime getDateValue(CSVValue f) {
    return (f == null) ? null : f.getDateValue();
  }

  private void execute(String sql) {
    log.debug("execute sql statement:{}{}", System.lineSeparator(), sql);
    try {
      Statement stmt = con.createStatement();
      stmt.execute(sql);
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void printSbj() {
    tablePrinter.print("subject");
  }

  public void printPhe() {
    tablePrinter.print("phenotype");
  }

  public static void main(String[] args) {
    Config config = new Config("test_files/config.properties");
    DB db = new DB(config);

    CSVValue id1 = new CSVValue("SOZIO_SIC", "1", DataType.STRING);

    CSVValue id2 = new CSVValue("SOZIO_SIC", "2", DataType.STRING);
    CSVValue id2Sex = new CSVValue("SOZIO_SEX", "female", DataType.STRING);

    CSVValue id3 = new CSVValue("SOZIO_SIC", "3", DataType.STRING);
    CSVValue id3Bd = new CSVValue("SOZIO_BIRTHDATE", "1999-01-01T00:00", DataType.DATE_TIME);
    CSVValue id3Phe1 = new CSVValue("SOZIO_F0001", "123", DataType.NUMBER, "SOZIO", "F0001");
    CSVValue id3Phe2 = new CSVValue("SOZIO_F0002", "ABC", DataType.STRING, "SOZIO", "F0002");
    CSVValue id3Phe2Date = new CSVValue("SOZIO_EDAT", "2023-05-06T01:02:03", DataType.DATE_TIME);

    db.insertSbj(id1, null, null);
    db.insertSbj(id1, null, null);
    db.insertSbj(id1, null, null);
    db.insertSbj(id2, id3Bd, null);
    db.insertSbj(id2, id3Bd, null);
    db.insertSbj(id2, id3Bd, null);
    db.insertSbj(id3, null, id2Sex);
    db.insertSbj(id3, null, id2Sex);
    db.insertSbj(id3, null, id2Sex);

    db.insertPhe(id3, id3Phe1, null);
    db.insertPhe(id3, id3Phe2, id3Phe2Date);

    db.printSbj();
    db.printPhe();
  }
}
