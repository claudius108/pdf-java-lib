package ro.kuberam.libs.java.pdf.metadata;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.junit.Test;

public class Bookmarks {

	@Test
	public void extractBookmarks() throws InvalidPasswordException, IOException {
		try (PDDocument document = PDDocument.load(new File("/home/claudius/adluc–agoge.pdf"))) {
			PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
			if (outline != null) {
				printBookmark(document, outline, "");
			} else {
				System.out.println("This document does not contain any bookmarks");
			}
		}
	}

	public void printBookmark(PDDocument document, PDOutlineNode bookmark, String indentation) throws IOException {
		PDOutlineItem current = bookmark.getFirstChild();
		
		while (current != null) {
			// one could also use current.findDestinationPage(document) to get the page
			// number,
			// but this example does it the hard way to explain the different types
			// Note that bookmarks can also do completely different things, e.g. link to a
			// website,
			// or to an external file. This example focuses on internal pages.
			System.out.println(indentation + current.getTitle() + ", page = " + current.findDestinationPage(document));
			
			if (current.getDestination() instanceof PDPageDestination) {
				PDPageDestination pd = (PDPageDestination) current.getDestination();
				System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
			} else if (current.getDestination() instanceof PDNamedDestination) {
				PDPageDestination pd = document.getDocumentCatalog()
						.findNamedDestinationPage((PDNamedDestination) current.getDestination());
				if (pd != null) {
					System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
				}
			} else if (current.getDestination() != null) {
				System.out.println(
						indentation + "Destination class: " + current.getDestination().getClass().getSimpleName());
			}

			if (current.getAction() instanceof PDActionGoTo) {
				PDActionGoTo gta = (PDActionGoTo) current.getAction();
				if (gta.getDestination() instanceof PDPageDestination) {
					PDPageDestination pd = (PDPageDestination) gta.getDestination();
					System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
				} else if (gta.getDestination() instanceof PDNamedDestination) {
					PDPageDestination pd = document.getDocumentCatalog()
							.findNamedDestinationPage((PDNamedDestination) gta.getDestination());
					if (pd != null) {
						System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
					}
				} else {
					System.out.println(
							indentation + "Destination class: " + gta.getDestination().getClass().getSimpleName());
				}
			} else if (current.getAction() != null) {
				System.out.println(indentation + "Action class: " + current.getAction().getClass().getSimpleName());
			}
			
			printBookmark(document, current, indentation + "    ");
			current = current.getNextSibling();
		}
	}

}
