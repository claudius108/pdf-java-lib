package ro.kuberam.libs.java.pdf.formControls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class SetTextFields {

	public static ByteArrayOutputStream run(InputStream pdfIs, Map<String, String> fieldsMap)
			throws IOException {

		PDDocument pdfDocument = PDDocument.load(pdfIs);
		PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
			PDField field = acroForm.getField(entry.getKey());
			if (field != null) {
				field.setValue(entry.getValue());
			} else {
				System.err.println("No field found with name:" + entry.getKey());
			}
		}

		pdfDocument.save(output);
		pdfDocument.close();

		return output;
	}

}
