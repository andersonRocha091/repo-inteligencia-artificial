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
public class Kohonen {

    // método que converte uma ArrayList<float[]> para String formato impressao
    public static String arraylistToString(ArrayList<float[]> resultado) {
        String resultadoString = "";
        for (float[] y : resultado) {
            resultadoString += "" + Arrays.toString(y) + "\n";
        }
        return "[" + resultadoString.replaceAll("]", ";").replaceAll("]]", "],\n").replaceAll("\\[", " ") + "]\n";
    }

    // método que converte uma ArrayList<float[][]> para String formato impressao
    public static String arraylistMatrizToString(ArrayList<float[][]> resultado) {
        String resultadoString = "[";
        for (float[][] y : resultado) {
            resultadoString += "" + Arrays.deepToString(y).replaceAll("],", ";\n").replaceAll("]]", "],\n").replaceAll("\\[", " ") + "\n";
        }
        return resultadoString + "]";
    }

    public static String matrizToString(float[][] matriz) {
        return "[" + Arrays.deepToString(matriz).replaceAll("],", ";\n").replaceAll("]]", "],\n").replaceAll("\\[", " ") + "}\n";
    }

    public static char[] classificacaoAmostras(Neuronio[][] mapaTreinado, ArrayList<float[]> entradaTeste) {
        char[] neuroniosVencedores = new char[entradaTeste.size()];
        int[] vencedoresAnteriores = new int[entradaTeste.size()];
        int[] vencedoresAtuais = new int[entradaTeste.size()];
        float normaAnterior = Float.POSITIVE_INFINITY;
        float norma = 0;
        int[] deslocamentoX = {0, 0, 1, 0, -1};
        int[] deslocamentoY = {0, 1, 0, -1, 0};
        float menorNorma = 0;
        char classePadrao='Z';
        int indiceVencedorX = 0;
        int indiceVencedorY = 0;
        int epocas = 0;

       
       //     vencedoresAnteriores = Arrays.copyOf(vencedoresAtuais, vencedoresAtuais.length);

            for (int i = 0; i < entradaTeste.size(); i++) {
                menorNorma = Float.POSITIVE_INFINITY;
                for (int j = 0; j < mapaTreinado.length; j++) {
                    for (int k = 0; k < mapaTreinado[0].length; k++) {

                        norma = calcularDistancia(entradaTeste.get(i), mapaTreinado[j][k].getVetorPesos());

                        if (norma < menorNorma) {
                            menorNorma = norma;
                            classePadrao = mapaTreinado[j][k].getClasse();
                            indiceVencedorX = k;
                            indiceVencedorY = j;
                        }
                    }
                }
                neuroniosVencedores[i] = classePadrao;
                norma = 0;
                //  System.out.println("vencedor x: "+indiceVencedorX+"vencedor y: "+indiceVencedorY);
                // System.out.println("norma:"+norma);

               /* for (int t = 0; t < deslocamentoX.length; t++) {
                    try {
                        rede[indiceVencedorY + deslocamentoY[t]][indiceVencedorX + deslocamentoX[t]].atualizarPesos(entrada.get(i), menorNorma);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                }*/


            }
        //    System.out.println("vetorAtual" + Arrays.toString(neuroniosVencedores));

            
            // normaAnterior = Float.POSITIVE_INFINITY;
           // menorNorma = 0;
           // epocas++;

            


        
        

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

    public static Neuronio[][] inicializarRede(int qtdNeuronios, int qtdPesosNeuronios) {
        int qtdLinhas = (int) Math.sqrt(qtdNeuronios);
        Neuronio[][] matrizNeuronios = new Neuronio[qtdLinhas][qtdLinhas];
        int indiceNeuronio = 1;
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

    /*public static float[] novoPeso(float[] peso, float taxaAprendizagem, float[] entrada) {        
     for (int i = 0; i < peso.length; i++) {
     peso[i] = peso[i] + taxaAprendizagem *( entrada[i] - peso[i]);                            
     }        
     return peso;
     }*/
    public static Neuronio[][] kohonen(ArrayList<float[]> entrada, Neuronio[][] rede, float aprendizagem) {
        int[] vencedoresAnteriores = new int[entrada.size()];
        int[] vencedoresAtuais = new int[entrada.size()];
        float normaAnterior = Float.POSITIVE_INFINITY;
        float norma = 0;
        int[] deslocamentoX = {0, 0, 1, 0, -1};
        int[] deslocamentoY = {0, 1, 0, -1, 0};
        float menorNorma = 0;
        int indiceVencedor = 0;
        int indiceVencedorX = 0;
        int indiceVencedorY = 0;
        int epocas = 0;

        do {
            vencedoresAnteriores = Arrays.copyOf(vencedoresAtuais, vencedoresAtuais.length);

            for (int i = 0; i < entrada.size(); i++) {
                menorNorma = Float.POSITIVE_INFINITY;
                for (int j = 0; j < rede.length; j++) {
                    for (int k = 0; k < rede[0].length; k++) {

                        norma = calcularDistancia(entrada.get(i), rede[j][k].getVetorPesos());

                        if (norma < menorNorma) {
                            menorNorma = norma;
                            indiceVencedor = rede[j][k].getIndice();
                            indiceVencedorX = k;
                            indiceVencedorY = j;
                        }
                    }
                }
                vencedoresAtuais[i] = indiceVencedor;

                //  System.out.println("vencedor x: "+indiceVencedorX+"vencedor y: "+indiceVencedorY);
                // System.out.println("norma:"+norma);

                for (int t = 0; t < deslocamentoX.length; t++) {
                    try {
                        rede[indiceVencedorY + deslocamentoY[t]][indiceVencedorX + deslocamentoX[t]].atualizarPesos(entrada.get(i), menorNorma);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                }


            }
            System.out.println("vetorAtual" + Arrays.toString(vencedoresAtuais));

            norma = 0;
            // normaAnterior = Float.POSITIVE_INFINITY;
            menorNorma = 0;
            epocas++;

            


        } while (!Arrays.equals(vencedoresAnteriores, vencedoresAtuais));
        
        for (int i = 0; i < vencedoresAtuais.length; i++) { //classificação neuronios
                for (int j = 0; j < rede.length; j++) {
                    for (int k = 0; k < rede[0].length; k++) {
                        if (vencedoresAtuais[i] == rede[j][k].getIndice()) {
                            if (i < 20) {
                                rede[j][k].setClasse('A');
                            }
                            else if(i>=20 && i<60){
                                rede[j][k].setClasse('B');
                                }
                            
                            else if(i>=60){
                                rede[j][k].setClasse('C');
                            }
                    }
                        }
                    }
                }
            
        for(int i=0;i<rede.length;i++){
            System.out.print(""+i+" ");
            for(int j=0; j<rede[0].length;j++){
                System.out.print(""+rede[i][j].getClasse());
            }
            System.out.println("");
        }
        return rede;
    }

    public static float calcularDistancia(float[] entrada, float[] peso) {

        float distancia = 0;
        float acumulador = 0;

        for (int i = 0; i < entrada.length; i++) {
            acumulador += Math.pow(entrada[i] - peso[i], 2);
        }
        distancia = (float) Math.sqrt(acumulador);

        return distancia;
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

        System.out.println("Mapa inicializado:\n " + Arrays.deepToString(mapaTopologico).replaceAll("],", ";\n").replaceAll("]]", "],\n").replaceAll("\\[", " ") + "\n");

        Neuronio[][] mapaTreinado = kohonen(entrada, mapaTopologico, taxaAprendizagem);

        System.out.println("Mapa treinado:\n " + Arrays.deepToString(mapaTreinado).replaceAll("],", ";\n").replaceAll("]]", "],\n").replaceAll("\\[", " ") + "\n");

        char[] neuroniosVencedores = classificacaoAmostras(mapaTreinado, entradaTeste);

        System.out.println("Neuronios vencedores classificacao \n" + Arrays.toString(neuroniosVencedores));
    }
}
