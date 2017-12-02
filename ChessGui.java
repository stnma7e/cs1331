import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;





/**
 * Gui interface for visualizing PGN files.
 *
 * @author sdelmerico3
 * @version 0.0.1
 */
public class ChessGui extends Application {
    @Override public void start(Stage stage) {
        stage.setTitle("ChessDb Gui");

        ChessDb gamesDb = new ChessDb();
        TableView<ChessGame> gamesTable = new TableView<ChessGame>();
        ObservableList<ChessGame> games =
            FXCollections.observableArrayList(gamesDb.getGames());

        gamesTable.setItems(games);
        TableColumn<ChessGame, String> eventCol =
            new TableColumn<ChessGame, String>("Event");
        eventCol.setCellValueFactory(new Callback<
            CellDataFeatures<ChessGame, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(
                CellDataFeatures<ChessGame, String> g) {
                return new SimpleStringProperty(g.getValue().getEvent());
            }
        });
        TableColumn<ChessGame, String> siteCol =
            new TableColumn<ChessGame, String>("Site");
        siteCol.setCellValueFactory(g ->
            new SimpleStringProperty(g.getValue().getSite()));
        TableColumn<ChessGame, String> dateCol =
            new TableColumn<ChessGame, String>("Date");
        dateCol.setCellValueFactory(g ->
            new SimpleStringProperty(g.getValue().getDate()));
        TableColumn<ChessGame, String> whiteCol =
            new TableColumn<ChessGame, String>("White");
        whiteCol.setCellValueFactory(g ->
            new SimpleStringProperty(g.getValue().getWhite()));
        TableColumn<ChessGame, String> blackCol =
            new TableColumn<ChessGame, String>("Black");
        blackCol.setCellValueFactory(g ->
            new SimpleStringProperty(g.getValue().getBlack()));
        TableColumn<ChessGame, String> resultCol =
            new TableColumn<ChessGame, String>("Result");
        resultCol.setCellValueFactory(g ->
            new SimpleStringProperty(g.getValue().getResult()));

        gamesTable.getColumns().setAll(eventCol, siteCol, dateCol, whiteCol,
            blackCol, resultCol);

        Button dismissButton = new Button("Dismiss");
        dismissButton.setOnAction(event -> Platform.exit());

        Button viewButton = new Button("View Game");
        ButtonType loginButtonType =
            new ButtonType("Login", ButtonData.OK_DONE);
        viewButton.setOnAction(event -> {
                ChessGame game =
                    gamesTable.getSelectionModel().getSelectedItem();
                if (game == null) {
                    return;
                }

                ObservableList<String> metaDataTitles =
                    FXCollections.observableArrayList(
                        "Event", "Site", "Date", "White", "Black", "Result",
                        "", "Moves");
                ListView<String> titleView =
                    new ListView<String>(metaDataTitles);
                ObservableList<String> metaData =
                    FXCollections.observableArrayList(
                        game.getEvent(), game.getSite(), game.getDate(),
                        game.getWhite(), game.getBlack(), game.getResult(), "");
                try {
                    for (int i = 1;; i++) {
                        metaData.add(game.getMove(i));
                    }

                } catch (IndexOutOfBoundsException err) {
                    String fuckCheckstyle = "";
                }
                ListView<String> dataView = new ListView<String>(metaData);

                HBox metaDataBox = new HBox();
                metaDataBox.getChildren().addAll(titleView, dataView);

                Stage viewStage = new Stage();
                Button dismissView = new Button("Dismiss");
                dismissView.setOnAction(event2 -> viewStage.close());

                VBox b = new VBox();
                b.getChildren().addAll(metaDataBox, dismissView);
                viewStage.setScene(new Scene(b));
                viewStage.show();
            }
        );

        ObservableList<String> options =
            FXCollections.observableArrayList(
                "Player",
                "Site",
                "Date",
                "Result"
            );
        final ComboBox<String> comboBox = new ComboBox(options);
        TextField search = new TextField();
        search.textProperty().addListener((observable, oldValue, newValue) -> {
                String searchType = comboBox.getSelectionModel()
                    .getSelectedItem();
                if (searchType == null || newValue.equals("")) {
                    ObservableList<ChessGame> newGames =
                        FXCollections.observableArrayList(gamesDb.getGames());
                    gamesTable.setItems(newGames);
                    return;
                }

                final String searchValue = newValue.toLowerCase();

                ObservableList<ChessGame> newGames =
                    FXCollections.observableArrayList(
                        gamesDb.getGames().stream().filter(game -> {
                                switch (searchType) {
                                case "Player":
                                    return game.getWhite().
                                        toLowerCase().contains(searchValue)
                                        || game.getBlack().
                                            toLowerCase().contains(searchValue);
                                case "Site":
                                    return game.getSite()
                                        .toLowerCase().contains(searchValue);
                                case "Date":
                                    return game.getDate()
                                        .toLowerCase().contains(searchValue);
                                case "Result":
                                    return game.getResult()
                                        .toLowerCase().contains(searchValue);
                                default:
                                    return true;
                                }
                            }
                ).collect(Collectors.toList()));
                gamesTable.setItems(newGames);
            }
        );

        HBox buttons = new HBox();
        buttons.getChildren().addAll(
            dismissButton, viewButton, comboBox, search);
        VBox root = new VBox();
        root.getChildren().addAll(gamesTable, buttons);

        stage.setScene(new Scene(root));
        stage.show();
    }
}
