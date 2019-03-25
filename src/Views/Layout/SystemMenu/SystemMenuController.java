package Views.Layout.SystemMenu;

import Icons.ApplicationIcons;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import Utilities.AppGlobal;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

/**
 * Controller for the system-menu.fxml page.
 *
 * @author Nick Gordon
 * @since 3/23/2019
 */
public class SystemMenuController
{
    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu exitMenu;

    @FXML
    private Menu minimizeMenu;

    @FXML
    private Menu maximizeMenu;

    private double xOffset;
    private double yOffset;

    private ApplicationIcons applicationIcons = new ApplicationIcons();

    /**
     * Lifecycle event for the fxml page.  Here all the controls have been initialized and can be accessed from code.
     * @throws IOException
     */
    public void initialize() throws IOException
    {
        menuBar.setOnMousePressed(event -> SetApplicationPosition(event));
        menuBar.setOnMouseDragged(event -> DrawApplicationWindow(event));

        Label minimizeMenuLabel = new Label();
        minimizeMenuLabel.setGraphic(applicationIcons.getMinimizeIcon());
        minimizeMenuLabel.setOnMouseClicked(event -> AppGlobal.getCurrentStage().setIconified(true));
        minimizeMenu.setGraphic(minimizeMenuLabel);

        Label maximizeMenuLabel = new Label();
        maximizeMenuLabel.setGraphic(applicationIcons.getMaximizeIcon());
        maximizeMenuLabel.setOnMouseClicked(event -> AppGlobal.getCurrentStage().setMaximized(!AppGlobal.getCurrentStage().isMaximized()));
        maximizeMenu.setGraphic(maximizeMenuLabel);

        Label exitMenuLabel = new Label();
        exitMenuLabel.setGraphic(applicationIcons.getCloseIcon());
        exitMenuLabel.setOnMouseClicked(event -> AppGlobal.shutdownApplication());
        exitMenu.setGraphic(exitMenuLabel);
    }

    private void SetApplicationPosition(MouseEvent event)
    {
        AppGlobal.setStageX(AppGlobal.getCurrentStage().getX() - event.getScreenX());
        AppGlobal.setStageY(AppGlobal.getCurrentStage().getY() - event.getScreenY());
    }

    private void DrawApplicationWindow(MouseEvent event)
    {
        AppGlobal.getCurrentStage().setX(event.getScreenX() + AppGlobal.getStageX());
        AppGlobal.getCurrentStage().setY(event.getScreenY() + AppGlobal.getStageY());
    }

    @FXML
    public void toPlaylist(ActionEvent event) {
        AppGlobal.getLayoutController().toPlaylist(event);
    }
}
