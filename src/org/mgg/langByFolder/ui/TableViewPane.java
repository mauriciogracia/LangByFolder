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

        for(int i = 0; i < 8 ; i++ ) {
            TableColumn<FileContext, String> col =
                    new TableColumn<>(ReportOptions.getHeader(i));

            col.setCellValueFactory(new PropertyValueFactory<>(FileContext.getPropertyNameForColumn(i)));
            tableView.getColumns().add(col);
        }

        VBox content = new VBox(tableView);

        content.setBorder(new Border(new BorderStroke(GraphicSettings.borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setPadding(GraphicSettings.borderMargin);
        this.setCenter(content) ;
    }

    @Override
    public void started() {
        tableView.getItems().clear();
    }

    @Override
    public void finished() {
        for(FileContext fc : rep.items ) {
            tableView.getItems().add(fc);
        }
    }
}
