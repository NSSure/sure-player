package Views.Setup;

import Utilities.AppGlobal;
import javafx.fxml.FXML;
import com.mpatric.mp3agic.ID3v2;

import Models.Track;
import com.google.gson.Gson;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.hildan.fxgson.FxGson;

import java.io.*;
import java.util.ArrayList;

/**
 * Controller for the video-playback.fxml page.
 *
 * @author Nick Gordon
 * @since 4/1/2018
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
        btnCompleteSetup.setDisable(true);
    }

    /**
     * Display the choose a directory prompt.
     * @param event The action event.
     */
    private void displayLocalDirectoryPrompt(ActionEvent event)
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(sceneBase.getScene().getWindow());

        // If the user selected a directory continue.
        if(selectedDirectory != null)
        {
            setLocalStorage(selectedDirectory);
        }
    }

    /**
     * Reads the files in the selected directory and creates a local json file containing the track information.
     * @param localDirectory The selected directory for the track information.
     */
    private void setLocalStorage(File localDirectory)
    {
        // List the files in the directory.
        File[] localTracks = localDirectory.listFiles();

        // If their are no file display a message to the user.
        if(localTracks.length == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Action Completed");
            alert.setHeaderText(null);
            alert.setContentText("Local music directory must have at least one file.");
            alert.showAndWait();

            return;
        }

        ArrayList<Track> tracks = new ArrayList<>();

        if(localTracks != null)
        {
            for(File localFile : localTracks)
            {
                // If the file is not a directory or an mp3 file we don't add it the the local tracks.
                if(!localFile.isDirectory() && (AppGlobal.isMp3(localFile.getName()) || AppGlobal.isMp4(localFile.getName())))
                {

                    // Get the mp3 tag information.
                    String path = localFile.toPath().toString();
                    ID3v2 localFileTags = null;
                    long lengthInSeconds = 0;

                    path = path.replace('\\', '/');

                    try
                    {
                        if (AppGlobal.isMp3(localFile.getName()))
                        {
                            Mp3File mp3 = new Mp3File(path);
                            lengthInSeconds = mp3.getLengthInSeconds();

                            if(mp3.hasId3v1Tag())
                            {
                                localFileTags = mp3.getId3v2Tag();
                            }
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
            }

            // If their were no valid mp3 tracks found in that directory we don't create the LocalTrackSource.json on the local file system.
            if(tracks.size() > 0)
            {
                try
                {
                    String storageDirectoryName = "Storage";
                    File localStorageDirectory = new File(storageDirectoryName);

                    if (!localStorageDirectory.exists())
                    {
                        localStorageDirectory.mkdir();
                    }

                    File musicSourceJsonFile = new File(storageDirectoryName + "/LocalTrackSource.json");
                    musicSourceJsonFile.createNewFile();

                    Gson gson = FxGson.coreBuilder().create();
                    String json = gson.toJson(tracks);

                    FileWriter fileWriter = new FileWriter(musicSourceJsonFile.getAbsolutePath());
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    bufferedWriter.write(json);
                    bufferedWriter.close();

                    // gson.toJson(tracks, writer);
                    // writer.close();

                    btnCompleteSetup.setDisable(false);
                }
                catch (IOException ex)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle("Local Import Failed");
                    alert.setContentText("Whoops! Something went wrong when attempting to import your local media files. Please try to import your local media again.");
                    alert.showAndWait();
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Action Completed");
                alert.setHeaderText(null);
                alert.setContentText("No music file found in directory.  Please choose another directory.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Hides the setup stage.
     * @param event The action event.
     */
    private void completeSetup(ActionEvent event)
    {
        sceneBase.getScene().getWindow().hide();
    }
}
