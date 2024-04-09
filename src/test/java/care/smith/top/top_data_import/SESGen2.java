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
import java.util.stream.Collectors;

public class SESGen2 {

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

  private static final Integer[] F0041 = {1, 2, 3, 4, 5, 6, 7, 95};
  private static final Integer[] F0055 = {0, 1, 2, 3, 98};
  private static final Integer[] F0070 = {0, 1, 99};
  private static final String[] F0072_78 = {
    "A", "A1", "A2", "A3", "B", "B1", "B2", "B3", "C", "C1", "C2", "C3", "C4", "D", "D1", "D2",
    "D3", "D4", "E", "E1", "E2", "E3", "E4", "F", "F1", "F2", "F3", "F4", "F5", "G", "G1", "G2",
    "G3", "H", "97"
  };
  private static final Integer[] F0026 = {1, 2, 99};
  private static final Integer[] F0035 = {0, 1, 99};
  private static final String[] F0084_87 = {
    "B", "P", "T", "F", "E", "H", "L", "N", "R", "M", "S", "K", "O", "C", "G", "U", "J", "V", "A",
    "Z", "X", "Q", "W", "D", "Y", "9"
  };

  public static void main(String[] args) throws IOException {
    try (CSVWriter writer = new CSVWriter(new FileWriter("test_files/csv/ses.csv"))) {
      writer.writeNext(HEADER);
      for (int i = 1; i <= 10000; i++) writer.writeNext(generateRecord(i));
    }
  }

  private static String[] generateRecord(int i) {
    List<String> record = new ArrayList<>();
    // SOZIO_SIC: ID
    record.add(String.valueOf(i));
    // SOZIO_EDAT: timestamp
    record.add(nextDate("2010-01-01T00:00:00"));
    // SOZIO_F0041: School-leaving qualification
    record.add(next(F0041));
    // SOZIO_F0045|46|47|48|49|50|51: Vocational qualification
    addVocationalQualification(record);
    // SOZIO_F0055: Employment
    record.add(next(F0055));
    // SOZIO_F0070: Former employment
    record.add(next(F0070));
    // SOZIO_F0072: Own profession
    record.add(next(F0072_78));
    // SOZIO_F0026|35|78: Partner's profession
    boolean hasPartner = addPartnerProfession(record);
    // SOZIO_F0083|84|86|87: Income
    addIncome(record, hasPartner);
    // SOZIO_F0079|80: Household size
    addHouseholdSize(record);

    return record.toArray(String[]::new);
  }

  private static void addIncome(List<String> record, boolean hasPartner) {
    if (nextInt(0, 2) == 0) {
      Integer partnerIncomeInt = (hasPartner) ? nextIncome() : 0;
      Integer ownIncomeInt = nextIncome();
      // SOZIO_F0083: Household income
      record.add(String.valueOf(partnerIncomeInt + ownIncomeInt));
      // SOZIO_F0084: Income group of the household
      record.add(null);
      // SOZIO_F0086: Own income
      record.add(String.valueOf(ownIncomeInt));
      // SOZIO_F0087: Own income group
      record.add(null);
    } else {
      int ownIncomeGroupIndex = nextInt(0, F0084_87.length);
      int householdIncomeGroupIndex =
          (hasPartner) ? nextInt(ownIncomeGroupIndex, F0084_87.length) : ownIncomeGroupIndex;
      // SOZIO_F0083: Household income
      record.add(null);
      // SOZIO_F0084: Income group of the household
      record.add(F0084_87[householdIncomeGroupIndex]);
      // SOZIO_F0086: Own income
      record.add(null);
      // SOZIO_F0087: Own income group
      record.add(F0084_87[ownIncomeGroupIndex]);
    }
  }

  private static void addVocationalQualification(List<String> record) {
    int num = nextInt(0, 4);
    List<Integer> indexes = ints(num, 0, 7);
    for (int i = 0; i < 7; i++) {
      if (indexes.contains(i)) record.add("1");
      else record.add("0");
    }
  }

  private static void addHouseholdSize(List<String> record) {
    int f0079 = nextInt(1, 7);
    int f0080 = 0;
    if (f0079 > 1) f0080 = nextInt(1, f0079);
    record.add(String.valueOf(f0079));
    record.add(String.valueOf(f0080));
  }

  private static boolean addPartnerProfession(List<String> record) {
    // SOZIO_F0026: Marital status
    String ms = next(F0026);
    record.add(ms);
    // SOZIO_F0035: Partnership
    String ps = next(F0035);
    record.add(ps);
    // SOZIO_F0078: Partner's profession
    if ("1".equals(ms) || "1".equals(ps)) {
      record.add(next(F0072_78));
      return true;
    } else {
      record.add(null);
      return false;
    }
  }

  private static Integer nextIncome() {
    List<Integer> income = new ArrayList<>();
    for (int i = 0; i < 1000; i += 200) income.add(i);
    for (int i = 1000; i < 5000; i += 100) income.add(i);
    for (int i = 5000; i < 25000; i += 1000) income.add(i);
    return income.get(nextInt(0, income.size()));
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

  private static List<Integer> ints(int size, int origin, int bound) {
    return ThreadLocalRandom.current()
        .ints(size, origin, bound)
        .distinct()
        .sorted()
        .boxed()
        .collect(Collectors.toList());
  }
}
