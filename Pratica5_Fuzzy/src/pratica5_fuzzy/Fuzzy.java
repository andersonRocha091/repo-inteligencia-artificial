package pratica5_fuzzy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Anderson e Maria
 */
public class Fuzzy {

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

    public static void main(String[] args) throws Exception {
        // Definição dos parâmetros do algoritmo
        float taxaAprendizagem = (float) 0.001;
        int qtdNeuronios = 16; // grid topologico 4x4
        int qtdPesosNeuronio = 3;  //pesos em cada neuronio.

        // Leitura de arquivos de entrada
        ArrayList<float[]> entrada = leArquivo("pratica05.fis");

        
    }
}
