package care.smith.top.top_data_import.default_csv_import.config;

import java.util.stream.Stream;

public class Util {
  protected static boolean isAllowedName(String name, String... allowedNames) {
    if (name == null || name.isBlank()) return false;
    return Stream.of(allowedNames).anyMatch(name.replace("_", "").toLowerCase()::equals);
  }
}
