package Views.Tracks;

import javafx.fxml.FXML;

import Enums.QueueType;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import jiconfont.icons.FontAwesome;

import Views.Layout.LayoutController;
import EventSystem.EventHandler;
import Utilities.AppGlobal;
import Utilities.TrackManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import Models.Track;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconNode;

/**
 * Controller for the tracks.fxml page.
 *
 * @author Nick Gordon
 * @since 3/27/2018
 */
public class TracksController
{
    private TrackManager trackManager;

    @FXML
    private TableView<Track> trackTable;

    private IntegerProperty hoverIndex = new SimpleIntegerProperty(-2);

    private LayoutController parentController;

    public EventHandler onTrackSelected;

    public TracksController()
    {
        trackManager = AppGlobal.getTrackManagerInstance();
    }

    /**
     * FXML Lifecycle function.
     */
    public void initialize()
    {
        // Register the fontawesome icons.
        IconFontFX.register(FontAwesome.getIconFont());

        // Set the table to display the track manager tracks.
        ObservableList<Track> tracks = FXCollections.observableArrayList(trackManager.getTracks());
        trackTable.setItems(tracks);

        // Custom row factory.
        trackTable.setRowFactory(tableView -> {
            final TableRow<Track> row = new TableRow<Track>(){
                @Override
                protected void updateItem(Track track, boolean empty){
                    super.updateItem(track, empty);

                    if(getIndex() == 0){
                        getStyleClass().add("current-track-text");
                    }

                    trackManager.currentTrackIndexProperty().addListener((observable) -> {
                        if(getIndex() == trackManager.getCurrentTrackIndex()){
                            // Source - https://stackoverflow.com/questions/20350099/programmatically-change-the-tableview-row-appearance
                            getStyleClass().add("current-track-text");
                        }
                        else {
                            getStyleClass().removeAll("current-track-text");
                        }
                    });
                }
            };

            // Source item hover binding taken from - https://stackoverflow.com/questions/44094265/adding-hover-listener-to-cells-of-a-specific-column-in-javafx-table
            row.hoverProperty().addListener((observable, wasHover, nowHover) -> {
                if(nowHover)
                {
                    hoverIndex.set(row.getIndex());
                }
            });

            return row;
        });

        TableColumn<Track, Track> actionColumn = new TableColumn<>("");

        // Custom cell factory for the menu cell.
        actionColumn.setCellFactory(col -> {
            IconNode actionIcon = new IconNode(FontAwesome.ELLIPSIS_H);
            actionIcon.setIconSize(25);
            actionIcon.setFill(Color.valueOf("#A7A7A7"));

            // Create a dropdown menu for each track in the table.
            MenuButton trackMenuButton = new MenuButton();

            MenuItem addToPlaylistItem = new MenuItem("Add to Playlist");
            addToPlaylistItem.setOnAction(this::onAddToPlaylistClicked);

            MenuItem goToArtistItem = new MenuItem("Queue Artist");
            goToArtistItem.setOnAction(this::onGoToArtistClicked);

            MenuItem goToAlbumItem = new MenuItem("Queue Album");
            goToAlbumItem.setOnAction(this::onGoToAlbumClicked);

            MenuItem goToGenreItem = new MenuItem("Queue Genre");
            goToGenreItem.setOnAction(this::onGoToGenreClicked);

            MenuItem removeFromLibraryItem = new MenuItem("Remove from Library");
            removeFromLibraryItem.setOnAction(this::onRemoveFromLibraryClicked);

            trackMenuButton.getItems().addAll(addToPlaylistItem, goToArtistItem, goToAlbumItem, goToGenreItem, removeFromLibraryItem);
            trackMenuButton.setGraphic(actionIcon);

            // Sets the graphic for the row menu to be a font awesome icon instead of a button.
            TableCell<Track, Track> cell = new TableCell<Track, Track>() {
                @Override
                public void updateItem(Track person, boolean empty) {
                    super.updateItem(person, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(trackMenuButton);
                    }
                }
            };

            // Displays the row menu only on hover.
            BooleanBinding cellRowHovered = Bindings.createBooleanBinding(() -> hoverIndex.get() == cell.getIndex(), cell.itemProperty(), hoverIndex);
            trackMenuButton.visibleProperty().bind(cellRowHovered);

            return cell;
        });

        trackTable.getColumns().add(actionColumn);

        trackManager.selectedTrackProperty().addListener(this::onSelectedTrackChanged);

        configure();
        setSelectedRow(trackManager.getSelectedTrack());
    }

    /**
     * Sets the selected row in the TableView.
     * @param track The track to select.
     */
    private void setSelectedRow(Track track)
    {
        int index = trackTable.getItems().indexOf(track);
        trackTable.getSelectionModel().select(index);
    }

    /**
     * Sets the parent controller for the view.
     * @param parentController The parent controller for the view.
     */
    public void setParentController(LayoutController parentController)
    {
        this.parentController = parentController;
    }

    /**
     * Changes the selected track in the table.
     * @param observable
     */
    private void onSelectedTrackChanged(Observable observable)
    {
        setSelectedRow(trackManager.getSelectedTrack());
    }

    /**
     * Fired when the menu item "Add to Playlist" is clicked.
     * @param event The action event.
     */
    private void onAddToPlaylistClicked(ActionEvent event)
    {
        System.out.println("ADD TO PLAYLIST");
    }

    /**
     * Fired when the menu item "Queue Artist" is clicked.  Loads the queue node.
     * @param event The action event.
     */
    private void onGoToArtistClicked(ActionEvent event)
    {
        Track trackPendingAction = trackTable.getItems().get(hoverIndex.get());
        this.parentController.toQueue(QueueType.ARTIST, trackPendingAction);
    }

    /**
     * Fired when the menu item "Queue Album" is clicked.  Loads the queue node.
     * @param event The action event.
     */
    private void onGoToAlbumClicked(ActionEvent event)
    {
        Track trackPendingAction = trackTable.getItems().get(hoverIndex.get());
        this.parentController.toQueue(QueueType.ALBUM, trackPendingAction);
    }

    /**
     * Fired when the menu item "Queue Genre" is clicked.  Loads the queue node.
     * @param event The action event.
     */
    private void onGoToGenreClicked(ActionEvent event)
    {
        Track trackPendingAction = trackTable.getItems().get(hoverIndex.get());
        this.parentController.toQueue(QueueType.GENRE, trackPendingAction);
    }

    /**
     * Fired when the menu item "Remove from Library" is clicked.
     * @param event The action event.
     */
    private void onRemoveFromLibraryClicked(ActionEvent event)
    {
        System.out.println("REMOVE");
    }

    /**
     * Configure the table view actions.
     */
    private void configure()
    {
        trackTable.setOnMouseClicked(this::onTableClicked);
    }

    // FXML

    /**
     * Handles click events on the table.  Single click selected the track and a double click toggles
     * track playback.
     * @param event The mouse click event.
     */
    private void onTableClicked(MouseEvent event)
    {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            TableView.TableViewSelectionModel model = trackTable.getSelectionModel();
            Track selectedTrack = (Track)model.getSelectedItem();
            trackManager.setSelectedTrack(selectedTrack);

            if(event.getClickCount() == 2){
                trackManager.toggleTrack(selectedTrack);
            }
        }
    }

    // Bindable properties

    private BooleanProperty isTrackSelected = new SimpleBooleanProperty(true);

    public BooleanProperty isTrackSelectedProperty()
    {
        return isTrackSelected;
    }
}
