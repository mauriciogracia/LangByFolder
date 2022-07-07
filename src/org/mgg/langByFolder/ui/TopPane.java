package org.mgg.langByFolder.ui;

import javafx.scene.layout.BorderPane;

public class TopPane extends BorderPane {

    public TopPane() {
        super() ;
        BorderPane content = new BorderPane();
        content.setTop(new FolderSelectPane()) ;
        content.setCenter(new ReportOptionsPane()) ;
        content.setBottom(new ProcessExportPane());
        this.setCenter(content) ;
    }
}
