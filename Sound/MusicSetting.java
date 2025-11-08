package Sound;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class MusicSetting {

    private final MusicManager musicManager = MusicManager.getInstance();
    private final Popup settingsPopup = new Popup();
    private final StackPane root;

    public MusicSetting(Stage stage, StackPane rootLayout) {
        this.root = rootLayout;
        createSettingsPopup();

        ImageView gearIcon = createGearButton();
        StackPane.setAlignment(gearIcon, Pos.TOP_RIGHT);
        StackPane.setMargin(gearIcon, new Insets(10));
        root.getChildren().add(gearIcon);
    }

    private ImageView createGearButton() {
        Image gearImg = new Image("file:resource/sounds/gear.png");
        ImageView gearView = new ImageView(gearImg);
        gearView.setFitWidth(32);
        gearView.setFitHeight(32);
        gearView.setOnMouseClicked(e -> showSettings());
        return gearView;
    }

    private void createSettingsPopup() {
        VBox pane = new VBox(15);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: rgba(30,30,30,0.9); -fx-padding: 20; -fx-background-radius: 12;");
        pane.setPrefWidth(300);

        Label title = new Label("Music Volume");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18;");

        Slider volumeSlider = new Slider(0, 1, musicManager.getVolume());
        volumeSlider.setPrefWidth(200);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                musicManager.setVolume(newVal.doubleValue())
        );

        Label closeBtn = new Label("Close");
        closeBtn.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 16;");
        closeBtn.setOnMouseClicked(e -> hideSettings());

        pane.getChildren().addAll(title, volumeSlider, closeBtn);
        settingsPopup.getContent().add(pane);
    }

    private void showSettings() {
        settingsPopup.show(root.getScene().getWindow());
        blurBackground(true);
    }

    private void hideSettings() {
        settingsPopup.hide();
        blurBackground(false);
    }

    private void blurBackground(boolean enable) {
        if (enable) {
            root.setEffect(new BoxBlur(10, 10, 3));
        } else {
            root.setEffect(null);
        }
    }
}
