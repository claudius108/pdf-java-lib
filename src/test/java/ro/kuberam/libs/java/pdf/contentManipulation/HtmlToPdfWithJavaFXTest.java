package ro.kuberam.libs.java.pdf.contentManipulation;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.sun.javafx.application.PlatformImpl;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HtmlToPdfWithJavaFXTest {

	@Rule
	public TestName name = new TestName();

	@Ignore
	@Test
	public void test1() throws IOException {
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
			job.getJobSettings().setJobName(name.getMethodName() + ".pdf");

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
	public void test2() throws IOException {
		PlatformImpl.startup(() -> {});
		
		PlatformImpl.startup(new Runnable() {
			@Override
			public void run() {
				final WebView webPage = new WebView();
				System.out.println("webPage = " + webPage);
				final WebEngine webEngine = webPage.getEngine();
				webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
						if (newState == State.SUCCEEDED) {
							Printer pdfPrinter = null;

							// Windows: Instal PDF24 from http://en.pdf24.org
							// Linux: Install cups-pdf http://www.cups-pdf.de

							// Optionally PDF24 could be configured for silent
							// mode from
							// "C:\Program Files
							// (x86)\PDF24\pdf24-SettingsUITool.exe"
							// from the Menu
							// "PDF Printer"
							// Set "Automatically save..." checkbox to true

							Iterator<Printer> iter = Printer.getAllPrinters().iterator();
							while (iter.hasNext()) {
								Printer printer = iter.next();

								if (printer.getName().endsWith("PDF")) {
									pdfPrinter = printer;
								}
							}
							System.out.println(pdfPrinter.getName());

							PrinterJob job = null;
							try {
								// clear margins
								PageLayout layout = pdfPrinter.createPageLayout(Paper.A4, PageOrientation.PORTRAIT,
										MarginType.HARDWARE_MINIMUM);

								job = PrinterJob.createPrinterJob(pdfPrinter);
								job.getJobSettings().setPageLayout(layout);
								job.getJobSettings().setJobName(name.getMethodName() + ".pdf");

								job.printPage(webPage);
								job.endJob();
							} finally {
								if (job != null) {
									job.endJob();
								}
							}
						}
					}
				});

				String url = "file:///home/claudius/workspaces/repositories/git/sarit-pm/tests/resources/fonts/main-fonts.html";
				System.out.println("url = " + url);

				webEngine.load(url);
			}
		});
	}
}
