/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package ro.kuberam.libs.java.pdf.contentManipulation;

import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;

import org.junit.Test;

import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.PrismTextLayout;

import javafx.scene.text.Font;

public class TextLayoutTest {

	class TestSpan implements TextSpan {
		String text;
		Object font;

		TestSpan(Object text, Object font) {
			this.text = (String) text;
			this.font = font;
		}

		@Override
		public String getText() {
			return text;
		}

		@Override
		public Object getFont() {
			return font;
		}

		@Override
		public RectBounds getBounds() {
			return null;
		}
	}

	public TextLayoutTest() {
	}

	private void setContent(PrismTextLayout layout, Object... content) {
		int count = content.length / 2;
		TextSpan[] spans = new TextSpan[count];
		int i = 0;
		while (i < content.length) {
			spans[i >> 1] = new TestSpan(content[i++], content[i++]);
		}
		layout.setContent(spans);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void buildRuns() {

		PrismTextLayout layout = new PrismTextLayout();
		PGFont font = (PGFont) Font.font("Sanskrit2003", 12).impl_getNativeFont();
		Font font3 = Font.loadFont("file:///home/claudius/workspaces/repositories/backup/fonts/Sanskrit2003.ttf", 10);

//		String text = "कारणत्त्वङ्गवाश्वादीनमपीति चेत् युक्तम्";
		String text = "त्त्व";
		System.out.println("initial text length = " + text.length());

		setContent(layout, text, font);
		GlyphList[] runs = layout.getRuns();

		System.out.println("code point = " + "\ue10d".codePointAt(0));
		System.out.println("font = " + font.getFontResource().getGlyphMapper().charToGlyph(2340));

		java.awt.Font font2 = null;
		try {
			font2 = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
					new File("//home/claudius/workspaces/repositories/backup/fonts/Sanskrit2003.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		final CharacterIterator it = new StringCharacterIterator(text);
		for(char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
			System.out.println("c = " + c);
		}

		for (int i = 0; i < runs.length; i++) {
			GlyphList glyphList = runs[i];
			int glyphCount = glyphList.getGlyphCount();
			System.out.println("glyphCount = " + glyphCount);

			for (int j = 0; j < glyphCount; j++) {
				int charOffset = glyphList.getCharOffset(j);
				System.out.println(charOffset + " " + text.charAt(charOffset) + " " + text.codePointAt(charOffset));
			}
		}
	}

}
