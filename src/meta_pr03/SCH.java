/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr03;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author spdlc
 */
public class SCH {
    /*ATRIBUTOS PARA LA CARGA DE FICHEROS*/
    private final Configurador config;
    private final ArchivoDatos archivo;
    private final Random random;
    
    /*ATRIBUTOS ESPECÍFICOS DEL SCH*/
    Poblacion poblacion; /*Población de hormigas*/
    private float feromonas[][]; /*Matriz de feromonas o matriz de información*/
    
    public SCH(String[] _args,Integer num_archivo, Integer sem){
        config = new Configurador(_args[0]);
        archivo = new ArchivoDatos(config.getArchivos().get(num_archivo));
        random = new Random(config.getSemillas().get(sem));
        
        /*INICIALIZACIÓN DE MATRIZ DE FEROMONAS (LA MATRIZ HEURÍSTICA ES LA MATRIZ DE DISTANCIAS CONTENIDA EN EL ARCHIVO)*/
        feromonas = new float[archivo.getTamMatriz()][archivo.getTamMatriz()];
        for (float[] feromona : feromonas)
            for (int j = 0; j < feromonas.length; j++) 
                feromona[j] = config.getcGreedy().get(num_archivo); 
        
        /*INICIALIZACIÓN DE LA POBLACIÓN DE HORMIGAS*/
        poblacion = new Poblacion(random,config.getNUM_HORMIGAS(), archivo);
        
    }
    
    public void AlgSCH(){
        int it = 0;
        
        while(it < config.getMAX_ITERACIONES()){
            while(!poblacion.poblacionCompleta(archivo.getTamSolucion())){
               /*NO ESTÁN LAS HORMIGAS COMPLETAS => AÑADIR A TODAS UN ELEMENTO Y DESPUÉS ACTUALIZACIÓN LOCAL*/
                for (Hormiga hormiga : poblacion.getV_poblacion()) {
                    /*POR CADA HORMIGA SACAMOS LAS DISTANCIAS DE TODOS LOS ELEMENTOS RESPECTO A LOS YA METIDOS EN EL VECTOR DE LA HORMIGA*/
                    ArrayList<Integer> LRC = dist_hormiga(hormiga);
                    
                    //CONTINUAR POR AQUÍ...QUEDA APLICAR LA REGLA DE TRANSICION A LA LRC DE LA HORMIGA
                }
                /*YA SE HA AÑADIDO 1 ELEMENTO NUEVO A CADA UNA DE LAS HORMIGAS -> ACTUALIZACIÓN LOCAL*/
            }
            /*YA SE HA COMPLETADO LA POBLACION DE HORMIGAS -> ACTUALIZACIÓN GLOBAL*/
            System.out.println(it);
            it += poblacion.getV_poblacion().size(); //DUDA: SE SUMAN 10 (HORMIGAS) O SE SUMA DE UNO EN UNO
            poblacion.reiniciarPoblacion(random, archivo.getTamMatriz());
        }
        
        
    }
    
    
    private ArrayList<Integer> dist_hormiga(Hormiga hormiga){
        ArrayList<Integer> LRC = new ArrayList<>();
        ArrayList<Pair<Integer,Double>> aportes = new ArrayList<>();
        
        /*CALCULAMOS TODOS LOS APORTES DE LOS ELEMENTOS EN N POSIBLES A SER PARTE DE LA SOLUCIÓN*/
        for (Integer elem : hormiga.getN())
            aportes.add(new Pair(elem, hormiga.distanciasElemento(elem, archivo.getMatrizDatos())));   
        aportes.sort((o1,o2) -> o1.getValue().compareTo(o2.getValue()));
        
        /*AÑADIMOS AL CONTENEDOR LRC TODOS LOS ELEMENTOS QUE SUPERAN PORCENTUALMENTE EL COSTE DE APORTE RESPECTO A LOS ELEMENTOS DE LA SOLUCIÓN*/
        for(int i = (int)(config.getPROB_LRC()*aportes.size()); i < aportes.size(); i++)
            LRC.add(aportes.get(i).getKey());
        
        return LRC;
    }
    
}
