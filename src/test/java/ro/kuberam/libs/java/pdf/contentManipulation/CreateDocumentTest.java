package ro.kuberam.libs.java.pdf.contentManipulation;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;

public class CreateDocumentTest {
	
	@Rule public TestName name = new TestName();
	
	private static File pdfFilePath;
	static {
		pdfFilePath = new File(
				"/home/claudius/workspaces/repositories/git/kuberam/libs/java/pdf/target/stamped-document.pdf");
	}

	@Ignore
	@Test
	public void test1() throws IOException {
		BrowserEngine webKit = BrowserFactory.getWebKit();

		PageConfiguration config = new PageConfiguration();
		config.setUserAgent("Custom User Agent String");

		Page page = webKit.navigate("http://httpbin.org/user-agent", config);
		page.show();
	}
}
