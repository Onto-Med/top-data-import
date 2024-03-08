package care.smith.top.top_data_import.life;

import care.smith.top.top_data_import.csv.CSVConverter;
import care.smith.top.top_data_import.csv.CSVRecord;
import java.nio.file.Path;
import java.util.Arrays;

public class LIFEConverter extends CSVConverter {

  public LIFEConverter(Path path) {
    super(path);
  }

  @Override
  protected void convert(CSVRecord csvRecord) {
    System.out.println(csvRecord);
    System.out.println(csvRecord.get(3));
    System.out.println(csvRecord.get("SOZIO_F0003"));
    System.out.println(Arrays.toString(csvRecord.getOthers("ID", "GRP", "SOZIO_F0002")));
    System.out.println();
  }
}
