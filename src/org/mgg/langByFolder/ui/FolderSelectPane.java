package org.mgg.langByFolder.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.TextField ;
import org.mgg.langByFolder.ReportOptions;

public class FolderSelectPane extends BorderPane {
    Button btnSelectFolder ;

    public FolderSelectPane(ReportOptions rep) {
        super() ;
        rep.setRootFolder("C:\\DATOS\\repos\\LangByFolder");

        btnSelectFolder = new Button("...") ;

        BorderPane content = new BorderPane();
        content.setPadding(GraphicSettings.borderPadding);

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        content.setTop(new Label("Folder to analyze...")) ;
        content.setCenter(new TextField(rep.getRootFolder())) ;
        content.setRight(btnSelectFolder);
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }
}
