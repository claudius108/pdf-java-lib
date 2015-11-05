package ro.kuberam.libs.java.pdf.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

public class GetMetadata {

	public static Map<String, Object> documentProperties(InputStream pdfIs) throws IOException, XMLStreamException {

		HashMap<String, Object> metadata = new HashMap<String, Object>();

		PDDocument pdfDocument = PDDocument.load(pdfIs);
		PDDocumentInformation documentInformation = pdfDocument.getDocumentInformation();

		metadata.put("title", documentInformation.getTitle());
		metadata.put("subject", documentInformation.getSubject());
		metadata.put("author", documentInformation.getAuthor());
		metadata.put("keywords", documentInformation.getKeywords());
		metadata.put("producer", documentInformation.getProducer());
		metadata.put("creator", documentInformation.getCreator());
		metadata.put("created", documentInformation.getCreationDate());
		metadata.put("modified", documentInformation.getModificationDate());
		metadata.put("format", pdfDocument.getVersion());
		metadata.put("number-of-pages", pdfDocument.getNumberOfPages());
		metadata.put("optimized", "");
		metadata.put("security", "");
		metadata.put("paper-size", "");
		metadata.put("fonts", pdfDocument.getDocumentCatalog().getCOSObject());

		pdfDocument.close();

		return metadata;
	}

}
