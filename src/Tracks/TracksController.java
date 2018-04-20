package Tracks;

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
            final TableRow<Track> row = new TableRow<>();

            row.hoverProperty().addListener((observable, wasHover, nowHover) -> {
                if(nowHover)
                {
                    hoverIndex.set(row.getIndex());
                }
            });

            trackManager.currentTrackIndexProperty().addListener((observable) -> {
               trackTable.getSelectionModel().select(trackManager.getCurrentTrackIndex());
            });

            return row;
        });

        TableColumn<Track, Track> actionColumn = new TableColumn<>("");

        actionColumn.setCellFactory(col -> {
            IconNode actionIcon = new IconNode(FontAwesome.ELLIPSIS_H);
            actionIcon.setIconSize(25);
            actionIcon.setFill(Color.valueOf("#A7A7A7"));

            MenuButton trackMenuButton = new MenuButton();

            MenuItem addToPlaylistItem = new MenuItem("Add to Playlist");
            addToPlaylistItem.setOnAction(this::onAddToPlaylistClicked);

            MenuItem goToArtistItem = new MenuItem("Go to Artist");
            goToArtistItem.setOnAction(this::onGoToArtistClicked);

            MenuItem goToAlbumItem = new MenuItem("Go to Album");
            goToAlbumItem.setOnAction(this::onGoToAlbumClicked);

            MenuItem removeFromLibraryItem = new MenuItem("Remove from Library");
            removeFromLibraryItem.setOnAction(this::onRemoveFromLibraryClicked);

            trackMenuButton.getItems().addAll(addToPlaylistItem, goToArtistItem, goToAlbumItem, removeFromLibraryItem);
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

        configure();
    }

    private void onAddToPlaylistClicked(ActionEvent event)
    {
        System.out.println("PLAYLIST");
    }

    private void onGoToArtistClicked(ActionEvent event)
    {
        System.out.println("ARTIST");
    }

    private void onGoToAlbumClicked(ActionEvent event)
    {
        System.out.println("ALBUM");
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
            Track queuedTrack = (Track)model.getSelectedItem();

            if(event.getClickCount() == 1){
                AppGlobal.getTrackManagerInstance().pendTrack(queuedTrack);

                if(onTrackSelected != null)
                {
                    onTrackSelected.execute();
                }
            }
            else if(event.getClickCount() == 2){
                AppGlobal.getTrackManagerInstance().toggleTrack(queuedTrack);
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
