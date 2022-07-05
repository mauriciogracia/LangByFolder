package org.mgg.langByFolder.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class TopPane extends BorderPane {

    public TopPane() {
        super() ;
        BorderPane content = new BorderPane();
        content.setTop(new FolderSelectPane()) ;
        content.setCenter(new ReportOptionsPane()) ;
        this.setCenter(content) ;
    }
}
