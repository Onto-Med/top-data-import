package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.top_data_import.Config;
import care.smith.top.top_data_import.default_csv_import.exception.IllegalFieldException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.LoggerFactory;

public class DefaultCSVConfig {

  private String dbURL;
  private String dbUser;
  private String dbPassword;

  private Map<String, SubjectField> subjectFields = new HashMap<>();
  private Map<String, EncounterField> encounterFields = new HashMap<>();
  private Map<String, PhenotypeField> phenotypeFields = new HashMap<>();

  public DefaultCSVConfig() {}

  public DefaultCSVConfig(String configFilePath) {
    try {
      Properties props = new Properties();
      props.load(new FileInputStream(configFilePath));

      setDatabase(
          props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.pw"));

      for (SubjectField field : SubjectField.values())
        setSubjectField(field, props.getProperty(field.getPropertyName()));

      for (EncounterField field : EncounterField.values())
        setEncounterField(field, props.getProperty(field.getPropertyName()));

      for (PhenotypeField field : PhenotypeField.values())
        setPhenotypeField(field, props.getProperty(field.getPropertyName()));
    } catch (IOException e) {
      LoggerFactory.getLogger(Config.class).warn(e.getMessage(), e);
    }
  }

  public void setDatabase(String url, String user, String password) {
    this.dbURL = url;
    this.dbUser = user;
    this.dbPassword = password;
  }

  public Connection getDatabaseConnection() throws SQLException {
    return DriverManager.getConnection(dbURL, dbUser, dbPassword);
  }

  public void setSubjectField(SubjectField field, String fieldName) {
    subjectFields.put(fieldName, field);
  }

  public SubjectField getSubjectField(String fieldName) throws IllegalFieldException {
    SubjectField field = subjectFields.get(fieldName);
    if (field != null) return field;
    return SubjectField.getField(fieldName);
  }

  public void setEncounterField(EncounterField field, String fieldName) {
    encounterFields.put(fieldName, field);
  }

  public EncounterField getEncounterField(String fieldName) throws IllegalFieldException {
    EncounterField field = encounterFields.get(fieldName);
    if (field != null) return field;
    return EncounterField.getField(fieldName);
  }

  public void setPhenotypeField(PhenotypeField field, String fieldName) {
    phenotypeFields.put(fieldName, field);
  }

  public PhenotypeField getPhenotypeField(String fieldName) throws IllegalFieldException {
    PhenotypeField field = phenotypeFields.get(fieldName);
    if (field != null) return field;
    return PhenotypeField.getField(fieldName);
  }
}
