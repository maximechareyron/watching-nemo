package mainwindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class ConsoleView extends BorderPane {

    private final PrintStream out;
    private final TextArea textArea;
    private final InputStream in;

    public ConsoleView() {
        this(Charset.defaultCharset());
    }

    public ConsoleView(Charset charset) {
        getStyleClass().add("console");
        this.textArea = new TextArea();
        this.textArea.setWrapText(true);
        setCenter(this.textArea);

        final TextInputControlStream stream = new TextInputControlStream(this.textArea, Charset.defaultCharset());
        try {
            this.out = new PrintStream(stream.getOut(), true, charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.in = stream.getIn();

        final ContextMenu menu = new ContextMenu();
        menu.getItems().add(createItem("Clear console", e -> {
            try {
                stream.clear();
                this.textArea.clear();
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }));
        this.textArea.setContextMenu(menu);

        setPrefWidth(600);
        setPrefHeight(400);
    }

    private MenuItem createItem(String name, EventHandler<ActionEvent> a) {
        final MenuItem menuItem = new MenuItem(name);
        menuItem.setOnAction(a);
        return menuItem;
    }

    public PrintStream getOut() {
        return out;
    }

    public InputStream getIn() {
        return in;
    }

}
