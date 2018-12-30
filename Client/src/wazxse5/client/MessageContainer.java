package wazxse5.client;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class MessageContainer extends TextFlow {


    public MessageContainer(String message) {
        super();
        Text textMessage = new Text(message);
        textMessage.setFill(Color.WHITE);
        textMessage.setFont(new Font(13));
        getChildren().add(textMessage);

        setTextAlignment(TextAlignment.RIGHT);
        setBackground(new Background(new BackgroundFill(Color.rgb(0, 132, 255), new CornerRadii(5), Insets.EMPTY)));
        setPadding(new Insets(3));
    }

    public MessageContainer(String from, String message) {
        super();
        Text textFrom = new Text(from + ":\n");
        textFrom.setFont(new Font(15));
        Text textMessage = new Text(message);
        textMessage.setFont(new Font(13));
        getChildren().addAll(textFrom, textMessage);

        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)));
        setPadding(new Insets(3));
    }
}
