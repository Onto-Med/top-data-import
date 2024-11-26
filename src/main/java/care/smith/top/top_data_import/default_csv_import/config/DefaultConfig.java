package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.top_data_import.Config;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Properties;
import java.util.Set;
import org.slf4j.LoggerFactory;

public class DefaultConfig {

  private String dbURL;
  private String dbUser;
  private String dbPassword;

  private EnumMap<SubjectField, String> subjectFields = SubjectField.getFields();
  private EnumMap<EncounterField, String> encounterFields = EncounterField.getFields();
  private EnumMap<PhenotypeField, String> phenotypeFields = PhenotypeField.getFields();

  public DefaultConfig() {}

  public DefaultConfig(String dbURL, String dbUser, String dbPassword) {
    setDatabase(dbURL, dbUser, dbPassword);
  }

  public DefaultConfig(String configFilePath) {
    try {
      Properties props = new Properties();
      props.load(new FileInputStream(configFilePath));

      setDatabase(
          props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.pw"));

      for (SubjectField field : getSubjectFields())
        setSubjectField(field, props.getProperty(field.getPropertyName()));

      for (EncounterField field : getEncounterFields())
        setEncounterField(field, props.getProperty(field.getPropertyName()));

      for (PhenotypeField field : getPhenotypeFields())
        setPhenotypeField(field, props.getProperty(field.getPropertyName()));
    } catch (IOException e) {
      LoggerFactory.getLogger(Config.class).warn(e.getMessage(), e);
    }
  }

  public void setDatabase(String url, String user, String password) {
    this.dbURL = url.trim();
    this.dbUser = user.trim();
    this.dbPassword = password.trim();
  }

  public Connection getDatabaseConnection() throws SQLException {
    return DriverManager.getConnection(dbURL, dbUser, dbPassword);
  }

  public void setSubjectField(SubjectField field, String fieldName) {
    if (field != null && fieldName != null && !fieldName.isBlank())
      subjectFields.put(field, fieldName.trim());
  }

  public void setEncounterField(EncounterField field, String fieldName) {
    if (field != null && fieldName != null && !fieldName.isBlank())
      encounterFields.put(field, fieldName.trim());
  }

  public void setPhenotypeField(PhenotypeField field, String fieldName) {
    if (field != null && fieldName != null && !fieldName.isBlank())
      phenotypeFields.put(field, fieldName.trim());
  }

  public EnumMap<SubjectField, String> getSubjectCSVFields() {
    return subjectFields;
  }

  public EnumMap<EncounterField, String> getEncounterCSVFields() {
    return encounterFields;
  }

  public EnumMap<PhenotypeField, String> getPhenotypeCSVFields() {
    return phenotypeFields;
  }

  public Set<SubjectField> getSubjectFields() {
    return subjectFields.keySet();
  }

  public Set<EncounterField> getEncounterFields() {
    return encounterFields.keySet();
  }

  public Set<PhenotypeField> getPhenotypeFields() {
    return phenotypeFields.keySet();
  }
}
