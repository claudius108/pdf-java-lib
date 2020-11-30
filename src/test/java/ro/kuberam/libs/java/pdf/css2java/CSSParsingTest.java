package ro.kuberam.libs.java.pdf.css2java;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;

import org.junit.Test;

import ro.kuberam.libs.java.pdf.CSS2Java.CSS2JavaModel;
import ro.kuberam.libs.java.pdf.CSS2Java.CssParser;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.propertyvalue.CSSSimpleValueWithUnit;
import com.helger.css.utils.CSSNumberHelper;
import com.helger.css.writer.CSSWriterSettings;

public class CSSParsingTest {

	final ECSSVersion eVersion = ECSSVersion.CSS30;
	final CSSWriterSettings aCSSWS = new CSSWriterSettings(eVersion, false);

	@Test
	public void testCssDeclarationParsing() throws IOException {
		String cssDeclaration = "left: 70pt; top: 50pt; font-family: Helvetica; font-size: 22pt;";

		CssParser cssParser = new CssParser();
		CSS2JavaModel css2JavaModel = cssParser.parseCssDeclaration(cssDeclaration);
		// System.out.println(css2JavaModel.getProperty("color"));
		assertNotNull(css2JavaModel);
		CSSSimpleValueWithUnit leftPropertyValue = CSSNumberHelper.getValueWithUnit(
				((CSSExpressionMemberTermSimple) css2JavaModel.getProperty("left")).getValue(), true);
		assertEquals(70, leftPropertyValue.getAsIntValue());
		assertEquals("pt", leftPropertyValue.getUnit().getName());

		CSSSimpleValueWithUnit topPropertyValue = CSSNumberHelper.getValueWithUnit(
				((CSSExpressionMemberTermSimple) css2JavaModel.getProperty("top")).getValue(), true);
		assertEquals(50, topPropertyValue.getAsIntValue());
		assertEquals("pt", topPropertyValue.getUnit().getName());

		CSSExpressionMemberTermSimple fontFamilyPropertyValue = (CSSExpressionMemberTermSimple) css2JavaModel
				.getProperty("font-family");
		assertEquals("Helvetica", fontFamilyPropertyValue.getValue());

		CSSSimpleValueWithUnit fontSizePropertyValue = CSSNumberHelper.getValueWithUnit(
				((CSSExpressionMemberTermSimple) css2JavaModel.getProperty("font-size")).getValue(), true);
		assertEquals(22, fontSizePropertyValue.getAsIntValue());
		assertEquals("pt", fontSizePropertyValue.getUnit().getName());
	}

	@Test
	public void testHexColorParsing() throws IOException {
		String cssDeclaration = "left: 70pt; top: 50pt; font-family: Helvetica; font-size: 22pt; color: #f0f8ff;";

		CssParser cssParser = new CssParser();
		CSS2JavaModel css2JavaModel = cssParser.parseCssDeclaration(cssDeclaration);

		Color colorPropertyValue = (Color) css2JavaModel.getProperty("color");
		assertEquals(240, colorPropertyValue.getRed());
		assertEquals(248, colorPropertyValue.getGreen());
		assertEquals(255, colorPropertyValue.getBlue());
	}

	@Test
	public void testRGBColorParsing() throws IOException {
		String cssDeclaration = "color: rgb(240,248,255);";

		CssParser cssParser = new CssParser();
		CSS2JavaModel css2JavaModel = cssParser.parseCssDeclaration(cssDeclaration);

		Color colorPropertyValue = (Color) css2JavaModel.getProperty("color");
		assertEquals(240, colorPropertyValue.getRed());
		assertEquals(248, colorPropertyValue.getGreen());
		assertEquals(255, colorPropertyValue.getBlue());
	}

	@Test
	public void testColorNameParsing() throws IOException {
		String cssDeclaration = "color: aliceblue;";

		CssParser cssParser = new CssParser();
		CSS2JavaModel css2JavaModel = cssParser.parseCssDeclaration(cssDeclaration);

		Color colorPropertyValue = (Color) css2JavaModel.getProperty("color");
		assertEquals(240, colorPropertyValue.getRed());
		assertEquals(248, colorPropertyValue.getGreen());
		assertEquals(255, colorPropertyValue.getBlue());
	}
}
