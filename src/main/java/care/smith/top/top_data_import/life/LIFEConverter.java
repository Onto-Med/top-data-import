package care.smith.top.top_data_import.life;

import care.smith.top.top_data_import.csv.CSV;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LIFEConverter {

  public void convert(Path directoryPath) throws IOException {
    Files.list(directoryPath)
        .filter(p -> !Files.isDirectory(p) && p.toString().endsWith(".csv"))
        .forEach(
            p -> {
              System.out.println(p);
              new CSV().read(p);
            });
  }

  public static void main(String[] args) throws IOException {
    new LIFEConverter().convert(Paths.get("test_files/csv"));
  }
}
