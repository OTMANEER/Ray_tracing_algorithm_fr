package projet3D;

// Vecteur 3D
public class Vecteur
{
    private double xAxe;
    private double yAxe;
    private double zAxe;
    // Créer un vecteur simple  sur un repere (O,i,j,k) les deux axes largeur et hauteur sont x,y et z est tjs perpendiculaire
    public Vecteur(double xAxe, double yAxe, double z)
    {
        this.xAxe = xAxe;
        this.yAxe = yAxe;
        this.zAxe = z;
    }

    /* Créer un  vecteur avec un point initiale et finale
    P1(x,y,z) et P2(x,y,z)
    */
    public Vecteur(Point p1, Point p2){
        this.xAxe = p2.x - p1.x;
        this.yAxe = p2.y - p1.y;
        this.zAxe = p2.z - p1.z;
    }
    /*
    La norme  du vecteur actuel
     */
    public double norme(){
        return Math.sqrt((this.xAxe * this.xAxe) + (this.yAxe * this.yAxe) + (this.zAxe * this.zAxe));
    }

    // point finale d'un vecteur, p est le point initiale
    public static Point ajouter(Point p, Vecteur v){
        return new Point(p.x + v.xAxe, p.y + v.yAxe, p.z + v.zAxe);
    }

    public static Vecteur ajouter(Vecteur vect1, Vecteur v2)
    {
        return new Vecteur(vect1.xAxe + v2.xAxe,vect1.yAxe + v2.yAxe,vect1.zAxe + v2.zAxe);
    }

 // soustraire deux vecteurs
    public static Vecteur soustraire(Vecteur vect1, Vecteur v2)
    {
        return new Vecteur(vect1.xAxe - v2.xAxe,vect1.yAxe - v2.yAxe,vect1.zAxe - v2.zAxe);
    }

    // Produit scalaire d'un vecteur par un valeur
    //chaque cordonée * le scalaire
    public static Vecteur produit_valeur(double c, Vecteur v)
    {
        return new Vecteur(c * v.xAxe, c * v.yAxe, c * v.zAxe);
    }
    /*
    Normaliser le vecteur
    ----> diviser sur la norme les cordonnées.
     */
    public void normaliser(){
        double norm = norme();
        if (norm == 0) {
            throw new ArithmeticException("La Norme = 0 !!!!!!");
        }
        xAxe = xAxe /norm;
        yAxe = yAxe /norm;
        zAxe =zAxe/norm;
    }
    public static double produit_scalaire(Vecteur vect1, Vecteur v2)
    {
        return vect1.xAxe * v2.xAxe +vect1.yAxe * v2.yAxe +vect1.zAxe * v2.zAxe;
    }
}
