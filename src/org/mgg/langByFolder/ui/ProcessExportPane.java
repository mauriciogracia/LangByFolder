package org.mgg.langByFolder.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class ProcessExportPane extends BorderPane {
    Button btnProcess ;
    Button btnExport ;
    public ProcessExportPane() {
        super() ;
        btnProcess = new Button("Process");
        btnExport = new Button("Export current results");

        HBox content = new HBox(btnProcess, btnExport) ;
        content.setPadding(GraphicSettings.borderPadding);

        btnProcess.setOnAction(event -> {
            //content.getChildren().setAll(label);
        });

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }
}
