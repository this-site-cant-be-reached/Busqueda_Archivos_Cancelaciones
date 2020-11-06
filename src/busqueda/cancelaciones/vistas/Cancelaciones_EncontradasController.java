/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busqueda.cancelaciones.vistas;

import busqueda.cancelaciones.clases.ListaArchivosCancelados;
import busqueda.cancelaciones.clases.Archivos_Cancelaciones;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Compumar
 */
public class Cancelaciones_EncontradasController implements Initializable {

    @FXML
    private AnchorPane id_anchor_pane_cancelaciones;
    @FXML
    private TableView<Archivos_Cancelaciones> id_tableView_cancelaciones_1;
    @FXML
    private TableColumn<Archivos_Cancelaciones, String> id_column_archivos_table_1;
    @FXML
    private TableColumn<Archivos_Cancelaciones, String> id_column_fecha_table_1;
    @FXML
    private TableView<Archivos_Cancelaciones> id_tableView_cancelaciones_2;
    @FXML
    private TableColumn<Archivos_Cancelaciones, String> id_column_archivos_table_2;
    @FXML
    private TableColumn<Archivos_Cancelaciones, String> id_column_fecha_table_2;
    @FXML
    private MenuButton id_btn_aplicaciones;

    @FXML
    private Button id_btn_atras;
    @FXML
    private Label id_label_aplicacion;

    @FXML
    private Label id_clasificacion_oks;

    @FXML
    private Label id_cantidad_archivos_OK;
    @FXML
    private Label id_cantidad_archivos_NOTOK;

    @FXML
    private Button id_btn_buscarAplicativos;

    @FXML
    private AutoCompleteTextField id_campo_texto_buscarAplicativo;

    private List<AutoCompleteTextField> listadoAutoComplete;

    static BasicFileAttributes attrCancelados;

    public String fechaDelHash;

    static Instant instantCancelados;

    static Path pathCancelados;

    Desktop desktopCancelaciones = Desktop.getDesktop();

    public static String validar_aplicacion;

    //ACORDARSE DE MIRAR EL NOMBRE DEL HOST PONIEND IPCONFIG/ALL PARA LA UBICACION DE LA CARPETA EN LA RED
//    public static File folderVIGI = new File("\\\\LaDesktopDeMariano\\Cancelaciones_share\\VIGI\\SYSOUT"); //VIGI
//    public static File folderRRHH = new File("\\\\LaDesktopDeMariano\\Cancelaciones_share\\RRHH\\SYSOUT"); //RRHH
//    public static File folderECRM = new File("\\\\LaDesktopDeMariano\\Cancelaciones_share\\ECRM\\SYSOUT"); //ECRM
//    public static File folderOIRM = new File("\\\\LaDesktopDeMariano\\Cancelaciones_share\\OIRM\\SYSOUT"); //OIRM
//    public static File folderIDRM = new File("\\\\LaDesktopDeMariano\\Cancelaciones_share\\IDRM\\SYSOUT"); //IDRM
    //NOMBRE PARA PRUEBA EN MI MAQUINA LOCAL 
//       public static File folderAplicativos = new File("\\\\LaDesktopDeMariano\\\\Cancelaciones_share");
//    public static File folderVIGI = new File("\\\\fscontrolm\\controlm\\VIGI\\SYSOUT"); //VIGI
//    public static File folderRRHH = new File("\\\\fscontrolm\\controlm\\RRHH\\SYSOUT"); //RRHH
//    public static File folderECRM = new File("\\\\fscontrolm\\controlm\\ECRM\\SYSOUT"); //ECRM
//    public static File folderOIRM = new File("\\\\fscontrolm\\controlm\\OIRM\\SYSOUT"); //OIRM
//    public static File folderIDRM = new File("\\\\fscontrolm\\controlm\\IDRM\\SYSOUT"); //IDRM
//    public static File folderNDRM = new File("\\\\fscontrolm\\controlm\\NDRM\\SYSOUT"); //NDRM
    //LOCACION DE DIRECTORIO COMPARTIDO DONDEESTAN LAS CARPETAS CON NOMBRE DE LOS PROGRAMAS
    public static File folderAplicativos = new File("\\\\fscontrolm\\\\controlm");
    //LISTADO DEFINITIVO PARA DETECTAR LOS CLICKS Y ABRIR ARCHIVOS DE LAS TABLAS
    ArrayList<File> listado_definitivo_OK;
    ArrayList<File> listado_definitivo_NOTOK;
    public static ArrayList<String> listado_folders_aplicativos;

    //FIN DE DECLARACION DE VARIABLES
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id_campo_texto_buscarAplicativo.clear();
        //Creating a graphic (image)
        Image img = new Image("/images/lupa para icono.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(23);

        view.setPreserveRatio(true);
        //Setting a graphic to the button
        id_btn_buscarAplicativos.setGraphic(view);

//        id_btn_aplicaciones = new MenuButton();
        //DE ENTRADA EL LABEL DE LA APLICACION SERA VIGIA QUE ES LA MAS IMPORTANTE
        listado_folders_aplicativos = new ArrayList<>();
        validar_aplicacion = null;

        //Recorro el path donde se encuentran las carpetas de aplicativos para setear un listado de ellos y obtener sus nombres para luego clasificar y generar lo que encuentra en el momento 
        if (folderAplicativos.listFiles() != null) {
            for (final File fileEntry : folderAplicativos.listFiles()) {
                if (fileEntry.isDirectory()) {

                    listado_folders_aplicativos.add(fileEntry.getName().toUpperCase());

                    //EMPEZAR A CREAR MENUITEMS SETEANDO LO NECESARIO PARA CADA UNO CON SU NOMBRE Y LUEGO VOLCARLO EN CADA INDEX DE LOS ITEMS DEL MENUBUTTON
                    MenuItem id_aplicacion = new MenuItem();
                    id_aplicacion.setId("id_item_" + fileEntry.getName().toLowerCase());
                    id_aplicacion.setMnemonicParsing(false);
                    id_aplicacion.setText(fileEntry.getName().toUpperCase());

                    id_aplicacion.setOnAction((event) -> {

                        validar_aplicacion = fileEntry.getName().toUpperCase();
                        setProcesosNOTOK();
                        setProcesosOK();

                    });

                    id_btn_aplicaciones.getItems().add(id_aplicacion);

                }

            }

        }

//       validar la primera aplicacion que encuentre ;
        if (listado_folders_aplicativos.isEmpty() == false) {
            validar_aplicacion = listado_folders_aplicativos.get(0);
            setProcesosNOTOK();
            setProcesosOK();
            detectarClickFilesNOTOK();
            detectarClickFilesOK();

            id_campo_texto_buscarAplicativo.getEntries().addAll(listado_folders_aplicativos);

        }
    }

    public void setProcesosNOTOK() {
        ObservableList<Archivos_Cancelaciones> data;
        listado_definitivo_NOTOK = new ArrayList<>();
        //PONGO EN NULL LAS LISTA DE FILES DE NOTOK Y OK DE LA CLASE ListaArchivosCancelados
        //EN REALIDAD LO QUE HAGO ES INSTANCIAR UN NUEVO ARRAYLIST DE TIPO FILE PARA SACAR EL QUE YA TENIA Y REEMPLAZAR POR UNO NUEVO xD
        ListaArchivosCancelados.setNullListaFilesNOTOK();
        ListaArchivosCancelados.setNullListaFilesOK();

        //Limpio el HASHMAP NOTOK XD
//        if (ListaArchivosCancelados.listadoFechaArchivosNOTOK == null) {
        ListaArchivosCancelados.listadoFechaArchivosNOTOK = new HashMap<>();
//        }
        //Limpio el HASHMAP OK
//        if (ListaArchivosCancelados.listadoFechaArchivosOK == null) {
        ListaArchivosCancelados.listadoFechaArchivosOK = new HashMap<>();
//        }
        //IF encuentra que la tabla tiene items seteados que los limpie asi no se duplican cuando elijo nuevamente la aplicacion
        if (id_tableView_cancelaciones_1.getItems() != null) {
            id_tableView_cancelaciones_1.getItems().clear();
        }
// TODO

        //AL ELEGIR EL MENU ITEM ID CORRESPONDIENTE SETEO EL LABEL DE QUE APLICACION ES 
        //Y HAGO LA VALIDACION PARA SABER QUE APLICACION ES LA QUE SE ELIGIÓ Y ENLISTAR EN BASE A LA FOLDER ELEGIDA DE APLICACION
        id_label_aplicacion.setText("Aplicación: " + validar_aplicacion);
        File folderAplicacion = new File(folderAplicativos + "\\" + validar_aplicacion + "\\SYSOUT");
        ListaArchivosCancelados.listFilesForFolder(folderAplicacion);

        //Uso el metodo para obtener las cancelaciones 
        data = id_tableView_cancelaciones_1.getItems();

        //LOOPEO Y SETEO LA TABLA CON LOS NOTOK ENCONTRADOS
        if (ListaArchivosCancelados.getListaFilesNOTOK() != null) {

            for (final File listadoProcesosNOTOK : ListaArchivosCancelados.getListaFilesNOTOK()) {

                for (HashMap.Entry entry : ListaArchivosCancelados.listadoFechaArchivosNOTOK.entrySet()) {
                    File key = (File) entry.getKey();
                    String fechaValue = (String) entry.getValue();
                    if (listadoProcesosNOTOK.getName().equals(key.getName())) {
                        fechaDelHash = fechaValue;
                        break;
                    }
//                    System.out.println("HashMap : FileName: " + key.getName() + " Fecha de Archivo: " + fechaValue);
                }

                System.out.println("LAS FECHAS COINCIDEN");
                data.add(new Archivos_Cancelaciones(listadoProcesosNOTOK.getName(), fechaDelHash));
//                System.out.println("Nombre del archivo : " + listadoProcesos.getName());
                //ADHIERO LOS ARCHIVOS QUE DIERON NOTOK DE LA APLICACION QUE SEA QUE HAYA ELEGIDO
                listado_definitivo_NOTOK.add(listadoProcesosNOTOK);

            }
        }
        id_cantidad_archivos_NOTOK.setText(listado_definitivo_NOTOK.size() + " Archivos en NOTOK encontrados");
    }

    //ACA EL METODO PARA LOS PROCESOS EN OK QUE TAMBIEN DIERON OK EL MISMO DIA DE LOS ELEGIDOS Y ENLISTARLOS 
    public void setProcesosOK() {

        funcion_cancelados_ok_mismoDia();
        id_cantidad_archivos_OK.setText(listado_definitivo_OK.size() + " Archivos en OK encontrados");

    }

    //FUNCION PARA DETECTAR EL CLICK DEL USUARIO Y ABRIR ARCHIVOS DE LOS NOTOK
    public void detectarClickFilesNOTOK() {

        id_tableView_cancelaciones_1.setRowFactory(tv -> {
            TableRow<Archivos_Cancelaciones> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {

                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

                    Archivos_Cancelaciones clickedRow = row.getItem();

                    System.out.println(clickedRow.getNombreArchivoCancelacion());

                    for (final File file : listado_definitivo_NOTOK) {

//                        System.out.println("Nombre de file listado de archivos : "+file.getName().toString());
//                        System.out.println("Nombre de clicked row archivo : "+clickedRow.getNombreArchivo().toString());
                        if (file.getName().equals(clickedRow.getNombreArchivoCancelacion())) {

                            try {
                                desktopCancelaciones.open(file);
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

    //FUNCION PARA DETECTAR EL CLICK DEL USUARIO Y ABRIR ARCHIVOS DE LOS NOTOK
    public void detectarClickFilesOK() {

        id_tableView_cancelaciones_2.setRowFactory(tv -> {
            TableRow<Archivos_Cancelaciones> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Archivos_Cancelaciones clickedRow = row.getItem();
                    System.out.println(clickedRow.getNombreArchivoCancelacion());

                    for (final File file : listado_definitivo_OK) {
//                        System.out.println("Nombre de file listado de archivos : "+file.getName().toString());
//                        System.out.println("Nombre de clicked row archivo : "+clickedRow.getNombreArchivo().toString());
                        if (file.getName().equals(clickedRow.getNombreArchivoCancelacion())) {

                            try {

                                desktopCancelaciones.open(file);
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

    //FUNCION VOLVER ATRAS 
    @FXML
    private void funcion_atras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Vista_De_Aplicacion.fxml"));
            Stage stage = (Stage) id_btn_atras.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setResizable(false);
            stage.setScene(scene);
//            vista_aplicacion.setBooleanCaseSensitive(false);
            stage.show();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @FXML
    private void funcion_cancelados_ok_mismoDia() {
        id_clasificacion_oks.setText("Los que dieron OK pero en el día NOTOK:");

        ObservableList<Archivos_Cancelaciones> data;
        listado_definitivo_OK = new ArrayList<>();

        //PONGO EN NULL LAS LISTA DE FILES DE NOTOK Y OK DE LA CLASE ListaArchivosCancelados
        //EN REALIDAD LO QUE HAGO ES INSTANCIAR UN NUEVO ARRAYLIST DE TIPO FILE PARA SACAR EL QUE YA TENIA Y REEMPLAZAR POR UNO NUEVO xD
//        ListaArchivosCancelados.setNullListaFilesNOTOK();
//        ListaArchivosCancelados.setNullListaFilesOK();
        //IF encuentra que la tabla tiene items seteados que los limpie asi no se duplican cuando elijo nuevamente la aplicacion
        if (id_tableView_cancelaciones_2.getItems() != null) {
            id_tableView_cancelaciones_2.getItems().clear();
        }

        data = id_tableView_cancelaciones_2.getItems();

        if (ListaArchivosCancelados.getListaFilesOK().isEmpty() == false) {

            for (HashMap.Entry listadoProcesosOK : ListaArchivosCancelados.listadoFechaArchivosOK.entrySet()) {

                boolean flag = false;
                File fileOK = (File) listadoProcesosOK.getKey();

                String fechaFileOK = (String) listadoProcesosOK.getValue();

                for (HashMap.Entry entry : ListaArchivosCancelados.listadoFechaArchivosNOTOK.entrySet()) {

                    File key = (File) entry.getKey();

//                        String fechaValue = (String) entry.getValue();
                    //Y SI DIRECTAMENTE RECORRO EL HASHMAP DE LOS OK Y DIRECTAMENTE LE ADHIERO AL ARRAYLIST listadoProcesosOK el key value(el archivo file) de la lista hashmap?
//                      BUENO ACA ESTA EL PROBLEMA DE QUE SETEA A LOS ARCHIVOS CON LA FECHA DEL HASHMAP CON FECHAS DE LOS NOT OK
//                      CORREGIR ESTO YA QUE SETEA A LOS ARCHIVOS CON LAS FECHAS DE LOS NOTOK    
                    if ((fileOK.getName().substring(5, 9)).equals(key.getName().substring(5, 9))) {
                        System.out.println("Bueenissss");
                        fechaDelHash = fechaFileOK;
                        flag = true;
                        break;
                    }
//                        System.out.println("HashMap : FileName: " + key.getName() + " Fecha de Archivo: " + fechaValue);
                }

//                    if (Vista_De_AplicacionController.getFechaSeleccionada().equals(fecha_completa)) {
                if (flag == true) {
                    data.add(new Archivos_Cancelaciones(fileOK.getName(), fechaDelHash));
//                    System.out.println("Fecha");
//                    System.out.println("Nombre del archivo : " + listadoProcesosOK.getName());
                    //ADHIERO LOS ARCHIVOS QUE DIERON OK DE LA APLICACION QUE SEA QUE HAYA ELEGIDO
                    listado_definitivo_OK.add(fileOK);
                }

            }

        }
        id_cantidad_archivos_OK.setText(listado_definitivo_OK.size() + " Archivos en OK encontrados");
    }

    @FXML
    private void funcion_mostrar_todos_procesosOK() {
        id_clasificacion_oks.setText("Todos los que dieron OK:");

        ObservableList<Archivos_Cancelaciones> data;
        listado_definitivo_OK = new ArrayList<>();

        //PONGO EN NULL LAS LISTA DE FILES DE NOTOK Y OK DE LA CLASE ListaArchivosCancelados
        //EN REALIDAD LO QUE HAGO ES INSTANCIAR UN NUEVO ARRAYLIST DE TIPO FILE PARA SACAR EL QUE YA TENIA Y REEMPLAZAR POR UNO NUEVO xD
        //IF encuentra que la tabla tiene items seteados que los limpie asi no se duplican cuando elijo nuevamente la aplicacion
        if (id_tableView_cancelaciones_2.getItems() != null) {
            id_tableView_cancelaciones_2.getItems().clear();
        }

        data = id_tableView_cancelaciones_2.getItems();

        if (ListaArchivosCancelados.getListaFilesOK().isEmpty() == false) {

            for (HashMap.Entry listadoProcesosOK : ListaArchivosCancelados.listadoFechaArchivosOK.entrySet()) {

                File fileOK = (File) listadoProcesosOK.getKey();

                String fechaFileOK = (String) listadoProcesosOK.getValue();

                System.out.println("Bueenissss");
                System.out.println("nombre de archivo hashok : " + fileOK.getName());
                fechaDelHash = fechaFileOK;

                data.add(new Archivos_Cancelaciones(fileOK.getName(), fechaDelHash));

                //ADHIERO LOS ARCHIVOS QUE DIERON OK DE LA APLICACION QUE SEA QUE HAYA ELEGIDO
                listado_definitivo_OK.add(fileOK);

            }

        }
        id_cantidad_archivos_OK.setText(listado_definitivo_OK.size() + " Archivos en OK encontrados");
    }

    //ESTA FUNCION PODRIA REALIZARSE AL COMIENZO ES DECIR AL INITIALIZE Y QUE SE SETEE UN HASHMAP DINAMICO
    //ESTA MEJORA SERIA PARA NO TENER QUE LOOPEAR Y VALIDAR EL CONTENIDO CADA VEZ QUE SE APRETA EL BOTON POR CADA APLICATIVO ELEGIDO
    //TRATAR DE IMPLEMENTAR LUEGO ESTO MENCIONADO
    @FXML
    private void funcion_losOK_pero_con_error(ActionEvent event) {
        id_clasificacion_oks.setText("Los que dieron OK pero con Errores adentro:");

        ObservableList<Archivos_Cancelaciones> data;
        listado_definitivo_OK = new ArrayList<>();

        //PONGO EN NULL LAS LISTA DE FILES DE NOTOK Y OK DE LA CLASE ListaArchivosCancelados
        //EN REALIDAD LO QUE HAGO ES INSTANCIAR UN NUEVO ARRAYLIST DE TIPO FILE PARA SACAR EL QUE YA TENIA Y REEMPLAZAR POR UNO NUEVO xD
        //IF encuentra que la tabla tiene items seteados que los limpie asi no se duplican cuando elijo nuevamente la aplicacion
        if (id_tableView_cancelaciones_2.getItems() != null) {
            id_tableView_cancelaciones_2.getItems().clear();
        }

        data = id_tableView_cancelaciones_2.getItems();

        if (ListaArchivosCancelados.getListaFilesOK().isEmpty() == false) {

            for (HashMap.Entry listadoProcesosOK : ListaArchivosCancelados.listadoFechaArchivosOK.entrySet()) {

                File fileOK = (File) listadoProcesosOK.getKey();

                String fechaFileOK = (String) listadoProcesosOK.getValue();

                //SI ENCUENTRA QUE ES UN ARCHIVO TXT ENTONCES ENTRA
                if ((fileOK.getName().substring(fileOK.getName().lastIndexOf('.') + 1, fileOK.getName().length()).toLowerCase()).equals("txt")) {
                    //SI ENCUENTRA DENTRO DE LA FUNCION QUE EL ARCHIVO Q LOOPEA CONTIENE ERROR Y END ERROR SETEA LA TABLA
                    if (leerArchivos(fileOK) == true) {

                        System.out.println("Bueenissss");
                        System.out.println("nombre de archivo hashok : " + fileOK.getName());
                        fechaDelHash = fechaFileOK;

                        data.add(new Archivos_Cancelaciones(fileOK.getName(), fechaDelHash));

                        //ADHIERO LOS ARCHIVOS QUE DIERON OK DE LA APLICACION QUE SEA QUE HAYA ELEGIDO
                        listado_definitivo_OK.add(fileOK);

                    }

                }

            }

        }
        id_cantidad_archivos_OK.setText(listado_definitivo_OK.size() + " Archivos en OK encontrados");
    }

    public boolean leerArchivos(File file) {

        String line = null;
        boolean archivo_OK_ConError = false;
        boolean contieneError = false;
        boolean contieneErrorEnd = false;

        try {
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                line = line.toLowerCase();

                if (line.contains("error")) {

                    contieneError = true;
                }
                if (line.contains("end error")) {

                    contieneErrorEnd = true;
                }

            }
            bufferedReader.close();
            if (contieneError == true && contieneErrorEnd == true) {
                archivo_OK_ConError = true;
            } else {
                archivo_OK_ConError = false;
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return archivo_OK_ConError;
    }

    @FXML
    private void funcion_boton_buscarAplicativos(ActionEvent event) {
        boolean contiene = false;
        if (listado_folders_aplicativos.isEmpty() == false) {
            for (String listado : listado_folders_aplicativos) {
                if (id_campo_texto_buscarAplicativo.getText().toUpperCase().equals(listado)) {

                    validar_aplicacion = id_campo_texto_buscarAplicativo.getText().toUpperCase();
                    contiene = true;
                }

            }
            if (contiene) {
                setProcesosNOTOK();
                setProcesosOK();
            }

        }

    }

}
