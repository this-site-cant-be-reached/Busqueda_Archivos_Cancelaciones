/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busqueda.cancelaciones.clases;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Compumar
 */
public class Archivos {

    private final StringProperty nombreArchivo = new SimpleStringProperty();
    private final StringProperty fechaArchivo = new SimpleStringProperty();
    
    public Archivos(){
          this("","");
         }
    
    public StringProperty nombreArchivoProperty() {
        return nombreArchivo;
    }

    public final String getNombreArchivo(){
       return nombreArchivoProperty().get();
    } 
    
    public final void setNombreArchivo(String nArchivo){
          nombreArchivoProperty().set(nArchivo);
    }
    
    
    public StringProperty fechaArchivoProperty() {
        return fechaArchivo;
    }
    
    public final String getFechaArchivo(){
       return fechaArchivoProperty().get();
    }
    
    public final void setFechaArchivo(String fArchivo){
        fechaArchivoProperty().set(fArchivo);
        
    }
    
   public Archivos(String nombreArchivo, String fechaArchivo){
       setNombreArchivo(nombreArchivo);
       setFechaArchivo(fechaArchivo);
   }
    
    


  
    
}
