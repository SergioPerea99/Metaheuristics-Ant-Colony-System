/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meta_pr03;

import java.util.ArrayList;

/**
 *
 * @author spdlc
 */
public class META_PR03 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Configurador config = new Configurador(args[0]);
//        System.out.println(config.getArchivos());
//        ArrayList<ArchivoDatos> archivos = new ArrayList<>();
//        for (int i = 0; i < config.getArchivos().size(); i++) {
//            archivos.add(new ArchivoDatos(config.getArchivos().get(i)));
//        }
        
        SCH colonia = new SCH(args,0,0);
        colonia.AlgSCH();
    }
    
}
