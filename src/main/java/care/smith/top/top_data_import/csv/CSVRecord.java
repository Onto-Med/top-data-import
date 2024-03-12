package care.smith.top.top_data_import.csv;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CSVRecord {

  private Map<String, CSVValue> recordMap = new HashMap<>();
  private CSVValue[] record;

  public CSVRecord(CSVHead[] header, String[] values) {
    record = new CSVValue[header.length];
    for (int i = 0; i < header.length; i++) {
      CSVValue val = new CSVValue(header[i], values[i]);
      record[i] = val;
      recordMap.put(val.getName(), val);
    }
  }

  public CSVValue get(int index) {
    return record[index];
  }

  public CSVValue get(String name) {
    return recordMap.get(name);
  }

  public CSVValue[] getOthers(String... excludedNames) {
    return getOthers(Set.of(excludedNames));
  }

  public CSVValue[] getOthers(Set<String> excludedNames) {
    return Arrays.stream(record)
        .filter(f -> !excludedNames.contains(f.getName()))
        .toArray(CSVValue[]::new);
  }

  @Override
  public String toString() {
    return Arrays.toString(record);
  }
}
