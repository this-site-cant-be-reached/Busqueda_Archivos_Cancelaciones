/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busqueda.cancelaciones.clases;

import busqueda.cancelaciones.vistas.Vista_De_AplicacionController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Compumar
 */
public class ListaArchivosCancelados {

    public static final String NOTOK = "NOTOK";
    public static final String OK = "OK";

    //PONER UNA ESPECIE DE MATRICES PARA ACUMULAR FECHA Y NOMBRE DE ARCHIVO EN CADA INDEX Y ESO LUEGO COMPARARLOS EN UN LOOP EN EL CONTROLLER
    //ESTO SERIA PARA QUE NO SE TENGA QUE REPETIR EL SETEADO DE FECHAS AHI Y EL CODIGO SEA MENOS ENGORROSO Y LABORIOSO Xd
    /**
     *
     */
    public static HashMap<File, String> listadoFechaArchivosNOTOK;
    public static HashMap<File, String> listadoFechaArchivosOK;

    public ArrayList<String> listadoFechasOK = new ArrayList<>();
    public ArrayList<String> listadoFechasNOTOK = new ArrayList<>();

    static String temporal = "";
    static public Path pathCancelados;
    static BasicFileAttributes attrCancelados;

    static Instant instantCancelados;

    public static ArrayList<File> listaFilesNOTOK = new ArrayList<>();
    public static ArrayList<File> listaFilesOK = new ArrayList<>();

    private static String filtroSubstringArchivo;

    private static Vista_De_AplicacionController app_controller;

    //---------FIN DE DECLARACIONES DE VARIABLES-------------- 
    //------------METODOS PARA BUSCAR EN CARPETA DE VIGI-------------
    public static ArrayList<File> getListaFilesNOTOK() {
        return listaFilesNOTOK;
    }

    public static void setNullListaFilesNOTOK() {
        listaFilesNOTOK = new ArrayList<>();
    }

    //--------------
    public static ArrayList<File> getListaFilesOK() {
        return listaFilesOK;
    }

    public static void setNullListaFilesOK() {
        listaFilesOK = new ArrayList<>();
    }


    //OBTENGO Y SETEO MATRICES DE FECHAS CON EL NOMBRE DEL ARCHIVO PARA DESPUES COMPARARLAS Y NO CALCULAR LAS FECHAS EN EL CONTROLLER 
    //-------------METODO PARA ENLISTAR LOS FILES DE LA FOLDER DE VIGI -----------
    public static void listFilesForFolder(final File folder) {

        if (folder.isDirectory() || folder.isFile()) {

            //Loopeo los archivos o directorios que encuentra en directorio de VIGIA
            for (final File fileEntry : folder.listFiles()) {

                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {

                    if (fileEntry.isFile()) {

                        pathCancelados = Paths.get(fileEntry.getAbsolutePath());

                        try {
                            attrCancelados = Files.readAttributes(pathCancelados, BasicFileAttributes.class);
                        } catch (IOException ex) {
                            Logger.getLogger(ListaArchivosCancelados.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        long date = attrCancelados.creationTime().toMillis();

                        instantCancelados = Instant.ofEpochMilli(date);

                        LocalDateTime fecha = LocalDateTime.ofInstant(instantCancelados, ZoneId.systemDefault());

                        String dia = String.valueOf(fecha.getDayOfMonth());
                        String mes = String.valueOf(fecha.getMonthValue());
                        String year = String.valueOf(fecha.getYear());

                        String hora = String.valueOf(fecha.getHour());
                        String minutos = String.valueOf(fecha.getMinute());
                        String segundos = String.valueOf(fecha.getSecond());

                        int numeroDia = fecha.getDayOfMonth();
                        int numeroMes = fecha.getMonthValue();
                        int numeroYear = fecha.getYear();

                        LocalDate archivoDate = LocalDate.of(numeroYear, numeroMes, numeroDia);

                        //int fecha_enNumero_completa = numeroDia + numeroMes + numeroYear;
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

                        app_controller = new Vista_De_AplicacionController();
                        //CREAR UNA HASHTABLE PARA HACER UN ARRAY ASOCIATIVO Y GUARDAR EL NOMBRE DEL ARCHIVO ASOCIADO CON LA FECHA . AMBOS STRINGS
                        //IMPORTANTE PARA DESPUES NO USAR TODO OTRA VEZ EN LA CLASE CONTROLER Y SOLO LOOPEAR COMPARANDO EL HASHTABLE CON LAS POSICIONES Y SETEAR LAS FECHAS DE ESA FORMA XD
                        // ACA UTILIZAR LOS compareTo xD
                        
                        //if general con el compareTo luego de parsear a LocalDate las fechas a comparar por rango xD          
                        
                        //PRUEBA DE LOS COMPARADORES ... SI DA NEGATIVO ES POR QUE EL DE LA IZQUIERDA ES MENOR CON EL QUE LO COMPARA  DE LA DERECHA
                        //SI DA POSITIVO ES POR QUE EL DE LA IZQUIERDA ES MAYOR QUE EL DE LA DERECHA
                        //SI DA 0 ES POR QUE SON IGUALES LOL XD
                        //----------------------  el compareTo() xD ---------------------------
                        System.out.println(app_controller.getFinalDate_1().compareTo(archivoDate));
                         System.out.println(app_controller.getFinalDate_2().compareTo(archivoDate));
                        
                        if ((app_controller.getFinalDate_1().compareTo(archivoDate)) <= 0 && (app_controller.getFinalDate_2().compareTo(archivoDate)) >= 0) {

                            temporal = fileEntry.getName();
                            filtroSubstringArchivo = temporal.substring(0, temporal.lastIndexOf('.')).toUpperCase();
                            //ACORDARSE AQUI QUE SE LE HACE ESTE SUBSSTRING POR QUE QUIERE QUE NO COMPARE EL NOTOK Y OK CON EL NOMBRE DEL APLICATIVO Y EL PROCESO XD
                            filtroSubstringArchivo = filtroSubstringArchivo.substring(9, filtroSubstringArchivo.length());
//                            System.out.println("Nombre del archivo a comparar con not ok y ok: " + filtroSubstringArchivo);

                            if (filtroSubstringArchivo.contains(NOTOK)) {
                                listaFilesNOTOK.add(fileEntry);
                                listadoFechaArchivosNOTOK.put(fileEntry, fecha_completa + " Hora: " + hora + ":" + minutos + ":" + segundos);

                            }

                            if (filtroSubstringArchivo.contains(OK) && !filtroSubstringArchivo.contains(NOTOK)) {
                                listaFilesOK.add(fileEntry);
                                listadoFechaArchivosOK.put(fileEntry, fecha_completa + " Hora: " + hora + ":" + minutos + ":" + segundos);
                                System.out.println("nombre archivos en listado ok : " + fileEntry.getName());

                            }

                        }
                    }
                }

            }

        }
    }

}
