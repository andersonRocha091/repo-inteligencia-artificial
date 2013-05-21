/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kohonen;

import java.util.Arrays;

/**
 *
 * @author Anderson
 */
public class Neuronio {

    private float[] vetorPesos;
    private int indice;

    public Neuronio(int tamanhoVetor, int indice) {
        inicializarPesos(tamanhoVetor);
        this.indice = indice;
    }

    public void inicializarPesos(int tamanhoVetor) {
        vetorPesos = new float[tamanhoVetor];
        for (int i = 0; i < vetorPesos.length; i++) {
            vetorPesos[i] = (float) Math.random();
        }

    }

    public float[] getVetorPesos() {
        return vetorPesos;
    }

    public void setVetorPesos(float[] vetorPesos) {
        this.vetorPesos = vetorPesos;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    @Override
    public String toString() {
        return ""+indice+"- "+Arrays.toString(vetorPesos); //To change body of generated methods, choose Tools | Templates.
    }   
    
}
