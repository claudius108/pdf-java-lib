package ro.kuberam.libs.java.pdf.contentManipulation;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.page.PageType;

public class HtmlToPdfWithWkhtmltopdfTest {

	@Rule
	public TestName name = new TestName();

	@Test
	public void test1() throws IOException, InterruptedException {
		Pdf pdf = new Pdf();

		pdf.addPage("/home/claudius/workspaces/repositories/git/sarit-pm/tests/resources/fonts/main-fonts.html", PageType.file);

		pdf.saveAs("target/" + getClass().getSimpleName() + "." + name.getMethodName() + ".pdf");
	}
}
