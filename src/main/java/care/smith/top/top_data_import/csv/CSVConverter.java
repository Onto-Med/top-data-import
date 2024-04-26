package care.smith.top.top_data_import.csv;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;

public abstract class CSVConverter {

  private Path path;
  private CSVHead[] header;

  public CSVConverter(Path path) {
    this.path = path;
    try {
      CSVReader csvReader = getReader(path);
      String[] headerLine = csvReader.readNext();
      if (headerLine == null) return;

      header =
          IntStream.range(0, headerLine.length)
              .mapToObj(i -> new CSVHead(headerLine[i]).index(i))
              .toArray(CSVHead[]::new);

      String[] line;
      while ((line = csvReader.readNext()) != null)
        for (int i = 0; i < line.length; i++) header[i].setDataType(line[i]);
      csvReader.close();
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
  }

  public CSVHead[] getHeader() {
    return header;
  }

  public void convert() {
    if (header == null) return;
    try {
      CSVReader csvReader = getReaderWithoutHeader(path);
      String[] line;
      while ((line = csvReader.readNext()) != null) convert(new CSVRecord(header, line));
      csvReader.close();
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
  }

  protected abstract void convert(CSVRecord csvRecord);

  //  private CSVReader getReader(Path path) throws IOException {
  //    return new CSVReader(Files.newBufferedReader(path));
  //  }

  private CSVReaderBuilder getBuilder(Path path) throws IOException {
    CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
    return new CSVReaderBuilder(Files.newBufferedReader(path)).withCSVParser(parser);
  }

  private CSVReader getReader(Path path) throws IOException {
    return getBuilder(path).build();
  }

  private CSVReader getReaderWithoutHeader(Path path) throws IOException {
    return getBuilder(path).withSkipLines(1).build();
  }
  //  private CSVReader getReaderWithoutHeader(Path path) throws IOException {
  //	  return new CSVReaderBuilder(Files.newBufferedReader(path)).withSkipLines(1).build();
  //  }
}
