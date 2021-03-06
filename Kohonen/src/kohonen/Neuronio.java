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
    private char classe;

    
    

    public Neuronio(int tamanhoVetor, int indice){
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
    public char getClasse() {
        return classe;
    }

    public void setClasse(char classe) {
        this.classe = classe;
    }

    public void setVetorPesos(float[] vetorPesos) {
        this.vetorPesos = vetorPesos;
    }
    public void atualizarPesos(float[] entrada, float taxaAprendizagem){
     for (int i = 0; i < vetorPesos.length; i++) {
            vetorPesos[i] = vetorPesos[i] + taxaAprendizagem *(entrada[i] - vetorPesos[i]);                            
        } 
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
