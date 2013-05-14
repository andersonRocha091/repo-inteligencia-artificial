package perceptronmulticamadas;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Anderson e Maria
 */
public class PerceptronMultiCamadas {

    // variaveis globais
    private static ArrayList<float[]> entrada;
    private static ArrayList<float[]> entradaTeste;
    private static ArrayList<float[]> valoresDesejados;
    private static ArrayList<float[]> valoresDesejadosTeste;

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
    
    public static ArrayList<float[]> classificacaoAmostras(ArrayList<float[][]> pesosCamadas,int[] qtdNeuronios, int qtdCamadaIntermediaria) {                
        ArrayList<float[]> saidasCamadas;
        ArrayList<float[]> saidaClassificacao = new ArrayList<float[]>();
        ArrayList<float[]> derivadaSigmoidI;
        boolean ehUltimaCamada = false;        
        for (int k = 0; k < entradaTeste.size(); k++) { //laco para cada padrao k de amostras
            saidasCamadas = new ArrayList<float[]>();
            derivadaSigmoidI = new ArrayList<float[]>();
            //Fase forward backpropagation            
//            System.out.println("entradaTeste.get("+k+") :\n"+Arrays.toString(entradaTeste.get(k))+"\n");
            float[] I = combinacaoMatrizes(pesosCamadas.get(0), entradaTeste.get(k), qtdCamadaIntermediaria); // primeira camada
            saidasCamadas.add(g(I, ehUltimaCamada));
            derivadaSigmoidI.add(glinha(I)); //calculando g'(I) para a primeira camada
            //System.out.println("Camada 0: \n" + Arrays.toString(I) + "\n" + "Saída da camada 0: \n" + Arrays.toString(saidasCamadas.get(0)) + "\n");
            // camadas intermediarias e a final
            for (int i = 1; i < pesosCamadas.size(); i++) {
                if (i == (pesosCamadas.size() - 1)) {
                    ehUltimaCamada = true;
                }                
                I = combinacaoMatrizes(pesosCamadas.get(i), saidasCamadas.get(i - 1), qtdNeuronios[i]);
                saidasCamadas.add(g(I, ehUltimaCamada));
                derivadaSigmoidI.add(glinha(I)); //calculando g'(I) para as camadas intermediarias e primeira
                //System.out.println("Camada " + i + ":\n " + Arrays.toString(I) + "\n" + "Saída da camada " + i + ":\n " + Arrays.toString(saidasCamadas.get(i)) + "\n");                    
                for (int j = 0; j < derivadaSigmoidI.size(); j++) {
                     //System.out.println("G'(I) da camada "+j+":"+Arrays.toString(derivadaSigmoidI.get(j)));
                }
            }
            ehUltimaCamada = false;
            saidaClassificacao.add(saidasCamadas.get(saidasCamadas.size()-1));

        }
        return saidaClassificacao; //retorna a saída da última camada
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
            float[] lineFloat = new float[line.length- qtdValoresDesejados];
            for (int i = 0; i < line.length - qtdValoresDesejados; i++) {
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
    
    public static void leArquivoTeste(String path, int qtdValoresDesejados) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(path)));
        entradaTeste = new ArrayList<float[]>();
        valoresDesejadosTeste = new ArrayList<float[]>();
        String[] line = null;
        String temp;
        while ((temp = reader.readLine()) != null) {
            line = temp.split("\\ ");
            float[] lineFloat = new float[line.length- qtdValoresDesejados];
            for (int i = 0; i < line.length - qtdValoresDesejados; i++) {
                lineFloat[i] = Float.parseFloat(line[i]);
            }
            entradaTeste.add(lineFloat);
            lineFloat = new float[qtdValoresDesejados];
            int j = 0;
            for (int i = line.length - qtdValoresDesejados; i < line.length; i++) {
                lineFloat[j++] = Float.parseFloat(line[i]);
            }
            valoresDesejadosTeste.add(lineFloat);
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
            pesosCamadas.add(gerarMatrizPeso(qtdNeuronios[i], qtdNeuronios[i - 1]+1));
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
                acumula[i] += pesosCamadaAnterior[i][j] * gradienteAnterior[i];
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

    public static float[][] novoPeso(float[][] pesoAnterior, float taxaAprendizagem,
            float[] gradiente, float[] saidaCamada, boolean comMomentum) {
        float[][] pesosAtualizado = new float[pesoAnterior.length][pesoAnterior[0].length];
        for (int i = 0; i < pesoAnterior.length; i++) {
            for (int j = 0; j < pesoAnterior[0].length; j++) {
                if(comMomentum){ // adiciona fator momentum na atualizacao
                    // Wji(t+1) = Wji(t) + α (Wji(t) - Wji(t-1)) + η . δj . Yj
                    // 0 ≤ α ≥ 0,9                    
                    pesosAtualizado[i][j] = pesoAnterior[i][j] + taxaAprendizagem * gradiente[i] * saidaCamada[j];
                }
                else{
                    pesosAtualizado[i][j] = pesoAnterior[i][j] + taxaAprendizagem * gradiente[i] * saidaCamada[j];
                }
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
            float taxaAprendizagem, boolean comMomentum) {
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
             //   System.out.println("entrada0: "+Arrays.toString(entrada.get(k)));
                float[] I = combinacaoMatrizes(pesosCamadas.get(0), entrada.get(k), qtdCamadaIntermediaria); // primeira camada
             // System.out.println("saida I0:"+Arrays.toString(I));
                saidasCamadas.add(g(I, ehUltimaCamada));
            //  System.out.println("saida Y(I0):"+Arrays.toString(saidasCamadas.get(0)));
                derivadaSigmoidI.add(glinha(I)); //calculando g'(I) para a primeira camada
                //System.out.println("Camada 0: \n" + Arrays.toString(I) + "\n" + "Saída da camada 0: \n" + Arrays.toString(saidasCamadas.get(0)) + "\n");
                // camadas intermediarias e a final
                for (int i = 1; i < pesosCamadas.size(); i++) {
                    if (i == (pesosCamadas.size() - 1)) {
                        ehUltimaCamada = true;
                    }
                    I = combinacaoMatrizes(pesosCamadas.get(i), saidasCamadas.get(i - 1), qtdNeuronios[i]);
              //  System.out.println("saida I"+i+Arrays.toString(I));
                    saidasCamadas.add(g(I, ehUltimaCamada));
             // System.out.println("saida Y(I"+i+"): "+Arrays.toString(saidasCamadas.get(i)));    
                    derivadaSigmoidI.add(glinha(I)); //calculando g'(I) para as camadas intermediarias e primeira
                    //System.out.println("Camada " + i + ":\n " + Arrays.toString(I) + "\n" + "Saída da camada " + i + ":\n " + Arrays.toString(saidasCamadas.get(i)) + "\n");                    
//                    for (int j = 0; j < derivadaSigmoidI.size(); j++) {
                         //System.out.println("G'(I) da camada "+j+":"+Arrays.toString(derivadaSigmoidI.get(j)));
//                    }
                }
                ehUltimaCamada = false;
                
//                System.out.println("Saida das camadas da rede para amostra "+k+"\n: "+arraylistToString(saidasCamadas)+"\n");
//                System.out.println("Matrizes de Pesos para amostra "+k+"\n: "+arraylistMatrizToString(pesosCamadas)+"\n");

                // calculo de E(k) e 'quase EQM'
                float E = E(saidasCamadas.get(saidasCamadas.size() - 1), valoresDesejados.get(k));
               // System.out.println("Y(I1): "+Arrays.toString(saidasCamadas.get(saidasCamadas.size() - 1)));
               // System.out.println("desejados: "+Arrays.toString(valoresDesejados.get(k)));
               // System.out.println("Erro e(k):"+E);
                EQM_atual += E;
                //System.out.println("\nErro E(k): " + E + "\n");

                // Fase Backward                
                //calculo gradiente ultima camada e atualizacao de pesos da ultima camada
                gradienteLast = gradienteUltimaCamada(valoresDesejados.get(k), saidasCamadas.get(saidasCamadas.size()-1), derivadaSigmoidI.get(derivadaSigmoidI.size()- 1));
               // System.out.println("Gradiente Ultima Camada:"+Arrays.toString(gradienteLast));
                //System.out.println("Gradiente ultima camada:\n"+Arrays.toString(gradienteLast));
                pesosCamadas.set(pesosCamadas.size() - 1, novoPeso(pesosCamadas.get(pesosCamadas.size()-1), taxaAprendizagem, gradienteLast, saidasCamadas.get(saidasCamadas.size()-2),comMomentum));
               //  System.out.println("Matriz pesos ultima camada atualizada: \n"+Arrays.deepToString(pesosCamadas.get(pesosCamadas.size()-1)).replaceAll("],", "],\n")+"\n");
                //calculo gradiente camadas intermediarias e atualizacao de pesos camadas intermediarias
                for (int i = pesosCamadas.size() - 2; i >= 1; i--) { // o pesosCamas.size - 2 é pra garantir que não começamos pela ultima camada.
                    gradiente = gradienteCamadasIntermediarias(gradienteLast, pesosCamadas.get(i + 1), derivadaSigmoidI.get(i));
                 //   System.out.println("Gradiente camada "+i+"\n: " + Arrays.toString(gradiente));
                    pesosCamadas.set(i, novoPeso(pesosCamadas.get(i), taxaAprendizagem, gradiente, saidasCamadas.get(i-1),comMomentum));
                  //  System.out.println("Matriz pesos camada "+i+" atualizada:\n"+Arrays.deepToString(pesosCamadas.get(i)).replaceAll("],", "],\n"));
                    gradienteLast = gradiente; // guarda o ultimo gradiente para o calculo do proximo
                }
                //calculo gradiente da primeira camada e atualizacao de pesos da primeira camada
                gradiente = gradienteCamadasIntermediarias(gradienteLast, pesosCamadas.get(1), derivadaSigmoidI.get(0));
              // System.out.println("Gradiente 1a camada:\n" + Arrays.toString(gradiente));
               //revisar calculo do gradiente das outras camadas.
                pesosCamadas.set(0, novoPeso(pesosCamadas.get(0), taxaAprendizagem, gradiente, entrada.get(k),comMomentum));
              // System.out.println("Matriz pesos 1a camada atualizada:\n"+Arrays.deepToString(pesosCamadas.get(0)).replaceAll("],", "],\n")+"\n");

                //atualizacao vetor de pesos das outras camadas
                derivadaSigmoidI.clear();
                saidasCamadas.clear();
            }
            // calculo EQM
            EQM_atual /= entrada.size();
            //System.out.println("EQM Atual: "+EQM_atual);
            //System.out.println("EQM Anterior: "+EQM_anterior+"\n");
            epocas++;
        }
        System.out.println("O treinamento teve "+epocas+" epocas.\n");
        return pesosCamadas;
    }
    
    // Pos-processa as saidas dos testes para normaliza-las
    public static ArrayList<float[]> posProcessar(ArrayList<float[]> resultadoAntes){
        ArrayList<float[]> resultadoPosProcessado = new ArrayList<float[]>();
        float[] novo;
        for (int i = 0; i < resultadoAntes.size(); i++) {
            novo = new float[resultadoAntes.get(0).length];
            for (int j = 0; j < resultadoAntes.get(0).length; j++) {
                // 1 , se y i ≥ 0.5                    
                if(resultadoAntes.get(i)[j] >= (float) 0.5){
                    novo[j] = 1;
                }
                // 0, se y i < 0.5
                else{
                    novo[j] = 0;
                }                
            }  
            resultadoPosProcessado.add(novo);
        }
        return resultadoPosProcessado;
    }

    public static void main(String[] args) throws Exception {        
        // Definição dos parâmetros do algoritmo
        int qtdTreino = 3;
        int qtdEntrada = 5; //4 entradas + 1 entrada do bias
        int[] qtdNeuronios = {5,3};
        int qtdValoresDesejados = 3;
        int qtdNeuronios1aCamadaIntermediaria = 5; // qtd neuronios na 1a camada intermediaria
        float erro = (float) 0.000001;
        float taxaAprendizagem = (float) 0.1;

        // Leitura de arquivos de entrada        
        leArquivo("treina.txt", qtdValoresDesejados);
        leArquivoTeste("teste.txt", qtdValoresDesejados);       

        // Variaveis para armazenar saidas do treinamento e da classificacao
        ArrayList<float[][]> resultadoBackNormal; // arraylist pesos resultado treinamento normal
        ArrayList<float[][]> resultadoBackMomentum; // arraylist pesos resultado treinamento momentum
        ArrayList<float[]> resultadoClassificacaoNormal; // saida da ultima camada rede teste
        ArrayList<float[]> resultadoClassificacaoMomentum; //saida da ultima camada rede teste momentum
                
        // Loop principal treinamentos + classificacoes      
        for (int k = 0; k < qtdTreino; k++) {
            //int[] qtdNeuronios = montaVetorCamadas(k);
            if(k==1){
                qtdNeuronios[0]=10; // segundo treinamento com 10 neuronios camada intermediaria
                qtdNeuronios1aCamadaIntermediaria = 10;
            }
            if(k==2){
                qtdNeuronios[0]=15; // terceiro treinamento com 15 neuronios camada intermediaria
                qtdNeuronios1aCamadaIntermediaria=15;
            }

            // Inicializacao das matrizes de pesos         
            ArrayList<float[][]> pesosInicial = inicializarRede(qtdNeuronios, qtdNeuronios.length, qtdEntrada);  
            System.out.print("Matriz peso inicial:\n"+arraylistMatrizToString(pesosInicial));
            
            System.out.println("---------  Treinamento " + k + "  -----------\n");            
            System.out.println("Treinamento Backpropagation Normal\n");
            // Fase de Treinamento backpropagation normal; new ArrayList...pesosInicial para passar por valor e não referencia
            resultadoBackNormal = backpropagation(new ArrayList<float[][]>(pesosInicial), erro, qtdNeuronios, qtdValoresDesejados, qtdNeuronios1aCamadaIntermediaria, taxaAprendizagem, false); 
            // Exibe resultado do treinamento
            System.out.print("Resultado Treinamento Back Normal:\n"+arraylistMatrizToString(resultadoBackNormal));
            // Classificacao do backpropagation normal
            resultadoClassificacaoNormal = classificacaoAmostras(resultadoBackNormal, qtdNeuronios, qtdNeuronios1aCamadaIntermediaria);
            // Exibe Classificacao Back Normal
            System.out.println("Resultado Classificacao Back Normal:\n"+arraylistToString(resultadoClassificacaoNormal)+"\n");
            // Exibe Classificacao Pos-processada Back Momentum
            System.out.println("Resultado Normal PosProcessado:\n"+arraylistToString(posProcessar(resultadoClassificacaoNormal))+"\n");

            System.out.println("Treinamento Backpropagation com Momentum\n");
            // Fase de Treinamento do backpropagation com momentum; new ArrayList...pesosInicial para passar por valor e não referencia
            resultadoBackMomentum = backpropagation(new ArrayList<float[][]>(pesosInicial), erro, qtdNeuronios, qtdValoresDesejados, qtdNeuronios1aCamadaIntermediaria, taxaAprendizagem, true);
            // Exibe resultado do treinamento
            System.out.println("Resultado Treinamento Back Momentum:\n"+arraylistMatrizToString(resultadoBackMomentum)+"\n");
            // Classificacao do backpropagation com momentum
            resultadoClassificacaoMomentum = classificacaoAmostras(resultadoBackMomentum, qtdNeuronios, qtdNeuronios1aCamadaIntermediaria);
            // Exibe Classificacao Back Momentum
            System.out.println("Resultado Classificacao Back Momentum:\n"+arraylistToString(resultadoClassificacaoMomentum)+"\n");
            // Exibe Classificacao Pos-processada Back Momentum
            System.out.println("Resultado Momentum PosProcessado:\n"+arraylistToString(posProcessar(resultadoClassificacaoMomentum))+"\n");
            
            // limpando as variaveis para o novo treinamento
            resultadoBackNormal.clear(); 
            resultadoBackMomentum.clear(); 
            resultadoClassificacaoNormal.clear();
            resultadoClassificacaoMomentum.clear();
        }        
    }    
}
