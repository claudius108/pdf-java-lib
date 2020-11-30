package ro.kuberam.libs.java.pdf.stamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.helger.commons.charset.CCharset;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriterSettings;

public class StampTest {
	
	@Rule public TestName name = new TestName();
	
	private static File pdfFilePath;
	static {
		try {
			pdfFilePath = new File(StampTest.class.getResource("../formControls/SF.pdf").toURI());

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void stampTest2() throws IOException {
		InputStream pdfIs = new FileInputStream(pdfFilePath);

		ByteArrayOutputStream output = null;

		StringStamper app = null;
		app = new StringStamper(pdfIs, "Stamped!",
				"left: 70pt; top: 70pt; font-family: Helvetica; font-size: 22pt; color: rgb(144,144,0);");

		try {
			output = app.stamp();
			FileOutputStream fos = new FileOutputStream(new File("target/" + name.getMethodName() + ".pdf"));
			output.writeTo(fos);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			output.reset();
		}
	}

	@Test
	public void testJsonup() throws IOException {
		String html = "<p>An <a href='http://example.com/' style=\"left: 70pt; top: 70pt; font-family: Helvetica; font-size: 22pt; color: rgb(144,144,0);\"><b>example</b></a> link.</p>";
		Document doc = Jsoup.parse(html);
		Element link = doc.select("a").first();

		String text = doc.body().text(); // "An example link"
		String linkHref = link.attr("href"); // "http://example.com/"
		String linkText = link.text(); // "example""

		String linkOuterH = link.outerHtml();
		// "<a href="http://example.com"><b>example</b></a>"
		String linkInnerH = link.html(); // "<b>example</b>"

		System.out.println(link.attributes().toString());
	}

	@Ignore
	@Test
	public void testReadExpressions() {
		final ECSSVersion eVersion = ECSSVersion.CSS30;
		final CSSWriterSettings aCSSWS = new CSSWriterSettings(eVersion, false);
		final Charset aCharset = CCharset.CHARSET_UTF_8_OBJ;
		final File aFile = new File("test-expression.css");
		final CascadingStyleSheet aCSS = CSSReader
				.readFromString(
						"#id {left: 70pt; top: 70pt; font-family: Helvetica; font-size: 22pt; color: rgb(144,144,0);}",
						eVersion);
		assertNotNull(aCSS);
		assertEquals(1, aCSS.getRuleCount());
		assertEquals(1, aCSS.getStyleRuleCount());
		final CSSStyleRule aSR = aCSS.getStyleRuleAtIndex(0);
		assertEquals("div", aSR.getSelectorsAsCSSString(aCSSWS, 0));
		assertEquals(23, aSR.getDeclarationCount());
		int i = 0;
		for (final CSSDeclaration aDecl : aSR.getAllDeclarations()) {
			final String sExpectedName = Character.toString((char) ('a' + i));
			assertEquals(sExpectedName, aDecl.getProperty());
			++i;
		}
	}

	@Ignore
	@Test
	public void testRubberStamper() throws IOException {
		InputStream pdfIs = new FileInputStream(pdfFilePath);

		// PDDocument document = PDDocument.loadNonSeq(pdfFilePath, new
		// RandomAccessFile(tempFile, READ_WRITE));
		// PDDocument document = PDDocument.load(pdfIs, new
		// RandomAccessFile(tempFile, "rw"));
		PDDocument document = PDDocument.load(pdfIs);

		for (PDPage page : document.getPages()) {
			PDFStreamParser parser = new PDFStreamParser(page);
			parser.parse();

		}

		// List<?> allPages = document.getDocumentCatalog().getPages();
		//
		// System.out.println(allPages.size());
		//
		// for (int i = 0; i < allPages.size(); i++) {
		// PDPage page = (PDPage) allPages.get(i);
		// // Get all annotations
		// List<PDAnnotation> annotations = page.getAnnotations();
		//
		// // Generate a new RubberStamp
		// PDAnnotationRubberStamp aRubber = new PDAnnotationRubberStamp();
		// aRubber.setName("fancy_name");
		// aRubber.setContents("This is the content of the generated stamp!");
		//
		// // Generate a custom appearance for the RubberStamp
		// FileInputStream fin = new
		// FileInputStream(StampTest.class.getResource("../aa.jpg").getFile());
		//
		// PDJpeg mypic = new PDJpeg(document, fin);
		//
		// PDResources myResources = new PDResources();
		// myResources.getXObjects().put("Im0", mypic);
		//
		// COSStream s = new COSStream(document.getDocument().getScratchFile());
		// s.createUnfilteredStream();
		//
		// PDAppearanceStream myDic = new PDAppearanceStream(s);
		// myDic.setResources(myResources);
		//
		// PDAppearanceDictionary appearance = new PDAppearanceDictionary(new
		// COSDictionary());
		// // appearance.setDownAppearance(new HashMap<String,
		// // COSObjectable>());
		// // appearance.getNormalAppearance().put("default", myDic);
		//
		// // Set the appearance of the RubberStamp to the custom look
		// aRubber.setAppearance(appearance);
		//
		// // Define and set the target rectangle
		// PDRectangle myrect = new PDRectangle();
		// myrect.setUpperRightX(250);
		// myrect.setUpperRightY(550);
		// myrect.setLowerLeftX(275);
		// myrect.setLowerLeftY(575);
		// aRubber.setRectangle(myrect);
		//
		// // Add the new RubberStamp to the document
		// annotations.add(aRubber);
		// try {
		// document.saveIncremental("target/testRubberStamper.pdf");
		// } catch (COSVisitorException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		//
		// // Finally write the new file and close it
		// try {
		// document.save("target/testRubberStamper.pdf");
		// } catch (COSVisitorException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// document.close();
	}

}
