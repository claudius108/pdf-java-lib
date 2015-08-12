package ro.kuberam.libs.java.pdf.contentManipulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.junit.Ignore;
import org.junit.Test;

public class SplitTest {

	private static File pdfFilePath;
	static {
		pdfFilePath = new File(
				"/home/claudius/workspaces/repositories/git/kuberam/libs/java/pdf/target/stamped-document.pdf");
	}

	@Ignore
	@Test
	public void test1() throws IOException {
		InputStream pdfIs = new FileInputStream(pdfFilePath);

		PDDocument pdfDocument = PDDocument.load(pdfFilePath);

		List<?> allPages = pdfDocument.getDocumentCatalog().getAllPages();

		for (int i = 0; i < allPages.size(); i++) {
			PDPage page = (PDPage) allPages.get(i);
			PDPageContentStream pageContentStream = new PDPageContentStream(pdfDocument, page, true, true);
			pageContentStream.close();
		}

		try {
			pdfDocument.save(new File(
					"/home/claudius/workspaces/repositories/git/kuberam/libs/java/pdf/target/compressed.pdf"));
		} catch (COSVisitorException e) {
			e.printStackTrace();
		}

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

//private static PDDocument loadPdfDocumentFromBytes(byte[] imageOfPdf) throws IOException {
//    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageOfPdf);
//    return PDDocument.load(byteArrayInputStream);
//
//}


