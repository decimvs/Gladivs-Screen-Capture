/*
 * Copyright (C) 2018 Guillermo Espert Carrasquer
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
package org.gespert.gladivs.GUI.Controllers;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.gespert.gladivs.GUI.GlobalKeyConsumer;
import org.gespert.gladivs.GUI.ScreenshotOptionsPopup;
import org.gespert.gladivs.GUI.OptionsPopup.ScreenshotOptionsPopupCreator;
import org.gespert.gladivs.Instances.KeysListener;
import org.gespert.gladivs.Instances.SettingsInstance;
import org.gespert.gladivs.Screenshots.CaptureRegion;
import org.gespert.gladivs.Screenshots.MonitorData;
import org.gespert.gladivs.Screenshots.ScreenImage;
import org.jnativehook.keyboard.NativeKeyEvent;

/**
 *
 * @author Guillermo Espert Carrasquer <gespert at yahoo dot es>
 */
public class CaptureRegionWindowController implements ScreenshotOptionsPopup, GlobalKeyConsumer{
    
    private Stage stage;
    private Scene scene;
    private AnchorPane parentPane;
    private Parent root;
    private Double originX, originY;
    private Rectangle selectionArea;
    private boolean isSelAreaDrawed = false;
    private boolean isDrawingSelArea = false;
    private boolean isPopupOptionsShowing = false;
    private Line hLine;
    private Line vLine;
    private ScreenshotOptionsPopupCreator sopPopup;
    private Image screenImage;
    private CaptureRegion captureRegion;
    private BufferedImage bf;
    private boolean setRegion = false;
    private boolean showArea = false;
    private MonitorData monitorData;
    
    /**
     * Crea una nova instància del creador de la finestra, amb una referència
     * a l'objecte de gestió de les Captures de regió. Crea un recuadre de 
     * selecció de tamany 0. Este constructor s'usa quan es vol definir
     * una nova àrea de selecció.
     * 
     * @param cr 
     */
    public CaptureRegionWindowController(CaptureRegion cr)
    {
        this(cr, new Rectangle(0,0,0,0));
    }
    
    /**
     * Crea una nova instància del creador de la finestra, amb una referència a
     * l'objecte de gestió de les Captures de regió. En a finestra s'utilitza el
     * recuadre passat per a dibuixar l'àrea de selecció. Este constructor s'usa
     * quant es vol mostrar una àrea de selecció prèvia.
     * 
     * @param cr
     * @param rec 
     */
    public CaptureRegionWindowController(CaptureRegion cr, Rectangle rec)
    {
        captureRegion = cr;
        
        //Inicialitzar el recuadre de sel·lecció
        selectionArea = rec;
        selectionArea.setFill(Color.rgb(0, 10, 100, 0.3));
        selectionArea.setStroke(Color.BLUEVIOLET);
        selectionArea.setStrokeWidth(2.0);
        
        //Inicialitzar les linies de seguiment del cursor
        hLine = new Line();
        vLine = new Line();
        hLine.setStroke(Color.CORNFLOWERBLUE);
        hLine.setStrokeWidth(1.5);
        vLine.setStroke(Color.CORNFLOWERBLUE);
        vLine.setStrokeWidth(1.5);
    }
    
    /**
     * Estableix una àrea de la pantalla per a ser capturada
     * @param rec 
     */
    public void setCaptureRegion(MonitorData md)
    {
        setRegion = true;
        
        createNewWindow(md);
    }
    
    /**
     * Captura una regió de la pantalla
     * @param rec 
     */
    public void captureRegion(MonitorData md)
    {
        setRegion = false;
        
        createNewWindow(md);
    }
    
    public void showSelectedArea(MonitorData md)
    {
        showArea = true;
        
        createNewWindow(md);
    }
    
    /**
     * Crea una instáncia de la finestra de sel·lecció de regió.
     * Es deu passar un rectangle de monitor que s'usará per a determinar
     * la posició y tamany de la finestra de base i de la posició i tamany
     * de la captura de pantalla.
     * @param rec 
     */
    private void createNewWindow(MonitorData md)
    {
        monitorData = md;

        //Preparar les variables
        stage = new Stage();
        parentPane = new AnchorPane();
        root = parentPane;
        scene = new Scene(root);

        //Configurar la finestra
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setX((double) md.getMonitorRectangle().x);
        stage.setY((double) md.getMonitorRectangle().y);
        stage.setWidth((double) md.getMonitorRectangle().width);
        stage.setHeight((double) md.getMonitorRectangle().height);
        stage.setAlwaysOnTop(true);
        scene.setCursor(Cursor.CROSSHAIR);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/icona_sense_sombra.png")));

        //Configurar les linies de seguiment
        hLine.setStartX(0);
        hLine.setEndX(md.getMonitorRectangle().width);
        vLine.setStartY(0);
        vLine.setEndY(md.getMonitorRectangle().height);

        //Afegir contenedor de imatges, obternir la captura i mostrarla
        bf = md.getScreenImage();
        screenImage = SwingFXUtils.toFXImage(bf, null);
        ImageView imageView = new ImageView(screenImage);
        AnchorPane.setTopAnchor(imageView, 0d);
        AnchorPane.setLeftAnchor(imageView, 0d);
        AnchorPane.setBottomAnchor(imageView, 0d);
        AnchorPane.setRightAnchor(imageView, 0d);
        parentPane.getChildren().addAll(imageView, hLine, vLine);

        //Afegir l'event de tancament de la finestra
        stage.addEventHandler(KeyEvent.KEY_RELEASED, onKeyReleased);
        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressed);
        stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDragged);
        stage.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleased);
        stage.addEventHandler(MouseEvent.MOUSE_MOVED, onMouseMoved);

        //Subscriure esta finestra en el consumidor de events globals de tecles.
        //---S'usa per a capturar el event de la tecla ESC a nivell global, així
        //---es controla que la finestra es puga tancar.
        KeysListener.getGKListener().addKeyConsumer(this);

        //Mostrar la finestra amb la captura
        stage.show();

        //Posicionar les línies en funció del punter del ratolí
        Point point = md.getMousePointer();
        hLine.setStartY(point.getY());
        hLine.setEndY(point.getY());
        vLine.setStartX(point.getX());
        vLine.setEndX(point.getX());

        if(showArea)
        {
            parentPane.getChildren().add(selectionArea);
            showScreenshotOptionsPopup();
        }

        //Assignar el focus al panell de base
        parentPane.requestFocus();
    }
    
    /**
     * Reseteja la selecció realitzada i permet de realitzar una nova selecció.
     */
    public void resetSelection()
    {
        parentPane.getChildren().remove(selectionArea);
        
        selectionArea.setX(0);
        selectionArea.setY(0);
        selectionArea.setWidth(0);
        selectionArea.setHeight(0);
        
        isPopupOptionsShowing = false;
        isDrawingSelArea = false;
        isSelAreaDrawed = false;
        showArea = false;
        
        parentPane.getChildren().addAll(hLine, vLine);
    }
    
    /**
     * Estableix la regió seleccionada com a regió preseleccionada. Esta regió
     * quedarà enmagazemada en els paràmetres i podrà ser recuperada en qualsevol
     * moment.
     */
    public void setSelectedRegion()
    {
        SettingsInstance.getGeneralSettings().setSelectedRegion(monitorData); 
        SettingsInstance.getGeneralSettings().saveSettings();
    }
    
    /**
     * Obre la finestra modal amb les opcions disponibles que es poden prendre
     * sobre l'imatge sel·leccionada
     */
    private void showScreenshotOptionsPopup()
    {
        sopPopup = new ScreenshotOptionsPopupCreator(this, ScreenshotPopupModes.REGION_CAPTURE);
        
        sopPopup.createNewPopup(monitorData);
        
        isPopupOptionsShowing = true;
    }
    
    public boolean saveCaptureToSelectedLocation(File selectedFile)
    {
        ScreenImage croped = new ScreenImage();
        return captureRegion.saveRegion(croped.cropImage(bf, new java.awt.Rectangle((int) selectionArea.getX(), (int) selectionArea.getY(), (int) selectionArea.getWidth(), (int) selectionArea.getHeight())), selectedFile);
    }
    
    /**
     * Retorna la instància del Stage d'esta finestra
     * @return 
     */
    public Stage getStage()
    {
        return stage;
    }
    
    /**
     * Gestiona el moviment de les línies de seguiment en relació al moviment del
     * punter del ratolí.
     */
    public EventHandler<MouseEvent> onMouseMoved = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            hLine.setStartY(event.getY());
            hLine.setEndY(event.getY());
            
            vLine.setStartX(event.getX());
            vLine.setEndX(event.getX());
        }
    };
    
    /**
     * Gestiona el comportament quant es pressiona un botó del ratolí.
     */
    public EventHandler<MouseEvent> onMousePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(event.getButton() == MouseButton.PRIMARY && !isSelAreaDrawed && !showArea)
            {
                originX = event.getX();
                originY = event.getY();
                selectionArea.setX(originX);
                selectionArea.setY(originY);
                
                //Si el selection area no está dins del panel l'incloem
                if(!parentPane.getChildren().contains(selectionArea)) parentPane.getChildren().add(selectionArea);
            }
        }
    };
    
    /**
     * Gestiona el comportament quant está realitzant-se la acció de arrastrar
     * amb el ratolí.
     */
    public EventHandler<MouseEvent> onMouseDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(!isSelAreaDrawed && !showArea)
            {
                selectionArea.setWidth(event.getX() - originX);
                selectionArea.setHeight(event.getY() - originY);
                isDrawingSelArea = true;
            }
        }
    };
    
    /**
     * Gestiona el comportament quan es solta un botó del ratolí.
     */
    public EventHandler<MouseEvent> onMouseReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(isDrawingSelArea && !isPopupOptionsShowing && !showArea)
            {
                isSelAreaDrawed = true;
                parentPane.getChildren().removeAll(hLine, vLine);
                
                monitorData.setAreaRectangle(selectionArea);
                
                showScreenshotOptionsPopup();
            }
        }
    };
    
    /**
     * Gestiona el comportament quant es solta una tecla del teclat.
     */
    public EventHandler<KeyEvent> onKeyReleased = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if(event.getCode() == KeyCode.ESCAPE)
            {
                closeStage();
            }
        }
    };   

    @Override
    public void keyReleasedNofier(NativeKeyEvent nke) {
        if(nke.getKeyCode() == NativeKeyEvent.VC_ESCAPE)
        {
            closeStage();
        }
    }
    
    private void closeStage()
    {
        if(sopPopup != null) sopPopup.closePopupWindow();
        stage.close();
    }
}
