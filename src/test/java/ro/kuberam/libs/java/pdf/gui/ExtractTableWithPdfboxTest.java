package ro.kuberam.libs.java.pdf.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.giaybac.traprange.PDFTableExtractor;
import com.giaybac.traprange.entity.Table;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.writers.CSVWriter;

public class ExtractTableWithPdfboxTest {

	@Rule
	public TestName name = new TestName();

	@Test
	public void traprangeTest1() throws IOException, XMLStreamException {
		PDFTableExtractor extractor = new PDFTableExtractor();

		List<Table> tables = extractor.setSource("/home/claudius/comune.pdf").addPage(5).extract();

		String html = tables.get(0).toHtml();

		try (Writer writer = new OutputStreamWriter(new FileOutputStream("/home/claudius/comune.html"), "UTF-8")) {
			for (Table table : tables) {
				writer.write(table.toHtml());
			}
		}

	}

	@Test
	public void tabulaTest1() {
		ObjectExtractor oe = null;
		try {
			PDDocument document = PDDocument.load(new File("/home/claudius/comune.pdf"));
			oe = new ObjectExtractor(document);
			Page page = oe.extract(7);
			oe.close();

			BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
			technology.tabula.Table table = bea.extract(page).get(0);

			StringBuilder sb = new StringBuilder();
			(new CSVWriter()).write(sb, table);
			String s = sb.toString();
			
			System.out.println(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
