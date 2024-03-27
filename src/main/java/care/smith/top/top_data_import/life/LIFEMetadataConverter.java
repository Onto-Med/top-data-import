package care.smith.top.top_data_import.life;

import care.smith.top.top_data_import.Util;
import care.smith.top.top_data_import.pdf.PDFReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.util.Strings;

public class LIFEMetadataConverter {

  private String txt;
  private String[] lines;
  private String assessment;
  private String tableAlias;
  private String tableName;

  public LIFEMetadataConverter(String filePath) {
    txt = PDFReader.read(filePath);
    lines = txt.split("\\R");
    assessment = lines[0].trim();
    int tabStart = txt.indexOf("tabelle:");
    int tabEnd = txt.indexOf(")", tabStart + 1);
    String[] tab =
        txt.substring(tabStart, tabEnd + 1)
            .replace("tabelle:", "")
            .replace("(", "")
            .replace(")", "")
            .trim()
            .split(" +");
    tableAlias = tab[0];
    tableName = tab[1];
    System.out.println(assessment + ":" + tableAlias + ":" + tableName);
    System.out.println("========================================================");
    //    System.out.println(txt);
  }

  public void convert() {
    int lastItemLine = -1;
    for (int i = 0; i < lines.length; i++) {
      if (lines[i].contains("(" + tableName + "_")) {
        lastItemLine = getItem(lastItemLine, i);
      }
    }
  }

  private int getItem(int lastItemLine, int actualItemLine) {
    String name =
        lines[actualItemLine].replace(tableName + "_", "").replace("(", "").replace(")", "").trim();

    String[] props = lines[actualItemLine - 1].trim().split(" +");

    String alias = props[props.length - 1].replace(tableAlias + "_", "").trim();

    int aliasPlusDatatypeLength = 2;
    String datatype = props[props.length - 2].trim();
    if (props.length >= 4 && "/".equals(props[props.length - 3].trim())) {
      datatype = props[props.length - 4].trim() + "/" + datatype;
      aliasPlusDatatypeLength = 4;
    }

    String desc = null;
    if (props.length > aliasPlusDatatypeLength)
      desc = Strings.join(List.of(props).subList(1, props.length - aliasPlusDatatypeLength), ' ');

    System.out.println(name + ":" + alias + ":" + datatype + ":" + desc);

    int lastCodeLine = getCodeList(actualItemLine);

    return (lastCodeLine == -1) ? actualItemLine : lastCodeLine;
  }

  private String getDescription(
      String[] props, int aliasLine, int aliasPlusDatatypeLength, int lastItemLine) {
    String desc = null;
    if (props.length > aliasPlusDatatypeLength)
      desc = Strings.join(List.of(props).subList(1, props.length - aliasPlusDatatypeLength), ' ');

    List<String> descLines = new ArrayList<>();

    for (int i = aliasLine - 1; i < lastItemLine; i--) {
      if (skip(i)) continue;
      descLines.add(lines[i]);
    }

    return desc;
  }

  private int getCodeList(int actualItemLine) {
    int codeLine = -1;
    for (int i = actualItemLine + 1; i < lines.length; i++) {
      if (skip(i)) continue;
      if (lines[i].contains("Code Bezeichnung")) {
        codeLine = i + 1;
        break;
      }
      break;
    }

    if (codeLine == -1) return -1;

    for (; codeLine < lines.length; codeLine++) {
      if (skip(codeLine) || lines[codeLine].contains("Code Bezeichnung")) continue;
      if (Util.isNumber(lines[codeLine].trim().split(" ")[0].trim())) {
        int split = lines[codeLine].indexOf(" ");
        String code = lines[codeLine].substring(0, split).trim();
        String label = lines[codeLine].substring(split + 1).trim();
        System.out.println(code + ":" + label);
      } else break;
    }

    return codeLine;
  }

  private boolean skip(int i) {
    return lines[i].startsWith("Code Fragentext")
        || lines[i].startsWith("Fragengruppe")
        || lines[i].startsWith("Code Beschreibung")
        || lines[i].startsWith("Seite")
        || assessment.equals(lines[i].trim());
  }

  public static void main(String[] args) {
    new LIFEMetadataConverter("test_files/T00001.pdf").convert();
    new LIFEMetadataConverter("test_files/D00140.pdf").convert();
  }
}
