
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
    
    
    private static ArrayList<Individuo> populacao;
    private static ArrayList<Individuo> populacaoSelecionada;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int geracoesMax = 200;
        int tamanhoPopulacao = 100;
        int populacaoIntermediaria = 20; // quantidade individuos q sera selecionada c a roleta
    
        
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
        gerarPopulacao(tamanhoPopulacao);
        imprimirPopulacao(populacao);        
        System.out.println("");
        
        // avaliou e ordenou
        funcaoAvaliacao();        
        imprimirPopulacao(populacao);        
        System.out.println("");
        Collections.sort(populacao);        
        imprimirPopulacao(populacao);
        
        // loop principal com criterio de parada
        while(geracoes<geracoesMax){     
            geracoes++;
            
            //seleciona a populacao
            selecao(populacaoIntermediaria);
            
            //aplica cruzamento
            cruzamentoPopulacao();
            
            //aplica mutacao
            mutacao();
            
            //avalia populacao e ordena
            funcaoAvaliacao();        
            imprimirPopulacao(populacao);        
            System.out.println("");
            Collections.sort(populacao);        
            imprimirPopulacao(populacao);
            
            //descarta os menos aptos
            descartarMenosAptos();            
        }        
        //System.out.println("Populacao convertida:\n"+Arrays.toString(converterPopulacaoBinarioFloat(populacao)));
    }
    
    public static void mutacao(){
        
        
    }
    
    public static void cruzamentoPopulacao(){
        //for (int i = 0; i < ; i++) {
            
        //}
    }
    
    public static Individuo cruzamentoIndividuo(Individuo a, Individuo b){
        Individuo novoIndividuo = new Individuo();
        int tamanhoCromossomo = a.getCromossomo().length;
        int[] cromossomoNovoIndividuo = new int[tamanhoCromossomo];
        int pontoIntermediarioCruzamento = tamanhoCromossomo/2;
        for (int i = 0; i < pontoIntermediarioCruzamento; i++) {
            cromossomoNovoIndividuo[i] = b.getCromossomo()[i];
        }        
        for (int i = pontoIntermediarioCruzamento; i < tamanhoCromossomo; i++) {
            cromossomoNovoIndividuo[i] = a.getCromossomo()[i];            
        }
        novoIndividuo.setCromossomo(cromossomoNovoIndividuo);
        return novoIndividuo;
    } 
    
    public static void descartarMenosAptos(){
        for (int i = 100; i < populacao.size(); i++) {
            populacao.remove(i);
        }
    }
    
    public static float quantidadePosicoesParaCadaIndividuo(float tamanhoVetor, float probabilidadeIndividuo, float somatorio){
        return (probabilidadeIndividuo*tamanhoVetor)/somatorio;
    }
    
    // adiciona individuos selecionados pela roleta no novo conjunto de individuos selecionados
    public static void selecao(int populacaoIntermediaria){
        int[] vetorSorteio = preparaRoleta();
        populacaoSelecionada = new ArrayList<Individuo>();
        for (int i = 0; i < populacaoIntermediaria; i++) {
            populacaoSelecionada.add(aplicaRoleta(vetorSorteio));            
        }
    }
    
    // prepara vetor para sorteio da roleta, a partir do somatorio das aptidoes e da 
    // probabilidade de cada individuo
    public static int[] preparaRoleta(){
        int tamanhoVetorRoleta = 1000;
        int[] vetorSorteio = new int[tamanhoVetorRoleta];        
        float somatorioAptidao = 0;
        int ultimaPosicaoOcupada = -1;
       
        for (int i = 0; i < populacao.size(); i++) {
            somatorioAptidao+=populacao.get(i).getAptidao();            
        }
        
        for (int i = 0; i < populacao.size(); i++) {
            float probabilidadeIndividuo = populacao.get(i).getAptidao()/somatorioAptidao;
            float qtdIndividuo = quantidadePosicoesParaCadaIndividuo(tamanhoVetorRoleta, probabilidadeIndividuo, somatorioAptidao);
            while(qtdIndividuo-->=0){
                vetorSorteio[ultimaPosicaoOcupada+1] = i;
                ultimaPosicaoOcupada+=1;
                System.out.println("posicao preenchida: "+ultimaPosicaoOcupada);
            }
        }
        return vetorSorteio;
    }
    
    public static Individuo aplicaRoleta(int[] vetorSorteio){        
        // sorteio da posicao do vetor
        int indiceSorteado = (int)Math.random()*1000;
        
        //if(vetorSorteio[indiceSorteado]==0){            
        //}
        
        return populacaoSelecionada.get(indiceSorteado);
    }
    
    public static void imprimirPopulacao(ArrayList<Individuo> populacao){
       // for (int i = 0; i < populacao.size(); i++) {
          //  System.out.println("Individuo "+i+": "+Arrays.toString(populacao.get(i).getCromossomo()).replace(".,",".\n"));
        //}        
          System.out.println(populacao.toString().replace(".,",",\n"));
    }
    
    // funcao avaliacao
    public static void funcaoAvaliacao(){
        converterPopulacaoBinarioFloat(populacao);
        for (int i = 0; i < populacao.size(); i++) {
            populacao.get(i).setAptidao(funcaoAptidao((populacao.get(i).getCromossomoFloat())));
        }
    }
    
    // calculando valor da funcao aptida f(x) = x*sen(10*Math.pi*x)+1
    public static float funcaoAptidao(float x){
        return x*(float)Math.sin(10*Math.PI*x)+1;
    }

    public static ArrayList<Individuo> gerarPopulacao(int tamanhoPopulacao){
        populacao = new ArrayList<Individuo>();
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
