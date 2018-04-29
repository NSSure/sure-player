package Views.Tracks;

import Enums.QueueType;
import Views.Layout.LayoutController;
import javafx.beans.Observable;
import javafx.fxml.FXML;

import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import jiconfont.icons.FontAwesome;

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

    // Lifecycle

    public void initialize()
    {
        // Source item hover binding taken from - https://stackoverflow.com/questions/44094265/adding-hover-listener-to-cells-of-a-specific-column-in-javafx-table

        IconFontFX.register(FontAwesome.getIconFont());

        ObservableList<Track> tracks = FXCollections.observableArrayList(AppGlobal.getTrackManagerInstance().getTracks());
        trackTable.setItems(tracks);

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
                            // https://stackoverflow.com/questions/20350099/programmatically-change-the-tableview-row-appearance
                            getStyleClass().add("current-track-text");
                        }
                        else {
                            getStyleClass().removeAll("current-track-text");
                        }
                    });
                }
            };

            row.hoverProperty().addListener((observable, wasHover, nowHover) -> {
                if(nowHover)
                {
                    hoverIndex.set(row.getIndex());
                }
            });

            return row;
        });

        TableColumn<Track, Track> actionColumn = new TableColumn<>("");

        actionColumn.setCellFactory(col -> {
            IconNode actionIcon = new IconNode(FontAwesome.ELLIPSIS_H);
            actionIcon.setIconSize(25);
            actionIcon.setFill(Color.valueOf("#A7A7A7"));

            MenuButton trackMenuButton = new MenuButton();

            MenuItem addToPlaylistItem = new MenuItem("Add to Views.Playlist");
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

            BooleanBinding cellRowHovered = Bindings.createBooleanBinding(() -> hoverIndex.get() == cell.getIndex(), cell.itemProperty(), hoverIndex);
            trackMenuButton.visibleProperty().bind(cellRowHovered);

            return cell;
        });

        trackTable.getColumns().add(actionColumn);

        trackManager.selectedTrackProperty().addListener(this::onSelectedTrackChanged);

        configure();
        setSelectedRow(trackManager.getSelectedTrack());
    }

    private void setSelectedRow(Track track)
    {
        int index = trackTable.getItems().indexOf(track);
        trackTable.getSelectionModel().select(index);
    }

    public void setParentController(LayoutController parentController)
    {
        this.parentController = parentController;
    }

    private void onSelectedTrackChanged(Observable observable)
    {
        setSelectedRow(trackManager.getSelectedTrack());
    }

    private void onAddToPlaylistClicked(ActionEvent event)
    {
        System.out.println("PLAYLIST");
    }

    private void onGoToArtistClicked(ActionEvent event)
    {
        Track trackPendingAction = trackTable.getItems().get(hoverIndex.get());
        this.parentController.toQueue(QueueType.ARTIST, trackPendingAction);
    }

    private void onGoToAlbumClicked(ActionEvent event)
    {
        Track trackPendingAction = trackTable.getItems().get(hoverIndex.get());
        this.parentController.toQueue(QueueType.ALBUM, trackPendingAction);
    }

    private void onGoToGenreClicked(ActionEvent event)
    {
        Track trackPendingAction = trackTable.getItems().get(hoverIndex.get());
        this.parentController.toQueue(QueueType.GENRE, trackPendingAction);
    }

    private void onRemoveFromLibraryClicked(ActionEvent event)
    {
        System.out.println("REMOVE");
    }

    private void configure()
    {
        trackTable.setOnMouseClicked(this::onTableClicked);
    }

    // FXML

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
