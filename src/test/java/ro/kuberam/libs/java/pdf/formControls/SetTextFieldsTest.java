package ro.kuberam.libs.java.pdf.formControls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.junit.Test;

public class SetTextFieldsTest {


	@SuppressWarnings("unchecked")
	@Test
	public void test1() throws IOException, XMLStreamException, COSVisitorException {

		InputStream pdfIs = getClass().getResourceAsStream("SF.pdf");
		
		ObjectInputStream ois = new ObjectInputStream(this.getClass().getResourceAsStream("fields.ser"));
		Map<String, String> fieldsMap = null;
		try {
			fieldsMap = (Map<String, String>) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();

		ByteArrayOutputStream output = SetTextFields.run(pdfIs, fieldsMap);

		try {
			FileOutputStream fos = new FileOutputStream(new File("target/result.pdf"));
			output.writeTo(fos);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			output.close();
		}
	}


}
