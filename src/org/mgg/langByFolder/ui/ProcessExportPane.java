package org.mgg.langByFolder.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.mgg.LangByFolder;
import org.mgg.langByFolder.ReportOptions;

import java.util.ArrayList;
import java.util.List;

public class ProcessExportPane extends BorderPane {
    Button btnProcess ;
    Button btnExport ;

    private final List<IReportEventsListener> listeners = new ArrayList<>();

    public ProcessExportPane(ReportOptions rep, TableViewPane tvp) {
        super() ;
        this.addListener(tvp);
        btnProcess = new Button("Process");
        btnExport = new Button("Export current results");

        HBox content = new HBox(btnProcess, btnExport) ;
        content.setPadding(GraphicSettings.borderPadding);

        btnProcess.setOnAction(event -> {
            // Notify everybody that may be interested.
            for (IReportEventsListener hl : listeners) {
                hl.started();
            }

            LangByFolder.processRootFolder(rep);

            // Notify everybody that may be interested.
            for (IReportEventsListener hl : listeners) {
                hl.finished();
            }
        });

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }

    public void addListener(IReportEventsListener toAdd) {
        listeners.add(toAdd);
    }

}
