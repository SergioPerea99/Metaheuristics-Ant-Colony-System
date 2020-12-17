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
public class Poblacion {
    
    private ArrayList<Hormiga> v_poblacion;

    
    public Poblacion (){
        v_poblacion = new ArrayList<>();
    }
    
    public Poblacion(Random _random, Integer num_individuos,ArchivoDatos _archivo){
        /*INICIALIZACIÓN DE LA POBLACIÓN*/
        v_poblacion = new ArrayList<>();

        while (v_poblacion.size() < num_individuos){
            Hormiga ind = new Hormiga(_archivo,_random);
            v_poblacion.add(ind);
        }
    }

    
    public ArrayList<Pair<Hormiga,Double>> ordenSegunCalidad(){
        Pair<Hormiga,Double> individuo_fitness;
        ArrayList<Pair<Hormiga,Double>> IndividuosFitness = new ArrayList();
        
        for (int i = 0; i < v_poblacion.size(); i++){
            individuo_fitness = new Pair<>(v_poblacion.get(i),v_poblacion.get(i).getCalidad());
            IndividuosFitness.add(individuo_fitness);
        }
       IndividuosFitness.sort((o1,o2) -> o1.getValue().compareTo(o2.getValue()));
       return IndividuosFitness;
    }

    /**
     * @return the v_poblacion
     */
    public ArrayList<Hormiga> getV_poblacion() {
        return v_poblacion;
    }

    /*APARENTEMENTE FUNCIONA: COMRPROBADO*/
    public void reiniciarPoblacion(Random _random,Integer num_elementos){
        int punto;
        for (Hormiga hormiga : v_poblacion) {
            hormiga.getN().addAll(hormiga.getSolucion()); //TODO (HECHO): AL LIMPIAR, TENGO QUE REESTRABLECER LOS ELEMENTOS EN N
            hormiga.getSolucion().clear();
            punto = _random.Randint(0, num_elementos-1);
            hormiga.getSolucion().add(punto);
            hormiga.getN().remove(punto);
            System.out.println(hormiga.getN().size());
        }
    }
    
    public void mostrarPoblacion(){
        getV_poblacion().forEach((individuo) -> {
            System.out.println(individuo.getSolucion());
            System.out.println(individuo.getSolucion().size()+" elementos en la solución de la hormiga --> "+individuo.getCalidad()+" de maximización de la calidad.");
        });
    }
    
    public boolean poblacionCompleta(Integer num_candidatos){
        for (Hormiga hormiga : v_poblacion)
            if (hormiga.getSolucion().size() < num_candidatos)
                return false;
        return true;
    }
}
