package Utilities;

import com.mpatric.mp3agic.ID3v2;
import javafx.beans.Observable;

import Models.Track;
import com.google.gson.Gson;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.hildan.fxgson.FxGson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class TrackManager
{
    private MediaPlayer mediaPlayer;
    private Track currentTrack;
    private ArrayList<Track> tracks;
    private int currentTrackIndex;
    private boolean hasPendingTrack;

    public void initialize()
    {
        try
        {
            String path = "Storage/LocalTrackSource.json";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

            // FxGson sample code source: https://stackoverflow.com/questions/32794500/serialize-javafx-model-with-gson
            Gson gson = FxGson.coreBuilder().setPrettyPrinting().disableHtmlEscaping().create();

            Track[] trackSource = gson.fromJson(bufferedReader, Track[].class);

            for(Track track : trackSource)
            {
                System.out.println("Track: " + track.getName());

                Mp3File mp3 = new Mp3File(track.getPath());
                ID3v2 tags = mp3.getId3v2Tag();
                track.setTags(tags);
            }

            tracks = new ArrayList<>(Arrays.asList(trackSource));
        }
        catch (IOException | UnsupportedTagException | InvalidDataException ex)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setHeaderText("Failed to Read Local Track Storage");
            alert.setContentText(ex.getMessage() + " in " + ex.getClass());
            alert.showAndWait();
        }
    }

    public void toggleTrack(Track queuedTrack)
    {
        if(hasPendingTrack && currentTrack != null)
        {
            if(currentTrack != null)
            {
                queuedTrack = currentTrack;
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setHeaderText("No Track Selected");
                alert.setContentText("Please select a track before hitting play.");
                alert.showAndWait();

                return;
            }
        }

        try
        {
            Media media = new Media(Paths.get(queuedTrack.getPath()).toUri().toString());

            if(hasPendingTrack || currentTrack == null || currentTrack.getName().compareToIgnoreCase(queuedTrack.getName()) != 0)
            {
                if(mediaPlayer != null)
                {
                    mediaPlayer.stop();

                }

                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.currentTimeProperty().addListener(this::incrementCurrentTime);
                mediaPlayer.setOnReady(this::mediaInformationLoaded);

                currentTrack = queuedTrack;
                currentTrackIndex = tracks.indexOf(currentTrack);

                hasPendingTrack = false;
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
        catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setHeaderText("Failed to Play Track");
            alert.setContentText(ex.getMessage() + " in " + ex.getClass());
            alert.showAndWait();
        }
    }

    public void mediaInformationLoaded()
    {
        currentTrackDuration.set(mediaPlayer.getTotalDuration().toSeconds());
    }

    public void pendTrack(Track track)
    {
        currentTrack = track;
        hasPendingTrack = true;
    }

    public Track getPrevious()
    {
        int previousIndex = currentTrackIndex - 1;
        Track previousTrack = tracks.get(previousIndex);
        return previousTrack;
    }

    public Track getNext()
    {
        int nextIndex = currentTrackIndex + 1;
        Track nextTrack = tracks.get(nextIndex);
        return nextTrack;
    }

    private void incrementCurrentTime(Observable observable)
    {
        currentTime.set(mediaPlayer.getCurrentTime().toSeconds());
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public int getCurrentTrackIndex() {
        return currentTrackIndex;
    }

    // Bindable properties

    private DoubleProperty currentTime = new SimpleDoubleProperty();

    public DoubleProperty currentTimeProperty()
    {
        return currentTime;
    }

    public double getCurrentTime()
    {
        return currentTime.get();
    }

    private DoubleProperty currentTrackDuration = new SimpleDoubleProperty();

    public DoubleProperty currentTrackDurationProperty()
    {
        return currentTrackDuration;
    }
}
