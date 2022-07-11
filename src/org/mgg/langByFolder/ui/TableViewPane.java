package org.mgg.langByFolder.ui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.mgg.langByFolder.DirectoryContext;
import org.mgg.langByFolder.FileContext;
import org.mgg.langByFolder.ReportOptions;

public class TableViewPane extends BorderPane implements IReportEventsListener{
    ReportOptions rep ;
    TableView <FileContext>tableView ;

    // https://jenkov.com/tutorials/javafx/tableview.html

    public TableViewPane(ReportOptions rep) {
        super() ;
        this.rep = rep ;
        tableView = new TableView<>();

        for(int i = 0; i < 8 ; i++ ) {
            TableColumn<FileContext, String> col =
                    new TableColumn<>(ReportOptions.getHeader(i));

            col.setCellValueFactory(new PropertyValueFactory<>(FileContext.getPropertyNameForColumn(i)));
            tableView.getColumns().add(col);
        }

        VBox content = new VBox(tableView);
        VBox.setVgrow(tableView,Priority.ALWAYS) ;

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }

    @Override
    public void started() {
        tableView.getItems().clear();

        //remove the dynamic language stats columns
        if(tableView.getColumns().size() > 8)
        {
            tableView.getColumns().remove(8, tableView.getColumns().size());
        }
    }

    @Override
    public void finished() {
        for(FileContext fc : rep.items ) {
            tableView.getItems().add(fc);
        }
        addLangStatsColumns() ;
    }

    void addLangStatsColumns() {
        Object obj = tableView.getItems().get(0) ;
        String titles = ((DirectoryContext) obj).getLangStats() ;

        String [] titleParts = titles.split(";") ;

        int i = 0 ;
        int j = i ;

        for(i = 0; i < titleParts.length/2 ; i++ ) {
            TableColumn<FileContext, String> col =
                    new TableColumn<>(titleParts[j]);

            //col.setCellValueFactory(new PropertyValueFactory<>(FileContext.getPropertyNameForColumn(i)));
            tableView.getColumns().add(col);
            j+= 2 ;
        }
    }
}
