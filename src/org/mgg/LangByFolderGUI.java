package org.mgg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mgg.langByFolder.ui.FolderSelect;

public class LangByFolderGUI extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override public void start(Stage stage) {
        Text text = new Text(10, 40, "Hello World!");
        text.setFont(new Font(40));

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane) ;
        borderPane.setTop(new FolderSelect());
        //@todo: continue here
        borderPane.setCenter(new FolderSelect());
        borderPane.setBottom(new FolderSelect());

        stage.setTitle("LangByFolder");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
