package org.mgg.langByFolder.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.mgg.langByFolder.stats.CompareByLanguageName;
import org.mgg.langByFolder.stats.CompareByLanguageOccurrence;
import org.mgg.langByFolder.ReportDetailLevel;
import org.mgg.langByFolder.ReportOptions;
import org.mgg.langByFolder.stats.LanguageStatsComparatorType;

public class ReportOptionsPane extends BorderPane implements ChangeListener<Toggle> {
    private final ReportOptions reportOptions ;
    RadioButton radioFoldersOnly ;
    RadioButton radioAllItems ;
    RadioButton radioArtifactsOnly  ;
    CheckBox cbShowHiddenFilesFolder  ;
    CheckBox cbShowUnknownExtensions  ;
    RadioButton radioStatsOrderByOccurrence  ;
    RadioButton radioStatsOrderByName  ;
    CompareByLanguageOccurrence compareByLanguageOccurrence;
    CompareByLanguageName compareByLanguageName;
    public ReportOptionsPane(ReportOptions rep)  {
        super() ;
        reportOptions = rep ;
        compareByLanguageName = new CompareByLanguageName();
        compareByLanguageOccurrence = new CompareByLanguageOccurrence();

        int row ;
        GridPane content = new GridPane();
        content.setPadding(GraphicSettings.borderPadding);

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        ToggleGroup tg = new ToggleGroup();
        radioFoldersOnly = new RadioButton("Folders only") ;
        radioAllItems = new RadioButton("Files & Folders") ;
        radioArtifactsOnly = new RadioButton("Artifacts only") ;

        radioFoldersOnly.setToggleGroup(tg);
        radioAllItems.setToggleGroup(tg);
        radioArtifactsOnly.setToggleGroup(tg);

        tg.selectedToggleProperty().addListener(this);

        //1st row - label for options
        row = 0 ;
        content.add(new Label("Show options"),0,row);

        //2nd row
        row++;
        content.add(radioAllItems,0,row);
        content.add(radioFoldersOnly,1,row);
        content.add(radioArtifactsOnly,2,row);

        cbShowHiddenFilesFolder = new CheckBox("Hidden files/folder") ;
        cbShowHiddenFilesFolder.selectedProperty().addListener((observable, oldValue, newValue) -> reportOptions.showHiddenItems = newValue);

        cbShowUnknownExtensions = new CheckBox("Unknown extensions") ;
        cbShowUnknownExtensions.selectedProperty().addListener((observable, oldValue, newValue) -> reportOptions.showUnknownExtensions = newValue);

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

        tg2.selectedToggleProperty().addListener(this);

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
        else if (reportOptions.reportDetailLevel == ReportDetailLevel.ALL_ITEMS) {
            radioAllItems.setSelected(true);
        }
        else if (reportOptions.reportDetailLevel == ReportDetailLevel.CUSTOM) {
            radioArtifactsOnly.setSelected(true);
        }

        cbShowHiddenFilesFolder.setSelected(reportOptions.showHiddenItems) ;
        cbShowUnknownExtensions.setSelected(reportOptions.showUnknownExtensions);

        if(reportOptions.langStatComparator.comparatorType == LanguageStatsComparatorType.BY_LANGUAGE_OCURRENCE) {
            radioStatsOrderByOccurrence.setSelected(true);
        }
        else {
            radioStatsOrderByName.setSelected(true);
        }
    }

    @Override
    public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if(newValue.equals(radioFoldersOnly)) {
            reportOptions.reportDetailLevel = ReportDetailLevel.FOLDER ;
        } else if(newValue.equals(radioAllItems)) {
            reportOptions.reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;
        } else if(newValue.equals(radioArtifactsOnly)) {
            reportOptions.reportDetailLevel = ReportDetailLevel.CUSTOM ;
        } else if(newValue.equals(radioStatsOrderByOccurrence) || newValue.equals(radioStatsOrderByName)) {
            reportOptions.langStatComparator = newValue.equals(radioStatsOrderByOccurrence) ? compareByLanguageOccurrence: compareByLanguageName;
        }
    }
}