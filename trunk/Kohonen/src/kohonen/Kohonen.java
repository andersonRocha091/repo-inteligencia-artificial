package kohonen;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author Anderson e Maria
 */
public class Kohonen{

    // método que converte uma ArrayList<float[]> para String formato impressao
    public static String arraylistToString(ArrayList<float[]> resultado) {
         String resultadoString = "";
         for (float[] y : resultado) {
             resultadoString += ""+Arrays.toString(y)+"\n";
         }
         return "["+resultadoString.replaceAll("]",";").replaceAll("]]","],\n").replaceAll("\\["," ")+"]\n";
    }
    
    // método que converte uma ArrayList<float[][]> para String formato impressao
    public static String arraylistMatrizToString(ArrayList<float[][]> resultado) {
         String resultadoString = "[";
         for (float[][] y : resultado) {
             resultadoString += ""+Arrays.deepToString(y).replaceAll("],",";\n").replaceAll("]]","],\n").replaceAll("\\["," ")+"\n";
         }
         return resultadoString+"]";
    }
    
    public static String matrizToString(float[][] matriz){
        return "["+Arrays.deepToString(matriz).replaceAll("],",";\n").replaceAll("]]","],\n").replaceAll("\\["," ")+"}\n";
    }
    
    public static int[] classificacaoAmostras(Neuronio[][] mapaTreinado, ArrayList<float[]> entradaTeste) {                
        int[] neuroniosVencedores = new int[entradaTeste.size()];
        
        return neuroniosVencedores;
    }   

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
    
    public static Neuronio[][] inicializarRede(int qtdNeuronios, int qtdPesosNeuronios){
        int qtdLinhas = (int)Math.sqrt(qtdNeuronios);
        Neuronio[][] matrizNeuronios = new Neuronio[qtdLinhas][qtdLinhas];
        int indiceNeuronio=1;
        for (int i = 0; i < qtdLinhas; i++) {
            for (int j = 0; j < qtdLinhas; j++) {
                Neuronio n = new Neuronio(qtdPesosNeuronios, indiceNeuronio++);
                matrizNeuronios[i][j] = n;
            }
        }             
        return matrizNeuronios;
    }

    public static float[] combinacaoMatrizes(float[][] pesosCamada, float[] entrada, int tamanhoI) {
        float[] resultado = new float[tamanhoI];
        for (int i = 0; i < pesosCamada.length; i++) {
            for (int j = 0; j < pesosCamada[0].length; j++) {
                resultado[i] += pesosCamada[i][j] * entrada[j];
            }
        }
        return resultado;
    }

    
    public static float[] novoPeso(float[] peso, float taxaAprendizagem, float[] entrada) {        
        for (int i = 0; i < peso.length; i++) {
            peso[i] = peso[i] + taxaAprendizagem *( entrada[i] - peso[i]);                            
        }        
        return peso;
    }
    
    public static Neuronio[][] kohonen(ArrayList<float[]> entrada, Neuronio[][] rede){
        int[] vencedoresAnteriores = new int[entrada.size()];
        int[] vencedoresAtuais = new int[entrada.size()];
                
        do{
            vencedoresAnteriores = Arrays.copyOf(vencedoresAtuais,vencedoresAtuais.length);
            
            for (int i = 0; i < entrada.size(); i++) {
                
                //for para calcular distancia entre entrada atual para cada neuronio
                
            }
            
        }while(!Arrays.equals(vencedoresAnteriores, vencedoresAtuais));
    
        return rede;
    }
    
    public static void main(String[] args) throws Exception {        
        // Definição dos parâmetros do algoritmo
        float taxaAprendizagem = (float) 0.001;
        int qtdNeuronios = 16; // grid topologico 4x4
        int qtdPesosNeuronio = 3;  //pesos em cada neuronio. 

        // Leitura de arquivos de entrada        
        ArrayList<float[]> entrada = leArquivo("treina.txt");
        ArrayList<float[]> entradaTeste = leArquivo("teste.txt");       

        Neuronio[][] mapaTopologico = inicializarRede(qtdNeuronios, qtdPesosNeuronio);
        
        System.out.println("Mapa inicializado:\n "+Arrays.deepToString(mapaTopologico).replaceAll("],",";\n").replaceAll("]]","],\n").replaceAll("\\["," ")+"\n");
        
        Neuronio[][] mapaTreinado = kohonen(entrada, mapaTopologico);
        
        System.out.println("Mapa treinado:\n "+Arrays.deepToString(mapaTreinado).replaceAll("],",";\n").replaceAll("]]","],\n").replaceAll("\\["," ")+"\n");
        
        int[] neuroniosVencedores = classificacaoAmostras(mapaTreinado,entradaTeste);
        
        System.out.println("Neuronios vencedores classificacao \n"+Arrays.toString(neuroniosVencedores));
    }    
}
