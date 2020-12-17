/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr03;

/**
 *
 * @author spdlc
 */
public class Random {
    
    private static final long MASK = 2147483647;
    private static final long PRIME = 65539;
    private static final double SCALE = 0.4656612875e-9;
    
    private long Seed;
    
    public Random(){
        Seed = 0;
    }

    Random(Long _semilla) {
        Seed = _semilla;
    }
    
    /* Inicializa la semilla al valor 'x'.
    Solo debe llamarse a esta funcion una vez en todo el programa */
    public void Set_random (long x){
        Seed = x;
    }

    /* Devuelve el valor actual de la semilla */
    public long Get_random (){
        return Seed;
    }

    /* Genera un numero aleatorio real en el intervalo [0,1[
       (incluyendo el 0 pero sin incluir el 1) */
    public double Rand(){
        return (( Seed = ( (Seed * PRIME) & MASK) ) * SCALE );
    }

    /* Genera un numero aleatorio entero en {low,...,high} */
    public int Randint(Integer low, Integer high){
        return (int)(low + (high-(low)+1) * Rand());
    }

    /* Genera un numero aleatorio real en el intervalo [low,...,high[
       (incluyendo 'low' pero sin incluir 'high') */
    public float Randfloat(float low, float high){
        return (float)(low + (high-(low))*Rand());
    }
    
}
