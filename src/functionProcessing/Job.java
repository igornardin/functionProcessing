package functionProcessing;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author vinicius.rodrigues
 */
public class Job implements Serializable{
    
    private double X0; //intervalo inicial para calculo
    private double X1; //intervalo final para calculo
    private double qtde_slices; //numero de passos a ser realizado para o cálculo
    private double slice; //tamanho do intervalo a ser realizado para cada cálculo 
    private ArrayList potencias_pos; //array que guarda todas as potencias aplicadas a x quando x é positivo
    private ArrayList potencias_neg; //array que guarda todas as potencias aplicadas a x quando x é negativo
    private int valor; //valores absolutos
    private double resultado; //resultado final
    private String message; //mensagem a ser passada para os processos
    
    //parametros que serao alterados para cada processo diferente
    private double part_X0; 
    private double part_X1; 
    private double part_qtde_slices; 
    
    public Job(double int_ini, double int_fim, double div){
        this.X0 = int_ini;        
        this.X1 = int_fim;
        this.qtde_slices = div;
        this.slice = (int_fim - int_ini) / div; //(h)
        this.valor = 0;
        this.resultado = 0;
        this.message = "";
        
        //apenas para teste com calculo realizado direto pelo master
        this.part_X0 = int_ini;
        this.part_X1 = int_fim;
        this.part_qtde_slices = div;
    }
    
    public Job(String msg){
        this.message = msg;
    }
    
    public void calcula(){
        this.resultado = calcula_area();
    }
    
    //aqui é realizado todo o calculo da area dentro dos limites part_X0 e part_X1
    public double calcula_area(){
        double x0, fx0, x1, fx1, result;
        x1 = this.part_X0; //apeans inicializo pois este valor será lido para o x0 no início do laço
        result = 0;
        //para cada um dos slices dentro do intervalo realizo o cálculo area retangulo + area triangulo e vou somando tudo para o resultado final
        for (int i = 0; i < this.part_qtde_slices; i++){
            x0 = x1;
            x1 = x0 + this.slice;
            fx0 = f(x0);
            fx1 = f(x1);
            //vamos calcular o retangulo desse slice
            if (fx0 > fx1) {
                result = result + retangulo(this.slice, fx1); //(x1 - x0).f(x1)
            } else {
                result = result + retangulo(this.slice, fx0); //(x1 - x0).f(x0)
            }
            //vamos calcular o triangulo desse slice
            if (fx0 > fx1){
                result = result + triangulo(this.slice, fx0 - fx1); //((x1 - x0).(f(x0) - f(x1)) / 2
            } else if (fx0 < fx1){
                result = result + triangulo(this.slice, fx1 - fx0); //((x1 - x0).(f(x1) - f(x0)) / 2
            } //se eles forem iguais nao faço calculo do triangulo pois nao tem e tudo foi calculado no retangulo
        }
        
        return result;
    }
    
    //aqui aplico a função desta tarefa
    public double f(double x){
        double result = 0;
        for (int i = 0; i < this.potencias_pos.size(); i++){ //calcula todas as potencias positivas
            result = result + (Math.pow(x, Integer.parseInt(this.potencias_pos.get(i).toString()))) * Integer.parseInt(this.potencias_pos.get(i+1).toString());
            i++; //o primeiro e a potencia e o segundo e o valor a ser multiplicado
        }
        for (int i = 0; i < this.potencias_neg.size(); i++){ //calcula todas as potencias negativas
            result = result - Math.pow(x, Integer.parseInt(this.potencias_neg.get(i).toString())) * Integer.parseInt(this.potencias_neg.get(i+1).toString());
            i++; //o primeiro e a potencia e o segundo e o valor a ser multiplicado
        }
        result = result + this.valor;
        return result;
    }
    
    public boolean add_funcao(String expr){
        this.potencias_pos = new ArrayList();
        this.potencias_neg = new ArrayList();
        String[] expr_split = expr.split(";"); //divide a função
        int i = 0;
        //for (int i = 0; i < expr_split.length; i++){//percorreremos cada elemento para construir os vetores e o resultado final
        while(i < expr_split.length){    
            if (expr_split[i].equals("+")){//verifica sinal, caso positivo
                if (expr_split[i+1].contains("x")){//verifica se elemento após o sinal possui o x
                    if (expr_split[i+1].contains("^")){//se possui o x entao veremos se ele possui potencia
                        this.potencias_pos.add(Integer.parseInt(expr_split[i+1].substring(expr_split[i+1].indexOf("^")+1)));//possuindo potencia, como anteriormente testamos se o sinal do x era positivo, inserimos essa potencia no vetor para futuro calculo
                    } else {
                        this.potencias_pos.add(1);//se não tiver potencia então ela é 1
                    }
                    this.potencias_pos.add(1); //adiciono multiplicador 1 pois depois do sinal já vem a expressão
                    i = i + 2;//avanço 2 pois li sinal e expressão;
                } else {
                    if (expr_split[i+2].contains("^")){//veremos se ele possui potencia
                        this.potencias_pos.add(Integer.parseInt(expr_split[i+2].substring(expr_split[i+2].indexOf("^")+1)));//possuindo potencia, como anteriormente testamos se o sinal do x era positivo, inserimos essa potencia no vetor para futuro calculo
                    } else {
                        this.potencias_pos.add(1);//se não tiver potencia então ela é 1
                    }
                    this.potencias_pos.add(Integer.parseInt(expr_split[i+1]));
                    i = i + 3; //avanço 3 pois li sinal, valor multiplicador da expressão e expressão
                    //this.valor = this.valor + Integer.parseInt(expr_split[i+1]);//não possuindo x então é um valor absoluto
                }
            } else {//caso sinal for negativo
                if (expr_split[i+1].contains("x")){//verifica se elemento após o sinal possui o x
                    if (expr_split[i+1].contains("^")){//se possui o x entao veremos se ele possui potencia
                        this.potencias_neg.add(Integer.parseInt(expr_split[i+1].substring(expr_split[i+1].indexOf("^")+1)));//possuindo potencia, como anteriormente testamos se o sinal do x era negativo, inserimos essa potencia no vetor para futuro calculo
                    } else {
                        this.potencias_neg.add(1);//se não tiver potencia então ela é 1
                    }
                    this.potencias_neg.add(1); //adiciono multiplicador 1 pois depois do sinal já vem a expressão
                    i = i + 2;//avanço 2 pois li sinal e expressão;
                } else {
                    if (expr_split[i+2].contains("^")){//se possui o x entao veremos se ele possui potencia
                        this.potencias_neg.add(Integer.parseInt(expr_split[i+2].substring(expr_split[i+2].indexOf("^")+1)));//possuindo potencia, como anteriormente testamos se o sinal do x era negativo, inserimos essa potencia no vetor para futuro calculo
                    } else {
                        this.potencias_neg.add(1);//se não tiver potencia então ela é 1
                    }
                    this.potencias_neg.add(Integer.parseInt(expr_split[i+1]));
                    i = i + 3; //avanço 3 pois li sinal, valor multiplicador da expressão e expressão
                    //this.valor = this.valor - Integer.parseInt(expr_split[i+1]);//não possuindo x então é um valor absoluto
                }
            }
        }
        return true;
    }
    
    public double retangulo(double base, double altura){
        return base * altura;
    }
    
    public double triangulo(double base, double altura){
        return (base * altura) / 2;
    }    
    
    public void add_resultado(double result){
        this.resultado = this.resultado + result;
    }    
    
    public double get_X0(){ //get X0
        return this.X0;
    }
    
    public double get_X1(){ //get intervalo_final
        return this.X1;
    }
    
    public double get_qtde_slices(){ //get subdivisões
        return this.qtde_slices;
    }
    
    public double get_part_qtde_slices(){
        return this.part_qtde_slices;
    }
    
    public double get_slice(){
        return this.slice;
    }
    
    public String get_msg(){ //get message
        return this.message;
    }
    
    public double get_result(){
        return this.resultado;
    }    
    
    public void set_part(double int_ini, double int_fim, double div){
        this.part_qtde_slices = div;
        this.part_X0 = int_ini;
        this.part_X1 = int_fim;
    }    
}
