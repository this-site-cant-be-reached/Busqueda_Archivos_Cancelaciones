/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busqueda.cancelaciones;

import busqueda.cancelaciones.clases.*;
import busqueda.cancelaciones.vistas.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Compumar
 */
public class Main extends Application {
    
    private Stage primaryStage;
    private AnchorPane rootLayout;
  
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Aplicación Para Búsqueda de Archivos .txt");

       

        initRootLayout();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            
            loader.setLocation(Main.class.getResource("vistas/Vista_De_Aplicacion.fxml"));
            
            rootLayout = (AnchorPane)loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
                
            primaryStage.setScene(scene); 
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
