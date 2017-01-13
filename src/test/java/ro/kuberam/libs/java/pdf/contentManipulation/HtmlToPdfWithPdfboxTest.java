package ro.kuberam.libs.java.pdf.contentManipulation;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class HtmlToPdfWithPdfboxTest {

	@Rule
	public TestName name = new TestName();

	// @Ignore
	@Test
	public void testPdfBox() throws IOException {
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		PDFont font = PDType0Font.load(document,
				new File("/home/claudius/workspaces/repositories/backup/fonts/Sanskrit2003.ttf"));
//		System.out.println("font = " + font.toUnicode(16779521));

		PDPageContentStream pageContentStream = new PDPageContentStream(document, page);

		BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();

		String inputText = "कारणत्त्वङ्गवाश्वादीनमपीति चेत् युक्तम्";
		Font font2 = new Font("Sanskrit2003", Font.PLAIN, 24);
		// FontRenderContext frc = new FontRenderContext(new AffineTransform(),
		// true, true);
		g.setFont(font2);
		FontRenderContext frc = g.getFontRenderContext();

		char[] chars = inputText.toCharArray();
		System.out.println("chars = " + chars.length);
		GlyphVector glyphVector = font2.layoutGlyphVector(frc, chars, 0, chars.length, 0);

		int length = glyphVector.getNumGlyphs();
		System.out.println("length = " + length);

		for (int i = 0; i < length; i++) {
			Shape glyph = glyphVector.getGlyphOutline(i);
			
			g.fill(glyph);
		}
		
		for (int i = 0, n = glyphVector.getNumGlyphs(); i < n; i++) {
			int charIndex = glyphVector.getGlyphCharIndex(i);
			int codePoint = inputText.codePointAt(charIndex);
			//System.out.println("codePoint = " + codePoint);
			
			System.out.println("glyphCode = " + font.toUnicode(glyphVector.getGlyphCode(i)));
		}

		g.dispose();

		pageContentStream.beginText();
		pageContentStream.setFont(font, 12);
		
		System.out.println("a = " + new String(font.encode(inputText), "UTF-8"));

		pageContentStream.newLineAtOffset(100, 700);
		pageContentStream.showText("त्त्व is correctly displayed with glyph substitution as " + "\ue10d");
		pageContentStream.newLine();
		//pageContentStream.showText(s);
		pageContentStream.endText();

		// Make sure that the content stream is closed:
		pageContentStream.close();

		// Save the results and ensure that the document is properly closed:
		document.save("target/" + getClass().getSimpleName() + "." + name.getMethodName() + ".pdf");
		document.close();
	}
}
