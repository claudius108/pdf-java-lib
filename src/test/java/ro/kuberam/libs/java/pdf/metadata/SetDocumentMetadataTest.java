package ro.kuberam.libs.java.pdf.metadata;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class SetDocumentMetadataTest {
	
	@Rule public TestName name = new TestName();
	
	@Test
	public void setDocumentMetadataTest1() throws IOException, XMLStreamException {
		PDDocument pdfDocument = null;

		InputStream pdfIs = getClass().getResourceAsStream("../formControls/SF.pdf");

		pdfDocument = PDDocument.load(pdfIs);

		PDDocumentInformation metadata = pdfDocument.getDocumentInformation();

		metadata.setAuthor("claudius");

		System.out.println(metadata.getAuthor());

		pdfDocument.save("target/" + name.getMethodName() + ".pdf");

		pdfDocument.close();

	}

}
