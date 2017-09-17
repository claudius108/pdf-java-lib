package ro.kuberam.libs.java.pdf.preflight;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class PreflightTest {

	@Rule
	public TestName name = new TestName();

	@Test
	public void test1() throws IOException, XMLStreamException {

		InputStream pdfIs = getClass().getResourceAsStream("fonts.pdf");
		PDDocument document = PDDocument.load(pdfIs);

		for (PDPage page : document.getPages()) {
			PDFStreamParser parser = new PDFStreamParser(page);
			parser.parse();
			List<Object> tokens = parser.getTokens();

			for (int j = 0; j < tokens.size(); j++) {
				Object next = tokens.get(j);
				if (next instanceof Operator) {
					Operator op = (Operator) next;

					if (op.getName().equals("Tj")) {
						// Tj takes one operator and that is the string
						// to display so lets update that operator
						COSString previous = (COSString) tokens.get(j - 1);
						String string = previous.getString();

						// System.out.println("string = " + string);
						// string = string.replaceFirst(strToFind, message);
						// previous.reset();
						// previous.append(string.getBytes("ISO-8859-1"));
					} else if (op.getName().equals("TJ")) {
						// COSArray previous = (COSArray) tokens.get(j - 1);
						// for (int k = 0; k < previous.size(); k++) {
						// Object arrElement = previous.getObject(k);
						// if (arrElement instanceof COSString) {
						// COSString cosString = (COSString) arrElement;
						// String string = cosString.getString();
						// string = string.replaceFirst(strToFind, message);
						// cosString.reset();
						// cosString.append(string.getBytes("ISO-8859-1"));
						// }
						// }
					}
				}
			}

		}

	}

}
