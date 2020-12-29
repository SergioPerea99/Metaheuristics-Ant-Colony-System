/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr03;

import java.util.ArrayList;
import java.util.HashSet;
import javafx.util.Pair;

/**
 *
 * @author spdlc
 */
public class Hormiga {

    private HashSet<Integer> vSolucion; //Contenedor de la solución de una hormiga.
    private HashSet<Integer> N;
    private double calidad;
    
    public Hormiga(ArchivoDatos _archivo, Random _random){
        
        /*INICIALIZACION DE TODOS LOS ELEMENTOS*/
        N = new HashSet<>(_archivo.getTamMatriz());
        for (int i=0; i < _archivo.getTamMatriz(); i++)
            N.add(i);
        
        vSolucion = new HashSet<>(_archivo.getTamSolucion());
        int inicial = _random.Randint(0, _archivo.getTamMatriz()-1);
        vSolucion.add(inicial); //Añadir un aleatorio entre todos los elementos candidatos
        N.remove(inicial);
        
        calidad = 0.0;
    }
    
    public Hormiga(Hormiga orig){
        /*INICIALIZACION DE TODOS LOS ELEMENTOS*/
        N = new HashSet<>(orig.N);
        vSolucion = new HashSet<>(orig.vSolucion);
        calidad = orig.calidad;
    }
    
    /**
     * 
     * @brief Sumatoria del coste final.
     * @post La suma de todas las distancias de cada uno de los puntos con respecto a los demás puntos.
     */
    public double costeSolucion(float[][] matrizDatos){
        calidad = 0.0;
        ArrayList<Integer> v_M = new ArrayList<>(vSolucion);
        for(int i = 0; i < v_M.size()-1; i++)
            for(int j = i+1; j < v_M.size(); j++){
                if(matrizDatos[v_M.get(i)][v_M.get(j)] != 0)
                    calidad += matrizDatos[v_M.get(i)][v_M.get(j)];
                else
                    calidad += matrizDatos[v_M.get(j)][v_M.get(i)];
            }
        return calidad;
    }
    
    
    /**
     * 
     * @brief Distancias de un elemento respecto a los candidatos.
     * @post Método general para cualquier algoritmo que usemos, ya que se deberá comprobar el coste
     * de un punto respecto a todos los demás de la solución.
     * @param elem Entero correspondiente que se quiere comprobar su distancia con los demás candidatos.
     * @return Suma de las distancias del elemento del parámetro con todos los candidatos.
     */
    protected double distanciasElemento(Integer elem, float[][] matrizDatos){
        
        double sumaDistancias = 0.0;
        
        //El error está dentro del vector Solucion que tiene un elemento = -1
        
        for(Integer i : vSolucion){
            if(matrizDatos[i][elem] != 0)
                    sumaDistancias += matrizDatos[i][elem];
            else
                    sumaDistancias += matrizDatos[elem][i];
        }
        return sumaDistancias;
    }
    
    
    /**
     * @brief Ordenación respecto Aporte/Elemento.
     * @post Ordena el vector de aportes que representa cada elemento de la solución con respecto a los demás.
     * @param v_distancias Contenedor Pair que indica el elemento y su aporte correspondiente.
     */
    public void ordenacionMenorAporte(ArrayList<Pair<Integer,Double>> v_distancias, float[][] matrizDatos){
        v_distancias.clear();
        ArrayList<Integer> v_solucion = new ArrayList<>(vSolucion);
        Pair<Integer,Double> añadir;
        for (int i = 0; i < v_solucion.size(); i++){
            añadir = new Pair<>(v_solucion.get(i),distanciasElemento(v_solucion.get(i), matrizDatos));
            v_distancias.add(añadir);
        }
       v_distancias.sort((o1,o2) -> o1.getValue().compareTo(o2.getValue()));
    } //TODO: ELIMINAR MÉTODO YA QUE ES INNECESARIO EN ESTA PRACTICA
    
    
    /**
     * @return the cromosoma
     */
    public HashSet<Integer> getSolucion() {
        return vSolucion;
    }

    /**
     * @return the n
     */
    public HashSet<Integer> getN() {
        return N;
    }
    
    /**
     * @return the coste
     */
    public double getCalidad() {
        return calidad;
    }
    
}
