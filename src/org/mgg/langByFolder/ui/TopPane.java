package org.mgg.langByFolder.ui;

import javafx.scene.layout.BorderPane;
import org.mgg.langByFolder.ReportOptions;

public class TopPane extends BorderPane {
    public TopPane(TableViewPane tvp, ReportOptions rep){
        super() ;
        BorderPane content = new BorderPane();
        content.setTop(new FolderSelectPane(rep)) ;
        content.setCenter(new ReportOptionsPane(rep)) ;
        ProcessExportPane pep = new ProcessExportPane(rep, tvp) ;
        content.setBottom(pep);
        this.setCenter(content) ;
    }
}
