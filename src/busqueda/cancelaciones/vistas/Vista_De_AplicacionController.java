/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busqueda.cancelaciones.vistas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Compumar
 */
public class Vista_De_AplicacionController implements Initializable {

    @FXML
    private AnchorPane anchorId; //para hacerle referencia al stage xD

//    Variables del fxml de la primera seccion de buscar archivos
    @FXML
    private Button browse_btn;

    @FXML
    private RadioButton id_radiobtn_1;

    @FXML
    private TextField id_filtro_caracteres;

    @FXML
    private Button id_btn_buscar_archivos;

    @FXML
    private TextField id_browse_text_direccion;

    //    Variables del fxml de la segunda seccion de buscar archivos
    @FXML
    private DatePicker datePicker;

    @FXML
    private RadioButton id_radiobtn_2;

    @FXML
    private Button id_btn_cancelaciones;
    @FXML
    private CheckBox id_checkBox_caseSensitive;

    //--------------------------------------
    //Declaraciones para obtener los filtros buscar archivos a linkear en la otra scene 
    private static String text_browse;
    private static String filtro_caracteres;

    //Booleano para Habilitar o Deshabilitar Case Sensitive
    private static boolean habilitar_case_sensitive;
    //Declaracion para obtener el filtro del datePicker
    private static String fecha_completa;

    private static String fecha_completa_date2;

    private boolean fecha_fue_seleccionada;

    private static int final_day_date1;
    private static int final_mes_date1;
    private static int final_year_date1;

    private static int final_day_date2;
    private static int final_mes_date2;
    private static int final_year_date2;

    private static LocalDate finalDatePicker_1;
    private static LocalDate finalDatePicker_2;

//----------------------------------------------------------
    @FXML
    private DatePicker datePicker_2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        finalDatePicker_1 = null;
        finalDatePicker_2 = null;
        fecha_completa_date2 = null;
        fecha_completa = null;
        //Seteo las cosas por si se carga de la otra escena con boton de atras
        datePicker.setValue(null);
        datePicker_2.setValue(null);
        fecha_fue_seleccionada = false;

        habilitar_case_sensitive = false;

        System.out.println("habilitarSensitive en Initialize VistaAplicacion: " + habilitar_case_sensitive);

        id_checkBox_caseSensitive.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {

            System.out.println("VALOR DEL BOOLEANO HABILITARCASESENSITIVE: " + newValue);
            habilitar_case_sensitive = newValue;
        });

//        Primera seccion donde empieza lo que se habilita y deshabilita
        id_btn_buscar_archivos.setDisable(true);

        id_radiobtn_1.setSelected(true);

//         Segunda seccion donde empieza lo que se habilita y deshabilita
        datePicker.setDisable(true);
        datePicker_2.setDisable(true);
        id_btn_cancelaciones.setDisable(true);

    }

//1)  *****************  Funciones de la primera sección *******************
    @FXML
    private void funcion_radiobtn_1(ActionEvent event) {

        if (id_radiobtn_2.isSelected() || id_radiobtn_1.isSelected() == false) {
            System.out.println("Seleccionaste radio button 1 xD");
            id_radiobtn_1.setSelected(true);
            id_radiobtn_2.setSelected(false);

//            Primera seccion de botones para habilitar 
            browse_btn.setDisable(false);
            id_browse_text_direccion.setDisable(false);
            id_filtro_caracteres.setDisable(false);
            id_checkBox_caseSensitive.setDisable(false);

//            Segunda seccion de botones a deshabilitar
            datePicker.setDisable(true);
            datePicker_2.setDisable(true);
            id_btn_cancelaciones.setDisable(true);

        }

    }

    //    funcion que se activa con el boton  browse
    @FXML
    private void browse_function(ActionEvent event) {

        boolean disable = true;
        final DirectoryChooser directoryChooser = new DirectoryChooser();
//        crea una nueva stage en base a la escena del anchorpane que tiene id
        Stage stage = (Stage) anchorId.getScene().getWindow();

//        lo asigno lo que muestra el show dialog a la variable file
        File file = directoryChooser.showDialog(stage);

//        si lo que se le asigna no es nulo entonces entrar
        if (file != null) {
            System.out.println("Path : " + file.getAbsolutePath());
//            setear como texto lo que encuentra en el path completo del archivo
            id_browse_text_direccion.setText(file.getAbsolutePath());

            if (on_input_text_changed()) {
                disable = false;
            }

            id_btn_buscar_archivos.setDisable(disable);

        }

    }

    @FXML
    private void keyReleaseProperty(KeyEvent event) {

        boolean isDisabled = true;

        if (on_input_text_changed()) {
            isDisabled = false;

            System.out.println(isDisabled);
        } else {
            System.out.println(isDisabled);
        }

        id_btn_buscar_archivos.setDisable(isDisabled);

    }

//   ********** La siguiente explicación es lo que realicé para reemplazar el addEventListener xD *****************
//    Esta función se linkea con 3 funciones : keyReleaseProperty , browse_function y key_release_browse 
//    Tener en cuenta que en las 3 funciones mencionadas se deshabilita o habilita el id_btn_buscar_archivos 
//    Este es el botón BUSCAR ARCHIVOS que para que esté habilitado se tiene que cumplir que haya por lo menos: 
//    1) 3 caracteres en el id_browse_text_direccion que sería la dirección de destino de la carpeta
//    2) 1 caracter como mínimo en el id_filtro_caracteres que sería el campo de texto donde se escribe el filtro de búsqueda
//    Tener en cuenta que las funciones keyRelease tanto para el textfield browse como el de filtro están por si se borra o escribe algo 
//            y el boton queda habilitado sin controlarse 
    private boolean on_input_text_changed() {

        String text_browse = id_browse_text_direccion.getText();
        String text_filtro = id_filtro_caracteres.getText();
        boolean isPath = false;

        if ((text_browse != null || text_browse.trim() != null) && text_browse.length() >= 3) {
            if ((text_filtro != null || text_filtro.trim() != null) && text_filtro.length() >= 1) {
                isPath = true;
            }
        }

        return isPath;
    }

    @FXML
    private void key_release_browse(KeyEvent event) {

        boolean isPath2 = true;

        if (on_input_text_changed()) {
            isPath2 = false;
        }

        id_btn_buscar_archivos.setDisable(isPath2);
    }
    // Funciones para obtener valor de los filtros 
    //Se usarán en ListaArchivos para comparar lo que encuentra

    public String getBrowseTextDireccion() {
        return text_browse;
    }

    public String getFiltroCaracteres() {
        return filtro_caracteres;
    }
//-------------------------------------------------------

    @FXML
    private void funcion_buscar_archivos(ActionEvent event) {

//      Obtengo lo que se llenó en el campo del path a buscar y el filtro de palabras para encontrar archivos 
        text_browse = id_browse_text_direccion.getText();
        filtro_caracteres = id_filtro_caracteres.getText();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Archivos_Encontrados.fxml"));
            Stage stage = (Stage) id_btn_buscar_archivos.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setResizable(true);
            stage.setScene(scene);
            stage.show();
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    //2)  *****************  Funciones de la segunda sección *******************   
    @FXML
    private void buscarCancelaciones(ActionEvent event) {

        //Obtengo la fecha escogida del datePicker 
        if (datePicker.getValue() != null && datePicker_2.getValue() != null) {

            int day_date1 = datePicker.getValue().getDayOfMonth();
            int mes_date1 = datePicker.getValue().getMonthValue();
            int year_date1 = datePicker.getValue().getYear();

            int day_date2 = datePicker_2.getValue().getDayOfMonth();
            int mes_date2 = datePicker_2.getValue().getMonthValue();
            int year_date2 = datePicker_2.getValue().getYear();

            if (year_date1 > year_date2) {

                String mensaje = "La fecha limite \"Hasta\" es menor que la fecha minima \"Desde\"\nLa fecha \"Desde\": "
                        + day_date1 + "/" + mes_date1 + "/" + year_date1 + " es mayor que la fecha \"Hasta\": " + day_date2 + "/" + mes_date2 + "/" + year_date2;
                new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).show();

            } else if (year_date1 == year_date2 && mes_date1 > mes_date2) {

                String mensaje = "La fecha limite \"Hasta\" es menor que la fecha minima \"Desde\"\nLa fecha \"Desde\": "
                        + day_date1 + "/" + mes_date1 + "/" + year_date1 + " es mayor que la fecha \"Hasta\": " + day_date2 + "/" + mes_date2 + "/" + year_date2;
                new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).show();
                System.out.println("Fecha 1: " + year_date1 + "/" + mes_date1 + "/" + day_date1 + "\nFecha 2: " + year_date2 + "/" + mes_date2 + "/" + day_date2);

            } else if (year_date1 == year_date2 && mes_date1 == mes_date2 && day_date1 > day_date2) {

                String mensaje = "La fecha limite \"Hasta\" es menor que la fecha minima \"Desde\"\nLa fecha \"Desde\": "
                        + day_date1 + "/" + mes_date1 + "/" + year_date1 + " es mayor que la fecha \"Hasta\": " + day_date2 + "/" + mes_date2 + "/" + year_date2;
                System.out.println("Fecha 1: " + year_date1 + "/" + mes_date1 + "/" + day_date1 + "\nFecha 2: " + year_date2 + "/" + mes_date2 + "/" + day_date2);

                new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).show();
            } else {
                fecha_fue_seleccionada = true;

                final_day_date1 = day_date1;
                final_day_date2 = day_date2;
                final_mes_date1 = mes_date1;
                final_mes_date2 = mes_date2;
                final_year_date1 = year_date1;
                final_year_date2 = year_date2;

                finalDatePicker_1 = LocalDate.of(year_date1, mes_date1, day_date1);
                finalDatePicker_2 = LocalDate.of(year_date2, mes_date2, day_date2);
                System.out.println("HOOLLAAAAAA: " + finalDatePicker_1);
            }

        }

        if (fecha_fue_seleccionada) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Cancelaciones_Encontradas.fxml"));
                Stage stage = (Stage) id_btn_cancelaciones.getScene().getWindow();
                Scene scene = new Scene(loader.load());
                stage.setResizable(true);
                stage.setScene(scene);
                stage.show();
            } catch (IOException io) {
                io.printStackTrace();
            }

        } else {
            String mensaje = "Elija un rango de fechas xD";
            new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).show();

        }

    }

    //Con estos 2 metodos luego comparare con el famoso compareTo xD en lista archivos cancelados
    public LocalDate getFinalDate_1() {
        return finalDatePicker_1;
    }

    public LocalDate getFinalDate_2() {
        return finalDatePicker_2;
    }

    public int getDiaPicker1() {
        return final_day_date1;
    }

    public int getDiaPicker2() {
        return final_day_date2;
    }

    public int getMesPicker1() {
        return final_mes_date1;
    }

    public int getMesPicker2() {
        return final_mes_date2;
    }

    public int getYearPicker1() {
        return final_year_date1;
    }

    public int getYearPicker2() {
        return final_year_date2;
    }

    @FXML
    private void funcion_radiobtn_2(ActionEvent event) {

        if (id_radiobtn_1.isSelected() || id_radiobtn_2.isSelected() == false) {
            System.out.println("Seleccionaste radio button 2 xD");
            id_radiobtn_1.setSelected(false);
            id_radiobtn_2.setSelected(true);

//            Segunda seccion para habilitar
            datePicker.setDisable(false);

//            datePicker_2.setDisable(false);
            id_btn_cancelaciones.setDisable(false);

//            Primera seccion de botones a deshabilitar
            id_checkBox_caseSensitive.setDisable(true);
            browse_btn.setDisable(true);
            id_browse_text_direccion.setDisable(true);
            id_filtro_caracteres.setDisable(true);
            id_btn_buscar_archivos.setDisable(true);

            fecha_fue_seleccionada = false;

        }
//

    }

    public boolean getBooleanCaseSensitive() {
        return habilitar_case_sensitive;
    }

    public void setBooleanCaseSensitive(boolean caseSensitive) {
        habilitar_case_sensitive = caseSensitive;
    }

    @FXML
    private void habilitar_caseSensitive(ActionEvent event) {

        id_checkBox_caseSensitive.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            //                id_checkBox_caseSensitive.setSelected(!newValue);
            System.out.println("VALOR DEL BOOLEANO HABILITARCASESENSITIVE: " + newValue);
            habilitar_case_sensitive = newValue;
        });

    }

    @FXML
    private void funcion_onAction_datePicker(ActionEvent event) {

        if (datePicker.getValue() != null) {
            datePicker_2.setDisable(false);
            DatePicker minDate = new DatePicker();
            minDate.setValue(datePicker.getValue());

            final Callback<DatePicker, DateCell> dayCellFactory;

            dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item.isBefore(minDate.getValue())) { //Disable all dates after required date
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;"); //To set background on different color

                    }

                }
            };
            //Finally, we just need to update our DatePicker cell factory as follow:
            datePicker_2.setDayCellFactory(dayCellFactory);

            System.out.println("Putooooooooooopooooo");
        } else {
            datePicker_2.setValue(null);
            datePicker_2.setDisable(true);
        }
    }

//
    @FXML
    private void funcion_onAction_datePicker_2(ActionEvent event) {

        if (datePicker_2.getValue() != null) {

            int day_date1 = datePicker.getValue().getDayOfMonth();
            int mes_date1 = datePicker.getValue().getMonthValue();
            int year_date1 = datePicker.getValue().getYear();

            int day_date2 = datePicker_2.getValue().getDayOfMonth();
            int mes_date2 = datePicker_2.getValue().getMonthValue();
            int year_date2 = datePicker_2.getValue().getYear();

            if (year_date1 > year_date2) {

                System.out.println("puta que lo re pario");
                datePicker_2.setValue(null);

                String mensaje = "La fecha limite \"Hasta\" es menor que la fecha minima \"Desde\"\nLa fecha \"Desde\": "
                        + day_date1 + "/" + mes_date1 + "/" + year_date1 + " es mayor que la fecha \"Hasta\": " + day_date2 + "/" + mes_date2 + "/" + year_date2;

                new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).show();

            } else if (year_date1 == year_date2 && mes_date1 > mes_date2) {

                System.out.println("puta que lo re pario");
                datePicker_2.setValue(null);

                String mensaje = "La fecha limite \"Hasta\" es menor que la fecha minima \"Desde\"\nLa fecha \"Desde\": "
                        + day_date1 + "/" + mes_date1 + "/" + year_date1 + " es mayor que la fecha \"Hasta\": " + day_date2 + "/" + mes_date2 + "/" + year_date2;

                new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).show();

            } else if (year_date1 == year_date2 && mes_date1 == mes_date2 && day_date1 > day_date2) {

                System.out.println("puta que lo re pario");
                datePicker_2.setValue(null);
                String mensaje = "La fecha limite \"Hasta\" es menor que la fecha minima \"Desde\"\nLa fecha \"Desde\": "
                        + day_date1 + "/" + mes_date1 + "/" + year_date1 + " es mayor que la fecha \"Hasta\": " + day_date2 + "/" + mes_date2 + "/" + year_date2;

                new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK).show();
            }

        }

    }

}
