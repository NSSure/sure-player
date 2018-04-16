package Playlist;

import EventSystem.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.File;

public class PlaylistController
{
    @FXML
    private VBox sceneBase;

    @FXML
    private ImageView playlistImagePreview;

    @FXML
    private void initialize()
    {

    }

    @FXML
    private void onPlaylistImageClicked(MouseEvent event)
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose Playlist Artwork");
        File file = fileChooser.showOpenDialog(sceneBase.getScene().getWindow());

        if(file != null)
        {
            Image fullImage = new Image(file.toURI().toString());

            PixelReader reader = fullImage.getPixelReader();
            WritableImage croppedImage = new WritableImage(reader, 0, 0, 200, 200);

            playlistImagePreview.setImage(croppedImage);
        }
    }

    @FXML
    private void onSaveClicked(MouseEvent event)
    {
        System.out.println("SAVE PLAYLIST CLICKED");
    }

    @FXML
    private void onCancelClicked(MouseEvent event)
    {
        System.out.println("CANCEL PLAYLIST CLICKED");
    }

    @FXML void onImageCropperClosed(WindowEvent event)
    {
        System.out.println("IMAGE CROPPER STAGE CLOSED");
    }
}
