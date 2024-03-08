package care.smith.top.top_data_import.csv;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CSVRecord {

  private Map<String, CSVField> recordMap = new HashMap<>();
  private CSVField[] record;

  public CSVRecord(CSVField[] header, String[] values) {
    record = new CSVField[header.length];
    for (int i = 0; i < header.length; i++) {
      CSVField val = new CSVField(header[i], values[i], i);
      record[i] = val;
      recordMap.put(val.getName(), val);
    }
  }

  public CSVField get(int index) {
    return record[index];
  }

  public CSVField get(String name) {
    return recordMap.get(name);
  }

  public CSVField[] getOthers(String... excludedNames) {
    return getOthers(Set.of(excludedNames));
  }

  public CSVField[] getOthers(Set<String> excludedNames) {
    return Arrays.stream(record)
        .filter(f -> !excludedNames.contains(f.getName()))
        .toArray(CSVField[]::new);
  }

  @Override
  public String toString() {
    return Arrays.toString(record);
  }
}
