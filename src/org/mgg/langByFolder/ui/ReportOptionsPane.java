package org.mgg.langByFolder.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.mgg.langByFolder.ReportDetailLevel;
import org.mgg.langByFolder.ReportOptions;

public class ReportOptionsPane extends BorderPane {
    private ReportOptions reportOptions ;
    RadioButton radioFoldersOnly ;
    RadioButton radioAllItems ;
    RadioButton radioArtifactsOnly  ;

    CheckBox cb1  ;

    CheckBox cb2  ;
    public ReportOptionsPane() {
        super() ;
        reportOptions = new ReportOptions() ;

        GridPane content = new GridPane();
        content.setPadding(GraphicSettings.borderPadding);

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        ToggleGroup tg = new ToggleGroup();
        radioFoldersOnly = new RadioButton("Folders only") ;
        radioAllItems = new RadioButton("All Items") ;
        radioArtifactsOnly = new RadioButton("Artifacts only") ;

        radioFoldersOnly.setToggleGroup(tg);
        radioAllItems.setToggleGroup(tg);
        radioArtifactsOnly.setToggleGroup(tg);

        content.add(radioFoldersOnly,0,0);
        content.add(radioAllItems,1,0);
        content.add(radioArtifactsOnly,2,0);
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;

        showCurrentOptions();
    }

    private void showCurrentOptions() {
        if (reportOptions.reportDetailLevel == ReportDetailLevel.FOLDER) {
            radioFoldersOnly.setSelected(true);
        }
    }

}
