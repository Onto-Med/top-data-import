package care.smith.top.top_data_import;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SESGen {

  private static final String[] HEADER = {
    "SOZIO_SIC",
    "SOZIO_EDAT",
    "SOZIO_F0041",
    "SOZIO_F0045",
    "SOZIO_F0046",
    "SOZIO_F0047",
    "SOZIO_F0048",
    "SOZIO_F0049",
    "SOZIO_F0050",
    "SOZIO_F0051",
    "SOZIO_F0055",
    "SOZIO_F0070",
    "SOZIO_F0072",
    "SOZIO_F0026",
    "SOZIO_F0035",
    "SOZIO_F0078",
    "SOZIO_F0083",
    "SOZIO_F0084",
    "SOZIO_F0086",
    "SOZIO_F0087",
    "SOZIO_F0079",
    "SOZIO_F0080"
  };

  private static final Integer[] F0041 = {1, 2, 3, 4, 5, 6, 7, 95, 98, 99, null};
  private static final Integer[] F0055 = {0, 1, 2, 3, 98, 99, null};
  private static final Integer[] F0070 = {0, 1, 98, 99, null};
  private static final String[] F0072 = {
    "A", "A1", "A2", "A3", "B", "B1", "B2", "B3", "C", "C1", "C2", "C3", "C4", "D", "D1", "D2",
    "D3", "D4", "E", "E1", "E2", "E3", "E4", "F", "F1", "F2", "F3", "F4", "F5", "G", "G1", "G2",
    "G3", "H", "97", "98", "99", null
  };
  private static final Integer[] F0026 = {1, 2, 3, 4, 5, 98, 99, null};
  private static final Integer[] F0035 = {0, 1, 98, 99, null};
  private static final String[] F0078 = {
    "A", "A1", "A2", "A3", "B", "B1", "B2", "B3", "C", "C1", "C2", "C3", "C4", "D", "D1", "D2",
    "D3", "D4", "E", "E1", "E2", "E3", "E4", "F", "F1", "F2", "F3", "F4", "F5", "G", "G1", "G2",
    "G3", "H", "97", "98", "99", null
  };
  private static final String[] F0084 = {
    "B", "P", "T", "F", "E", "H", "L", "N", "R", "M", "S", "K", "O", "C", "G", "U", "J", "V", "A",
    "Z", "X", "Q", "W", "D", "Y", "8", "9", null
  };
  private static final String[] F0087 = {
    "B", "P", "T", "F", "E", "H", "L", "N", "R", "M", "S", "K", "O", "C", "G", "U", "J", "V", "A",
    "Z", "X", "Q", "W", "D", "Y", "8", "9", null
  };

  public static void main(String[] args) throws IOException {
    try (CSVWriter writer = new CSVWriter(new FileWriter("test_files/csv/ses10.csv"))) {
      writer.writeNext(HEADER);
      for (int i = 1; i <= 10000; i++) writer.writeNext(generateRecord(i));
    }
  }

  private static String[] generateRecord(int i) {
    List<String> record = new ArrayList<>();
    record.add(String.valueOf(i));
    record.add(nextDate("2010-01-01T00:00:00"));
    record.add(next(F0041));
    record.add(next(0, 1));
    record.add(next(0, 1));
    record.add(next(0, 1));
    record.add(next(0, 1));
    record.add(next(0, 1));
    record.add(next(0, 1));
    record.add(next(0, 1));
    record.add(next(F0055));
    record.add(next(F0070));
    record.add(next(F0072));
    record.add(next(F0026));
    record.add(next(F0035));
    record.add(next(F0078));
    record.add(String.valueOf(nextInt(0, 20001)));
    record.add(next(F0084));
    record.add(String.valueOf(nextInt(0, 20001)));
    record.add(next(F0087));

    int f0079 = nextInt(1, 8);
    int f0080 = 0;
    if (f0079 > 1) f0080 = nextInt(1, f0079);
    record.add(String.valueOf(f0079));
    record.add(String.valueOf(f0080));

    return record.toArray(String[]::new);
  }

  private static String next(String[] vals) {
    return vals[nextInt(0, vals.length)];
  }

  private static String next(Integer... vals) {
    Integer res = vals[nextInt(0, vals.length)];
    return (res == null) ? null : res.toString();
  }

  private static String nextDate(String dateOrigin) {
    return nextDate(LocalDateTime.parse(dateOrigin), LocalDateTime.now());
  }

  private static String nextDate(LocalDateTime dateOrigin, LocalDateTime dateBound) {
    long origin = Timestamp.valueOf(dateOrigin).getTime();
    long bound = Timestamp.valueOf(dateBound).getTime();
    return new Timestamp(nextLong(origin, bound))
        .toLocalDateTime()
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  private static long nextLong(long origin, long bound) {
    return ThreadLocalRandom.current().nextLong(origin, bound);
  }

  private static int nextInt(int origin, int bound) {
    return ThreadLocalRandom.current().nextInt(origin, bound);
  }
}
