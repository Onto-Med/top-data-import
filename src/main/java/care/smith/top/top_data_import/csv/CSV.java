package care.smith.top.top_data_import.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class CSV {

  private Field[] fields;

  public void read(Path path) {

    //    CSVParser parser =
    //        new CSVParserBuilder().withSeparator(',').withIgnoreLeadingWhiteSpace(true).build();

    //			CSVReader csvReader = new CSVReaderBuilder(reader)
    //			    .withSkipLines(0)
    //			    .withCSVParser(parser)
    //			    .build();

    try {
      CSVReader csvReader = getReader(path);
      setFields(csvReader);

    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
  }

  private boolean setFields(CSVReader csvReader) throws CsvValidationException, IOException {
    String[] header = csvReader.readNext();
    if (header == null) return false;
    fields =
        Arrays.stream(header)
            .filter(f -> !f.isBlank())
            .map(f -> new Field(f.trim()))
            .toArray(Field[]::new);
    if (fields.length == 0) return false;

    String[] line;
    while ((line = csvReader.readNext()) != null) {
      System.out.println(Arrays.toString(line));
      for (int i = 0; i < line.length; i++) fields[i].setDataType(line[i].trim());
    }

    System.out.println(Arrays.toString(fields));

    return true;
  }

  private CSVReader getReader(Path path) throws IOException {
    return new CSVReader(Files.newBufferedReader(path));
  }
}
