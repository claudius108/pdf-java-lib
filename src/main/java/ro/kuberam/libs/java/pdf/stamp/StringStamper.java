package ro.kuberam.libs.java.pdf.stamp;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import ro.kuberam.libs.java.pdf.CSS2Java.CSS2JavaModel;
import ro.kuberam.libs.java.pdf.CSS2Java.CssParser;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.propertyvalue.CSSSimpleValueWithUnit;
import com.helger.css.utils.CSSNumberHelper;
import com.helger.css.writer.CSSWriterSettings;

/**
 * 
 * @author claudius.teodorescu@gmail.com
 */
public class StringStamper {

	private Integer textRotation = 0;
	private Color color = null;

	private InputStream inputPdfIs;
	private String stampString;
	private String stampSelector;
	private String stampStyling;
	private String fontFamily = "Verdana";
	private Float fontSize = 14f;
	private Float stampLeft = 0f;
	private Float stampTop = 0f;

	final ECSSVersion eVersion = ECSSVersion.CSS30;
	final CSSWriterSettings aCSSWS = new CSSWriterSettings(eVersion, false);

	/**
	 * The constructors
	 */
	public StringStamper(InputStream inputPdfIs, String stampString, String stampStyling) {
		this.inputPdfIs = inputPdfIs;
		this.stampString = stampString;
		this.stampStyling = stampStyling;
	}

	public StringStamper(InputStream inputPdfIs, String stampString, String stampSelector,
			String stampStyling) {
		this.inputPdfIs = inputPdfIs;
		this.stampString = stampString;
		this.stampSelector = stampSelector;
		this.stampStyling = stampStyling;
	}

	/**
	 * The stamping function.
	 * 
	 * @throws IOException
	 * @throws COSVisitorException
	 */
	public ByteArrayOutputStream stamp() {

		PDDocument doc = null;
		File tempFile = new File("result.tmp");
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
//			doc = PDDocument.load(inputPdfIs, true);
			doc = PDDocument.load(inputPdfIs, new RandomAccessFile(tempFile, "rw"));

			List<?> allPages = doc.getDocumentCatalog().getPages().getKids();

			CssParser cssParser = new CssParser();
			CSS2JavaModel css2JavaModel = cssParser.parseCssDeclaration(stampStyling);

			CSSSimpleValueWithUnit leftPropertyValue = CSSNumberHelper.getValueWithUnit(
					((CSSExpressionMemberTermSimple) css2JavaModel.getProperty("left")).getValue(), true);
			stampLeft = (float) leftPropertyValue.getAsIntValue();
			CSSSimpleValueWithUnit topPropertyValue = CSSNumberHelper.getValueWithUnit(
					((CSSExpressionMemberTermSimple) css2JavaModel.getProperty("top")).getValue(), true);
			stampTop = (float) topPropertyValue.getAsIntValue();
			CSSExpressionMemberTermSimple fontFamilyPropertyValue = (CSSExpressionMemberTermSimple) css2JavaModel
					.getProperty("font-family");
			fontFamily = fontFamilyPropertyValue.getValue();
			CSSSimpleValueWithUnit fontSizePropertyValue = CSSNumberHelper.getValueWithUnit(
					((CSSExpressionMemberTermSimple) css2JavaModel.getProperty("font-size")).getValue(),
					true);
			fontSize = (float) fontSizePropertyValue.getAsIntValue();
			color = (Color) css2JavaModel.getProperty("color");

			PDFont font = PDType1Font.getStandardFont(fontFamily);

			for (int i = 0; i < allPages.size(); i++) {
				// create an empty page and a geo object to use for calcs
				PDPage page = (PDPage) allPages.get(i);
				PDRectangle mediabox = page.findMediaBox();
				float marginLeft = (mediabox.getWidth() - page.findCropBox().getWidth()) / 2;
				float marginTop = (mediabox.getHeight() - page.findCropBox().getHeight()) / 2;

//				System.out.println("mediabox.getLowerLeftX() " + mediabox.getUpperRightX());
//				System.out.println("mediabox.getUpperRightY() " + mediabox.getUpperRightY());
//				System.out.println("mediabox.getHeight() " + mediabox.getHeight());
//				System.out.println("mediabox.getWidth() " + mediabox.getWidth());
//				System.out.println("page.findCropBox().getHeight() " + page.findCropBox().getHeight());
//				System.out.println("page.findCropBox().getWidth() " + page.findCropBox().getWidth());
//				System.out.println("marginLeft " + marginLeft);
//				System.out.println("marginTop " + marginTop);
//				System.out.println();

				marginTop = (marginTop == 0) ? fontSize : marginTop;
				float top = mediabox.getHeight() - (stampTop + marginTop);
				float left = stampLeft + marginLeft;

				// calculate the width of the string according to the font
				float stringWidth = font.getStringWidth(stampString) * fontSize / 1000f;

				// determine the rotation stuff. Is the the loaded page in
				// landscape mode? (for axis and string dims)
				int pageRot = page.findRotation();
				boolean pageRotated = pageRot == 90 || pageRot == 270;

				// are we rotating the text?
				// boolean textRotated = textRot != 0 || textRot != 360;
				boolean textRotated = false;

				// calc the diff of rotations so the text stamps
				int totalRot = pageRot - textRotation;

				// calc the page dimensions
				float pageWidth = pageRotated ? mediabox.getHeight() : mediabox.getWidth();
				float pageHeight = pageRotated ? mediabox.getWidth() : mediabox.getHeight();

				// determine the axis of rotation
				double centeredXPosition = pageRotated ? pageHeight / 2f : (pageWidth - stringWidth) / 2f;
				double centeredYPosition = pageRotated ? (pageWidth - stringWidth) / 2f : pageHeight / 2f;

				// append the content to the existing stream
				PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true, true);
				contentStream.beginText();

				// set font and font size
				contentStream.setFont(font, fontSize);

				// set the stroke (text) color
				contentStream.setNonStrokingColor(color);

				// if we are rotating, do it
				if (pageRotated) {

					// rotate the text according to the calculations above
					contentStream.setTextRotation(Math.toRadians(totalRot), centeredXPosition,
							centeredYPosition);

				} else if (textRotated) {

					// rotate the text according to the calculations above
					contentStream.setTextRotation(Math.toRadians(textRotation), left, top);

				} else {

					// no rotate, just move it.
					contentStream.moveTextPositionByAmount(left, top);
				}

				contentStream.drawString(stampString);

				contentStream.endText();

				contentStream.close();
			}

			doc.save(output);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (COSVisitorException e) {
			e.printStackTrace();
		} finally {

			if (doc != null) {
				try {
					doc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return output;
	}
}
