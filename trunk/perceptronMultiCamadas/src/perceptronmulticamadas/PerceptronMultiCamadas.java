package perceptronmulticamadas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Anderson
 */
public class PerceptronMultiCamadas {

    // variaveis globais
    private static ArrayList<float[]> entrada;
    private static ArrayList<float[]> valoresDesejados;

    public static int[] classificacaoAmostras(ArrayList<float[]> entradaClassificacao,
            float[] vetorPeso) {
        float u = 0;
        int[] y = new int[entradaClassificacao.size()];
        for (int i = 0; i < entradaClassificacao.size(); i++) { //Classificação das amostras;
            for (int j = 0; j < entradaClassificacao.get(i).length; j++) {
                u += (entradaClassificacao.get(i)[j]) * vetorPeso[j];
            }
            y[i] = u < 0 ? -1 : 1;
            u = 0;
        }
        return y;
    }

    public static void exibirClassificacao(int[] y) {
        for (int i = 0; i < y.length; i++) {
            System.out.println("Amostra:" + (i + 1) + " " + "Classificação:" + y[i]); //exibição para cada treino
        }
    }

    public static void leArquivo(String path, int qtdValoresDesejados) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(path)));
        entrada = new ArrayList<float[]>();
        valoresDesejados = new ArrayList<float[]>();
        String[] line = null;
        String temp;
        while ((temp = reader.readLine()) != null) {
            line = temp.split("\\ ");
            float[] lineFloat = new float[line.length];
            for (int i = 0; i < line.length-qtdValoresDesejados+1; i++) 
                lineFloat[i] = Float.parseFloat(line[i]);
            entrada.add(lineFloat);
            lineFloat = new float[qtdValoresDesejados];
            int j = 0;
            for (int i = line.length-qtdValoresDesejados; i < line.length; i++){
                lineFloat[j++] = Float.parseFloat(line[i]);
            }
            valoresDesejados.add(lineFloat);            
        }
    }

    public static float[][] gerarMatrizPeso(int linhas, int colunas) {
        float[][] matrizPeso = new float[linhas][colunas];
        for (int i = 0; i < linhas; i++) 
            for (int j = 0; j < colunas; j++) 
                matrizPeso[i][j] = (float) Math.random();                    
        return matrizPeso;
    }

    public static ArrayList<float[][]> inicializarRede(int qtdNeuronios[], int camadas, int qtdEntrada) {
        ArrayList<float[][]> pesosCamadas = new ArrayList<float[][]>();
        pesosCamadas.add(gerarMatrizPeso(qtdNeuronios[0], qtdEntrada)); //adiciona a matriz de peso da primeira camada
        for (int i = 1; i < camadas; i++) 
            pesosCamadas.add(gerarMatrizPeso(qtdNeuronios[i], qtdNeuronios[i - 1]));        
        return pesosCamadas;
    }

    public static float[] combinacaoMatrizes(float[][] pesosCamada, float[] entrada, int tamanhoI) {
        float[] resultado = new float[tamanhoI];
        for (int i = 0; i < pesosCamada.length; i++) 
            for (int j = 0; j < pesosCamada[0].length; j++) 
                resultado[i] += pesosCamada[i][j] * entrada[j];                    
        return resultado;
    }

    public static float sigmoid(float u) {
        return (float) (1 / (1 + Math.pow(Math.E, (-0.5 * u)))); // o beta da formula eh 0.5
    }

    public static float dg(float u) {
        return (float) 0.5 * sigmoid(u) * (1 - sigmoid(u)); // o beta da formula eh 0.5
    }

    public static float[] g(float[] I, boolean ultimaCamada) {
       float[] Y;
        if(ultimaCamada==false){//flag para verificação se é ultima camada, evitar o bias na saida da ultima camada
            Y = new float[I.length + 1];  // +1 espaco para o bias
            Y[0] = -1;
          for (int i = 1; i < I.length + 1; i++) 
            Y[i] = sigmoid(I[i - 1]);
       }
       else{
           Y = new float[I.length];  // Sem espaço para p/ bias caso ultima camada
           for (int i = 0; i < I.length; i++) 
            Y[i] = sigmoid(I[i]);
       }
        
               
        return Y;
    }

    public static ArrayList<float[][]> backpropagation(ArrayList<float[][]> pesosCamadas,
            float erro, int[] qtdNeuronios, int qtdValoresDesejados, int qtdCamadaIntermediaria) {
        float EQM_atual = 1;
        float EQM_anterior = Float.POSITIVE_INFINITY;
        int epocas = 0;
        boolean last = false;
        ArrayList<float[]> saidasCamadas = new ArrayList<float[]>();

        //while(Math.abs(EQM_atual-EQM_anterior)>erro){
        for (int k = 0; k < entrada.size(); k++) {
            // Fase Forward
            float[] I = combinacaoMatrizes(pesosCamadas.get(0),entrada.get(k),qtdCamadaIntermediaria); // primeira camada
            saidasCamadas.add(g(I,last));
            System.out.println("Camada 0: \n" + Arrays.toString(I) + "\n" + "Saída da camada 0: \n" + Arrays.toString(saidasCamadas.get(0)) + "\n");
            // camadas intermediarias e a final
            for (int i = 1; i < pesosCamadas.size(); i++) {
                if(i==(pesosCamadas.size()-1))
                    last=true;
                I = combinacaoMatrizes(pesosCamadas.get(i), saidasCamadas.get(i - 1), qtdNeuronios[i]);
                saidasCamadas.add(g(I,last));
                System.out.println("Camada " + i + ":\n " + Arrays.toString(I) + "\n" + "Saída da camada " + i + ":\n " + Arrays.toString(saidasCamadas.get(i)) + "\n");
            }
            System.out.println("Todas Saidas:");
          //  for (int i=0;i<saidasCamadas.size();i++){
                
                
            //    System.out.println(Arrays.toString(saidasCamadas.get(i))+"\n");
           // }
            last=false;
            // Fase Backward
            //calculo gradiente local da ultima camada
            for (int i = 0; i < qtdNeuronios[qtdNeuronios.length - 1]; i++) {
//                    float valorDesejado = entrada.get(k)[]
//                    float gradiente = (entrada)
            }
        }
        // calculo EQM
        epocas++;
        //}
        return pesosCamadas;
    }

    public static void main(String[] args) throws Exception {
        // Definição dos parâmetros do algoritmo
        int qtdTreino = 1;
        int qtdNeuronios[] = {5, 3}; //
        int qtdEntrada = 5; //4 entradas + 1 entrada do bias
        int qtdValoresDesejados = 3;
        int qtdCamadaIntermediaria = 5; // qtd neuronios na 1a camada intermediaria
        float erro = (float) 0.01;
//        int qtdNeuronios[] = {3, 2, 1}; //
//        int qtdEntrada = 3; //2 entradas + 1 entrada do bias

        // Leitura de arquivos de entrada
        leArquivo("treina.txt",qtdValoresDesejados);
        System.out.println("Matriz valores desejados");
        for (int i = 0; i < valoresDesejados.size(); i++)
            System.out.println(Arrays.toString(valoresDesejados.get(i)));
//        ArrayList<float[]> entrada = leArquivo("treinamento.txt");
//        ArrayList<float[]> entradaClassificacao = leArquivo("teste.txt");        

        // Inicializacao das matrizes de pesos 
        ArrayList<float[][]> pesosInicial = inicializarRede(qtdNeuronios, qtdNeuronios.length, qtdEntrada);
        for (int i = 0; i < pesosInicial.size(); i++) 
            System.out.println("Matriz de peso inicial " + i + ":\n" + Arrays.deepToString(pesosInicial.get(i)).replaceAll("],", "],\n") + "\n");
        ArrayList<float[][]> pesosAux = new ArrayList<float[][]>(); // variavel auxiliar
//        float[][] matrizPeso1 = {{(float)0.2,(float)0.4,(float)0.5},
//                                  {(float)0.3,(float)0.6,(float)0.7},
//                                  {(float)0.4,(float)0.8,(float)0.3}};
//        System.out.println("tamanho pesosCamadas: "+pesosCamadas.size());
//        System.out.println("tamanho linha matrizPeso1: "+matrizPeso1.length);
//        System.out.println("Tamanho coluna: "+matrizPeso1[0].length);
        //pesosCamadasInicial.set(0, matrizPeso1);        

        // Loop principal treinamentos + classificacoes
        for (int k = 0; k < qtdTreino; k++) {

            System.out.println("---------  Treinamento "+k+"  -----------");

            // Fase de Treinamento backpropagation normal
            pesosAux = backpropagation(pesosInicial, erro, qtdNeuronios, qtdValoresDesejados, qtdCamadaIntermediaria);

            // Classificacao do backpropagation normal

            // Fase de Treinamento do backpropagation com momentum

            // Classificacao do backpropagation com momentum
        }
    }
}
