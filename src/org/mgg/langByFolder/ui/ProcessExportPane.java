package org.mgg.langByFolder.ui;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.mgg.LangByFolder;
import org.mgg.langByFolder.ReportOptions;

import java.util.ArrayList;
import java.util.List;

public class ProcessExportPane extends BorderPane implements EventHandler<MouseEvent> {
    Button btnProcess ;
    Button btnExport ;
    ReportOptions reportOptions ;

    private final List<IReportEventsListener> listeners = new ArrayList<>();

    public ProcessExportPane(ReportOptions rep) {
        super() ;
        reportOptions = rep ;
        btnProcess = new Button("Process");
        btnProcess.setDefaultButton(true);
        btnExport = new Button("Export current results");

        HBox content = new HBox(btnProcess, btnExport) ;
        content.setPadding(GraphicSettings.borderPadding);

        btnProcess.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }

    public void addReportEventListener(IReportEventsListener toAdd) {
        listeners.add(toAdd);
    }

    void setComponentsEnabled(boolean enabled) {
        btnProcess.setDisable(!enabled);
        btnExport.setDisable(!enabled);
    }
    @Override
    public void handle(MouseEvent event) {
        Object component = event.getSource() ;

        if(component == btnProcess) {

            setComponentsEnabled(false);
            // Notify everybody that may be interested.
            for (IReportEventsListener iReportEventsListener : listeners) {
                iReportEventsListener.started();
            }

            LangByFolder.processRootFolder(reportOptions);

            // Notify everybody that may be interested.
            for (IReportEventsListener iReportEventsListener : listeners) {
                iReportEventsListener.finished();
            }
            setComponentsEnabled(true);
        }
    }
}
