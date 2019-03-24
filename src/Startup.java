import Utilities.AppGlobal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.*;
import java.io.File;
import java.io.IOException;

/**
 * Controller for the queue.fxml page.
 *
 * @author Nick Gordon
 * @since 3/27/2018
 */
public class Startup extends Application
{
    private static Stage pStage;

    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        Startup.pStage = pStage;
    }

    /**
     * Primary stage construction.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Hide the primary stage until startup is complete.
        primaryStage.setOpacity(0.0);

        checkLocalStorage(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("/Views/Layout/layout.fxml"));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("/Views/Layout/main.css");

        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.setTitle("Track Player");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        primaryStage.setMaximized(false);
        primaryStage.show();

        // Startup is complete show the primary stage.
        primaryStage.setOpacity(1.0);

        AppGlobal.setCurrentStage(primaryStage);
    }

    /**
     * Checks if the user already selected a directory to read their tracks from.  If not we show them the
     * setup stage.
     * @param primaryStage
     */
    private void checkLocalStorage(Stage primaryStage)
    {
        File trackSource = new File("Storage/LocalTrackSource.json");

        // If setup is not complete show the setup stage and let them select their local directory that contains
        // their music tracks.
        if(!trackSource.exists())
        {
            try
            {
                Stage dialog = new Stage();

                Parent setupRoot = FXMLLoader.load(getClass().getResource("/Views/Setup/Setup.fxml"));
                dialog.setScene(new Scene(setupRoot));

                // Show the setup stage as a modal of the primary stage.
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

    /**
     * Starting point of the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
