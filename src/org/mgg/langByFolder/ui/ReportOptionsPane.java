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
    CheckBox cbShowHiddenFilesFolder  ;
    CheckBox cbShowUnknownExtensions  ;
    RadioButton radioStatsOrderByOccurrence  ;
    RadioButton radioStatsOrderByName  ;
    public ReportOptionsPane() {
        super() ;
        int row ;
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

        //1st row - label for options
        row = 0 ;
        content.add(new Label("Show options"),0,row);

        //2nd row
        row++;
        content.add(radioFoldersOnly,0,row);
        content.add(radioAllItems,1,row);
        content.add(radioArtifactsOnly,2,row);

        cbShowHiddenFilesFolder = new CheckBox("Hidden files/folder") ;
        cbShowUnknownExtensions = new CheckBox("Unknown extensions") ;

        //3rd row
        row++ ;
        content.add(cbShowHiddenFilesFolder,0,2);
        content.add(cbShowUnknownExtensions,1,2);

        //4th row
        ToggleGroup tg2 = new ToggleGroup();
        radioStatsOrderByOccurrence = new RadioButton("Order stats by occurrence") ;
        radioStatsOrderByName = new RadioButton("Order stats by name") ;

        radioStatsOrderByOccurrence.setToggleGroup(tg2);
        radioStatsOrderByName.setToggleGroup(tg2);

        row++;
        content.add(radioStatsOrderByOccurrence,0,row);
        content.add(radioStatsOrderByName,1,row);

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
