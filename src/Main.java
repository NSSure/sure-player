import Utilities.LocalStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.*;

import java.io.File;
import java.io.IOException;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setOpacity(0.0);

        checkLocalStorage(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("/Views/Layout/layout.fxml"));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("/Views/Layout/main.css");

        primaryStage.setTitle("Track Player");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        primaryStage.setMaximized(true);
        primaryStage.show();

        primaryStage.setOpacity(1.0);
    }

    private void checkLocalStorage(Stage primaryStage)
    {
        File trackSource = new File("Storage/LocalTrackSource.json");

        if(!trackSource.exists())
        {
            try
            {
                Stage dialog = new Stage();

                Parent setupRoot = FXMLLoader.load(getClass().getResource("/Views/Setup/Setup.fxml"));
                dialog.setScene(new Scene(setupRoot));

                dialog.initOwner(primaryStage);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
            }
            catch (IOException ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Failed to Read Local Directory");
                alert.setContentText("Whoops! Something went wrong when attempting to read through you local music library.");
                alert.showAndWait();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
