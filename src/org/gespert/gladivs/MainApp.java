/*
 * Copyright (C) 2016-2018 Guillermo Espert Carrasquer [gespert@yahoo.es]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gespert.gladivs;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.application.Application;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import org.gespert.gladivs.Instances.KeysListener;
import org.gespert.gladivs.Instances.SettingsInstance;
import org.gespert.gladivs.Instances.SystemTray;
import org.gespert.gladivs.Instances.Windows;
import org.gespert.gladivs.TrayMenu.CheckForUpdates;


public class MainApp extends Application {
    
    public static final String APP_VERSION = "0.7";
    public static final Integer APP_MAJOR_VERSION = 0;
    public static final Integer APP_MINUS_VERSION = 7;
    public static final Integer APP_REVISION_VERSION = 0;
    public static final String APP_NAME = "Gladivs Screen Capture";
    public static final String APP_AUTHOR = "Guillermo Espert Carrasquer";
    
    public MainApp()
    {        
        KeysListener.registerHook();

        KeysListener.addGlobalKeyListener();
    }
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        //Establim la llengua predeterminada
        String languageCode = SettingsInstance.getGeneralSettings().getInterfaceLanguage();
        
        if(languageCode != null && !languageCode.equals("null"))
        {
            Locale.setDefault(new Locale(languageCode));
        }
        
        if(java.awt.SystemTray.isSupported())
        {
            //S'usa per a evitar el tancament de la aplicació si no hi ha cap formulari
            //obert
            Platform.setImplicitExit(false);

            SystemTray.getInstance().displayIconInTray();
            
            if(SettingsInstance.getGeneralSettings().getSearchForAppUpdates())
            {
                CheckForUpdates.checkForUpdates();
            }
        }
        else
        {
            System.out.println("System tray icon is not supported in this system.");
            Windows.getMainWindow().getStage().show();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Esta funció definida en Application es invocada quant l'aplicació rep l'ordre de tancar o finalitzar
     * Finalitza tots els fils de la aplicació.
     */
    @Override
    public void stop()
    {
        KeysListener.unregisterHook();
        SystemTray.getInstance().removeIconFromTray();
        Platform.exit();
        System.exit(1);
    }
}
