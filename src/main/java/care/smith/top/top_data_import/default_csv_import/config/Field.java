package care.smith.top.top_data_import.default_csv_import.config;

import care.smith.top.model.DataType;

public interface Field {

  String getFieldName();

  String getDBProperties();

  DataType getDataType();

  String getPropertyName();
}
