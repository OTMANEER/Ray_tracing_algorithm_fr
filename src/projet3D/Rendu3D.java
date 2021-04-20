package projet3D;

import java.io.FileOutputStream;
import java.io.IOException;

public class Rendu3D
{
    static int nbpixelsTouche=0;
    static int nbpixelsNonTouche=0;
    private static final Logger log = new Logger();
    private static final int CAMERA = -999;

    // la fonction principale L'algo


    public static FileOutputStream ecrire_image(String nom,Scene scene) throws IOException {
        FileOutputStream image = new FileOutputStream(nom);
        int padding = (4 - scene.largeur * 3 % 4) % 4;

        int heightImage = (scene.largeur * 3 + padding) * scene.hauteur;
        int size = 54 + heightImage;
        image.write(new byte[] {'B', 'M'});
        image.write(size & 0x000000FF);
        image.write((size & 0x0000FF00) >>> 8);
        image.write((size & 0x00FF0000) >>> 16);
        image.write((size & 0xFF000000) >>> 24);
        image.write(new byte[] {0, 0, 0, 0});
        image.write(new byte[] {0x36, 0, 0, 0});
        image.write(new byte[] {0x28, 0, 0, 0});
        image.write(scene.largeur & 0x000000FF);
        image.write((scene.largeur & 0x0000FF00) >>> 8);
        image.write((scene.largeur & 0x00FF0000) >>> 16);
        image.write((scene.largeur & 0xFF000000) >>> 24);
        image.write(scene.hauteur & 0x000000FF);
        image.write((scene.hauteur & 0x0000FF00) >>> 8);
        image.write((scene.hauteur & 0x00FF0000) >>> 16);
        image.write((scene.hauteur & 0xFF000000) >>> 24);
        image.write(new byte[] {1, 0});    // premier couleur
        image.write(new byte[] {0x18, 0}); // 24 bits par pixel
        image.write(new byte[] {0, 0, 0, 0});
        image.write(heightImage & 0x000000FF);
        image.write((heightImage & 0x0000FF00) >>> 8);
        image.write((heightImage & 0x00FF0000) >>> 16);
        image.write((heightImage & 0xFF000000) >>> 24);
        image.write(new byte[] {0x13, 0x0B, 00, 00}); //  resolution Horisontale
        image.write(new byte[] {0x13, 0x0B, 00, 00}); // resolution verticale
        image.write(new byte[] {0, 0, 0, 0});  //nombre couleurs
        image.write(new byte[] {0, 0, 0, 0});  //  toutes les couleurs sont importantes
        return image;
    }
    // la couleur du pixel  sur la caméra
    public static Couleur couleur_sur_camera(Rayon rayon, Scene scene){
        Couleur outputCouleur = new Couleur(0, 0, 0);// Noir
        double coefficient = 1;
        int zIndex = 0;
        do {
            // Premier objet intersecté par le rayon
            Scene.Sphere sphere = null;
            double distance = 2000.0f;
            // trouver l'objet en question
            for (Scene.Sphere s : scene.lesSpheres) {
                Intersection inter = toucheObjet(rayon, s, distance);
                distance = inter.distance;
                if (inter.intersecte) { // true? Ok c'est sauvegarder l'objet
                    nbpixelsTouche++;
                    sphere = s;
                }
            }
            // le boolean est false? Arreter!
            if (sphere == null) {
                nbpixelsNonTouche++;
                break;
            }
            // Le point d'intersection
            Point pointIntersection =Vecteur.ajouter(rayon.origine,Vecteur.produit_valeur(distance, rayon.direction));
            // Vecteur normale sur la surface de l'objet
            Vecteur vectNormale = new Vecteur(sphere.point, pointIntersection);
            try {
                vectNormale.normaliser();          //normaliser le.
            } catch (ArithmeticException e) {
                break; //impossible la norme est nulle
            }
            Scene.MaterielPrincipale currentMaterielPrincipale = sphere.materiel;
            for (Scene.Lumiere lumiere : scene.lumieres) {
                // Rayon lumineux
                Rayon rayonLumineux = new Rayon(pointIntersection,new Vecteur(pointIntersection, lumiere.origine_light));
                // produit scalaire entre le rayon et le vecteur normale sur la surface de l'objet
                double projectionLumiere = Vecteur.produit_scalaire(rayonLumineux.direction, vectNormale);
                //entre les deux vecteurs, Ou bien confondu avec une des deux
                if (projectionLumiere <= 0.0)
                    continue;

                double distanceLumiere = rayonLumineux.direction.norme();
                rayonLumineux.direction.normaliser();
                projectionLumiere /= distanceLumiere;

                boolean surOmbre = false;
                for (Scene.Sphere s : scene.lesSpheres) {
                    Intersection inter = toucheObjet(rayonLumineux, s, distanceLumiere);
                    if (inter.intersecte) {
                        surOmbre = true;
                        break;
                    }
                }

                if (!surOmbre)
                {
                    // coeffecient de Lambert
                    double lambert = Vecteur.produit_scalaire(rayonLumineux.direction, vectNormale) * coefficient;

                    outputCouleur.ajouter_couleur(Couleur.multiplier_couleurs(lambert, Couleur.multiplier_couleurs(
                        lumiere.couleur_light, currentMaterielPrincipale.diffusion)));

                    // Ombrage de Phong
                    Vecteur v = Vecteur.soustraire(rayonLumineux.direction, rayon.direction);
                    double norm = v.norme();
                    if (norm != 0.0) {

                        double projectionVisible =
                                Vecteur.produit_scalaire(rayon.direction, vectNormale);
                        double phongOmbrage =Math.max(projectionLumiere - projectionVisible, 0.0)/ norm;
                        //(Coefficient*R)^alpha
                        //https://en.wikipedia.org/wiki/Blinn%E2%80%93Phong_reflection_model
                        phongOmbrage = coefficient * Math.pow(phongOmbrage,currentMaterielPrincipale.intensite);
                        Couleur c = Couleur.multiplier_couleurs(phongOmbrage, currentMaterielPrincipale.refraction);
                        c = Couleur.multiplier_couleurs(c, lumiere.couleur_light);
                        outputCouleur.ajouter_couleur(c); // blinn-phong
                    }
                }
            }
            coefficient *= currentMaterielPrincipale.reflection;
            // R1 incident, R2 le vecteur normale alorsle rayon reflechi et R1-2*D.R2*R2
            Vecteur projectionAlongNormal = Vecteur.produit_valeur(
                    Vecteur.produit_scalaire(rayon.direction, vectNormale), vectNormale);
            //Le rayon refléchi
            rayon.origine = pointIntersection;
            rayon.direction = Vecteur.soustraire(rayon.direction,Vecteur.produit_valeur(2, projectionAlongNormal));
            zIndex++;
        } while ((coefficient > 0) && (zIndex < 10));
        return outputCouleur;
    }

    // Intersection de Rayon emis avec l'objet en question
    private static class Intersection {
        // distance entre l'origine de rayon et le point d'intersection
        double distance;
        boolean intersecte;// Avant de proceder est ce qu'il intersecte déjà


       // Distance entre l'origine de rayon et le point d'intersection
        Intersection(double distance, boolean intersecte){
            this.distance = distance;
            this.intersecte = intersecte;
        }
    }

    private static void image_finale(String fichierSortie, Scene scene) throws IOException
    {
        FileOutputStream f = ecrire_image(fichierSortie,scene);
        int padding = (4 - scene.largeur * 3 % 4) % 4;
        log.log("Algorithme en cours ...");

        // Envoyer un rayon d'apres chaque pixel de la camera et calculer la couleur
        for (int y = 0; y < scene.hauteur; y++) {
            for (int x = 0; x < scene.largeur; x++) {

                // commencer par un pixel noir, ajouter les couleurs en determinant les rayon qui traversent ce pixel
                Couleur couleurFinale = new Couleur(0, 0, 0);
                for (double dx = x; dx < x + 1; dx += 0.5) {
                    for (double dy = y; dy < y + 1; dy += 0.5) {


                        double pourcentage = 0.25;// chaque couleur contribue par 1/4 dans la couleur finale
                        Rayon rayonvue = new Rayon(new Point(dx, dy, CAMERA),new Vecteur(0, 0, 1));
                        Couleur couleur = couleur_sur_camera(rayonvue, scene);
                        couleur.exposure(1.0);
                        couleurFinale.ajouter_couleur(Couleur.multiplier_couleurs(pourcentage, couleur));
                    }
                }

                // Gamma correction
                couleurFinale.correction_gamma();
                // Ecrire les bytes d'un pixels sur l'image
                f.write(couleurFinale.convertBytes());// write n'accepte que des bytes
            }
            // Pour tout pixel sur l'image rembourrer le par des 0, multiple de 4.
            for (int i = 0; i < padding; i++) {
                f.write(0);
            }

        }

        log.log("Algorithme terminé. ");
        log.log(nbpixelsTouche+" pixels touchés");
        log.log(nbpixelsNonTouche+" pixels non touchés");
        f.close();
        log.log("Au revoir, vérifiez " + fichierSortie + "!");
    }
//est ce que le rayon intersecte l'objet, est ce que la distance entrer ce point d'inter et l'origine du rayon est moins que cette distance
    // Obligation
    private static Intersection toucheObjet(Rayon rayon, Scene.Sphere sphere,
                                            double distance)
    {
        Vecteur d = new Vecteur(rayon.origine, sphere.point);

        double a = Vecteur.produit_scalaire(rayon.direction, d);

        double teta = a * a - Vecteur.produit_scalaire(d, d)+sphere.rayon * sphere.rayon;

        if (teta < 0.0) {
            return new Intersection(distance, false); // Pas d'intersection
        }
        double debut = a - Math.sqrt(teta);
        double fin = a + Math.sqrt(teta);

        if (debut > 0.11 && debut < distance) {
            return new Intersection(debut, true);
        }

        if (fin > 0.11 && fin < distance) {
            return new Intersection(fin, true);
        }

        return new Intersection(distance, false);
    }
    public static void main(String[] args) throws Exception
    {
        String fichierSortie = "sortie.bmp";
        long start = System.currentTimeMillis();
        Scene scene = new Scene();
        image_finale(fichierSortie, scene);
        long end = System.currentTimeMillis();
        log.log(" Sur: "+ (end-start)/1000+" Seconds");
    }
}
