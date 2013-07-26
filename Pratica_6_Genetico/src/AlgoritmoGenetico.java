
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Maria e Anderson
 */
public class AlgoritmoGenetico {
    
    private static ArrayList<Individuo> populacao;
    private static ArrayList<Individuo> populacaoSelecionada;
    
    public static void main(String[] args) {
        int geracoesMax = 200;
        int tamanhoPopulacao = 100;
        int populacaoIntermediaria = 20; // quantidade individuos q sera selecionada c a roleta
        float taxaCruzamento = (float) 0.70; // chance de um par de individuos selecionados cruzar 
        float taxaMutacao = (float) 0.01; // chance de um individuo selecionado sofrer mutacao
        
        ArrayList<Individuo> melhorIndividuoDeCadaGeracao = new ArrayList<Individuo>();
                
        int geracoes = 0; 
                
        // gerou a populacao
        gerarPopulacao(tamanhoPopulacao);
        
        // avaliou, ordena e imprime a populacao
        funcaoAvaliacao();        
        Collections.sort(populacao);        
        //imprimirPopulacao(populacao);
        
        // loop principal com criterio de parada
        while(geracoes<geracoesMax){     
            //System.out.println("tamanho da populacao no inicio da geracao: "+populacao.size());
            
            // guarda o melhor Individuo dessa geracao
            melhorIndividuoDeCadaGeracao.add(populacao.get(0));
            
            geracoes++;           
            
            //seleciona a populacao
            selecao(populacaoIntermediaria);
            
            //aplica cruzamento
            cruzamentoPopulacao(taxaCruzamento);
            
            //aplica mutacao
            mutacao(taxaMutacao);
            
            //avalia, ordena e imprime a populacao
            funcaoAvaliacao();        
            Collections.sort(populacao);        
            //imprimirPopulacao(populacao);
            
            //System.out.println("tamanho da populacao no final da geracao: "+populacao.size());
            
            //descarta os menos aptos
            descartarMenosAptos();            
        }        
        //System.out.println("Populacao convertida:\n"+Arrays.toString(converterPopulacaoBinarioFloat(populacao)));
        //System.out.println("Quantidade de geracoes: "+geracoes);
        System.out.println("\n\nMelhores Individuos por geracao: ");
        System.out.println(melhorIndividuoDeCadaGeracao.toString().replace(".,",",\n"));
        
        //populacao final
        //imprimirPopulacao(populacao);  
    }
    
    public static boolean sorteioProbabilidade (float probabilidadeSerSelecionado){
        double sorteio = Math.random();
        //System.out.println("Sorteio Probabilidade cruz e mutacao: "+sorteio);
        //System.out.println("Probabilidade: "+probabilidadeSerSelecionado);
        if(sorteio<=probabilidadeSerSelecionado)
            return true;
        return false;
    }
    
    public static void mutacao(float taxaMutacao){
         for (int i = 0; i < populacaoSelecionada.size(); i++) {
            // pega individuos dois a dois para cruzamento e só cruza se 
            // for sorteado dentro da taxa de cruzamento
            if(sorteioProbabilidade(taxaMutacao)){
                //System.out.println("Houve mutacao");
                int[] cromossomoNovo = populacaoSelecionada.get(i).getCromossomo();
                //System.out.println("Cromossomo antes da mutacao: "+Arrays.toString(cromossomoNovo));
                cromossomoNovo[cromossomoNovo.length/2] = cromossomoNovo[cromossomoNovo.length/2]==0 ? 1 : 0; // faz mutacao no gen do meio do cromossomo
                //System.out.println("Cromossomo depoi da mutacao: "+Arrays.toString(cromossomoNovo));
                populacaoSelecionada.get(i).setCromossomo(cromossomoNovo); // seta o novo cromossomo apos a mutacao
            }
        }        
    }
    
    public static void cruzamentoPopulacao(float taxaCruzamento){
        for (int i = 1; i < populacaoSelecionada.size(); i=i+2) {
            // pega individuos dois a dois para cruzamento e só cruza se 
            // for sorteado dentro da taxa de cruzamento
            if(sorteioProbabilidade(taxaCruzamento)){
                //System.out.println("Houve cruzamento do inviduo "+(i-1)+" com o individuo "+i);
                Individuo resultadoCruzamento = cruzamentoIndividuo(populacaoSelecionada.get(i-1), populacaoSelecionada.get(i));
                populacao.add(resultadoCruzamento);
            }
        }
    }
    
    public static Individuo cruzamentoIndividuo(Individuo a, Individuo b){
        Individuo novoIndividuo = new Individuo();
        int tamanhoCromossomo = a.getCromossomo().length;
        //System.out.println("Cromossomo pai: "+Arrays.toString(a.getCromossomo()));
        //System.out.println("Cromossomo mae: "+Arrays.toString(b.getCromossomo()));
        int[] cromossomoNovoIndividuo = new int[tamanhoCromossomo];
        int pontoIntermediarioCruzamento = tamanhoCromossomo/2;
        for (int i = 0; i < pontoIntermediarioCruzamento; i++) {
            cromossomoNovoIndividuo[i] = b.getCromossomo()[i];
        }        
        for (int i = pontoIntermediarioCruzamento; i < tamanhoCromossomo; i++) {
            cromossomoNovoIndividuo[i] = a.getCromossomo()[i];            
        }
        novoIndividuo.setCromossomo(cromossomoNovoIndividuo);
        //System.out.println("Cromossomo fil: "+Arrays.toString(cromossomoNovoIndividuo)+"\n");
        return novoIndividuo;
    } 
    
    public static void descartarMenosAptos(){
        int tamanhoExtra = populacao.size();
        for (int i = 100; i < tamanhoExtra; i++) {
            populacao.remove(100);
        }
    }
    
    // adiciona individuos selecionados pela roleta no novo conjunto de individuos selecionados
    public static void selecao(int populacaoIntermediaria){
        float[] vetorSorteio = geraVetorRoleta();
        //System.out.println("tamanho vetor sorteio gerado: "+vetorSorteio.length);
        //System.out.println("Vetor Sorteio gerado: "+Arrays.toString(vetorSorteio).replace(",",",\n"));
        populacaoSelecionada = new ArrayList<Individuo>();
        for (int i = 0; i < populacaoIntermediaria; i++) {
            populacaoSelecionada.add(aplicaRoleta(vetorSorteio));            
        }
    }
    
     public static Individuo aplicaRoleta(float[] vetorSorteio){        
        // sorteio da posicao do vetor com limitacao do valor maximo
        float numeroSorteado = (float)Math.random()*vetorSorteio[vetorSorteio.length-1];         
        //System.out.println("Numero sorteado: "+numeroSorteado);        
        int indiceSorteado  = -1;
        for (int i = 0; i < vetorSorteio.length; i++) {
             if(vetorSorteio[i]>numeroSorteado){
                 indiceSorteado = i;
                 break;
             }
        }        
        //System.out.println("Pegou indice sorteado: "+indiceSorteado);        
        return populacao.get(indiceSorteado);
    }
    
    public static float[] geraVetorRoleta(){
        float[] vetorRoleta = new float[populacao.size()]; 
        //System.out.println("Tamanho do vetor roleta: "+vetorRoleta.length);
        //dar shift para não ter problema com numeros negativos
        float shift = populacao.get(populacao.size()-1).getAptidao();
        //System.out.println("Imprimindo o vetor Roleta:");
        if(shift<0){
            //System.out.println("Valor do shift: "+shift);
            // dando um shift para os números negativos
            vetorRoleta[0] = populacao.get(0).getAptidao();
            //System.out.println("vetorRoleta[0]: "+vetorRoleta[0]+"; Aptidao individuo[0]: "+populacao.get(0).getAptidao());
            for (int i = 1; i < vetorRoleta.length; i++) {
                float aptidaoComShift = populacao.get(i).getAptidao()-shift;
                vetorRoleta[i] = vetorRoleta[i-1] + aptidaoComShift;
                //System.out.println("vetorRoleta["+i+"]: "+vetorRoleta[i]+"; Aptidao individuo["+i+"]: "+populacao.get(i).getAptidao());
            }
        }
        // nao precisa do shift pois a menor aptidao eh positiva
        else{
            //System.out.println("Nao usou shift.");
            vetorRoleta[0] = populacao.get(0).getAptidao();
            //System.out.println("vetorRoleta[0]: "+vetorRoleta[0]+"; Aptidao individuo[0]: "+populacao.get(0).getAptidao());
            for (int i = 1; i < vetorRoleta.length; i++) {
                vetorRoleta[i] = vetorRoleta[i-1] + populacao.get(i).getAptidao();
                //System.out.println("vetorRoleta["+i+"]: "+vetorRoleta[i]+"; Aptidao individuo["+i+"]: "+populacao.get(i).getAptidao());
            }
        }
        return vetorRoleta;
    }
    
    public static void imprimirPopulacao(ArrayList<Individuo> populacao){
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
            populacao.get(i).setCromossomoFloat(- 1 + acumula*(3/((float)Math.pow(2,22)-1))); 
            acumula = 0;
            expoente=0;
        }
    }
}