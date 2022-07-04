package org.mgg.langByFolder.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.TextField ;

public class FolderSelect extends BorderPane {

    public FolderSelect() {
        super() ;
        BorderPane margin = new BorderPane();
        margin.setPadding(GraphicSettings.borderPadding);

        margin.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        margin.setTop(new Label("Folder to check...")) ;
        margin.setCenter(new TextField("path/to/folder/to/scan")) ;
        margin.setRight(new Button("..."));
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(margin) ;
    }
}
