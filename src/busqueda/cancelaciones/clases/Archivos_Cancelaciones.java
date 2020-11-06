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
 * @author Mariano
 */
public class Archivos_Cancelaciones {
    
    private final StringProperty nombreArchivoCancelacion = new SimpleStringProperty();
    private final StringProperty fechaArchivoCancelacion = new SimpleStringProperty();
    
    public Archivos_Cancelaciones(){
        this("","");
      }

    public Archivos_Cancelaciones(String nombreArchivoCancelacion , String fechaArchivoCancelacion){
        setNombreArchivoCancelacion(nombreArchivoCancelacion);
        setFechaArchivoCancelacion(fechaArchivoCancelacion);
    }
    
    
    public StringProperty nombreArchivoCancelacionProperty(){
            return nombreArchivoCancelacion;
    }
    
    public final String getNombreArchivoCancelacion(){
           return nombreArchivoCancelacionProperty().get();
    }
    
    public final void setNombreArchivoCancelacion(String nArchivo){
        nombreArchivoCancelacionProperty().set(nArchivo);
    }
    
    //-----------------------------
    
    public StringProperty fechaArchivoCancelacionProperty(){
       return fechaArchivoCancelacion;
    }
    
    public final String getFechaArchivoCancelacion(){
        return fechaArchivoCancelacionProperty().get();
    }
    
    public final void setFechaArchivoCancelacion(String fArchivoCancelacion){
         fechaArchivoCancelacionProperty().set(fArchivoCancelacion);
    }
    
    
    
}
