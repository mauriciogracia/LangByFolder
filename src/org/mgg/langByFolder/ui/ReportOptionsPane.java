package org.mgg.langByFolder.ui;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.mgg.langByFolder.CompareByLanguageName;
import org.mgg.langByFolder.CompareByLanguageOccurrence;
import org.mgg.langByFolder.ReportDetailLevel;
import org.mgg.langByFolder.ReportOptions;

public class ReportOptionsPane extends BorderPane implements EventHandler<MouseEvent> {
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
        radioFoldersOnly.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        radioAllItems = new RadioButton("Files & Folders") ;
        radioAllItems.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        radioArtifactsOnly = new RadioButton("Artifacts only") ;
        radioArtifactsOnly.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        radioFoldersOnly.setToggleGroup(tg);
        radioAllItems.setToggleGroup(tg);
        radioArtifactsOnly.setToggleGroup(tg);

        //1st row - label for options
        row = 0 ;
        content.add(new Label("Show options"),0,row);

        //2nd row
        row++;
        content.add(radioAllItems,0,row);
        content.add(radioFoldersOnly,1,row);
        content.add(radioArtifactsOnly,2,row);

        cbShowHiddenFilesFolder = new CheckBox("Hidden files/folder") ;
        cbShowHiddenFilesFolder.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        cbShowUnknownExtensions = new CheckBox("Unknown extensions") ;
        cbShowUnknownExtensions.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        //3rd row
        row++ ;
        content.add(cbShowHiddenFilesFolder,0,2);
        content.add(cbShowUnknownExtensions,1,2);

        //4th row
        ToggleGroup tg2 = new ToggleGroup();
        radioStatsOrderByOccurrence = new RadioButton("Order stats by occurrence") ;
        radioStatsOrderByOccurrence.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

        radioStatsOrderByName = new RadioButton("Order stats by name") ;
        radioStatsOrderByName.addEventFilter(MouseEvent.MOUSE_CLICKED, this);

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
        else if (reportOptions.reportDetailLevel == ReportDetailLevel.ALL_ITEMS) {
            radioAllItems.setSelected(true);
        }
        else if (reportOptions.reportDetailLevel == ReportDetailLevel.CUSTOM) {
            radioArtifactsOnly.setSelected(true);
        }
    }

    @Override
    public void handle(MouseEvent event) {
        Object srcComponent =event.getSource() ;

        if(srcComponent.equals(radioFoldersOnly)) {
            reportOptions.reportDetailLevel = ReportDetailLevel.FOLDER ;
        } else if(srcComponent.equals(radioAllItems)) {
            reportOptions.reportDetailLevel = ReportDetailLevel.ALL_ITEMS ;
        } else if(srcComponent.equals(radioArtifactsOnly)) {
            reportOptions.reportDetailLevel = ReportDetailLevel.CUSTOM ;
        } else if(srcComponent.equals(cbShowHiddenFilesFolder)) {
            reportOptions.showHiddenItems = ((CheckBox)srcComponent).isSelected() ;
        } else if(srcComponent.equals(cbShowUnknownExtensions)) {
            reportOptions.showUnknownExtensions = ((CheckBox)srcComponent).isSelected() ;
        } else if(srcComponent.equals(radioStatsOrderByOccurrence) || srcComponent.equals(radioStatsOrderByName)) {
            reportOptions.langStatComparator = srcComponent.equals(radioStatsOrderByOccurrence) ? compareByLanguageOccurrence: compareByLanguageName;
        }
    }
}