
import java.text.DecimalFormat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nephila
 */
public class AlgoritmoGenetico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int geracoesMax = 200;
        int tamanhoPopulacao = 100;
        int x1 = -1;
        int x2 = 2;

        String[] populacao = gerarPopulacao(tamanhoPopulacao,(float)x1,(float)x2);

        System.out.println(binarioParaFloat("10111101111111000101010110000110"));

    }

    public static String[] gerarPopulacao(int tamanhoPopulacao, float x1, float x2){
        String[] populacao = new String[tamanhoPopulacao];
        for (int i = 0; i < tamanhoPopulacao; i++) {
            float novoIndividuo = (x2+1)*((float)Math.random())+x1;
            DecimalFormat df = new DecimalFormat("0.000000");
            String formatado = df.format((double)novoIndividuo);
            populacao[i] = gerarBinario(Float.parseFloat(formatado.replace(",", ".")));
            //System.out.println("individuo "+i+":"+populacao[i]);//+"; float: "+binarioParaFloat(populacao[i]));
        }        
        return populacao;
    }

    public static float sorteia(float x1, float x2){
        return x2*((float)Math.random())+x1;
    }

    public static String gerarBinario(float num){
        return Integer.toBinaryString(Float.floatToIntBits(num));
    }

    public static float binarioParaFloat(String binario){
        return Float.intBitsToFloat(Integer.parseInt(binario,2));
    }

}
