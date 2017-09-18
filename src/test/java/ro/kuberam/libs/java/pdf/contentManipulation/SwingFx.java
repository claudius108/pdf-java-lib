package ro.kuberam.libs.java.pdf.contentManipulation;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class SwingFx extends Application {
	
	public static JPanel viewerComponentPanel;
	
	public static void main(String[] args) {
		Application.launch(args);
	}

    @Override
    public void start (Stage stage) throws MalformedURLException {
        final SwingNode swingNode = new SwingNode();

        //createSwingContent(swingNode);

        StackPane pane = new StackPane();
        pane.getChildren().add(showPDF());

        stage.setTitle("Swing in JavaFX");
        stage.setScene(new Scene(pane, 250, 150));
        stage.show();
        }

//    private void createSwingContent(final SwingNode swingNode) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                swingNode.setContent(showPDF());
//            }
//        });
//    }
    
    public static Node showPDF() throws MalformedURLException {

		String filePath = "/home/claudius/Downloads/comune.pdf";

		// build a controller
		SwingController controller = new SwingController();

		// Build a SwingViewFactory configured with the controller
		SwingViewBuilder factory = new SwingViewBuilder(controller);

		// Use the factory to build a JPanel that is pre-configured
		// with a complete, active Viewer UI.
		viewerComponentPanel = factory.buildViewerPanel();

		// add copy keyboard command
		ComponentKeyBinding.install(controller, viewerComponentPanel);

		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller.getDocumentViewController()));

		final SwingNode swingNode = new SwingNode();
		//createAndSetSwingContent(swingNode, viewerComponentPanel);

		// Open a PDF document to view
		controller.openDocument(filePath);
		return swingNode;

	}
}