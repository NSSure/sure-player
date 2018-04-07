package Setup;

import Models.Track;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.hildan.fxgson.FxGson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Nick on 4/1/2018.
 */
public class SetupController
{
    @FXML
    private VBox sceneBase;

    @FXML
    private Button btnLocalDirectory;

    @FXML
    private Button btnCompleteSetup;

    public void initialize()
    {
        btnLocalDirectory.setOnAction(this::displayLocalDirectoryPrompt);
        btnCompleteSetup.setOnAction(this::completeSetup);
    }

    private void displayLocalDirectoryPrompt(ActionEvent event)
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(sceneBase.getScene().getWindow());

        if(selectedDirectory != null)
        {
            setLocalStorage(selectedDirectory);
        }
    }

    private void setLocalStorage(File localDirectory)
    {
        File[] localTracks = localDirectory.listFiles();
        ArrayList<Track> tracks = new ArrayList<>();

        if(localTracks != null)
        {
            for(File track : localTracks)
            {
                String path = track.toURI().toString();
                tracks.add(new Track(track.getName(), path));
            }

            try
            {
                Writer writer = new FileWriter("Storage/LocalTrackSource.json");

                Gson gson = FxGson.coreBuilder().create();
                gson.toJson(tracks, writer);

                writer.close();
            }
            catch (IOException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Local Import Failed");
                alert.setContentText("Whoops! Something went wrong when attempting to import your local media files. Please try to import your local media again.");
                alert.showAndWait();
            }
        }
    }

    private void completeSetup(ActionEvent event)
    {
        sceneBase.getScene().getWindow().hide();
    }
}
