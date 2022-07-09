package org.mgg.langByFolder.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.TextField ;

public class FolderSelectPane extends BorderPane {
    Button btnSelectFolder ;

    public FolderSelectPane() {
        super() ;

        btnSelectFolder = new Button("...") ;

        BorderPane content = new BorderPane();
        content.setPadding(GraphicSettings.borderPadding);

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        content.setTop(new Label("Folder to check...")) ;
        content.setCenter(new TextField("C:\\DATOS\\repos\\LangByFolder")) ;
        content.setRight(btnSelectFolder);
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }
}
