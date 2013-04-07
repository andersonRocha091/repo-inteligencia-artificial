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

        

        float aprendizagem = (float) 0.01;
        int qtdTreino = 5;
        boolean erro = false;
        float valorDesejado = 0;
        float u = 0;
        int y = 0;
        int epoca=0;

        //treinamento
        for (int k = 0; k < qtdTreino; k++) {
            System.out.println("\nInicial");
            
            for (int i = 0; i < qtdPeso; i++) {
            vetorPeso[i] = (float) Math.random();
        }   
            imprimePesos(vetorPeso);
            do{
                
                erro=false;
                for(int i=0;i<entrada.size();i++){
                    for(int j=0;j<entrada.get(i).length-1;j++){
                            u += Float.parseFloat(entrada.get(i)[j])*vetorPeso[j];
                            valorDesejado = Float.parseFloat(entrada.get(i)[4]);
                    }
                    y=u<0?-1:1;
                    u=0;
                    if(y!=valorDesejado){
                    for(int j=0;j<entrada.get(i).length-1;j++){
                       vetorPeso[j] = vetorPeso[j]+aprendizagem*(valorDesejado-y)*Float.parseFloat(entrada.get(i)[j]);     
                    }
                    erro=true;
                    }
                    
                }
                epoca++;
                
            }while(erro==true);     
            
            System.out.println("epocas:"+epoca);
            epoca=0;
            System.out.println("Final");
            imprimePesos(vetorPeso);
            System.out.println("");
            //Fase de Classificação
            System.out.println("Classificação das Amostras Treino:"+(k+1));
            
            
            for (int i = 0; i < entradaClassificacao.size(); i++) { //Classificação das amostras;
                    for (int j = 0; j < entradaClassificacao.get(i).length; j++) {
                        u += (Float.parseFloat(entradaClassificacao.get(i)[j])) * vetorPeso[j];
                        //float valorDesejado = Float.parseFloat(entradaClassificacao.get(i)[entradaClassificacao.get(0).length - 1]);
                        //if (y != valorDesejado) {
                          //  float valorEntrada = Float.parseFloat(entrada.get(i)[j]);
                            //vetorPeso[j] = vetorPeso[j] + aprendizagem * (valorDesejado - y) * valorEntrada;
                            //erro = true;
                       // }
                    }
                    y = u < 0 ? -1 : 1;
                    
                    System.out.println("Amostra:"+(i+1)+" "+"Classificação:"+y); //exibição para cada treino
                    u=0;
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
