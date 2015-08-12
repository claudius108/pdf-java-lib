package ro.kuberam.libs.java.pdf.metadata;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.junit.Test;

public class SetDocumentMetadataTest {

	@Test
	public void test1() throws IOException, XMLStreamException {
		PDDocument pdfDocument = null;
		
		try {
			InputStream pdfIs = getClass().getResourceAsStream("../formControls/SF.pdf");

			pdfDocument = PDDocument.load(pdfIs, true);

			PDDocumentInformation metadata = pdfDocument.getDocumentInformation();

			metadata.setAuthor("claudius");

			System.out.println(metadata.getAuthor());

			pdfDocument.save("target/SetDocumentMetadataTest.pdf");
		} catch (COSVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pdfDocument.close();
		}
	}

}
