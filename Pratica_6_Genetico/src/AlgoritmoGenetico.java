
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

        int[][] populacao = gerarPopulacao(tamanhoPopulacao);

        //System.out.println(binarioParaFloat("10111101111111000101010110000110"));

       
        float testes = (float) 3.56;
        Byte by = Byte.valueOf(Float.toString(testes));
       // String bin = by.

    }

    public static int[][] gerarPopulacao(int tamanhoPopulacao){
        int[][] populacao = new int[tamanhoPopulacao][22];
        for (int i = 0; i < tamanhoPopulacao; i++) {
            for (int j = 0; j < populacao.length; j++) {
                populacao[i][j] = sorteia();
                
            }
        }        
        return populacao;
    }

    public static int sorteia(){
        double sorteio = Math.random();
        int x;
        if(sorteio<0.5)
            x = 0;        
        else
            x = 1;                
        return x; 
    }
    
    public static float[] converterPopulacaoBinarioFloat(int[][] populacao){
        float[] populacaoFloat = new float[22];
        float acumula = 0;
        for (int i = 0; i < populacao.length; i++) {
            for (int j = 0; j < populacaoFloat.length; j++) {
                acumula += populacao[i][j]*Math.pow(2,j);
            }
            populacaoFloat[i] = - 1 + acumula*(3/((float)Math.pow(2,22)-1)); 
            acumula = 0;
        }
        return populacaoFloat;
    }

    public static String gerarBinario(float num){
        return Integer.toBinaryString(Float.floatToIntBits(num));
    }

    public static double binarioParaFloat(String binario){
        return Float.intBitsToFloat(Integer.parseInt(binario,2));
       // return Double.longBitsToDouble(Long.parseLong(binario,2));
    }

}
