package perceptronmulticamadas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Anderson
 */
public class PerceptronMultiCamadas {

    private static ArrayList<float[][]> vetoresPesosdasCamadas;

    public static ArrayList<float[]> leArquivo(String path) throws Exception {
        ArrayList<float[]> dadosArquivo = new ArrayList<float[]>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(path)));
        String[] line = null;
        String temp;
        while ((temp = reader.readLine()) != null) {
            line = temp.split("\\ ");
            float[] lineFloat = new float[line.length];
            for (int i = 0; i < line.length; i++) {
                lineFloat[i] = Float.parseFloat(line[i]);
            }
            dadosArquivo.add(lineFloat);
        }
        return dadosArquivo;
    }

    public static void imprimeMatrizPesos(float[][] matrizPeso) {
        for (int i = 0; i < matrizPeso.length; i++) {
            for (int j = 0; j < matrizPeso[0].length; j++) {
                System.out.print(" " + matrizPeso[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static void imprimeVetor(float[] vetor) {
        for (int i = 0; i < vetor.length; i++)
            System.out.println(vetor[i]+" ");
    }

    public static float[][] gerarMatrizPeso(int linhas, int colunas) {
        float[][] matrizPeso = new float[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                matrizPeso[i][j] = (float) Math.random();
            }
        }
        return matrizPeso;
    }

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

    public static ArrayList<float[][]> inicializarRede(int qtdNeuronios[], int camadas, int qtdEntrada) {
        ArrayList<float[][]> pesosCamadas = new ArrayList<float[][]>();
        pesosCamadas.add(gerarMatrizPeso(qtdNeuronios[0], qtdEntrada)); //adiciona a matriz de peso da primeira camada
        for (int i = 1; i < camadas; i++) 
            pesosCamadas.add(gerarMatrizPeso(qtdNeuronios[i], qtdNeuronios[i - 1]));        
        return pesosCamadas;
    }

    public static float[] combinacaoMatrizes(float[][] pesosCamada, float[] entrada, int tamanhoI){
        float[] resultado = new float[tamanhoI];
        for (int i = 0; i < pesosCamada.length; i++) 
            for (int j = 0; j < pesosCamada[0].length; j++) 
                resultado[i] += pesosCamada[i][j]*entrada[j];                    
        return resultado;
    }

    public static float sigmoid(float x){
        return (float)(1/(1+Math.pow(Math.E,(-0.5*x))));
    }

    public static float[] excluirValoresDesejados(float[] vetor, int qtdValoresDesejados){
        float[] novoVetor = new float[vetor.length-qtdValoresDesejados];
        for (int i = 0; i < vetor.length - qtdValoresDesejados; i++)
            novoVetor[i] = vetor[i];
        return novoVetor;
    }
    
    public static ArrayList<float[][]> backpropagation(ArrayList<float[][]> pesosCamadas, float[] entrada,
                                                        float erro, int[] qtdNeuronios){
        float EQM_atual = 1;
        float EQM_anterior = Float.POSITIVE_INFINITY;

        //while(Math.abs(EQM_atual-EQM_anterior)>erro){
            
            // forward

            // para a combinacao linear da camada de entrada excluir os valores desejados do vetor
            float[] I = combinacaoMatrizes(pesosCamadas.get(0), 
                                           excluirValoresDesejados(entrada, 3),
                                           5); // quantidade de neuronios da 1a camada intermediaria
            System.out.println("Camada 0");
            imprimeVetor(I);
            float[] Y = new float[I.length];
            for (int i = 0; i < I.length; i++) 
                Y[i] = sigmoid(I[i]);
            System.out.println("Saída da camada 0");
            imprimeVetor(Y);
            // primeira camada intermediaria em diante
            for (int i = 1; i < pesosCamadas.size(); i++){
                I = combinacaoMatrizes(pesosCamadas.get(i), Y, qtdNeuronios[i]);
                System.out.println("Camada "+i);
                imprimeVetor(I);
                Y = new float[I.length];
                for (int j = 0; j < I.length; j++)
                    Y[j] = sigmoid(I[j]);
                System.out.println("Saída da camada "+i);
                imprimeVetor(Y);
            }

            //backward


            // calculo EQM
        //}
        return pesosCamadas;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<float[]> entrada = leArquivo("treina.txt");


        // ArrayList<float[]> entradaClassificacao = leArquivo("teste.txt");
        int qtdTreino = 1;
        int qtdNeuronios[] = {5, 3}; //
        int qtdEntrada = 5; //4 entradas + 1 entrada do bias

        ArrayList<float[][]> pesosCamadasInicial = inicializarRede(qtdNeuronios, qtdNeuronios.length, qtdEntrada);
        for (int i = 0; i < pesosCamadasInicial.size(); i++) {
            imprimeMatrizPesos(pesosCamadasInicial.get(i));
        }
        ArrayList<float[][]> pesosCamadas = new ArrayList<float[][]>();

        //loop de treinamentos perceptron
        for (int k = 0; k < qtdTreino; k++) {
              pesosCamadas = backpropagation(pesosCamadasInicial, entrada.get(k),
                                            (float)0.01,qtdNeuronios);

              // teste do backpropagation normal

              // backpropagation com momentum
              // teste do backpropagation com momentum
              

//            System.out.println("Treinamento padrao: " + (k + 1) + ":");
//
//            float[] vetorPeso = gerarVetorPeso(entrada.get(0).length - 1);
//            System.out.println("Vetor de pesos inicial: ");
//            imprimePesos(vetorPeso);
//
//            //Fase de treinamento
//            vetorPeso = adaline(vetorPeso, (float) 0.0025, entrada);
//            System.out.println("Vetor de pesos final: ");
//            imprimePesos(vetorPeso);
//
//            //Fase de classificação
//            System.out.println("Classificação de amostras do treino:" + (k + 1));
//            int[] y = classificacaoAmostras(entradaClassificacao, vetorPeso);
//            exibirClassificacao(y);
//
//            System.out.println("Treinamento com momentum: " + (k + 1) + ":");
//
//
//            System.out.println("");
        }
    }
}
