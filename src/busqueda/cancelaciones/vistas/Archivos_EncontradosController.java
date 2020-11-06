/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busqueda.cancelaciones.vistas;

import busqueda.cancelaciones.clases.Archivos;
import busqueda.cancelaciones.clases.ListaArchivos;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Compumar
 */
public class Archivos_EncontradosController implements Initializable {

    @FXML
    private TableView<Archivos> tableView;

    @FXML
    private TableColumn<Archivos, String> nombreArchivo;

    @FXML
    private TableColumn<Archivos, String> fechaArchivo;

    public static File c_folder = new File("C:/archivos");

    public static BasicFileAttributes attr;

    public static Instant instant;

    public static Path path;

    Desktop desktop = Desktop.getDesktop();

    public File folder;
    @FXML
    private Button id_btn_atras;

    ArrayList<File> listadoDefinitivoArchivos = null;

    Vista_De_AplicacionController vista_aplicacion;

    String caracteres_finales;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<Archivos> data;

        ListaArchivos listitaArchivos = new ListaArchivos();

        //Instancio un objeto para linkear los filtros que obtengo en la primer scene         
        vista_aplicacion = new Vista_De_AplicacionController();

        //linkeo lo que se colocó en el filtro de direccion del browse xD
        String text_browse_direccion = vista_aplicacion.getBrowseTextDireccion();
        folder = new File(text_browse_direccion);

        //INICIALIZO FILTRO DE CARACTERES PARA QUE NO QUEDE CACHE
        System.out.println("HOLA DESDE INITIALIZE Archivos_EncontradosController: " + vista_aplicacion.getFiltroCaracteres());
//        caracteres_finales = vista_aplicacion.getFiltroCaracteres();
        //*************ACA ELIMINO POR SI CAMBIE DE ESCENA CON EL BOTON "ATRAS" Y QUEDA ALGO EN LISTFILES DE ListaArchivos 
        if (ListaArchivos.listFiles != null) {
            for (int i = ListaArchivos.listFiles.size() - 1; i >= 0; i--) {
                ListaArchivos.listFiles.remove(i);
            }
        }
        if (ListaArchivos.listFiles != null) {
            System.out.println("listFiles contiene algo y no es nulo xD");
            System.out.println(ListaArchivos.listFiles.toString());
        }

        //RECORRO Y COMPARO LOS FILTROS CON LO QUE ENCUENTRA EN LAS FOLDERS xD
        listadoDefinitivoArchivos = listitaArchivos.listFilesForFolder(folder);

        //
        data = tableView.getItems();

        for(final File file : listadoDefinitivoArchivos) {
         
//            System.out.println("listado de nombres de archivos: " + index + " : " + file.getName());
            path = Paths.get(file.getAbsolutePath());
            try {
                attr = Files.readAttributes(path, BasicFileAttributes.class);

            } catch (IOException ex) {
                Logger.getLogger(Archivos_EncontradosController.class.getName()).log(Level.SEVERE, null, ex);
            }
            long date = attr.creationTime().toMillis();

            instant = Instant.ofEpochMilli(date);
            LocalDateTime fecha = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//            System.out.println("Hora: "+fecha.getHour()+"\n Minutos: "+fecha.getMinute()+"\n Segundos: "+fecha.getSecond());
            String dia = String.valueOf(fecha.getDayOfMonth());
            String mes = String.valueOf(fecha.getMonthValue());
            String year = String.valueOf(fecha.getYear());

            String hora = String.valueOf(fecha.getHour());
            String minutos = String.valueOf(fecha.getMinute());
            String segundos = String.valueOf(fecha.getSecond());

            if (hora.length() == 1) {
                hora = "0" + hora;
            }
            if (minutos.length() == 1) {
                minutos = "0" + minutos;
            }

            if (segundos.length() == 1) {
                segundos = "0" + segundos;
            }

            String fecha_completa = dia.concat("/").concat(mes).concat("/").concat(year);
            data.add(new Archivos(file.getName(), fecha_completa + " Hora: " + hora + ":" + minutos + ":" + segundos));

        }

        tableView.setRowFactory(tv -> {
            TableRow<Archivos> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Archivos clickedRow = row.getItem();
                    System.out.println(clickedRow.getNombreArchivo());

                    for (final File file : listadoDefinitivoArchivos) {
//                        System.out.println("Nombre de file listado de archivos : "+file.getName().toString());
//                        System.out.println("Nombre de clicked row archivo : "+clickedRow.getNombreArchivo().toString());
                        //IF ENCUENTRA EL ARCHIVO EN LISTADO ENTONCES AL HACER DOBLE CLICK LO ABRE XD
                           if (file.getName().toString().equals(clickedRow.getNombreArchivo().toString())) {

                            try {
                                
                                desktop.open(file);
                            } catch (IOException ex) {
                                Logger.getLogger(Archivos_EncontradosController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                }
            });

            return row;

        });

    }


    @FXML
    private void funcion_btn_atras(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Vista_De_Aplicacion.fxml"));
            Stage stage = (Stage) id_btn_atras.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}
