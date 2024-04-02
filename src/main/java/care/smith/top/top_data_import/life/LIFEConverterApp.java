package care.smith.top.top_data_import.life;

import care.smith.top.top_data_import.Config;
import care.smith.top.top_data_import.db.DB;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LIFEConverterApp {

  private Config config;
  private DB db;
  private Path pvPath;

  public LIFEConverterApp(Config config) {
    this.config = config;
    this.db = new DB(config);
    this.pvPath = Paths.get(config.getPvPath());
  }

  public void convert() throws IOException {
    Files.list(pvPath)
        .filter(p -> !Files.isDirectory(p))
        .forEach(
            p -> {
              if (p.toString().endsWith(".csv")) new LIFEDataConverter(p, db, config).convert();
              else if (p.toString().endsWith(".pdf"))
                new LIFEMetadataConverter(p, config).convert();
            });

    db.printSbj();
    db.printPhe();
  }

  public static void main(String[] args) throws IOException {
    new LIFEConverterApp(new Config("test_files/config.properties")).convert();
  }
}
