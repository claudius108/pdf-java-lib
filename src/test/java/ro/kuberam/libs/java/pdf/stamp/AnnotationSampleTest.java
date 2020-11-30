package ro.kuberam.libs.java.pdf.stamp;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.contents.ITextString;
import org.pdfclown.documents.contents.TextChar;
import org.pdfclown.documents.contents.colorSpaces.DeviceRGBColor;
import org.pdfclown.documents.contents.composition.PrimitiveComposer;
import org.pdfclown.documents.contents.composition.XAlignmentEnum;
import org.pdfclown.documents.contents.composition.YAlignmentEnum;
import org.pdfclown.documents.contents.fonts.StandardType1Font;
import org.pdfclown.documents.interaction.annotations.TextMarkup;
import org.pdfclown.documents.interaction.annotations.TextMarkup.MarkupTypeEnum;
import org.pdfclown.files.File;
import org.pdfclown.files.SerializationModeEnum;
import org.pdfclown.tools.PageStamper;
import org.pdfclown.tools.TextExtractor;
import org.pdfclown.util.math.Interval;
import org.pdfclown.util.math.geom.Quad;

public class AnnotationSampleTest {
	
	@Rule public TestName name = new TestName();
	
	@Test
	public void testHighlight() throws IOException {
		// AnnotationSample as = new AnnotationSample();
		// as.run();

		File file = new File("/home/claudius/PDFClown/main/res/samples/input/pdf/alice.pdf");

		// Define the text pattern to look for!
		String textRegEx = "rabbit";
		Pattern pattern = Pattern.compile(textRegEx, Pattern.CASE_INSENSITIVE);

		// Instantiate the extractor!
		TextExtractor textExtractor = new TextExtractor(true, true);

		for (final Page page : file.getDocument().getPages()) {
			// Extract the page text!
			Map<Rectangle2D, List<ITextString>> textStrings = textExtractor.extract(page);

			// Find the text pattern matches!
			final Matcher matcher = pattern.matcher(TextExtractor.toString(textStrings));

			// Highlight the text pattern matches!
			textExtractor.filter(textStrings, new TextExtractor.IIntervalFilter() {
				@Override
				public boolean hasNext() {
					return matcher.find();
				}

				@Override
				public Interval next() {
					return new Interval(matcher.start(), matcher.end());
				}

				@Override
				public void process(Interval interval, ITextString match) {
					// Defining the highlight box of the text pattern match...
					List highlightQuads = new ArrayList();
					{
						/*
						 * NOTE: A text pattern match may be split across
						 * multiple contiguous lines, so we have to define a
						 * distinct highlight box for each text chunk.
						 */
						Rectangle2D textBox = null;
						for (TextChar textChar : match.getTextChars()) {
							Rectangle2D textCharBox = textChar.getBox();
							if (textBox == null) {
								textBox = (Rectangle2D) textCharBox.clone();
							} else {
								if (textCharBox.getY() > textBox.getMaxY()) {
									highlightQuads.add(Quad.get(textBox));
									textBox = (Rectangle2D) textCharBox.clone();
								} else {
									textBox.add(textCharBox);
								}
							}
						}
						highlightQuads.add(Quad.get(textBox));
					}
					// Highlight the text pattern match!
					new TextMarkup(page, null, MarkupTypeEnum.Highlight, highlightQuads);
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			});
		}

		file.save(SerializationModeEnum.Incremental);

	}

	@Test
	public void testStamp() throws IOException {

		File file = new File("/home/claudius/PDFClown/main/res/samples/input/pdf/testStampSignature2.pdf");

		Document document = file.getDocument();

		PageStamper stamper = new PageStamper();

		// 2. Numbering each page...
		StandardType1Font font = new StandardType1Font(document, StandardType1Font.FamilyEnum.Courier,
				true, false);
		DeviceRGBColor redColor = DeviceRGBColor.get(Color.RED);
		int margin = 32;
		for (Page page : document.getPages()) {
			// 2.1. Associate the page to the stamper!
			stamper.setPage(page);

			// 2.2. Stamping the page number on the foreground...
			{
				PrimitiveComposer foreground = stamper.getForeground();

				foreground.setFont(font, 16);
				foreground.setFillColor(redColor);

				Dimension2D pageSize = page.getSize();
				int pageNumber = page.getIndex() + 1;
				boolean pageIsEven = (pageNumber % 2 == 0);
				foreground.showText(
						"Stamped!",
						new Point2D.Double((pageIsEven ? margin : pageSize.getWidth() - margin), pageSize
								.getHeight() - margin), (pageIsEven ? XAlignmentEnum.Left
								: XAlignmentEnum.Right), YAlignmentEnum.Bottom, 0);
			}

			// 2.3. End the stamping!
			stamper.flush();
		}
		
		file.save(SerializationModeEnum.Incremental);
	}

}
