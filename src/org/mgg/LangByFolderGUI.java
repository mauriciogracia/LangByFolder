package org.mgg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mgg.langByFolder.ReportOptions;
import org.mgg.langByFolder.ui.TableViewPane;
import org.mgg.langByFolder.ui.TopPane;

public class LangByFolderGUI extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override public void start(Stage stage) {
        Text text = new Text(10, 40, "Hello World!");
        text.setFont(new Font(40));

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane) ;

        ReportOptions rep = new ReportOptions() ;
        TableViewPane tvp = new TableViewPane(rep) ;
        borderPane.setTop(new TopPane(tvp,rep));
        borderPane.setCenter(tvp);

        stage.setTitle("LangByFolder");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
