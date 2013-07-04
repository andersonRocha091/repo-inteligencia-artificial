/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzy;

import java.util.Arrays;

/**
 *
 * @author Anderson
 */
public class Fuzzy {

    /**
     * @param args the command line arguments
     */
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

    public static void main(String[] args) {
        fuzificacaoTemperatura();
        fuzificacaoVolume();
        fuzificacaoPressao();
    }

    public static void fuzificacaoTemperatura() {

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
        
        double fatorIncremento = (1200.00 - 800.00) / 499.00;

        discursoTemperatura[0] = 800;
      //  System.out.println("fator incremento:" + fatorIncremento);
        for (int i = 1; i < 499; i++) {
            discursoTemperatura[i] = discursoTemperatura[i - 1] + fatorIncremento;
        }
        discursoTemperatura[499]=1200.00;
        //gerando o conjunto fuzzy para baixa temperatura
       // System.out.println("\n Baixa temperatura: \n");
        baixaTemperatura=trapezoidal(discursoTemperatura, 800.00, 1000.00, 800.00,900.00);
        //System.out.println("baixaTemperatura:"+ Arrays.toString(baixaTemperatura).replaceAll(",", ",\n"));
       mediaTemperatura = triangular(discursoTemperatura, 900.00, 1100.00, 1000.00);
      //  System.out.println("\n alta temperatura: \n");
       altaTemperatura = trapezoidal(discursoTemperatura, 1000.00, 1200.00, 1100.00, 1200.00);
        

        // System.out.println("vetorTemperatura:"+ Arrays.toString(discursoTemperatura).replaceAll(",", ",\n"));
    }

    public static void fuzificacaoVolume() {

        discursoVolume = new double[500];
        double fatorIncremento = (12.00 - 2.00) / 499.00;

        discursoVolume[0] = 2;
      //  System.out.println("fator incremento:" + fatorIncremento);
        for (int i = 1; i < 499; i++) {

            discursoVolume[i] = discursoVolume[i - 1] + fatorIncremento;

        }
        discursoVolume[499]=12.00;
        System.out.println("\npequeno volume:\n" );
        pequenoVolume=trapezoidal(discursoVolume, 2.00, 7.00, 2.00, 4.50);
        System.out.println("\n medio volume:\n" );
       medioVolume = triangular(discursoVolume, 4.50, 9.50, 7.00);
       System.out.println("\n grande Volume: \n");
       grandeVolume = trapezoidal(discursoVolume, 7.0, 12.00, 9.50, 12.00);
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
                System.out.println(discurso[i]+", "+resultado[i]);
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
             System.out.println(discurso[i]+", "+resultado[i]);
            }
        return resultado;
        
    }

    public static void fuzificacaoPressao() {

        discursoPressao = new double[500];
        double fatorIncremento = (12.00 - 4.00) / 499.00;

        discursoPressao[0] = 4;
      //  System.out.println("fator incremento:" + fatorIncremento);
        for (int i = 1; i < 499; i++) {

            discursoPressao[i] = discursoPressao[i - 1] + fatorIncremento;

        }
        discursoPressao[499]=12.00;
        
        System.out.println("\nbaixa pressao:\n" );
        baixaPressao=trapezoidal(discursoPressao, 4.00, 8.00, 4.00, 5.00);
        System.out.println("\n media pressao:\n" );
       mediaPressao = triangular(discursoPressao, 6.00, 10.00, 8.00);
       System.out.println("\n alta Pressao: \n");
       altaPressao = trapezoidal(discursoPressao, 8.00, 12.00, 11.00, 12.00);
        // System.out.println("vetorPressao:"+ Arrays.toString(discursoPressao).replaceAll(",", ",\n"));
    }
}
