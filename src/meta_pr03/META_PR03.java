/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr03;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author spdlc
 */
public class META_PR03 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Configurador config = new Configurador(args[0]);
        System.out.println(config.getArchivos());
        ArrayList<ArchivoDatos> archivos = new ArrayList<>();
        for (int i = 0; i < config.getArchivos().size(); i++) {
            archivos.add(new ArchivoDatos(config.getArchivos().get(i)));
        }
        
        
        ExecutorService ejecutor = Executors.newCachedThreadPool();
        try{
            for(int j = 0; j < archivos.size(); j++){
                CountDownLatch cdl = new CountDownLatch(config.getSemillas().size());
                ArrayList<SalidaDatos> m = new ArrayList<>();
                SalidaDatos salida;
                for(int k = 0; k < config.getSemillas().size(); k++){
                    salida = new SalidaDatos(args, j, cdl, k, config);
                    m.add(salida);
                    ejecutor.execute(salida);
                }
                cdl.await();
                for(int k = 0; k < m.size(); k++){
                    guardaArchivo("log/SCH"+config.getAlfa()+"A"+config.getBeta()+"B/"+archivos.get(j).getNombre()+"_"+config.getSemillas().get(k)+".txt",m.get(k).getLog());
                }
            }
             
        } catch (InterruptedException ex) {
            Logger.getLogger(META_PR03.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void guardaArchivo(String ruta,String texto){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try{
            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);
            
            pw.print(texto);
            
        }catch(IOException e1){
        } finally {
            try{
                if(fichero != null)
                    fichero.close();
            }catch(IOException e2){
            }
        }
    }
    
    
}
