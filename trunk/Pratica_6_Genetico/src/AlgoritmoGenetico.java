
import java.util.ArrayList;
import java.util.Collections;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nephila
 */
public class AlgoritmoGenetico {
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int geracoesMax = 200;
        int tamanhoPopulacao = 100;
        int geracoes = 0; 
        int x1 = -1;
        int x2 = 2;
        
        /*ArrayList<Individuo> populacao = new ArrayList<Individuo>();
        int[]crom = {1,0,0,0,1,0,1,1,1,0,1,1,0,1,0,1,0,0,0,1,1,1};
        Individuo novoIndividuo = new Individuo(crom);
        populacao.add(novoIndividuo);*/
        
        //teste sorteio de 0 a 999
        /*for (int i = 0; i < 1000; i++) {
            System.out.println("Indice sorteado: "+(int)(Math.random()*1000));
        }*/
        
        // gerou a populacao
        ArrayList<Individuo> populacao = gerarPopulacao(tamanhoPopulacao);
        imprimirPopulacao(populacao);        
        System.out.println("");
        
        // avaliou e ordenou
        funcaoAvaliacao(populacao);        
        imprimirPopulacao(populacao);        
        System.out.println("");
        Collections.sort(populacao);        
        imprimirPopulacao(populacao);
        
        while(geracoes++<200){
            
            
        }
        
        //System.out.println("Populacao convertida:\n"+Arrays.toString(converterPopulacaoBinarioFloat(populacao)));
        
    }
    
    public float quantidadePosicoesParaCadaIndividuo(float tamanhoVetor, float probabilidadeIndividuo, float somatorio){
        return (probabilidadeIndividuo*tamanhoVetor)/somatorio;
    }
    
    public ArrayList<Individuo> roleta(ArrayList<Individuo> populacaoAtual){
        int tamanhoVetorRoleta = 1000;
        int[] vetorSorteio = new int[tamanhoVetorRoleta];        
        float somatorioAptidao = 0;
        int ultimaPosicaoOcupada = -1;
       
        for (int i = 0; i < populacaoAtual.size(); i++) {
            somatorioAptidao+=populacaoAtual.get(i).getAptidao();            
        }
        
        for (int i = 0; i < populacaoAtual.size(); i++) {
            float probabilidadeIndividuo = populacaoAtual.get(i).getAptidao()/somatorioAptidao;
            float qtdIndividuo = quantidadePosicoesParaCadaIndividuo(tamanhoVetorRoleta, probabilidadeIndividuo, somatorioAptidao);
            while(qtdIndividuo-->=0){
                vetorSorteio[ultimaPosicaoOcupada+1] = i;
                ultimaPosicaoOcupada+=1;
            }
        }
        // sorteio da posicao do vetor
        int indiceSorteado = (int)Math.random()*1000;
        if(vetorSorteio[indiceSorteado]==0){
            
        }
        
        return (ArrayList<Individuo>) populacaoAtual.clone();
    }
    
    public static void imprimirPopulacao(ArrayList<Individuo> populacao){
       // for (int i = 0; i < populacao.size(); i++) {
          //  System.out.println("Individuo "+i+": "+Arrays.toString(populacao.get(i).getCromossomo()).replace(".,",".\n"));
        //}        
          System.out.println(populacao.toString().replace(".,",",\n"));
    }
    
    // ou funcao avaliacao
    public static void funcaoAvaliacao(ArrayList<Individuo> populacao){
        converterPopulacaoBinarioFloat(populacao);
        for (int i = 0; i < populacao.size(); i++) {
            populacao.get(i).setAptidao(funcaoAptidao((populacao.get(i).getCromossomoFloat())));
        }
    }
    
    // f(x) = x*sen(10*Math.pi*x)+1
    public static float funcaoAptidao(float x){
        return x*(float)Math.sin(10*Math.PI*x)+1;
    }

    public static ArrayList<Individuo> gerarPopulacao(int tamanhoPopulacao){
        ArrayList<Individuo> populacao = new ArrayList<Individuo>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            int[] cromossomoNovoIndividuo = new int[22];
            for (int j = 0; j < 22; j++) {
                cromossomoNovoIndividuo[j]= sorteia();               
            }
            Individuo novoIndividuo = new Individuo(cromossomoNovoIndividuo);
            populacao.add(novoIndividuo);
        }        
        return populacao;
    }

    public static int sorteia(){
        double sorteio = Math.random();
        int x;
        if(sorteio<0.5)
            x = 0;        
        else
            x = 1;                
        return x; 
    }
    
    public static void converterPopulacaoBinarioFloat(ArrayList<Individuo> populacao){
        float acumula = 0;
        int expoente = 0;
        for (int i = 0; i < populacao.size(); i++) {
            for (int j = 21; j >= 0; j--) {
                acumula += populacao.get(i).getCromossomo()[j]*Math.pow(2,expoente++);
            }
            //System.out.println("acumula: "+acumula);
            populacao.get(i).setCromossomoFloat(- 1 + acumula*(3/((float)Math.pow(2,22)-1))); 
            acumula = 0;
            expoente=0;
        }
    }

    public static String gerarBinario(float num){
        return Integer.toBinaryString(Float.floatToIntBits(num));
    }

    public static double binarioParaFloat(String binario){
        return Float.intBitsToFloat(Integer.parseInt(binario,2));
       // return Double.longBitsToDouble(Long.parseLong(binario,2));
    }

}
