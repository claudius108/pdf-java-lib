package ro.kuberam.libs.java.pdf.contentManipulation;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BareBoneGraphics extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Graphics in JavaFX");
		Group root = new Group();
		Scene scene = new Scene(root, 550, 250, Color.WHITE);

		Font font = Font.loadFont("file:///home/claudius/workspaces/repositories/backup/fonts/Sanskrit2003.ttf", 40);
		Text text = new Text(10, 50, "कारणत्त्वङ्गवाश्वादीनमपीति चेत् युक्तम्");
		text.setFont(font);

		String processedText = text.getText();

		processedText.chars().forEach(number -> System.out.println(number));

		// for (int i = 0; i < processedText.codePointCount(0,
		// processedText.chars()); i++) {
		// System.out.println(processedText.charAt(i));
		// }

		root.getChildren().add(text);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

}