package mainwindow;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrawFishes {
    static public Pane p;

    static void draw(String info)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                double width = ((ImageView)p.getChildren().get(0)).getFitWidth();
                double height = ((ImageView)p.getChildren().get(0)).getFitHeight();
                System.out.println("TAILLE" + width);
                System.out.println(height);
                if (p.getChildren().size() > 1) {
                    p.getChildren().remove(1, p.getChildren().size() - 1);
                }

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
                        System.out.println(s);

                        double fishX = width * Integer.parseInt(matcher.group(1)) / 100;
                        double fishY = height * Integer.parseInt(matcher.group(2)) / 100;
                        double fishWidth = width * Integer.parseInt(matcher.group(3)) / 100;
                        double fishHeight = height * Integer.parseInt(matcher.group(4)) / 100;

                        Rectangle rect = new Rectangle(fishX,
                                fishY,
                                fishX + fishWidth >= width ? width - fishX : fishWidth,
                                fishY + fishHeight >= height ? height - fishY : fishHeight);
                        rect.setFill(Color.BLACK);
                        p.getChildren().add(rect);

                    }
                }
            }
        });
    }
}
