package functionProcessing;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author vinicius.rodrigues
 */
public class IntervalGenerator {
    
    //crescente
    public int gera_intervalo(int position, double y, int z){
        int resultado = (int) (position * y * z);
        return resultado;
    }
    
    //decrescente
    public int gera_intervalo(int position, float w, double y, int z){
        int resultado = (int) (w - (position * y * z));
        return resultado;
    }
    
    //onda
    public int gera_intervalo(int position, double v, float w, double y, int z){
        int resultado = (int) ((v * z * Math.sin(y * position)) + (v * z + w));
        return resultado;
    }
    
    //exponenciais
    public int gera_intervalo(int position, double v, float w, double y, int z, String tipo){
        int resultado = 0;
        if (tipo.equalsIgnoreCase("ex+")){
            resultado = (int) (Math.exp((position / w) * y) + z);
        } else if (tipo.equalsIgnoreCase("ex-")){
            resultado = (int) (Math.exp(((v - position) / w) * y) + z);
        }
        return resultado;
    }
    
}
