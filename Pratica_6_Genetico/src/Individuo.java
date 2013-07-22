
import java.util.Arrays;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nephila
 */
public class Individuo implements Comparable<Individuo>{
    private int[] cromossomo;
    private float cromossomoFloat;
    private float aptidao;
    
    public Individuo(int[] cromossomo){
        this.cromossomo = cromossomo;
        this.aptidao = 0;
    }

    public float getCromossomoFloat() {
        return cromossomoFloat;
    }

    public void setCromossomoFloat(float cromossomoFloat) {
        this.cromossomoFloat = cromossomoFloat;
    }
    
    public int[] getCromossomo() {
        return cromossomo;
    }

    public void setCromossomo(int[] cromossomo) {
        this.cromossomo = cromossomo;
    }

    public float getAptidao() {
        return aptidao;
    }

    public void setAptidao(float aptidao) {
        this.aptidao = aptidao;
    }
    
    public int compareTo(Individuo outroIndividuo) {
        if (this.aptidao < outroIndividuo.aptidao) {
            return 1;
        }
        if (this.aptidao > outroIndividuo.aptidao) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public String toString(){
        return "Cromossomo: "+Arrays.toString(this.cromossomo)+", cromossomo float: "+this.cromossomoFloat+", aptidao: "+this.aptidao+".";
    }
}
