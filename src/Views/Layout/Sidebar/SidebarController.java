package Views.Layout.Sidebar;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.io.IOException;

/**
 * Controller for the playlist-directory.fxml page.
 *
 * @author Nick Gordon
 * @since 4/8/2018
 */
public class SidebarController
{
    @FXML
    private ListView<String> yourLibraryListView;

    public void initialize() throws IOException {
        ObservableList<String> items = FXCollections.observableArrayList ("Recently Played", "Songs", "Artists");

        yourLibraryListView.setItems(items);
        yourLibraryListView.setOnMouseClicked(event -> yourLibraryListView.getSelectionModel().getSelectedItem());
    }
}
