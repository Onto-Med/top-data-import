package care.smith.top.top_data_import.pdf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.LoggerFactory;

public class PDFReader {

  public static String read(Path filePath) {
    return read(filePath.toFile());
  }

  public static String read(String filePath) {
    return read(new File(filePath));
  }

  public static String read(File file) {
    try {
      PDDocument document = Loader.loadPDF(file);
      PDFTextStripper stripper = new PDFTextStripper();
      stripper.setSortByPosition(true);
      String text = stripper.getText(document);
      document.close();
      return text;
    } catch (IOException e) {
      LoggerFactory.getLogger(PDFReader.class).warn(e.getMessage(), e);
    }
    return null;
  }
}
