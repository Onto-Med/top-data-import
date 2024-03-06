package care.smith.top.top_data_import;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

public class Config {

  private Properties props = new Properties();

  private Config() {}

  private Config load(InputStream propsStream) {
    try {
      props.load(propsStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return this;
  }

  public static Config getInstanceFromResource(String resourceName) {
    return new Config().load(Config.class.getClassLoader().getResourceAsStream(resourceName));
  }

  public static Config getInstance(String configFilePath) {
    Config config = new Config();
    try {
      config.load(new FileInputStream(configFilePath));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return config;
  }

  public String getDbUrl() {
    return props.getProperty("db_url");
  }

  public String getDbUser() {
    return props.getProperty("db_user");
  }

  public String getDbPw() {
    return props.getProperty("db_pw");
  }

  @Override
  public String toString() {
    StringWriter writer = new StringWriter();
    props.list(new PrintWriter(writer));
    return writer.getBuffer().toString();
  }

  public static void main(String[] args) {
    System.out.println(Config.getInstance("test_files/config.properties"));
  }
}
