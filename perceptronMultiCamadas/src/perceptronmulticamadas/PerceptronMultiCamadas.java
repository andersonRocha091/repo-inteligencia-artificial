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
            for (int i = 0; i < line.length - qtdValoresDesejados + 1; i++) {
                lineFloat[i] = Float.parseFloat(line[i]);
            }
            entrada.add(lineFloat);
            lineFloat = new float[qtdValoresDesejados];
            int j = 0;
            for (int i = line.length - qtdValoresDesejados; i < line.length; i++) {
                lineFloat[j++] = Float.parseFloat(line[i]);
            }
            valoresDesejados.add(lineFloat);
        }
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

    public static ArrayList<float[][]> inicializarRede(int qtdNeuronios[], int camadas, int qtdEntrada) {
        ArrayList<float[][]> pesosCamadas = new ArrayList<float[][]>();
        pesosCamadas.add(gerarMatrizPeso(qtdNeuronios[0], qtdEntrada)); //adiciona a matriz de peso da primeira camada
        for (int i = 1; i < camadas; i++) {
            pesosCamadas.add(gerarMatrizPeso(qtdNeuronios[i], qtdNeuronios[i - 1]));
        }
        return pesosCamadas;
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

    public static float sigmoid(float u) {
        return (float) (1 / (1 + Math.pow(Math.E, (-0.5 * u)))); // o beta da formula eh 0.5
    }

    public static float derivadaSigmoid(float u) {
        return (float) 0.5 * sigmoid(u) * (1 - sigmoid(u)); // o beta da formula eh 0.5
    }

    public static float[] g(float[] I, boolean ultimaCamada) {
        float[] Y;
        if (ultimaCamada == false) {//flag para verificação se é ultima camada, evitar o bias na saida da ultima camada
            Y = new float[I.length + 1];  // +1 espaco para o bias
            Y[0] = -1;
            for (int i = 1; i < I.length + 1; i++) {
                Y[i] = sigmoid(I[i - 1]);
            }
        } else {
            Y = new float[I.length];  // Sem espaço para p/ bias caso ultima camada
            for (int i = 0; i < I.length; i++) {
                Y[i] = sigmoid(I[i]);
            }
        }
        return Y;
    }

    public static float[] glinha(float[] I) { //calculando a derivada de I
        float[] Y = new float[I.length];
        for (int i = 0; i < I.length; i++) {
            Y[i] = derivadaSigmoid(I[i]);
        }
        return Y;
    }

    public static float[] gradienteUltimaCamada(float[] valoresDesejados, float[] saidas, float[] derivadaI) {
        float[] delta = new float[saidas.length];
        for (int i = 0; i < saidas.length; i++) {
            delta[i] = (valoresDesejados[i] - saidas[i]) * derivadaI[i];
        }
        return delta;
    }

    public static float[] gradienteCamadasIntermediarias(float[] gradienteAnterior, float[][] pesosCamadaAnterior, float[] derivadaCamadaAtual) {
        float[] delta = new float[derivadaCamadaAtual.length];
        float[] acumula = new float[pesosCamadaAnterior[0].length];
        float[] acumulaAnterior = new float[pesosCamadaAnterior[0].length];
        for (int i = 0; i < pesosCamadaAnterior.length; i++) {
            for (int j = 0; j < pesosCamadaAnterior[0].length; j++) {
                acumula[j] = pesosCamadaAnterior[i][j] * gradienteAnterior[i];
            }
            for (int j = 0; j < pesosCamadaAnterior[0].length; j++) {
                acumula[j] = acumula[j] + acumulaAnterior[j];
            }
            acumulaAnterior = (float[]) acumula.clone(); //metodo copia de vetor
        }
        for (int i = 0; i < derivadaCamadaAtual.length; i++) {
            delta[i] = acumula[i] * derivadaCamadaAtual[i];
        }
        return delta;
    }

    public static float[][] novoPeso(float[][] pesoAnterior, float taxaAprendizagem, float[] gradiente, float[] saidaCamada) {
        float[][] pesosAtualizado = new float[pesoAnterior.length][pesoAnterior[0].length];
        for (int i = 0; i < pesoAnterior.length; i++) {
            for (int j = 0; j < pesoAnterior[0].length; j++) {
                pesosAtualizado[i][j] = pesoAnterior[i][j] + taxaAprendizagem * gradiente[i] * saidaCamada[i];
            }
        }
        return pesosAtualizado;
    }

    public static float E(float[] Y, float[] valoresDesejados) {
        float E = 0;
        for (int i = 0; i < Y.length; i++) {
            E += Math.pow(valoresDesejados[i] - Y[i], 2);
        }
        return E/2;
    }

    public static ArrayList<float[][]> backpropagation(ArrayList<float[][]> pesosCamadas,
            float erro, int[] qtdNeuronios, int qtdValoresDesejados, int qtdCamadaIntermediaria,
            float taxaAprendizagem) {
        float EQM_atual = 1;
        float EQM_anterior = Float.POSITIVE_INFINITY;
        int epocas = 0;
        boolean ehUltimaCamada = false;
        ArrayList<float[]> saidasCamadas = new ArrayList<float[]>();
        ArrayList<float[]> derivadaSigmoidI = new ArrayList<float[]>();
        float[] gradienteLast;
        float[] gradiente;

        while(Math.abs(EQM_atual-EQM_anterior)>erro){
            EQM_anterior = EQM_atual;
            for (int k = 0; k < entrada.size(); k++) {
                // Fase Forward
                float[] I = combinacaoMatrizes(pesosCamadas.get(0), entrada.get(k), qtdCamadaIntermediaria); // primeira camada
                saidasCamadas.add(g(I, ehUltimaCamada));
                derivadaSigmoidI.add(glinha(I)); //calculando g'(I) para a primeira camada
                System.out.println("Camada 0: \n" + Arrays.toString(I) + "\n" + "Saída da camada 0: \n" + Arrays.toString(saidasCamadas.get(0)) + "\n");
                // camadas intermediarias e a final
                for (int i = 1; i < pesosCamadas.size(); i++) {
                    if (i == (pesosCamadas.size() - 1)) {
                        ehUltimaCamada = true;
                    }
                    I = combinacaoMatrizes(pesosCamadas.get(i), saidasCamadas.get(i - 1), qtdNeuronios[i]);
                    saidasCamadas.add(g(I, ehUltimaCamada));
                    derivadaSigmoidI.add(glinha(I)); //calculando g'(I) para as camadas intermediarias e primeira
                    System.out.println("Camada " + i + ":\n " + Arrays.toString(I) + "\n" + "Saída da camada " + i + ":\n " + Arrays.toString(saidasCamadas.get(i)) + "\n");                    
                    for (int j = 0; j < derivadaSigmoidI.size(); j++) {
                         System.out.println("G'(I) da camada "+j+":"+Arrays.toString(derivadaSigmoidI.get(j)));
                    }
                }
                ehUltimaCamada = false;

                // calculo de E(k) e 'quase EQM'
                float E = E(saidasCamadas.get(saidasCamadas.size() - 1), valoresDesejados.get(k));
                EQM_atual += E;
                System.out.println("\nErro E(k): " + E + "\n");

                // Fase Backward
                //calculo gradiente ultima camada e atualizacao de pesos da ultima camada
                gradienteLast = gradienteUltimaCamada(valoresDesejados.get(k), saidasCamadas.get(saidasCamadas.size()-1), derivadaSigmoidI.get(derivadaSigmoidI.size()- 1));
                System.out.println("Gradiente ultima camada:\n"+Arrays.toString(gradienteLast)+"\n");
                pesosCamadas.set(pesosCamadas.size() - 1, novoPeso(pesosCamadas.get(pesosCamadas.size()-1), taxaAprendizagem, gradienteLast, saidasCamadas.get(saidasCamadas.size()-2)));
                System.out.println("Matriz pesos ultima camada atualizada: \n"+Arrays.deepToString(pesosCamadas.get(pesosCamadas.size()-1)));    
                //calculo gradiente camadas intermediarias e atualizacao de pesos camadas intermediarias
                for (int i = pesosCamadas.size() - 2; i >= 1; i--) { // o pesosCamas.size - 2 é pra garantir que não começamos pela ultima camada.
                    gradiente = gradienteCamadasIntermediarias(gradienteLast, pesosCamadas.get(i + 1), derivadaSigmoidI.get(i));
                    System.out.println("Gradiente camada "+i+": " + Arrays.toString(gradiente)+"\n");
                    pesosCamadas.set(i, novoPeso(pesosCamadas.get(i), taxaAprendizagem, gradiente, saidasCamadas.get(i-1)));
                    System.out.println("Matriz pesos camada "+i+" atualizada:\n"+Arrays.deepToString(pesosCamadas.get(pesosCamadas.size()-1)));
                    gradienteLast = gradiente; // guarda o ultimo gradiente para o calculo do proximo
                }
                //calculo gradiente da primeira camada e atualizacao de pesos da primeira camada
                gradiente = gradienteCamadasIntermediarias(gradienteLast, pesosCamadas.get(1), derivadaSigmoidI.get(0));
                System.out.println("Gradiente 1a camada: " + Arrays.toString(gradiente)+"\n");
                pesosCamadas.set(0, novoPeso(pesosCamadas.get(0), taxaAprendizagem, gradiente, entrada.get(k)));
                System.out.println("Matriz pesos camada 1a atualizada:\n"+Arrays.deepToString(pesosCamadas.get(pesosCamadas.size()-1)));                

                //atualizacao vetor de pesos das outras camadas
                derivadaSigmoidI.clear();
                saidasCamadas.clear();
            }
            // calculo EQM
            EQM_atual /= entrada.size();
            epocas++;
        }
        System.out.println("\n------------- O treinamento teve "+epocas+" epocas.---------------\n");
        return pesosCamadas;
    }

    public static void main(String[] args) throws Exception {

        // Definição dos parâmetros do algoritmo
        int qtdTreino = 1;
        int qtdNeuronios[] = {5, 3}; //
        int qtdEntrada = 5; //4 entradas + 1 entrada do bias
        int qtdValoresDesejados = 3;
        int qtdNeuronios1aCamadaIntermediaria = 5; // qtd neuronios na 1a camada intermediaria
        float erro = (float) 0.01;
        float taxaAprendizagem = (float) 0.1;

        // parametros do caso de teste slide Angelo
//        int qtdTreino = 1;
//        int qtdNeuronios[] = {3, 2, 1}; //
//        int qtdEntrada = 3; //2 entradas + 1 entrada do bias
//        int qtdValoresDesejados = 1;
//        int qtdNeuronios1aCamadaIntermediaria = 3; // qtd neuronios na 1a camada intermediaria
//        int qtdValoresDesejados = 1;
//        float erro = (float) 0.01;
//        float taxaAprendizagem = (float) 0.1;

        // Leitura de arquivos de entrada        
        leArquivo("treina.txt", qtdValoresDesejados);
        System.out.println("Matriz valores desejados: ");
        for (int j = 0; j < valoresDesejados.size(); j++) {
            System.out.println(Arrays.toString(valoresDesejados.get(j)));
        }

//        leArquivo("teste.txt");
//        leArquivo("treinamento.txt",qtdValoresDesejados);

        // Inicializacao das matrizes de pesos 
        ArrayList<float[][]> pesosInicial = inicializarRede(qtdNeuronios, qtdNeuronios.length, qtdEntrada);
        for (int i = 0; i < pesosInicial.size(); i++) {
            // metodo para imprimir matriz Arrays.deepToString
            System.out.println("\nMatriz de peso inicial " + i + ":\n" + Arrays.deepToString(pesosInicial.get(i)).replaceAll("],", "],\n") + "\n");
        }
        ArrayList<float[][]> pesosAux = new ArrayList<float[][]>(); // variavel auxiliar
//       float[][] matrizPeso1 = {{(float)0.2,(float)0.4,(float)0.5},{(float)0.3,(float)0.6,(float)0.7},{(float)0.4,(float)0.8,(float)0.3}};
//        pesosInicial.set(0, matrizPeso1);        

        // Loop principal treinamentos + classificacoes
        int k;
        for (k = 0; k < qtdTreino; k++) {
            System.out.println("---------  Treinamento " + k + "  -----------");
            // Fase de Treinamento backpropagation normal
            pesosAux = backpropagation(pesosInicial, erro, qtdNeuronios, qtdValoresDesejados, qtdNeuronios1aCamadaIntermediaria, taxaAprendizagem);
            // Classificacao do backpropagation normal
            // Fase de Treinamento do backpropagation com momentum
            // Classificacao do backpropagation com momentum
        }
        System.out.println("Houveram "+k+" treinamentos.");
    }    
}
