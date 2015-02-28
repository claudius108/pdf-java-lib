package ro.kuberam.libs.java.pdf.CSS2Java;

import java.awt.Color;

import javax.annotation.Nonnull;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.decl.CSSRGB;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.utils.CSSColorHelper;
import com.helger.css.utils.ECSSColor;
import com.helger.css.writer.CSSWriterSettings;

public class CssParser {
	final private ECSSVersion eVersion = ECSSVersion.CSS30;
	final private CSSWriterSettings aCSSWS = new CSSWriterSettings(eVersion, false);

	public CssParser() {
	}

	public CSS2JavaModel parseCssDeclaration(String cssDeclaration) {
		CSSDeclarationList aDeclList = CSSReaderDeclarationList.readFromString(cssDeclaration, eVersion);

		final CSS2JavaModel css2JavaModel = new CSS2JavaModel();

		CSSVisitor.visitAllDeclarations(aDeclList, new DefaultCSSVisitor() {
			public void onDeclaration(@Nonnull final CSSDeclaration cssDeclaration) {
				String propertyName = cssDeclaration.getProperty();

				if (propertyName.equals("color")) {
					css2JavaModel.setProperty("color", _parseColorCSSString(cssDeclaration.getExpression()
							.getMemberAtIndex(0).getAsCSSString(aCSSWS, 0)));
				} else {
					css2JavaModel.setProperty(propertyName,
							_parseSimpleCSSProperty(cssDeclaration, propertyName));
				}

			}
		});

		return css2JavaModel;
	}

	private CSSExpressionMemberTermSimple _parseSimpleCSSProperty(CSSDeclaration cssDeclaration,
			String cssPropertyName) {
		CSSExpressionMemberTermSimple cssExpression = (CSSExpressionMemberTermSimple) cssDeclaration
				.getExpression().getMemberAtIndex(0);

		return cssExpression;
	}

	private Color _parseColorCSSString(String colorCSSString) {
		Color color = new Color(0, 0, 0, 1);
		if (CSSColorHelper.isRGBColorValue(colorCSSString)) {
			CSSRGB aRGB = CSSColorHelper.getParsedRGBColorValue(colorCSSString);
			color = new Color(Integer.valueOf(aRGB.getRed()), Integer.valueOf(aRGB.getGreen()),
					Integer.valueOf(aRGB.getBlue()));
		} else if (CSSColorHelper.isHexColorValue(colorCSSString)) {
			color = Color.decode(colorCSSString);
		} else if (CSSColorHelper.isColorValue(colorCSSString)) {
			color = Color.decode(ECSSColor.getFromNameCaseInsensitiveOrNull(colorCSSString)
					.getAsHexColorValue());
		}

		return color;
	}

}
