package ro.kuberam.libs.java.pdf.contentManipulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.junit.Test;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;

public class CreateDocumentTest {

	private static File pdfFilePath;
	static {
		pdfFilePath = new File(
				"/home/claudius/workspaces/repositories/git/kuberam/libs/java/pdf/target/stamped-document.pdf");
	}

	@Test
	public void testStampSignature2() throws IOException {
		BrowserEngine webKit = BrowserFactory.getWebKit();

		PageConfiguration config = new PageConfiguration();
		config.setUserAgent("Custom User Agent String");

		Page page = webKit.navigate("http://httpbin.org/user-agent", config);
		page.show();
	}
}
