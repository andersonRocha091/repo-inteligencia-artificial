package pratica1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Anderson
 */
public class Pratica1 {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("treinamento.txt")));
        ArrayList<String[]> entrada = new ArrayList<String[]>();
        ArrayList<String[]> entradaClassificacao = new ArrayList<String[]>();
        String[] line = null;
        String temp;

        while ((temp = reader.readLine()) != null) {
            line = temp.split("\\ ");
            entrada.add(line);
        }
        
        reader = new BufferedReader(new InputStreamReader(new FileInputStream("teste.txt"))); //leitura das entradas a classificar
            
            while ((temp = reader.readLine()) != null) {
                line = temp.split("\\ ");
                entradaClassificacao.add(line);
            }

        int qtdPeso = entrada.get(0).length - 1;
        float[] vetorPeso = new float[qtdPeso];

        for (int i = 0; i < qtdPeso; i++) {
            vetorPeso[i] = (float) Math.random();
        }

        float aprendizagem = (float) 0.01;
        int qtdTreino = 5;
        boolean erro = false;
        float u = 0;
        int y = 0;

        //treinamento
        for (int k = 0; k < qtdTreino; k++) {
            System.out.println("\nInicial");
            imprimePesos(vetorPeso);
            do {
                erro = false;
                for (int i = 0; i < entrada.size(); i++) {
                    for (int j = 0; j < entrada.get(i).length - 1; j++) {
                        u += (Float.parseFloat(entrada.get(i)[j])) * vetorPeso[j];
                        y = u > 0 ? 1 : -1;
                        float valorDesejado = Float.parseFloat(entrada.get(i)[entrada.get(0).length - 1]);
                        if (y != valorDesejado) {
                            float valorEntrada = Float.parseFloat(entrada.get(i)[j]);
                            vetorPeso[j] = vetorPeso[j] + aprendizagem * (valorDesejado - y) * valorEntrada;
                            erro = true;
                        }
                    }

                }
            } while (erro == false);
            System.out.println("Final");
            imprimePesos(vetorPeso);
            System.out.println("");
            //Fase de Classificação
            System.out.println("Classificação das Amostras Treino:"+(k+1));
            
            
            for (int i = 0; i < entradaClassificacao.size(); i++) { //Classificação das amostras;
                    for (int j = 0; j < entradaClassificacao.get(i).length - 1; j++) {
                        u += (Float.parseFloat(entradaClassificacao.get(i)[j])) * vetorPeso[j];
                        //float valorDesejado = Float.parseFloat(entradaClassificacao.get(i)[entradaClassificacao.get(0).length - 1]);
                        //if (y != valorDesejado) {
                          //  float valorEntrada = Float.parseFloat(entrada.get(i)[j]);
                            //vetorPeso[j] = vetorPeso[j] + aprendizagem * (valorDesejado - y) * valorEntrada;
                            //erro = true;
                       // }
                    }
                    y = u > 0 ? 1 : -1;
                    System.out.println("Amostra:"+(i+1)+" "+"Classificação:"+y); //exibição para cada treino
                    
                }
            
        }
        reader.close();


    }

    public static void imprimePesos(float[] vetorPeso) {
        System.out.println("Imprimindo vetor peso: ");
        for (int i = 0; i < vetorPeso.length; i++) {
            System.out.println(vetorPeso[i]);
        }
    }
}
