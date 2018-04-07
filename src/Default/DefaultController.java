package Default;

import Models.Track;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import com.google.gson.Gson;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.hildan.fxgson.FxGson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class DefaultController
{
    @FXML
    private TableView trackTable;

    @FXML
    private Slider playbackProgress;

    @FXML
    private Label lblCurrentTrackDescription;

    private MediaPlayer mediaPlayer;
    private Track currentTrack;
    ObservableList<Track> tracks;

    public void initialize()
    {
        try
        {
            String path = "Storage/LocalTrackSource.json";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            // FxGson sample code source: https://stackoverflow.com/questions/32794500/serialize-javafx-model-with-gson
            Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();

            Track[] json = gson.fromJson(bufferedReader, Track[].class);
            tracks = FXCollections.observableArrayList(Arrays.asList(json));

            trackTable.setItems(tracks);
        }
        catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setHeaderText("Failed to Read Local Track Storage");
            alert.setContentText(ex.getMessage() + " in " + ex.getClass());
            alert.showAndWait();
        }

        configure();
    }

    private void configure()
    {
        trackTable.setOnMouseClicked(this::onTableClicked);
    }

    private void toggleTrack(Track queuedTrack)
    {
        Media media = new Media(queuedTrack.getPath());

        if(currentTrack == null || currentTrack.getName().compareToIgnoreCase(queuedTrack.getName()) != 0)
        {
            if(mediaPlayer != null)
            {
                mediaPlayer.stop();

            }

            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.currentTimeProperty().addListener(this::displayPlaybackProgress);

            currentTrack = queuedTrack;
            lblCurrentTrackDescription.textProperty().bind(currentTrack.nameProperty());
        }

        MediaPlayer.Status playbackStatus = mediaPlayer.getStatus();

        switch (playbackStatus)
        {
            case PLAYING:
                mediaPlayer.stop();
                break;
            case STOPPED:
            case UNKNOWN:
                mediaPlayer.play();
                break;
            default:
                break;
        }
    }

    // Listener Functions

    private void displayPlaybackProgress(javafx.beans.Observable observable)
    {
        playbackProgress.setValue(mediaPlayer.getCurrentTime().toSeconds());
    }

    private void onTableClicked(MouseEvent event)
    {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                TableView.TableViewSelectionModel model = trackTable.getSelectionModel();
                Track queuedTrack = (Track)model.getSelectedItem();

                toggleTrack(queuedTrack);
            }
        }
    }

    // FXML Functions

    @FXML
    public void onPreviousClicked(MouseEvent event)
    {
        TableView.TableViewSelectionModel model = trackTable.getSelectionModel();

        int selectedIndex = model.getSelectedIndex();
        int previousIndex = selectedIndex - 1;

        Track queuedPreviousTrack = tracks.get(previousIndex);

        if(queuedPreviousTrack != null)
        {
            model.select(previousIndex);
            toggleTrack(queuedPreviousTrack);
        }
    }

    @FXML
    public void onStartClicked(MouseEvent event)
    {
        TableView.TableViewSelectionModel model = trackTable.getSelectionModel();
        Track queuedTrack = (Track)model.getSelectedItem();

        toggleTrack(queuedTrack);
    }

    @FXML
    public void onNextClicked(MouseEvent event)
    {
        TableView.TableViewSelectionModel model = trackTable.getSelectionModel();

        int selectedIndex = model.getSelectedIndex();
        int nextIndex = selectedIndex + 1;

        Track queuedNextTrack = tracks.get(nextIndex);

        if(queuedNextTrack != null)
        {
            model.select(nextIndex);
            toggleTrack(queuedNextTrack);
        }
    }
}
