package mainwindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrawFishes {
    static private Pane p;

    DrawFishes(Pane p)
    {
        this.p = p;
    }

    static void draw(String info) throws Exception
    {
        double width = p.getWidth();
        double height = p.getHeight();
        p.getChildren().remove(1, p.getChildren().size());

        System.out.println(width);
        System.out.println(height);

        String infoList[] = info.split(" \\[");

        for (String s : infoList) {
            Pattern pattern = Pattern.compile(".* at (\\d+)x(\\d+), (\\d+)x(\\d+), (\\d+)\\]");
            Matcher matcher = pattern.matcher(s);

            if (matcher.find()) {
                if (matcher.groupCount() != 5) {
                    // TODO: Display error message ?
                    System.out.println("ish..");
                    continue;
                }

                Rectangle rect = new Rectangle(width * Integer.parseInt(matcher.group(1)) / 100,
                        height * Integer.parseInt(matcher.group(2)) / 100,
                        width * Integer.parseInt(matcher.group(3)) / 100,
                        height * Integer.parseInt(matcher.group(4)) / 100);
                rect.setFill(Color.BLACK);
                p.getChildren().add(rect);
            }
        }

/*
        for (String fishInfo : infoList) {
            Rectangle rect = new Rectangle(20,20,200,200);
            p.setStyle("-fx-background-color: blue;");
        }*/

    }
}
