package ro.kuberam.libs.java.pdf.formControls;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class GetTextFields {

	public static Map<String, String> run(InputStream pdfIs) throws IOException, XMLStreamException {

		PDDocument pdfDocument = PDDocument.load(pdfIs, true);
		PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

		@SuppressWarnings("unchecked")
		List<PDField> fields = acroForm.getFields();
		Iterator<PDField> fieldsIter = fields.iterator();

		Map<String, String> result = new HashMap<String, String>();

		while (fieldsIter.hasNext()) {
			PDField field = fieldsIter.next();
			getTextField(field, field.getPartialName(), result);
		}

		pdfDocument.close();

		return result;
	}

	private static void getTextField(final PDField field, String sParent, Map<String, String> result)
			throws IOException, XMLStreamException {
		List<COSObjectable> kids = field.getKids();
		if (kids != null) {
			Iterator<COSObjectable> kidsIter = kids.iterator();
			if (!sParent.equals(field.getPartialName())) {
				sParent = sParent + "." + field.getPartialName();
			}
			while (kidsIter.hasNext()) {
				Object pdfObj = kidsIter.next();
				PDField kid = (PDField) pdfObj;
				if (kid instanceof PDField) {
					getTextField(kid, sParent, result);
				}
			}
		} else {
			if (field.getFieldType().equals("Tx")) {
				result.put(field.getFullyQualifiedName(), field.getValue());
			}
		}
	}
}
