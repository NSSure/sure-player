package Views.Video;

import Utilities.AppGlobal;
import Utilities.TrackManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaView;

import java.io.IOException;

/**
 * Controller for the video-playback.fxml page.
 *
 * @author Nick Gordon
 * @since 5/11/2019
 */
public class VideoPlaybackController
{
    @FXML
    private StackPane videoPlaybackPane;

    @FXML
    private MediaView videoPlaybackView;

    /**
     * Lifecycle event for the fxml page.  Here all the controls have been initialized and can be accessed from code.
     * @throws IOException
     */
    public void initialize() throws IOException
    {
        try
        {
            TrackManager trackManager = AppGlobal.getTrackManagerInstance();
            this.videoPlaybackView.setMediaPlayer(trackManager.getMediaPlayer());
        }
        catch (MediaException ex)
        {
            System.out.println(ex);
        }
    }
}
