package fuzzy;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Anderson e Maria Alice
 */
public class Fuzzy {

    private static double[] discursoTemperatura;
    private static double[] discursoVolume;
    private static double[] discursoPressao;
    private static double[] baixaTemperatura;
    private static double[] mediaTemperatura;
    private static double[] altaTemperatura;
    private static double[] pequenoVolume;
    private static double[] medioVolume;
    private static double[] grandeVolume;
    private static double[] baixaPressao;
    private static double[] mediaPressao;
    private static double[] altaPressao;    
    private static int[][] regrasInferencia;

    public static void main(String[] args) {
        
        // conjuntos fuzzy
        discursoTemperatura = new double[500];
        baixaTemperatura = new double[500];
        mediaTemperatura = new double[500];
        altaTemperatura = new double[500];
        pequenoVolume = new double[500];
        medioVolume = new double[500];
        grandeVolume = new double[500];
        baixaPressao = new double[500];
        mediaPressao = new double[500];
        altaPressao = new double[500];

        // entrada de temperatura e volume
        double[][] entrada = new double[][]{{965.00,11.00},
                                             {920.00,7.6},
                                             {1050.00,6.3},
                                             {843.00,8.6},
                                             {1122.00,5.2}};

        // listas de vetores resultantes em que o alphacorte nao foi igual a 0
        ArrayList<double[]> conjuntosSaidaAlphaCorte = new ArrayList<double[]>();

        // Regras de inferencia armazenadas no formato de matriz
        // baixa = 0, media = 1, alta = 2 para temperatura e pressao
        // pequeno = 3, medio = 4, grande = 5 para volume
        regrasInferencia = new int[][]{{0,3,0}, // 1ª regra - temperatura, volume, pressao
                                       {1,3,0}, // 2ª regra
                                       {2,3,1}, // 3ª regra
                                       {0,4,0}, // 4ª regra
                                       {1,4,1}, // 5ª regra
                                       {2,4,2}, // 6ª regra
                                       {0,5,1}, // 7ª regra
                                       {1,5,2}, // 8ª regra
                                       {2,5,2}, // 9ª regra
                                        };

        // etapas de fuzificação da temperatura, volume e pressao
        fuzificacaoTemperatura();
        fuzificacaoVolume();
        fuzificacaoPressao();

        // armazena pertinencia da entrada referente a todos os conjuntos fuzzy de temperatura,
        // voume e pressao na matriz resultados
        double[][] resultado = calculaPertinenciaEntrada(entrada);        
        imprimeMatrizResultado(resultado);

        // aplicação das regras de inferencia mandani, agregação max e desfuzificação
        for(int i=0; i<5; i++){
            conjuntosSaidaAlphaCorte.clear();
            for(int z=0; z<9; z++){
                // aplicacao da regra de inferencia z
                double alphacorte = Math.min(resultado[i][regrasInferencia[z][0]], resultado[i][regrasInferencia[z][1]]);
                //cria o conjunto resultado após aplicação do alphacorte
                if(alphacorte>0){
                    double[] novoConjunto = criaConjuntoAlphaCorte(alphacorte, retornaConjuntoPressao(regrasInferencia[z][2]));
                    conjuntosSaidaAlphaCorte.add(novoConjunto);
                }
            }
            // agregação nos conjuntos das regras ativadas
            double[] resultadoAgregacao = agregacaoMax(conjuntosSaidaAlphaCorte);
            System.out.println("Resultado da agregacao para a entrada "+i+": \n"+Arrays.toString(resultadoAgregacao).replace(",", ",\n"));

            // desfuzificação
            double resultadoFinal = desfuzificacao(resultadoAgregacao);
            System.out.println("\nPressao desfuzificada referente a entrada "+i+" : "+resultadoFinal+" atm.\n");
        }
    }

    // Desfuzificacao centro de area
    public static double desfuzificacao(double[] resultadoAgregacao){
        double resultadoFinal;
        double acumulaNumerador = 0;
        double acumulaDenominador = 0;
        for (int i = 0; i < resultadoAgregacao.length; i++) {
            acumulaNumerador += discursoPressao[i]*resultadoAgregacao[i];
            acumulaDenominador += resultadoAgregacao[i];
        }
        resultadoFinal = acumulaNumerador/acumulaDenominador;
        return resultadoFinal;
    }

    public static double[] agregacaoMax(ArrayList<double[]> conjuntos){
        double[] vetorFinal = new double[conjuntos.get(0).length];
        double max = 0;
        for (int i = 0; i < vetorFinal.length; i++) {
            if(conjuntos.size()>1){
                max = conjuntos.get(0)[i];
                for (int j = 1; j < conjuntos.size(); j++) {
                    if(max<conjuntos.get(j)[i]){
                        max=conjuntos.get(j)[i];
                    }
                }
                vetorFinal[i]=max;
            }
            else{
                vetorFinal[i]=conjuntos.get(0)[i];
            }
        }
        return vetorFinal;
    }

    public static double[] retornaConjuntoPressao(int indice){
        if(indice==0){
            return baixaPressao;
        }
        else if(indice==1){
            return mediaPressao;
        }
        return altaPressao;
    }

    public static double[] criaConjuntoAlphaCorte(double alphaCorte, double[] conjuntoVelho){
        double[] conjuntoNovo = new double[conjuntoVelho.length];
        for (int i = 0; i < conjuntoNovo.length; i++) {
            if(alphaCorte<conjuntoVelho[i]){
                conjuntoNovo[i]=alphaCorte;
            }
            else{
                conjuntoNovo[i]=conjuntoVelho[i];
            }
        }
        return conjuntoNovo;
    }

    public static void imprimeMatrizResultado(double[][] resultado){
         for (int i = 0; i < resultado.length; i++) {
            System.out.println("\nGraus de pertinencia para entrada "+i);
            System.out.println("baixa temperatura: "+resultado[i][0]);
            System.out.println("media temperatura: "+resultado[i][1]);
            System.out.println("alta temperatura: "+resultado[i][2]);
            System.out.println("pequeno volume: "+resultado[i][3]);
            System.out.println("medio volume: "+resultado[i][4]);
            System.out.println("grande volume: "+resultado[i][5]);            
        }
         System.out.println("");
    }

    public static double[][] calculaPertinenciaEntrada(double[][] entrada){
        double[][] resultado = new double[entrada.length][6];
        for (int i = 0; i < entrada.length; i++) {
            // calculando o grau de pertinencia do conj baixa temperatura para a entrada i de temperatura
            resultado[i][0] = procura(baixaTemperatura, discursoTemperatura, entrada[i][0]);
            // calculando o grau de pertinencia do conj medio temperatura para a entrada i de temperatura
            resultado[i][1] = procura(mediaTemperatura, discursoTemperatura, entrada[i][0]);
            // calculando o grau de pertinencia do conj alto temperatura para a entrada i de temperatura
            resultado[i][2] = procura(altaTemperatura, discursoTemperatura, entrada[i][0]);
            // calculando o grau de pertinencia do conj pequeno volume para a entrada i de volume
            resultado[i][3] = procura(pequenoVolume, discursoVolume, entrada[i][1]);
            // calculando o grau de pertinencia do conj medio volume para a entrada i de volume
            resultado[i][4] = procura(medioVolume, discursoVolume, entrada[i][1]);
            // calculando o grau de pertinencia do conj grande volume para a entrada i de volume
            resultado[i][5] = procura(grandeVolume, discursoVolume, entrada[i][1]);
        }
        return resultado;
    }

    // retorna o grau de pertinencia do conjunto referente a uma entrada
    public static double procura(double[] conjunto, double[] discurso, double entrada){
        int indiceAchou = -1;
        for (int i = 1; i < discurso.length; i++) {
            if(discurso[i]>=entrada){                
                double diferenca1 = discurso[i]-entrada;    
                double diferenca2 = discurso[i-1]-entrada;    
                if(Math.abs(diferenca1)>Math.abs(diferenca2)){ // pega o mais proximo
                    indiceAchou = i-1;
                }
                else{
                    indiceAchou = i;
                }
                break;
            }
        }
        return conjunto[indiceAchou];
    }

    public static void fuzificacaoTemperatura() {        
        double fatorIncremento = (1200.00 - 800.00) / 499.00;

        discursoTemperatura[0] = 800;
        // System.out.println("fator incremento:" + fatorIncremento);
        for (int i = 1; i < 499; i++) {
            discursoTemperatura[i] = discursoTemperatura[i - 1] + fatorIncremento;
        }
        discursoTemperatura[499]=1200.00;
        System.out.println("\nConjunto de Baixa Temperatura: \n");
        baixaTemperatura=trapezoidal(discursoTemperatura, 800.00, 1000.00, 800.00,900.00);
        System.out.println("\nConjunto de Media Temperatura: \n");
        mediaTemperatura = triangular(discursoTemperatura, 900.00, 1100.00, 1000.00);
        System.out.println("\nConjunto de Alta Temperatura: \n");
        altaTemperatura = trapezoidal(discursoTemperatura, 1000.00, 1200.00, 1100.00, 1200.00);
    }

    public static void fuzificacaoVolume() {
        discursoVolume = new double[500];
        double fatorIncremento = (12.00 - 2.00) / 499.00;
        discursoVolume[0] = 2;
        for (int i = 1; i < 499; i++) {
            discursoVolume[i] = discursoVolume[i - 1] + fatorIncremento;
        }
        discursoVolume[499]=12.00;
        System.out.println("\nConjunto de Pequeno Volume:\n" );
        pequenoVolume=trapezoidal(discursoVolume, 2.00, 7.00, 2.00, 4.50);
        System.out.println("\nConjunto de Medio Volume:\n" );
        medioVolume = triangular(discursoVolume, 4.50, 9.50, 7.00);
        System.out.println("\nConjunto de Grande Volume: \n");
        grandeVolume = trapezoidal(discursoVolume, 7.0, 12.00, 9.50, 12.00);
    }

    public static void fuzificacaoPressao() {
        discursoPressao = new double[500];
        double fatorIncremento = (12.00 - 4.00) / 499.00;
        discursoPressao[0] = 4;
        for (int i = 1; i < 499; i++) {
            discursoPressao[i] = discursoPressao[i - 1] + fatorIncremento;
        }
        discursoPressao[499]=12.00;
        System.out.println("\nConjunto de Baixa Pressao:\n" );
        baixaPressao=trapezoidal(discursoPressao, 4.00, 8.00, 4.00, 5.00);
        System.out.println("\nConjunto de Media Pressao:\n" );
        mediaPressao = triangular(discursoPressao, 6.00, 10.00, 8.00);
        System.out.println("\nConjunto de Alta Pressao: \n");
        altaPressao = trapezoidal(discursoPressao, 8.00, 12.00, 11.00, 12.00);
    }
    
    public static double[] trapezoidal(double[] discurso, double a, double b, double m, double n){
        double[] resultado = new double[discurso.length];
        
        for (int i = 0; i < discurso.length; i++) {
                if (discurso[i] < a) {
                    resultado[i] = 0;
                } else if (discurso[i] >= a && discurso[i] < m) {
                    //resultado[i]=discurso[i]-a
                    resultado[i]=(discurso[i]-a)/(m-a);
                } else if (discurso[i] >= m && discurso[i] <= n) {
                    resultado[i] = 1;
                } else if (discurso[i] > n && discurso[i] <= b) {
                    //System.out.println("discurso: "+discurso[i]);
                    resultado[i] = (b - discurso[i]) / (b - n);
                }
                else if(discurso[i]>b){
                    
                    resultado[i]=0;
                }
                //System.out.println(discurso[i]+", "+resultado[i]);
                //System.out.println(resultado[i]+",");
                System.out.println("Discurso: "+discurso[i]+", Pertinencia: "+resultado[i]);
            }
        return resultado;
    }
    
    public static double[] triangular(double[] discurso, double a, double b, double m){
        double[] resultado = new double[discurso.length];
        
        for (int i = 0; i < discurso.length; i++) {
                if (discurso[i] <= a) {
                    resultado[i] = 0;
                } else if (discurso[i] > a && discurso[i] <= m) {
                    resultado[i]=(discurso[i]-a)/(m-a);
                } else if (discurso[i] > m && discurso[i] <= b) {
                    resultado[i]=(b-discurso[i])/(b-m);
                }
                else if(discurso[i]>b){
                    resultado[i]=0;
                }
             System.out.println("Discurso: "+discurso[i]+", Pertinencia: "+resultado[i]);
             // System.out.println(resultado[i]+",");
            }
        return resultado;   
    }
}
