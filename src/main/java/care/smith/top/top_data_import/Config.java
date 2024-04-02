package care.smith.top.top_data_import;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

public class Config {

  private Properties props = new Properties();

  public Config(String configFilePath) {
    try {
      props.load(new FileInputStream(configFilePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getPvPath() {
    return props.getProperty("pv.path");
  }

  public String getPhenotypesPath() {
    return props.getProperty("phenotypes.path");
  }

  public String getDbUrl() {
    return props.getProperty("db.url");
  }

  public String getDbUser() {
    return props.getProperty("db.user");
  }

  public String getDbPw() {
    return props.getProperty("db.pw");
  }

  public String getColId() {
    return props.getProperty("col.id");
  }

  public String getColDate() {
    return props.getProperty("col.date");
  }

  public String getColSex() {
    return props.getProperty("col.sex");
  }

  public String getColBirthDate() {
    return props.getProperty("col.birthdate");
  }

  @Override
  public String toString() {
    StringWriter writer = new StringWriter();
    props.list(new PrintWriter(writer));
    return writer.getBuffer().toString();
  }

  public static void main(String[] args) {
    System.out.println(new Config("test_files/config.properties"));
  }
}
