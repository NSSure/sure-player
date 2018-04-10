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
import javafx.event.EventHandler;
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
    public static EventSystem.EventHandler onTrackStarted;
    public static EventSystem.EventHandler onElapsedTimeChanged;

    private static MediaPlayer mediaPlayer;
    private static Track currentTrack;
    private static ArrayList<Track> tracks;
    private static int currentTrackIndex;
    private static boolean hasPendingTrack;
    private static DoubleProperty currentTime = new SimpleDoubleProperty();

    public static void initialize()
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

    public static void toggleTrack(Track queuedTrack)
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
                mediaPlayer.currentTimeProperty().addListener(TrackManager::incrementCurrentTime);

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

                    if(onTrackStarted.canExecute())
                    {
                        onTrackStarted.execute();
                    }
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

    public static void pendTrack(Track track)
    {
        currentTrack = track;
        hasPendingTrack = true;
    }

    public static Track getPrevious()
    {
        int previousIndex = currentTrackIndex - 1;
        Track previousTrack = tracks.get(previousIndex);
        return previousTrack;
    }

    public static Track getNext()
    {
        int nextIndex = currentTrackIndex + 1;
        Track nextTrack = tracks.get(nextIndex);
        return nextTrack;
    }

    private static void incrementCurrentTime(Observable observable)
    {
        currentTime.set(mediaPlayer.getCurrentTime().toSeconds());

        if(onElapsedTimeChanged.canExecute())
        {
            onElapsedTimeChanged.execute();
        }
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static Track getCurrentTrack() {
        return currentTrack;
    }

    public static void setCurrentTrack(Track currentTrack) {
        TrackManager.currentTrack = currentTrack;
    }

    public static ArrayList<Track> getTracks() {
        return tracks;
    }

    public static void setTracks(ArrayList<Track> tracks) {
        TrackManager.tracks = tracks;
    }

    public static int getCurrentTrackIndex() {
        return currentTrackIndex;
    }

    public static DoubleProperty currentTimeProperty()
    {
        return currentTime;
    }

    public static double getCurrentTime()
    {
        return currentTime.get();
    }
}
