package projet3D;

import java.util.Arrays;

// Cette classe a pour but d'enregistrer les données sur une structures
// On aura besoin de la position qui definie le point centre de l'objet en question
public class Paire {
    String nomenclature; // Nom de regle
    int[] positions;
    double[] RGBS; // Tableau de Red Green Blue -> pour remplir les couleurs des matériaux;
    int rayon;
    public Paire(String nomenclature, int[] positions, double[] RGBS) {
        this.nomenclature = nomenclature;
        this.positions = positions;
        this.RGBS = RGBS;
    }

    public int getRayon() {
        return rayon;
    }

    public void setRayon(int rayon) {
        this.rayon = rayon;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public int[] getPositions() {
        return positions;
    }

    public void setPositions(int[] positions) {
        this.positions = positions;
    }

    public double[] getRGBS() {
        return RGBS;
    }

    public void setRGBS(double[] RGBS) {
        this.RGBS = RGBS;
    }

    @Override
    public String toString() {
        return "Paire{" +
                "name='" + nomenclature + '\'' +
                ", positions=" + Arrays.toString(positions) +
                ", RGBS=" + Arrays.toString(RGBS) +
                '}';
    }
}
