package ro.kuberam.libs.java.pdf.contentManipulation;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class RotatePageTest {
	
	@Rule public TestName name = new TestName();

	@Test
	public void rotatePageTest1() throws IOException, XMLStreamException {
		PDDocument pdfDocument = null;

		InputStream pdfIs = getClass().getResourceAsStream("../D5_US4268005A_002-6_edit.pdf");

		pdfDocument = PDDocument.load(pdfIs);

		for (PDPage page : pdfDocument.getPages()) {
			page.setRotation(360);;
		}

		pdfDocument.save("target/" + name.getMethodName() + ".pdf");

		pdfDocument.close();

	}

}
