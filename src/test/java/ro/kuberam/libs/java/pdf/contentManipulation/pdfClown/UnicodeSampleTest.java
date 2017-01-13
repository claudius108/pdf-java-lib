package ro.kuberam.libs.java.pdf.contentManipulation.pdfClown;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import org.junit.Test;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.contents.composition.BlockComposer;
import org.pdfclown.documents.contents.composition.PrimitiveComposer;
import org.pdfclown.documents.contents.composition.XAlignmentEnum;
import org.pdfclown.documents.contents.composition.YAlignmentEnum;
import org.pdfclown.documents.contents.fonts.Font;
import org.pdfclown.files.File;

/**
 * This sample demonstrates the PDF Clown's <b>support to Unicode-compliant
 * fonts</b>.
 * 
 * @author Stefano Chizzolini (http://www.stefanochizzolini.it)
 * @version 0.1.2, 11/30/12
 */

public class UnicodeSampleTest extends Sample {
	private static final float Margin = 36;

	@Test
	@Override
	public void run() {
		// 1. Instantiate a new PDF file!
		File file = new File();
		Document document = file.getDocument();

		// 2. Insert the contents into the document!
		populate(document);

		// 3. Serialize the PDF file!
		serialize(file, "Unicode", "using Unicode fonts", "Unicode");
	}

	/**
	 * Populates a PDF file with contents.
	 */
	private void populate(Document document) {
		// 1. Add the page to the document!
		Page page = new Page(document); // Instantiates the page inside the
										// document context.
		document.getPages().add(page); // Puts the page in the pages collection.

		// 2.1. Create a content composer for the page!
		PrimitiveComposer composer = new PrimitiveComposer(page);

		// 2.2. Create a block composer!
		BlockComposer blockComposer = new BlockComposer(composer);

		// 3. Inserting contents...
		// Define the font to use!
		Font font = Font.get(document, "/home/claudius/workspaces/repositories/backup/fonts/Sanskrit2003.ttf");
		// Define the paragraph break size!
		Dimension breakSize = new Dimension(0, 10);
		// Define the text to show!
		String[] titles = new String[] { "Devanagari" };
		String[] bodies = new String[] { "कारणत्त्वङ्गवाश्वादीनमपीति चेत् युक्तम्" };
		String[] sources = new String[] { "" };
		// Begin the content block!
		blockComposer.begin(new Rectangle2D.Double(Margin, Margin, page.getSize().getWidth() - Margin * 2,
				page.getSize().getHeight() - Margin * 2), XAlignmentEnum.Justify, YAlignmentEnum.Top);
		for (int index = 0, length = titles.length; index < length; index++) {
			composer.setFont(font, 12);
			blockComposer.showText(titles[index]);
			blockComposer.showBreak();

			composer.setFont(font, 11);
			blockComposer.showText(bodies[index]);
			blockComposer.showBreak(XAlignmentEnum.Right);

			composer.setFont(font, 8);
			blockComposer.showText("[Source: " + sources[index] + "]");
			blockComposer.showBreak(breakSize, XAlignmentEnum.Justify);
		}
		// End the content block!
		blockComposer.end();

		// 4. Flush the contents into the page!
		composer.flush();
	}
}