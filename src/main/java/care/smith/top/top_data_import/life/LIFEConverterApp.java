package care.smith.top.top_data_import.life;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LIFEConverterApp {

  public void convert(Path directoryPath) throws IOException {
    Files.list(directoryPath)
        .filter(p -> !Files.isDirectory(p) && p.toString().endsWith(".csv"))
        .forEach(p -> new LIFEConverter(p).convert());
  }

  public static void main(String[] args) throws IOException {
    new LIFEConverterApp().convert(Paths.get("test_files/csv"));
  }
}
