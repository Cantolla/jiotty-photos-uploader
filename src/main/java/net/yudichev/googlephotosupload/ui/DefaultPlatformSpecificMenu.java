package net.yudichev.googlephotosupload.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

final class DefaultPlatformSpecificMenu implements PlatformSpecificMenu {
    private MenuItem exitMenuItem;
    private MenuItem preferencesMenuItem;
    private MenuItem aboutMenuItem;

    @Override
    public void initialize(MenuBar menuBar) {
        Menu fileMenu = new Menu("File");
        ObservableList<MenuItem> fileMenuItems = fileMenu.getItems();

        aboutMenuItem = new MenuItem("About...");
        fileMenuItems.add(aboutMenuItem);

        preferencesMenuItem = new MenuItem("Settings...");
        fileMenuItems.add(preferencesMenuItem);

        fileMenuItems.add(new SeparatorMenuItem());
        exitMenuItem = new MenuItem("Exit");

        fileMenuItems.add(exitMenuItem);
        menuBar.getMenus().add(0, fileMenu);
    }

    @Override
    public void onExitAction(EventHandler<ActionEvent> onExitEventHandler) {
        exitMenuItem.setOnAction(onExitEventHandler);
    }

    @Override
    public void onPreferencesAction(EventHandler<ActionEvent> onPreferencesEventHandler) {
        preferencesMenuItem.setOnAction(onPreferencesEventHandler);
    }

    @Override
    public void setOnAboutAction(EventHandler<ActionEvent> onAboutAction) {
        aboutMenuItem.setOnAction(onAboutAction);
    }
}
