package ro.kuberam.libs.java.pdf.contentManipulation;

import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GlyphVectorTest {
  public static void main(String[] args) {
    JFrame jf = new JFrame("Demo");
    Container cp = jf.getContentPane();
    MyCanvas tl = new MyCanvas();
    cp.add(tl);
    jf.setSize(700, 400);
    jf.setVisible(true);
  }
}

class MyCanvas extends JComponent {

  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    String s = "कारणत्त्वङ्गवाश्वादीनमपीति चेत् युक्तम्";
    Font font = new Font("Sanskrit2003", Font.PLAIN, 48);
//    FontRenderContext frc = g2.getFontRenderContext();
    FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
    g2.translate(40, 80);

    char[] chars = s.toCharArray();
    GlyphVector gv = font.layoutGlyphVector(frc, chars, 0, chars.length, 0);// createGlyphVector(frc, s);
    
    int length = gv.getNumGlyphs();
    
    for (int i = 0; i < length; i++) {
      Shape glyph = gv.getGlyphOutline(i);
      //System.out.println(gv.getGlyphMetrics(i).getType());
      g2.fill(glyph);
    }
  }
}