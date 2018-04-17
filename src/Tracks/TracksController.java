package Tracks;

import EventSystem.EventHandler;
import Utilities.AppGlobal;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;

import Utilities.TrackManager;

import Models.Track;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class TracksController
{
    @FXML
    private TableView trackTable;

    private ObservableList<Track> tracks;

    public EventHandler onTrackSelected;

    // Lifecycle

    public void initialize()
    {
        tracks = FXCollections.observableArrayList(AppGlobal.getTrackManagerInstance().getTracks());
        trackTable.setItems(tracks);

        configure();
    }

    private void configure()
    {
        trackTable.setOnMouseClicked(this::onTableClicked);
    }

    // FXML

    private void onTableClicked(MouseEvent event)
    {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            TableView.TableViewSelectionModel model = trackTable.getSelectionModel();
            Track queuedTrack = (Track)model.getSelectedItem();

            if(event.getClickCount() == 1){
                AppGlobal.getTrackManagerInstance().pendTrack(queuedTrack);

                if(onTrackSelected != null)
                {
                    onTrackSelected.execute();
                }
            }
            else if(event.getClickCount() == 2){
                AppGlobal.getTrackManagerInstance().toggleTrack(queuedTrack);
            }
        }
    }

    // Bindable properties

    private BooleanProperty isTrackSelected = new SimpleBooleanProperty(true);

    public BooleanProperty isTrackSelectedProperty()
    {
        return isTrackSelected;
    }
}
