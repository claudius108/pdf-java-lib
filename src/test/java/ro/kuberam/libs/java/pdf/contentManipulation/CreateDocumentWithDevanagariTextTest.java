package ro.kuberam.libs.java.pdf.contentManipulation;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.web.WebEngine;

public class CreateDocumentWithDevanagariTextTest {

	@Rule
	public TestName name = new TestName();

	private static File pdfFilePath;
	static {
		pdfFilePath = new File(
				"/home/claudius/workspaces/repositories/git/kuberam/libs/java/pdf/target/stamped-document.pdf");
	}

	@Test
	public void testPdfPrinting() throws IOException {
		Printer pdfPrinter = null;

		// Windows: Instal PDF24 from http://en.pdf24.org
		// Linux: Install cups-pdf http://www.cups-pdf.de

		// Optionally PDF24 could be configured for silent mode from
		// "C:\Program Files (x86)\PDF24\pdf24-SettingsUITool.exe" from the Menu
		// "PDF Printer"
		// Set "Automatically save..." checkbox to true

		Iterator<Printer> iter = Printer.getAllPrinters().iterator();
		while (iter.hasNext()) {
			Printer printer = iter.next();

			if (printer.getName().endsWith("PDF")) {
				pdfPrinter = printer;
			}
		}

		String url = "/home/claudius/workspaces/repositories/git/sarit-pm/tests/resources/fonts/main-fonts.html";

		Page page = BrowserFactory.getWebKit().navigate(url);

		WebEngine engine = (WebEngine) page.getEngine();

		// Note: Optionally -webkit-print-color-adjust: exact; could be used for
		// fix background-color problem
		// @see
		// http://stackoverflow.com/questions/14987496/background-color-not-showing-in-print-preview

		PrinterJob job = null;
		try {
			page.show();
			// clear margins
			PageLayout layout = pdfPrinter.createPageLayout(Paper.A4, PageOrientation.PORTRAIT,
					MarginType.HARDWARE_MINIMUM);

			job = PrinterJob.createPrinterJob(pdfPrinter);
			job.getJobSettings().setPageLayout(layout);
			job.getJobSettings().setJobName("Sample Printing Job");

			engine.print(job);
			job.endJob();
		} finally {
			if (job != null) {
				job.endJob();
			}
			page.close();
		}
	}

	@Test
	public void testPdfBox() throws IOException {
		// Create a document and add a page to it
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		// Create a new font object by loading a TrueType font into the document
		PDFont font = PDType0Font.load(document,
				new File("/home/claudius/workspaces/repositories/backup/fonts/Sanskrit2003.ttf"));

		// Start a new content stream which will "hold" the to be created
		// content
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Define a text content stream using the selected font, moving the
		// cursor and drawing the text "Hello World"
		contentStream.beginText();
		contentStream.setFont(font, 12);
		contentStream.moveTextPositionByAmount(100, 700);
		contentStream.showText("कारणत्त्वङ्गवाश्वादीनमपीति चेत् युक्तम्");
		contentStream.endText();

		// Make sure that the content stream is closed:
		contentStream.close();

		// Save the results and ensure that the document is properly closed:
		document.save("target/" + name.getMethodName() + ".pdf");
		document.close();

	}
}
