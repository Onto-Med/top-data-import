package care.smith.top.top_data_import.life;

import care.smith.top.model.Category;
import care.smith.top.model.Entity;
import care.smith.top.model.Phenotype;
import care.smith.top.top_data_import.Config;
import care.smith.top.top_data_import.Util;
import care.smith.top.top_data_import.pdf.PDFReader;
import care.smith.top.top_phenotypic_query.util.Entities;
import care.smith.top.top_phenotypic_query.util.builder.Cat;
import care.smith.top.top_phenotypic_query.util.builder.Phe;
import care.smith.top.top_phenotypic_query.util.builder.Res;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LIFEMetadataConverter {
  private final Logger LOGGER = LoggerFactory.getLogger(LIFEMetadataConverter.class);
  private String txt;
  private String[] lines;
  private String assessment;
  private String tableAlias;
  private String tableName;
  private Config config;

  private final String COL_TAB_PREFIX;
  private static final String TAB = "tabelle:";
  private static final String CODE_TEXT = "Code Fragentext";
  private static final String CODE_DESC = "Code Beschreibung";
  private static final Pattern CODE_LIST = Pattern.compile("C.?o.?de Bezeichnung");
  private static final String PAGE = "Seite";
  private static final String QUEST_GROUP = "Fragengruppe";

  private List<Entity> phenotypes = new ArrayList<>();

  public LIFEMetadataConverter(Path filePath, Config config) {
    this.config = config;
    txt = PDFReader.read(filePath);
    lines = txt.split("\\R");
    assessment = lines[0].trim();
    lines = Arrays.stream(lines).map(String::trim).filter(l -> !skip(l)).toArray(String[]::new);
    int tabStart = txt.indexOf(TAB);
    int tabEnd = txt.indexOf(")", tabStart + 1);
    String[] tab =
        txt.substring(tabStart, tabEnd + 1)
            .replace(TAB, "")
            .replace("(", "")
            .replace(")", "")
            .trim()
            .split(" +");
    tableAlias = tab[0];
    tableName = tab[1];
    COL_TAB_PREFIX = "(" + tableName + "_";
    LOGGER.debug(assessment + ":" + tableAlias + ":" + tableName);
    LOGGER.debug("========================================================");
    for (String line : lines) LOGGER.debug(line);
  }

  public LIFEMetadataConverter(String filePath, Config config) {
    this(Path.of(filePath), config);
  }

  private boolean skip(String line) {
    return line.startsWith(CODE_TEXT)
        || line.startsWith(CODE_DESC)
        || line.startsWith(PAGE)
        || assessment.equals(line.trim());
  }

  public void convert() {
    Category cat = null;

    for (int i = 0; i < lines.length; i++) {
      if (lines[i].startsWith(QUEST_GROUP)) {
        cat = getGroup(i);
        phenotypes.add(cat);
      } else if (lines[i].contains(COL_TAB_PREFIX)) getItem(i, cat);
    }

    Entities.writeJSON(
        config.getPhenotypesPath() + "/" + tableAlias + ".json", phenotypes.toArray(Entity[]::new));
  }

  private String cleanID(String id) {
    return id.replace("/", "-");
  }

  private Category getGroup(int actualGroupLine) {
    int lastGroupLine = getLastGroupLine(actualGroupLine);
    String name = lines[actualGroupLine].substring(QUEST_GROUP.length()).trim();
    StringBuilder desc = null;

    for (int i = actualGroupLine + 1; i <= lastGroupLine; i++) {
      if (desc == null) desc = new StringBuilder(lines[i]);
      else desc.append(" ").append(lines[i]);
    }

    LOGGER.debug("---------- GROUP ----------");
    LOGGER.debug(name + ":" + desc);
    LOGGER.debug("---------------------------");

    if (desc == null) return new Cat(cleanID(name)).titleDe(name).get();
    return new Cat(cleanID(name)).titleDe(name).descriptionDe(desc.toString()).get();
  }

  private int getLastGroupLine(int actualGroupLine) {
    for (int i = actualGroupLine + 1; i < lines.length; i++)
      if (lines[i].contains(COL_TAB_PREFIX)) return i - 2;
    return lines.length - 1;
  }

  private void getItem(int actualItemLine, Category cat) {

    int lastItemLine = getLastItemLine(actualItemLine);

    String name =
        lines[actualItemLine]
            .substring(lines[actualItemLine].indexOf(COL_TAB_PREFIX))
            .replace(COL_TAB_PREFIX, "")
            .replace(")", "")
            .trim();

    String[] props = lines[actualItemLine - 1].trim().split(" +");

    String alias = props[props.length - 1].replace(tableAlias + "_", "").trim();

    int aliasPlusDatatypeLength = 2;
    String datatype = props[props.length - 2].trim();
    if ("/".equals(props[props.length - 3].trim())) {
      datatype = props[props.length - 4].trim() + "/" + datatype;
      aliasPlusDatatypeLength = 4;
    }

    String desc = getDescription(props, aliasPlusDatatypeLength, actualItemLine, lastItemLine);

    LOGGER.debug(name + ":" + alias + ":" + datatype + ":" + desc);

    Phe phe = new Phe(name, tableAlias, name).titleDe(alias).descriptionDe(desc);
    if ("TEXT".equals(datatype)) phe.string();
    else if ("ZAHL".equals(datatype)) phe.number();
    else if ("DATUM/ZEIT".equals(datatype)) phe.dateTime();
    if (cat != null) phe.category(cat);
    Phenotype phenotype = phe.get();

    phenotypes.add(phenotype);

    getCodeList(actualItemLine, lastItemLine, phenotype);
  }

  private int getLastItemLine(int actualItemLine) {
    for (int i = actualItemLine + 1; i < lines.length; i++) {
      if (lines[i].startsWith(QUEST_GROUP)) return i - 1;
      if (lines[i].contains(COL_TAB_PREFIX)) return i - 2;
    }
    return lines.length - 1;
  }

  private String getDescription(
      String[] props, int aliasPlusDatatypeLength, int actualItemLine, int lastItemLine) {

    StringBuilder desc =
        new StringBuilder(
            Strings.join(List.of(props).subList(1, props.length - aliasPlusDatatypeLength), ' '));

    String descPart =
        lines[actualItemLine].substring(0, lines[actualItemLine].indexOf(COL_TAB_PREFIX)).trim();
    if (!descPart.isBlank()) desc.append(" ").append(descPart);

    for (int i = actualItemLine + 1; i <= lastItemLine; i++) {
      if (CODE_LIST.matcher(lines[i]).find()) break;
      desc.append(" ").append(lines[i]);
    }

    return desc.toString();
  }

  private void getCodeList(int actualItemLine, int lastItemLine, Phenotype phe) {
    int codeLine = -1;
    for (int i = actualItemLine + 1; i <= lastItemLine; i++) {
      if (CODE_LIST.matcher(lines[i]).find()) {
        codeLine = i + 1;
        break;
      }
    }

    if (codeLine == -1) return;

    for (int i = codeLine; i <= lastItemLine; i++) {
      if (CODE_LIST.matcher(lines[i]).find()) continue;
      if (Util.isNumber(lines[i].split(" ")[0].trim())) {
        int split = lines[i].indexOf(" ");
        String code = lines[i].substring(0, split).trim();
        String label = lines[i].substring(split + 1).trim();
        LOGGER.debug(code + ":" + label);

        phenotypes.add(
            new Phe(phe.getId() + "_" + code)
                .titleDe(code)
                .descriptionDe(label)
                .restriction(phe, Res.of(new BigDecimal(code)))
                .get());

      } else break;
    }
  }

  public static void main(String[] args) {

    new LIFEMetadataConverter(
            "examples/project_agreement_1/D00001_Example.pdf", new Config("config.properties"))
        .convert();
  }
}
