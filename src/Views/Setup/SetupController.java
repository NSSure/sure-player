package Views.Setup;

import Models.Track;
import com.google.gson.Gson;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
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
            for(File localFile : localTracks)
            {
                String path = localFile.toPath().toString();
                ID3v2 localFileTags = null;
                long lengthInSeconds = 0;

                path = path.replace('\\', '/');

                try
                {
                    Mp3File mp3 = new Mp3File(path);
                    lengthInSeconds = mp3.getLengthInSeconds();

                    if(mp3.hasId3v1Tag())
                    {
                        localFileTags = mp3.getId3v2Tag();
                    }
                }
                catch (IOException | UnsupportedTagException | InvalidDataException ex)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Failed to Read Local File Tags");
                    alert.setContentText("Whoops! Something went wrong attempting to read your local files tags. Please try to import your local media again.");
                    alert.showAndWait();
                }

                tracks.add(new Track(localFile.getName(), path, lengthInSeconds, localFileTags));
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
