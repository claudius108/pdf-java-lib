package ro.kuberam.libs.java.pdf.ocr;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.junit.Ignore;
import org.junit.Test;

public class OcrTest {

	@Ignore
	@Test
	public void test1() throws IOException, XMLStreamException {

        File imageFile = null;
		try {
			imageFile = new File(getClass().getResource("sample-1.jpg").toURI());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Tesseract instance = Tesseract.getInstance();  // JNA Interface Mapping
        // Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping

        try {
        	System.out.println(imageFile.exists());
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        
	}

}

