package Views.Playlist;

import EventSystem.EventBus;
import Models.Playlist;

import Utilities.AppGlobal;
import Utilities.PlaylistUtility;
import Views.Layout.LayoutController;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javafx.scene.image.PixelReader;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.File;
import javafx.stage.*;

public class PlaylistController
{
    @FXML
    private VBox sceneBase;

    @FXML
    private ImageView playlistImagePreview;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtDescription;

    private Playlist playlist;

    private LayoutController parentController;

    public EventBus onPlaylistCreationAborted;
    public EventBus onPlaylistCreationFinished;

    private boolean validateRequiredFields()
    {
        if(txtName.getText().isEmpty() || txtDescription.getText().isEmpty())
        {
            return false;
        }

        return true;
    }

    @FXML
    private void onPlaylistImageClicked(MouseEvent event)
    {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Choose Views.Playlist Artwork");
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
        if(validateRequiredFields())
        {
            Playlist playlist = new Playlist(txtName.getText(), txtDescription.getText());
            PlaylistUtility playlistUtil = new PlaylistUtility();

            playlistUtil.add(playlist);

            EventBus.broadcast("playlist-added", playlist);
            AppGlobal.getLayoutController().onPlaylistCreationFinished();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Required Fields Missing");
            alert.setHeaderText(null);
            alert.setContentText("Whoops! A playlist needs to have a name and a description.  Please fill out the required fields and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onCancelClicked(MouseEvent event)
    {
        AppGlobal.getLayoutController().onPlaylistCreationAborted();
    }

    public void setParentController(LayoutController parentController)
    {
        this.parentController = parentController;
    }
}
