package care.smith.top.top_data_import.pdf;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFReader {

  public static String read(String filePath) {
    try {
      PDDocument document = Loader.loadPDF(new File(filePath));
      String text = new PDFTextStripper().getText(document);
      document.close();
      return text;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
