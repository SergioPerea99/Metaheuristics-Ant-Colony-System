/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr03;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Vector;
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
    private final float feromonas[][]; /*Matriz de feromonas o matriz de información*/
    private Hormiga hormigaElite;
    private double costeElite;
    
    /*ATRIBUTOS EXTRA*/
    private ArrayList<Pair<Hormiga,Integer>> procesoMejora;
    private Integer it;
    private final int num_archivo;
    
    public SCH(String[] _args,Integer _num_archivo, Integer sem){
        config = new Configurador(_args[0]);
        archivo = new ArchivoDatos(config.getArchivos().get(_num_archivo));
        random = new Random(config.getSemillas().get(sem));
        
        
        /*INICIALIZACIÓN DE MATRIZ DE FEROMONAS (LA MATRIZ HEURÍSTICA ES LA MATRIZ DE DISTANCIAS CONTENIDA EN EL ARCHIVO)*/
        feromonas = new float[archivo.getTamMatriz()][archivo.getTamMatriz()];
        for (float[] feromona : feromonas)
            for (int j = 0; j < feromonas.length; j++) 
                feromona[j] = config.getcGreedy().get(_num_archivo); 
        
        /*INICIALIZACIÓN DE LA POBLACIÓN DE HORMIGAS*/
        poblacion = new Poblacion(random,config.getNUM_HORMIGAS(), archivo);
        hormigaElite = poblacion.getV_poblacion().get(0);
        costeElite = 0.0;
        
        /*EXTRA*/
        procesoMejora = new ArrayList<>();
        it = 0;
        num_archivo = _num_archivo;
    }
    
    public void AlgSCH(){
        double tiempo = 0.0;
        while(getIt() < config.getMAX_ITERACIONES() && tiempo < config.getTiempo_max()){
            long inicio = System.currentTimeMillis();
            while(!poblacion.poblacionCompleta(archivo.getTamSolucion())){
               /*MIENTRA NO ESTÁN LAS HORMIGAS COMPLETAS => AÑADIR A TODAS UN ELEMENTO Y DESPUÉS ACTUALIZACIÓN LOCAL*/
                for (Hormiga hormiga : poblacion.getV_poblacion()) {
                    
                    /*POR CADA HORMIGA SACAMOS LAS DISTANCIAS DE TODOS LOS ELEMENTOS RESPECTO A LOS YA METIDOS EN EL VECTOR DE LA HORMIGA*/
                    ArrayList<Integer> LRC = dist_hormiga(hormiga);
                    
                    
                    /*Elección del elemento (de la LRC) a añadir en la hormiga:
                    * - Para cada elemento -> calculo feromona^alfa * heuristica^beta
                    * - La sumatoria de cada elemento de la LRC
                    * - Cual es el de mayor aporte entre los que se van calculando
                    */
                    int elemMaxAporte = -1;
                    double maxAporte = 0.0; 
                    double sumatoriaAportes = 0.0;
                    double heurYferom[] = new double[LRC.size()];
                    for(int i  = 0; i < LRC.size(); i++)
                        heurYferom[i] = 0.0;
                    
                    for (int i = 0; i < LRC.size(); i++){ //Para cada posición de par<elemento,valor> de la LRC...
                        for (int j = 0; j < hormiga.getSolucion().size(); j++) //Para cada elemento de la solución...
                            heurYferom[i] += Math.pow(feromonas[j][LRC.get(i)], config.getAlfa()) * Math.pow(archivo.getMatrizDatos()[j][LRC.get(i)], config.getBeta());
                        
                        if (maxAporte < heurYferom[i]){
                            maxAporte = heurYferom[i]; //Mejor valor aportado
                            elemMaxAporte = LRC.get(i); //Mejor elemento de la LRC
                        }
                        
                        sumatoriaAportes += heurYferom[i];
                    }
                    
                    /* ---- APARENTEMENTE FUNCIONANDO ----*/
                    
                    /*FUNCION DE TRANSICION
                    * q = Aleatorio [0,1]...
                    * IF q <= q0 entonces
                    *   - Cáculo del peso porcentual de cada aporte respecto a los que ya están en la hormiga
                    *   - Aleatorio que elige el nuevo elemento en la hormiga 
                    * ELSE
                    * nuevo elemento a la hormiga = elemMaxAPorte
                    */
                    float q = random.Randfloat(0,1);
                    if (q <= config.getQ0()){
                        reglaTransicionClasica(LRC,heurYferom, hormiga, sumatoriaAportes);
                    }else{
                        hormiga.getSolucion().add(elemMaxAporte);
                        hormiga.getN().remove(elemMaxAporte);
                    }
                    
                    //APARENTEMENTE FUNCIONA CORRECTAMENTE: AÑADE LOS ELEMENTOS A LA PAR Y PARECE QUE NO HA DADO NINGUNA CASUÍSTICA LA CUAL NO FUNCIONE
                }

                /*ACTUALIZACIÓN LOCAL*/
                actualizacionLocalFeromonas();
                
            }
            
            /*YA SE HA COMPLETADO LA POBLACION DE HORMIGAS:
            * - Encontrar la mejor hormiga.
            * - Actualización global respecto a la mejor hormiga encontrada en el proceso anterior.
            * - Actualizar (si así es) la mejor hormiga encontrada en el proceso anterior por la hormiga élite del algoritmo.
            */
            double mejorCoste_hormiga = Double.MIN_VALUE;
            int indexMejorHormiga = -1;
            for (int h = 0; h < poblacion.getV_poblacion().size(); h++) {
                double calidad = poblacion.getV_poblacion().get(h).costeSolucion(archivo.getMatrizDatos());
                if (calidad > mejorCoste_hormiga){
                    indexMejorHormiga = h;
                    mejorCoste_hormiga = calidad;
                }
            }
            
            /*ACTUALIZACIÓN (O NO) DE LA HORMIGA ÉLITE*/
            if (mejorCoste_hormiga > costeElite){
                //System.out.print(costeElite+" --> ");
                costeElite = mejorCoste_hormiga;
                hormigaElite = new Hormiga(poblacion.getV_poblacion().get(indexMejorHormiga));
                getProcesoMejora().add(new Pair<>(hormigaElite, getIt())); //Almacena en que iteracion y que hormiga ha sido cambiada -> logs
                //System.out.print(costeElite);
                //System.out.println(" :: "+getIt()+" :: "+costeElite+" ( actual = "+hormigaElite.getCalidad()+" ) :: "+tiempo+" segundos");
            }
            
            /*ACTUALIZACION GLOBAL*/
            actualizacionGlobalFeromonas(poblacion.getV_poblacion().get(indexMejorHormiga), mejorCoste_hormiga);
            
            
            long fin = System.currentTimeMillis();
            tiempo += ((double)(fin - inicio)/1000);
            
            it++;
            
            poblacion.reiniciarPoblacion(random, archivo.getTamMatriz());
       
        }

    }
    
    
    private ArrayList<Integer> dist_hormiga(Hormiga hormiga){
        ArrayList<Integer> LRC = new ArrayList<>();
        ArrayList<Pair<Integer,Double>> aportes = new ArrayList<>();
        
        /*CALCULAMOS TODOS LOS APORTES DE LOS ELEMENTOS EN N POSIBLES A SER PARTE DE LA SOLUCIÓN*/
        ArrayList<Integer> v_n = new ArrayList<>(hormiga.getN());
        for (int i = 0; i < v_n.size(); i++)
            aportes.add(new Pair(v_n.get(i), hormiga.distanciasElemento(v_n.get(i), archivo.getMatrizDatos())));   
        aportes.sort((o1,o2) -> o1.getValue().compareTo(o2.getValue()));
        
        /*AÑADIMOS AL CONTENEDOR LRC TODOS LOS ELEMENTOS QUE SUPERAN PORCENTUALMENTE EL COSTE DE APORTE RESPECTO A LOS ELEMENTOS DE LA SOLUCIÓN*/
        for(int i = (int)(config.getPROB_LRC()*aportes.size()); i < aportes.size(); i++)
            LRC.add(aportes.get(i).getKey());
        
        return LRC; //Devuelvo Pair para poder luego calcular a ese elemento de la LRC su calidad respecto a la fermonoma^alfa * heuristica*beta
    }
    
    
    private void reglaTransicionClasica(ArrayList<Integer> LRC,double heurYferom[], Hormiga hormiga, double sumatoriaAportes){
        ArrayList<Double> porcentajesAportes = new ArrayList<>(LRC.size()); //Contenedor del aporte de cada elemento de la LRC PORCENTUALMENTE
        
        
        for(int i = 0; i < LRC.size(); i++)
            porcentajesAportes.add((heurYferom[i] / sumatoriaAportes));
        
        
        float aleatorioElem = random.Randfloat(0,1);
        float sumSeqPorcentual = 0;
        
        boolean aniadido = false;
        for (int i = 0; i < LRC.size() && !aniadido; i++) {
            sumSeqPorcentual += porcentajesAportes.get(i);
            if (aleatorioElem <= sumSeqPorcentual) { 
                hormiga.getSolucion().add(LRC.get(i));
                hormiga.getN().remove(LRC.get(i));
                aniadido = true;
            } 
        }
          
    }
    
    
    private void actualizacionLocalFeromonas(){
        
        for (int i = 0; i < poblacion.getV_poblacion().size(); i++) { //Por cada hormiga...
            ArrayList<Integer> v_h = new ArrayList<>(poblacion.getV_poblacion().get(i).getSolucion());
            for (int pos_elem = 0; pos_elem < v_h.size(); pos_elem++) { //Por cada elemento de la solución (no el)de la hormiga...
                feromonas[v_h.get(pos_elem)][v_h.get(v_h.size()-1)] = (1-config.getPhi())*feromonas[v_h.get(pos_elem)][v_h.get(v_h.size()-1)] + (config.getPhi()*config.getcGreedy().get(num_archivo));
                feromonas[v_h.get(v_h.size()-1)][v_h.get(pos_elem)] = feromonas[v_h.get(pos_elem)][v_h.get(v_h.size()-1)];
            }
        }
    }
    
    private void actualizacionGlobalFeromonas(Hormiga mejorHormigaActual, double costeActual){
        /*Actualización de feromona de los elementos que la hormiga trae en su solución*/
        for (Integer i : mejorHormigaActual.getSolucion()) { //Por cada elemento de la hormiga...
            for (Integer j : mejorHormigaActual.getN()) { //Para cada elemento (no partícipe de la solución de la hormiga)...
                feromonas[i][j] += config.getRho() * costeActual;
                feromonas[j][i] = feromonas [i][j];
            }
        }

        /*EVAPORACIÓN: Aplicada a toda la matriz de feromonas*/
        for (int i = 0; i < archivo.getTamMatriz()-1; i++){
            for (int j = i+1; j < archivo.getTamMatriz(); j++){
                feromonas[i][j] = (1-config.getRho())*feromonas[i][j];
                feromonas[j][i] = feromonas[i][j];
            }
        }
        
    }
    
    /**
     * @return the hormigaElite
     */
    public Hormiga getHormigaElite(){
        return hormigaElite;
    }

    /**
     * @return the procesoMejora
     */
    public ArrayList<Pair<Hormiga,Integer>> getProcesoMejora() {
        return procesoMejora;
    }

    /**
     * @return the it
     */
    public Integer getIt() {
        return it;
    }
    
}
