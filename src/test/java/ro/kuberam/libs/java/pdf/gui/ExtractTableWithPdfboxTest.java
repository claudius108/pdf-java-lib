package ro.kuberam.libs.java.pdf.gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.giaybac.traprange.PDFTableExtractor;
import com.giaybac.traprange.entity.Table;

public class ExtractTableWithPdfboxTest {

	@Rule
	public TestName name = new TestName();

	@Test
	public void traprangeTest1() throws IOException, XMLStreamException {
		PDFTableExtractor extractor = new PDFTableExtractor();

		List<Table> tables = extractor.setSource("/home/claudius/Downloads/comune.pdf").addPage(48).extract();

		String html = tables.get(0).toHtml();

		try (Writer writer = new OutputStreamWriter(new FileOutputStream("/home/claudius/Downloads/comune.html"),
				"UTF-8")) {
			for (Table table : tables) {
				writer.write("Page: " + (table.getPageIdx() + 1) + "\n");
				writer.write(table.toHtml());
			}
		}

	}

}
