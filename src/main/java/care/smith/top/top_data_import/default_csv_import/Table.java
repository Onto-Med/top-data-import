package care.smith.top.top_data_import.default_csv_import;

public enum Table {
  SUBJECT("subject"),
  ENCOUNTER("encounter"),
  PHENOTYPE("phenotype");

  private String name;

  private Table(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
