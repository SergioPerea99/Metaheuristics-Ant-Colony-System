/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr03;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author spdlc
 */
public class SalidaDatos implements Runnable {
    /*ATRIBUTOS COMPARTIDOS PARA TODOS LOS ALGORITMOS.*/
    private final Integer semilla;
    private final Integer num_archivo;
    private final Configurador config;
    private final StringBuilder log;
    private final CountDownLatch cdl;
    private final String[] args;

    public SalidaDatos(String[] _args, Integer _num_archivo, CountDownLatch _cdl, Integer _semilla, Configurador _config){
        num_archivo = _num_archivo;
        config = _config;
        cdl = _cdl;
        semilla = _semilla;
        log = new StringBuilder();
        args = _args;
      
    }   
    
    @Override
    public void run() { //Método principal de cada hilo.
        long tiempoInicial, tiempoFinal;
        System.out.println("Archivo "+config.getArchivos().get(num_archivo)+" :: Algoritmo SCH_alfa"+config.getAlfa().toString()+"_beta"+config.getBeta().toString()+" :: Nº_semilla = "+semilla);
        ArrayList<Integer> v_M;

                
        log.append("Archivo "+config.getArchivos().get(num_archivo)+" :: Algoritmo SCH_alfa"+config.getAlfa().toString()+"_beta"+config.getBeta().toString()+" :: Nº_semilla = "+semilla+".\n\n");
        SCH sch = new SCH(args,num_archivo, semilla);
        log.append("ELEMENTO INICIAL DE PARTIDA:\n"+sch.getHormigaElite().getSolucion()+"\nCOSTE INICIAL: "+sch.getHormigaElite().getCalidad()+"\n\n\n");

        //Ejecución de la metaheurística.
        tiempoInicial = System.currentTimeMillis();
        sch.AlgSCH();
        tiempoFinal = System.currentTimeMillis();

        //Finalización de la metahuerística.
        log.append("CONSTRUCCIÓN HACIA LA SOLUCIÓN FINAL:\n");
        for (int i = 0; i < sch.getProcesoMejora().size(); i++){
            log.append("Coste solución = "+sch.getProcesoMejora().get(i).getKey().getCalidad()+" :: Iteración = "+sch.getProcesoMejora().get(i).getValue()+"\n");
            log.append("Vector solución = "+sch.getProcesoMejora().get(i).getKey().getSolucion()+"\n\n");
        }
        log.append("\n SOLUCIÓN FINAL:\n"+sch.getHormigaElite().getSolucion()+".\n\n");
        log.append("COSTE SOLUCIÓN:  "+sch.getHormigaElite().getCalidad()+".\n\n");
        log.append("DURACIÓN: " + (tiempoFinal - tiempoInicial) + " milisegundos :: ITERACIONES: "+sch.getIt()+".\n\n");
        cdl.countDown(); //Para asegurar la finalización del hilo.

    }
    
    public String getLog(){
        return log.toString();
    }
}