package net.yudichev.googlephotosupload.ui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import net.yudichev.googlephotosupload.core.KeyedError;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.System.lineSeparator;
import static javafx.scene.control.SelectionMode.MULTIPLE;
import static javafx.scene.input.KeyCombination.CONTROL_ANY;
import static javafx.scene.input.KeyCombination.META_ANY;
import static net.yudichev.googlephotosupload.ui.OperatingSystemDetection.OSType;
import static net.yudichev.googlephotosupload.ui.OperatingSystemDetection.getOperatingSystemType;

public final class FailureLogFxController {
    public TableView<KeyedError> tableView;

    @SuppressWarnings("rawtypes")
    public static void copySelectionToClipboard(TableView<?> table) {
        Set<Integer> rows = new TreeSet<>();
        for (TablePosition tablePosition : table.getSelectionModel().getSelectedCells()) {
            rows.add(tablePosition.getRow());
        }
        StringBuilder stringBuilder = new StringBuilder(rows.size() * 256);
        boolean firstRow = true;
        for (Integer row : rows) {
            if (!firstRow) {
                stringBuilder.append(lineSeparator());
            }
            firstRow = false;
            boolean firstCol = true;
            for (TableColumn<?, ?> column : table.getColumns()) {
                if (!firstCol) {
                    stringBuilder.append('\t');
                }
                firstCol = false;
                Object cellData = column.getCellData(row);
                stringBuilder.append(cellData == null ? "" : cellData.toString());
            }
        }
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(stringBuilder.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }

    public void initialize() {
        tableView.getSelectionModel().setSelectionMode(MULTIPLE);
        KeyCodeCombination keyCodeCopy1 = new KeyCodeCombination(KeyCode.C, getOperatingSystemType() == OSType.MacOS ? META_ANY : CONTROL_ANY);
        KeyCodeCombination keyCodeCopy2 = new KeyCodeCombination(KeyCode.INSERT, CONTROL_ANY);
        tableView.setOnKeyPressed(event -> {
            if (keyCodeCopy1.match(event) || keyCodeCopy2.match(event)) {
                copySelectionToClipboard(tableView);
            }
        });

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("key"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("error"));
    }

    public void addFailures(Collection<KeyedError> failures) {
        tableView.getItems().addAll(failures);
    }
}
