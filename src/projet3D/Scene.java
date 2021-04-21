package projet3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// La scene  de l'algorithme
public class Scene
{
    public List<Sphere> lesSpheres;
    // Les sources de la lumière
    public List<Lumiere> lumieres;
    public int largeur;
    public int hauteur;
    // RGB de l'bjet -> Sphere dans ce cas
    public static class MaterielPrincipale {
        public Couleur diffusion;// Couleur principale
        public double reflection;
        public Couleur refraction;
        public double intensite;//Intensité relative des premiers rayons réfléchis

       public MaterielPrincipale(Couleur diffusion, double reflection, Couleur refraction, double intensite) {
            this.diffusion = diffusion;
            this.refraction = refraction;
            this.reflection = reflection;
            this.intensite = intensite;
        }
    }

    // La classe Sphere -> L'objet principale
    public class Sphere
    {
        public Point point;
        public double rayon;
        public MaterielPrincipale materiel;

        public Sphere(Point point, double rayon, MaterielPrincipale materiel)
        {
            this.point = point;
            this.rayon = rayon;
            this.materiel = materiel;
        }
    }

    // Source de la lumière dans la scene
    public class Lumiere
    {
        public Point origine_light;
        public Couleur couleur_light;
        public Lumiere(Point origine_light, Couleur couleur_light)
        {
            this.origine_light = origine_light;
            this.couleur_light = couleur_light;
        }
    }

        public Scene() throws Exception {
            lesSpheres = new ArrayList<Sphere>();
            lumieres = new ArrayList<Lumiere>();
            definir_scene();
        }

    // La scene principale
       void definir_scene(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Donner largeur:");
        largeur = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Donner  hauteur:");
        hauteur = scanner.nextInt();
        Reader reader = new Reader("test.txt");
        // Recuperer les règles du fichier
           ArrayList<Paire> paires = null;
           try {
               paires = reader.builTokens();
           } catch (Exception e) {
               System.out.println("Le fichier des regles n'est pas en bon format");
               System.out.println("Veuillez respecter les memes regels d'ecritures que précédement!");
           }
           for (Paire paire:paires){
            if(paire.getNomenclature().equals("light_source")){
                lumieres.add(new Lumiere(
                        new Point(paire.getPositions()[0], paire.getPositions()[1], paire.getPositions()[2]), // Origin
                        new Couleur(paire.getRGBS()[0], paire.getRGBS()[1], paire.getRGBS()[2])           // Intensity
                ));
            }else if(paire.getNomenclature().equals("sphere")){

                lesSpheres.add(new Sphere(
                        new Point(paire.getPositions()[0], paire.getPositions()[1], paire.getPositions()[2]),   // Center
                        paire.getRayon(),                           // Radius
                        new MaterielPrincipale(
                                new Couleur(paire.getRGBS()[0], paire.getRGBS()[1], paire.getRGBS()[2]),
                                1,
                                new Couleur(1, 1, 1),
                                60
                        )
                ));
            }else if(paire.getNomenclature().equals("camera")){
                new Point(paire.getPositions()[0], paire.getPositions()[1], paire.getPositions()[2]);
            }
        }
    }
}
