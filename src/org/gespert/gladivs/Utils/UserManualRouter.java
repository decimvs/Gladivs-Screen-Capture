package org.gespert.gladivs.Utils;

import javafx.scene.control.Alert;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

public class UserManualRouter {

    public static void showOnlineHelpInNavigator()
    {
        String helpLanguage;
        String defaultLanguage = Locale.getDefault().getLanguage();

        if(defaultLanguage != null)
        {
            if(defaultLanguage.equals("es") || defaultLanguage.equals("en"))
            {
                helpLanguage = defaultLanguage;
            }
            else
            {
                helpLanguage = "en";
            }
        }
        else
        {
            helpLanguage = "en";
        }

        String fullUrl = "https://gladivs.com/" + helpLanguage + "/manual/gladivssc-0-7";

        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URL(fullUrl).toURI());
            } catch (IOException | URISyntaxException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("An error was occurred when trying to show the help in the web browser.\n You can see the help online at https://gladivs.com.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("It is not possible to show you the help in your web browser because your system not permits that.\n You can see the help online at https://gladivs.com.");
            alert.show();
        }
    }

}
