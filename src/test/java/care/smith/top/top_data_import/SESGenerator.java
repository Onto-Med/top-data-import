package care.smith.top.top_data_import;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SESGenerator {

  public static void main(String[] args) throws IOException {
    try (CSVWriter writer = new CSVWriter(new FileWriter("test_files/csv/ses.csv"))) {
      for (int i = 0; i < 10; i++) writer.writeNext(generateRecord());
    }
  }

  private static String[] generateRecord() {
    return null;
  }
}
