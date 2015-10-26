package ro.kuberam.libs.java.pdf.stamp;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

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
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			doc = PDDocument.load(inputPdfIs);

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

			COSDictionary fontDictionary = new COSDictionary();
			fontDictionary.setItem(COSName.SUBTYPE, COSName.TYPE1);
			fontDictionary.setName(COSName.BASE_FONT, fontFamily);

			PDFont font = new PDType1Font(fontDictionary);

			for (PDPage page : doc.getPages()) {
				PDRectangle mediabox = page.getMediaBox();
				float marginLeft = (mediabox.getWidth() - page.getCropBox().getWidth()) / 2;
				float marginTop = (mediabox.getHeight() - page.getCropBox().getHeight()) / 2;

				marginTop = (marginTop == 0) ? fontSize : marginTop;
				float top = mediabox.getHeight() - (stampTop + marginTop);
				float left = stampLeft + marginLeft;

				// calculate the width of the string according to the font
				float stringWidth = font.getStringWidth(stampString) * fontSize / 1000f;

				// determine the rotation stuff. Is the the loaded page in
				// landscape mode? (for axis and string dims)
				int pageRot = page.getRotation();
				boolean pageRotated = pageRot == 90 || pageRot == 270;

				// are we rotating the text?
				// boolean textRotated = textRot != 0 || textRot != 360;
				boolean textRotated = false;

				// calc the diff of rotations so the text stamps
				int totalRot = pageRot - textRotation;

				PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true, true);

				PDRectangle pageSize = page.getMediaBox();
				// calculate to center of the page
				int rotation = page.getRotation();
				boolean rotate = rotation == 90 || rotation == 270;
				float pageWidth = rotate ? pageSize.getHeight() : pageSize.getWidth();
				float pageHeight = rotate ? pageSize.getWidth() : pageSize.getHeight();
				float centerX = rotate ? pageHeight / 2f : (pageWidth - stringWidth) / 2f;
				float centerY = rotate ? (pageWidth - stringWidth) / 2f : pageHeight / 2f;
				// append the content to the existing stream
				contentStream.beginText();
				// set font and font size
				contentStream.setFont(font, fontSize);

				if (rotate) {
					// rotate the text according to the page rotation
					contentStream.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, centerX, centerY));
				} else {
					contentStream.setTextMatrix(Matrix.getTranslateInstance(left, top));
				}

				contentStream.setNonStrokingColor(color);

				// if we are rotating, do it
				// if (pageRotated) {
				//
				// // rotate the text according to the calculations above
				// contentStream.setTextRotation(Math.toRadians(totalRot),
				// centeredXPosition,
				// centeredYPosition);
				//
				// } else if (textRotated) {
				//
				// // rotate the text according to the calculations above
				// contentStream.setTextRotation(Math.toRadians(textRotation),
				// left, top);
				//
				// } else {
				//
				// // no rotate, just move it.
				// contentStream.moveTextPositionByAmount(left, top);
				// }

				contentStream.showText(stampString);
				contentStream.endText();
				contentStream.close();
			}

			doc.save(output);

		} catch (IOException e) {
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
