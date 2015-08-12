package ro.kuberam.libs.java.pdf.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class GetDocumentMetadataTest {

	@Test
	public void test1() throws IOException, XMLStreamException {

		InputStream pdfIs = getClass().getResourceAsStream("../contentRasterization/frus1969-76v19p1.pdf");

		Map<String, Object> metadata = GetMetadata.documentProperties(pdfIs);

		System.out.println(metadata);
	}

}
