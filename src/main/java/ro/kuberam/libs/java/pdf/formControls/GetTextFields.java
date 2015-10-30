package ro.kuberam.libs.java.pdf.formControls;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;

public class GetTextFields {

	public static Map<String, String> run(InputStream pdfIs) throws IOException, XMLStreamException {

		PDDocument pdfDocument = PDDocument.load(pdfIs);
		PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		List<PDField> fields = acroForm.getFields();

		Map<String, String> result = new HashMap<String, String>();

		for (PDField field : fields) {
			getTextField(field, field.getPartialName(), result);
		}

		pdfDocument.close();

		return result;
	}

	private static void getTextField(final PDField field, String sParent, Map<String, String> result)
			throws IOException, XMLStreamException {

		String partialName = field.getPartialName();

		if (field instanceof PDNonTerminalField) {
			if (!sParent.equals(partialName)) {
				sParent = sParent + "." + partialName;
			}

			for (PDField child : ((PDNonTerminalField) field).getChildren()) {
				getTextField(child, sParent, result);
			}
		} else {
			result.put(field.getFullyQualifiedName(), field.getValueAsString());
		}
	}
}
