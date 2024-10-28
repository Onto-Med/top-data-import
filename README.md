# TOP Data Import

## Introduction

This GitHub repository contains the source code for a software component that integrates the Terminology- and
Ontology-based Phenotyping (TOP) Framework into the LIFE-Adult-Study data request workflow. The primary goal of this
component is to streamline the process of accessing and utilizing clinical research data within the study by providing a
user-friendly interface for building computable phenotype models and improving data accessibility through standardized
metadata.

### Key Features

- **Computable Phenotype Modeling:** Enables researchers to create and refine phenotype models using standardized
  terminologies, facilitating data exploration and analysis.
- **Metadata Standardization:** Converts the study's existing metadata into a structured and machine-readable format,
  enhancing interoperability and mapping to external standards.
- **Data Request Streamlining:** Simplifies the data request process by providing a more efficient and user-friendly
  approach.
- **Integration with the LIFE-Adult-Study:** Seamlessly integrates with the existing study infrastructure to improve
  data accessibility and research efficiency.

## Usage

When a LIFE project agreement is approved, a dataset is provided as a CSV file and associated metadata as a PDF. These
files are used as input to the TOP data import component. The programmatic use of this component is described below.

1. Create a copy of `config.properties.dist` and modify the file as needed (e.g., provide paths to CSV and PDF input
   files).

```sh
cp config.properties.dist config.properties
```

2. Run `care.smith.top.top_data_import.life.LIFEConverterApp.main` and provide the configuration file as argument.
   Alternatively, you can call the converter the following way:

```java
import care.smith.top.top_data_import.life.LIFEConverterApp;

public static void main(String[] args) {
  new LIFEConverterApp(new Config("config.properties")).convert();
}
```

3. The resulting phenotype model, H2 database, and database adapter will be stored as specified in the configuration
   file.

## Contribution and Development

Please see our [Contributing Guide](CONTRIBUTING.md).

## License

The code in this repository and the package `care.smith.top:top-data-import` are licensed under [MIT](LICENSE).
