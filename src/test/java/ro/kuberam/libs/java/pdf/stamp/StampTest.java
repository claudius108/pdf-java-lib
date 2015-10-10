package ro.kuberam.libs.java.pdf.stamp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Ignore;
import org.junit.Test;

public class StampTest {

	private static File pdfFilePath;
	static {
		try {
			pdfFilePath = new File(StampTest.class.getResource("../formControls/SF.pdf").toURI());

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStampSignature2() throws IOException {
		InputStream pdfIs = new FileInputStream(pdfFilePath);

		ByteArrayOutputStream output = null;

		StringStamper app = null;
		app = new StringStamper(pdfIs, "Stamped!",
				"left: 70pt; top: 70pt; font-family: Helvetica; font-size: 22pt; color: rgb(144,144,0);");

		try {
			output = app.stamp();
			FileOutputStream fos = new FileOutputStream(new File("target/testStampSignature2.pdf"));
			output.writeTo(fos);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} finally {
			output.close();
		}

	}

	@Ignore
	@Test
	public void testRubberStamper() throws IOException {
		InputStream pdfIs = new FileInputStream(pdfFilePath);
		File tempFile = new File("result.tmp");

		// PDDocument document = PDDocument.loadNonSeq(pdfFilePath, new
		// RandomAccessFile(tempFile, READ_WRITE));
		// PDDocument document = PDDocument.load(pdfIs, new
		// RandomAccessFile(tempFile, "rw"));
		PDDocument document = PDDocument.load(pdfIs);
		
		for( PDPage page : document.getPages() )
        {
            PDFStreamParser parser = new PDFStreamParser(page);
            parser.parse();
            
        }

//		List<?> allPages = document.getDocumentCatalog().getPages();
//
//		System.out.println(allPages.size());
//
//		for (int i = 0; i < allPages.size(); i++) {
//			PDPage page = (PDPage) allPages.get(i);
//			// Get all annotations
//			List<PDAnnotation> annotations = page.getAnnotations();
//
//			// Generate a new RubberStamp
//			PDAnnotationRubberStamp aRubber = new PDAnnotationRubberStamp();
//			aRubber.setName("fancy_name");
//			aRubber.setContents("This is the content of the generated stamp!");
//
//			// Generate a custom appearance for the RubberStamp
//			FileInputStream fin = new FileInputStream(StampTest.class.getResource("../aa.jpg").getFile());
//
//			PDJpeg mypic = new PDJpeg(document, fin);
//
//			PDResources myResources = new PDResources();
//			myResources.getXObjects().put("Im0", mypic);
//
//			COSStream s = new COSStream(document.getDocument().getScratchFile());
//			s.createUnfilteredStream();
//
//			PDAppearanceStream myDic = new PDAppearanceStream(s);
//			myDic.setResources(myResources);
//
//			PDAppearanceDictionary appearance = new PDAppearanceDictionary(new COSDictionary());
//			// appearance.setDownAppearance(new HashMap<String,
//			// COSObjectable>());
//			// appearance.getNormalAppearance().put("default", myDic);
//
//			// Set the appearance of the RubberStamp to the custom look
//			aRubber.setAppearance(appearance);
//
//			// Define and set the target rectangle
//			PDRectangle myrect = new PDRectangle();
//			myrect.setUpperRightX(250);
//			myrect.setUpperRightY(550);
//			myrect.setLowerLeftX(275);
//			myrect.setLowerLeftY(575);
//			aRubber.setRectangle(myrect);
//
//			// Add the new RubberStamp to the document
//			annotations.add(aRubber);
//			try {
//				document.saveIncremental("target/testRubberStamper.pdf");
//			} catch (COSVisitorException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		// Finally write the new file and close it
//		try {
//			document.save("target/testRubberStamper.pdf");
//		} catch (COSVisitorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		document.close();
	}

}
