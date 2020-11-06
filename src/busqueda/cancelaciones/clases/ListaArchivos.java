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
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Compumar
 */
public class ListaArchivos {

    public static final String UTO = "uto";
    public static final String ATI = "ati";
    public static BasicFileAttributes attr; //clase para atributos de un archivo xD

    public static Instant instant;

    public static Path pathC = Paths.get("C:\\archivos");
//    public File folder;
    public static File c_folder = new File("C:/archivos");
    public static File f_folder = new File("F:/archivos_en_f");
    static String temp = "";
    public static ArrayList<File> listFiles = new ArrayList<>();
    //Instancio un objeto para linkear los filtros que obtengo en la primer scene         
    private static Vista_De_AplicacionController vista_aplicacion = new Vista_De_AplicacionController();
//    private static String text_filtro_caracteres = vista_aplicacion.getFiltroCaracteres();
    private static String text_filtro_caracteres;
    //Filtro caracteres
    private static String filtroCaracteres;
    private static String nameFile;

        
   //+++++++++++++++++FIN DE DECLARACIONES DE VARIABLES xD ++++++++++++++++++++++++++ 
    
    
   public void setListFiles(ArrayList<File> lista){
         listFiles = lista;
   }
    
    
//    public static ArrayList<File> listFilesForFolderC(final File folder) {
    public static ArrayList<File> listFilesForFolder(final File folder) {
        
        
        text_filtro_caracteres = new Vista_De_AplicacionController().getFiltroCaracteres();
        
        if (folder.isDirectory() || folder.isFile()) {

//        loopeo los archivos o directorios que encuentra en directorio c 
            for (final File fileEntry : folder.listFiles()) {
//            si lo que encuentra es un directorio guarda el path sumado y vuelve al metodo con parametro de lo que encontró
//            así entra con el folder en el directorio que encontro y chequea dentro si hay archivos o mas directorios        
                if (fileEntry.isDirectory()) {

                    listFilesForFolder(fileEntry);

                } else {
//                si encuentra que es un archivo y no una folder entra 
                    if (fileEntry.isFile()) {

//                    guardo en variable temp para luego asignarle a nameFile para compararlo con el filtro que quiero 
                        temp = fileEntry.getName();

                        //-------------SECCION PARA EL CASE SENSITIVE------------------------------
                        //En caso de que sea sensitivo entrar aca
                        System.out.println("Hola desde ListaArchivos mira el HabilitarSensitive: "+vista_aplicacion.getBooleanCaseSensitive());
                        if (vista_aplicacion.getBooleanCaseSensitive()) {
                            filtroCaracteres = text_filtro_caracteres;
                            nameFile = temp.substring(0, temp.lastIndexOf('.'));
                        }//En caso de que no sea sensitivo entrar aca
                        else {
                            filtroCaracteres = text_filtro_caracteres.toLowerCase();
                            nameFile = temp.substring(0, temp.lastIndexOf('.')).toLowerCase();
                        }
                           System.out.println("QUE TENES FILTRO? : "+text_filtro_caracteres);

                        if (nameFile.contains(filtroCaracteres)) {
                            System.out.println("File= " + folder.getAbsolutePath() + "\\" + temp.substring(0, temp.lastIndexOf('.')));

                            listFiles.add(fileEntry);

                        }

                    }

                }
            }
        }
        
        
        return listFiles;
    }

}
