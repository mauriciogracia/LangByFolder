package org.mgg.langByFolder.ui;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.control.TextField ;
import org.mgg.langByFolder.ReportOptions;

public class FolderSelectPane extends BorderPane implements EventHandler<KeyEvent> {
    Button btnSelectFolder ;
    TextField rootFolder ;

    ReportOptions reportOptions ;
    public FolderSelectPane(ReportOptions rep) {
        super() ;
        reportOptions = rep ;
        reportOptions.setRootFolder("C:\\DATOS\\repos\\LangByFolder");
        rootFolder = new TextField(rep.getRootFolder());
        rootFolder.addEventFilter(KeyEvent.KEY_TYPED,this);

        btnSelectFolder = new Button("...") ;

        BorderPane content = new BorderPane();
        content.setPadding(GraphicSettings.borderPadding);

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        content.setTop(new Label("Folder to analyze...")) ;
        content.setCenter(rootFolder) ;
        content.setRight(btnSelectFolder);
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }

    @Override
    public void handle(KeyEvent event) {
        reportOptions.setRootFolder(((TextField) event.getSource()).getText());
    }
}
