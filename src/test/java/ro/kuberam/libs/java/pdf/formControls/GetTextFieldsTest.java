package ro.kuberam.libs.java.pdf.formControls;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class GetTextFieldsTest {

	@Test
	public void test1() throws IOException, XMLStreamException {

		InputStream pdfIs = getClass().getResourceAsStream("SF.pdf");

		Map<String, String> result = GetTextFields.run(pdfIs);

		FileOutputStream fos = new FileOutputStream("target/result.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(result);
		oos.close();
	}

}
