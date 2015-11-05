package ro.kuberam.libs.java.pdf.contentManipulation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class SplitTest {
	
	@Rule public TestName name = new TestName();
	
	@Test
	public void splitTest1() throws IOException {
		InputStream pdfIs = getClass().getResourceAsStream("../formControls/SF.pdf");

		PDDocument pdfDocument = PDDocument.load(pdfIs);

		for (PDPage page : pdfDocument.getPages()) {
			PDPageContentStream pageContentStream = new PDPageContentStream(pdfDocument, page, true, true);
			pageContentStream.close();
		}

		pdfDocument.save(new File("target/" + name.getMethodName() + ".pdf"));

		// try {
		//
		// COSStream stream = new
		// COSStream(pdfDocument.getDocument().getScratchFile());
		// OutputStream output = stream.createUnfilteredStream();
		// int length = new
		// Long(pdfDocument.getDocument().getScratchFile().length()).intValue();
		// byte[] bytes = new byte[length];
		// pdfDocument.getDocument().getScratchFile().read(bytes, 0, length);
		// //output.write(bytes);
		// stream.setFilters(COSName.FLATE_DECODE);
		//
		// System.out.println(length);
		//
		// FileOutputStream fos = new FileOutputStream(
		// "/home/claudius/workspaces/repositories/git/kuberam/libs/java/pdf/target/compressed.pdf");
		// fos.write(bytes);
		// fos.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	// splitting:
	// splitter.setSplitAtPage( split );
	// documents = splitter.split( document );
	// for( int i=0; i<documents.size(); i++ )
	// {
	// PDDocument doc = (PDDocument)documents.get( i );
	// String fileName = pdfFile.substring(0,
	// pdfFile.length()4 ) + "" + i + ".pdf";
	// writeCompressedDocument( doc, fileName );
	// }

	// saving w/ compression:
	// fileOut = new FileOutputStream( fileName );

}

// private static PDDocument loadPdfDocumentFromBytes(byte[] imageOfPdf) throws
// IOException {
// ByteArrayInputStream byteArrayInputStream = new
// ByteArrayInputStream(imageOfPdf);
// return PDDocument.load(byteArrayInputStream);
//
// }

