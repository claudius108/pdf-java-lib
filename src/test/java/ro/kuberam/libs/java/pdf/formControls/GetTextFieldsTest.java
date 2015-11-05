package ro.kuberam.libs.java.pdf.formControls;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class GetTextFieldsTest {
	
	@Rule public TestName name = new TestName();
	
	@Test
	public void getTextFieldsTest1() throws IOException, XMLStreamException {

		InputStream pdfIs = getClass().getResourceAsStream("GetTextFieldsTest.pdf");

		Map<String, String> result = GetTextFields.run(pdfIs);

		FileOutputStream fos = new FileOutputStream("target/" + name.getMethodName() + ".ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(result);
		oos.close();
	}

}
