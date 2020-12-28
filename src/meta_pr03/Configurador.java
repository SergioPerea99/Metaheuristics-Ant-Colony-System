/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author spdlc
 */
public class Configurador {
    private ArrayList<String> archivos;
    private ArrayList<Long> semillas; //Para las diferentes semillas usadas.
    private Integer MAX_ITERACIONES;
    private Integer NUM_HORMIGAS;
    private Float PROB_LRC;
    private ArrayList<Float> cGreedy;
    private Integer alfa;
    private Integer beta;
    private float q0;
    private float phi;
    private float rho;
    private long tiempo_max;
    
    public Configurador(String ruta){
        archivos = new ArrayList<>();
        semillas = new ArrayList<>();
        cGreedy = new ArrayList<>();
        
        String linea;
        FileReader f = null;
        try{
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while((linea=b.readLine())!= null){
                String[] split = linea.split("="); //Dividimos la línea por iguales.
                switch(split[0]){
                    case "Archivos":
                        String[] vArchivos = split[1].split(" "); //Volvemos a dividir por espacios.
                        for (int i = 0; i < vArchivos.length; i++)
                            archivos.add(vArchivos[i]);
                        break;
                      
                    case "Semillas":
                        String[] vSemillas = split[1].split(" "); //Volvemos a dividir por espacios.
                        for (int i = 0; i < vSemillas.length; i++)
                            semillas.add(Long.parseLong(vSemillas[i]));
                        break;
                    
                    case "MaximoIteraciones":
                        MAX_ITERACIONES = Integer.parseInt(split[1]);
                        break;
                        
                    case "Num_Hormigas":
                        NUM_HORMIGAS = Integer.parseInt(split[1]);
                        break;
                    case "Inicializacion_Feromona":
                        String[] vCgreedy = split[1].split(" "); //Volvemos a dividir por espacios.
                        for (int i = 0; i < vCgreedy.length; i++)
                            cGreedy.add(Float.parseFloat(vCgreedy[i]));
                        break;
                    case "PROB_LRC":
                        PROB_LRC = Float.parseFloat(split[1]);
                        break;
                    case "alfa":
                        alfa = Integer.parseInt(split[1]);
                        break;
                    case "beta":
                        beta = Integer.parseInt(split[1]);
                        break;
                    case "q0":
                        q0 = Float.parseFloat(split[1]);
                        break;
                    case "actualizacion_local_feromona":
                        phi = Float.parseFloat(split[1]);
                        break;
                    case "actualizacion_global_feromona":
                        rho = Float.parseFloat(split[1]);
                        break;
                    case "tiempo_maximo":
                        tiempo_max = Long.parseLong(split[1]);
                        break;
                    default:
                        break;
                    
                    //... (AÑADIR CASOS, SI APARECEN MÁS PARÁMETROS).
                }
                
            }
        }catch(Exception e){
            System.out.println(e);
        };
    };
    
    //GETTERS y SETTERS

    /**
     * @return the archivos
     */
    public ArrayList<String> getArchivos() {
        return archivos;
    }

    /**
     * @return the semillas
     */
    public ArrayList<Long> getSemillas() {
        return semillas;
    }

    /**
     * @param archivos the archivos to set
     */
    public void setArchivos(ArrayList<String> archivos) {
        this.archivos = archivos;
    }
    
    /**
     * @param semillas the semillas to set
     */
    public void setSemillas(ArrayList<Long> semillas) {
        this.semillas = semillas;
    }

    /**
     * @return the MAX_ITERACIONES
     */
    public Integer getMAX_ITERACIONES() {
        return MAX_ITERACIONES;
    }

    /**
     * @return the NUM_HORMIGAS
     */
    public Integer getNUM_HORMIGAS() {
        return NUM_HORMIGAS;
    }

    /**
     * @return the cGreedy
     */
    public ArrayList<Float> getcGreedy() {
        return cGreedy;
    }

    /**
     * @return the PROB_LRC
     */
    public Float getPROB_LRC() {
        return PROB_LRC;
    }

    /**
     * @return the alfa
     */
    public Integer getAlfa() {
        return alfa;
    }

    /**
     * @return the beta
     */
    public Integer getBeta() {
        return beta;
    }

    /**
     * @return the q0
     */
    public float getQ0() {
        return q0;
    }
    
    /**
     * 
     * @return the phi
     */
    public float getPhi(){
        return phi;
    }

    /**
     * @return the rho
     */
    public float getRho() {
        return rho;
    }

    /**
     * @return the tiempo_max
     */
    public long getTiempo_max() {
        return tiempo_max;
    }
    
    
}
