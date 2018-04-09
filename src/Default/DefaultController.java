package Default;

import javafx.fxml.FXML;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;

import Utilities.TrackManager;

import Models.Track;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class DefaultController
{
    @FXML
    private TableView trackTable;

    private ObservableList<Track> tracks;

    public void initialize()
    {
        tracks = FXCollections.observableArrayList(TrackManager.getTracks());
        trackTable.setItems(tracks);

        configure();
    }

    private void configure()
    {
        trackTable.setOnMouseClicked(this::onTableClicked);
    }

    private void onTableClicked(MouseEvent event)
    {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            TableView.TableViewSelectionModel model = trackTable.getSelectionModel();
            Track queuedTrack = (Track)model.getSelectedItem();

            if(event.getClickCount() == 1){
                TrackManager.pendTrack(queuedTrack);
            }
            else if(event.getClickCount() == 2){
                TrackManager.toggleTrack(queuedTrack);
            }
        }
    }
}
