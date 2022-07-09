package org.mgg.langByFolder.ui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
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

        //@todo: getHeader method should be used here
        TableColumn<FileContext, String> column1 =
                new TableColumn<>("Item Path");

        column1.setCellValueFactory(
                new PropertyValueFactory<>("itemPath"));


        TableColumn<FileContext, String> column2 =
                new TableColumn<>("Col 2");

        column2.setCellValueFactory(
                new PropertyValueFactory<>("artifactName"));


        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        VBox content = new VBox(tableView);

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }

    @Override
    public void started() {

    }

    @Override
    public void finished() {
        for(FileContext fc : rep.items ) {
            tableView.getItems().add(fc);
        }
    }
}
