package pratica2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Anderson
 */
public class Pratica2 {

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

    public static void imprimePesos(float[] vetorPeso) {
        for (int i = 0; i < vetorPeso.length; i++) {
            System.out.println(vetorPeso[i]);
        }
    }

    public static float[] gerarVetorPeso(int size) {
        float[] vetorPeso = new float[size];
        for (int i = 0; i < size; i++) {
            vetorPeso[i] = (float) Math.random();
        }
        return vetorPeso;
    }

    public static float[] perceptron1Camada(float[] vetorPeso, float taxaAprendizagem,
            ArrayList<float[]> entrada) {
        int epoca = 0;
        boolean erro;
        float u = 0;
        int y = 0;
        float valorDesejado = 0;
        do {
            erro = false;
            for (int i = 0; i < entrada.size(); i++) {
                for (int j = 0; j < entrada.get(i).length - 1; j++) {
                    u += (entrada.get(i)[j]) * vetorPeso[j];
                    valorDesejado = entrada.get(i)[4];
                }
                y = u < 0 ? -1 : 1;
                u = 0;
                if (y != valorDesejado) {
                    for (int j = 0; j < entrada.get(i).length - 1; j++) {
                        vetorPeso[j] = vetorPeso[j] + taxaAprendizagem * (valorDesejado - y) * entrada.get(i)[j];
                    }
                    erro = true;
                }
            }
            epoca++;
        } while (erro == true);

        System.out.println("O treinamento teve "+epoca+" epocas.");
        epoca = 0;
        return vetorPeso;
    }

    public static int[] classificacaoAmostras(ArrayList<float[]> entradaClassificacao,
            float[] vetorPeso) {
        float u = 0;
        int[] y = new int[entradaClassificacao.size()];
        for (int i = 0; i < entradaClassificacao.size(); i++) { //Classificação das amostras;
            for (int j = 0; j < entradaClassificacao.get(i).length; j++)
                u += (entradaClassificacao.get(i)[j]) * vetorPeso[j];
            y[i] = u < 0 ? -1 : 1;
            u = 0;
        }
        return y;
    }

    public static void exibirClassificacao(int[] y){
        for (int i = 0; i < y.length; i++)
            System.out.println("Amostra:" + (i + 1) + " " + "Classificação:" + y[i]); //exibição para cada treino
    }

    public static void main(String[] args) throws Exception {
        ArrayList<float[]> entrada = leArquivo("treinamento.txt");
        ArrayList<float[]> entradaClassificacao = leArquivo("teste.txt");
        int qtdTreino = 5;

        //loop de treinamentos perceptron
        for (int k = 0; k < qtdTreino; k++) {
            System.out.println("Treinamento "+(k+1)+":");

            float[] vetorPeso = gerarVetorPeso(entrada.get(0).length - 1);
            System.out.println("Vetor de pesos inicial: ");
            imprimePesos(vetorPeso);

            //Fase de treinamento
            vetorPeso = perceptron1Camada(vetorPeso, (float) 0.01, entrada);
            System.out.println("Vetor de pesos final: ");
            imprimePesos(vetorPeso);

            //Fase de classificação
            System.out.println("Classificação de amostras do treino:" + (k + 1));
            int[] y = classificacaoAmostras(entradaClassificacao, vetorPeso);
            exibirClassificacao(y);

            System.out.println("");
        }
    }
}
