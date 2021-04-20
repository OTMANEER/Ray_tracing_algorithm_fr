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
       void definir_scene() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Donner largeur:");
        largeur = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Donner  hauteur:");
        hauteur = scanner.nextInt();
        Reader reader = new Reader("test.txt");
        // Recuperer les règles du fichier
        ArrayList<Paire> paires = reader.builTokens();
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
            }
        }

   /*   MaterielPrincipale whiteMaterial = new MaterielPrincipale(
            new Couleur(1.0, 1.0, 1.0),  // Diffusion
            1.0,                       // Reflection
            new Couleur(11.0, 11.0, 11.0),  // Specularity
            60                         // Specular power
        );

        MaterielPrincipale redMaterial = new MaterielPrincipale(
            new Couleur(1.0, 0.0, 0.0),  // Diffusion
            5.0,                       // Reflection
            new Couleur(1.0, 1.0, 1.0),  // Specularity
            60                         // Specular power
        );
                                      
        MaterielPrincipale greenMaterial = new MaterielPrincipale(
            new Couleur(0.0, 1.0, 0.0),  // Diffusion
            0.5,                       // Reflection
            new Couleur(1.0, 1.0, 1.0),  // Specularity
            60                         // Specular power
        );

        MaterielPrincipale blueMaterial = new MaterielPrincipale(
            new Couleur(0.0, 0.0, 1.0),  // Diffusion
            0.5,                       // Reflection
            new Couleur(1.0, 1.0, 1.0),  // Specularity
            60                         // Specular power
        );

        MaterielPrincipale yellowMaterial = new MaterielPrincipale(
            new Couleur(1.0, 1.0, 0.0),  // Diffusion
            0.5,                       // Reflection
            new Couleur(1.0, 1.0, 1.0),  // Specularity
            60                         // Specular power
        );

        MaterielPrincipale cyanMaterial = new MaterielPrincipale(
            new Couleur(0.0, 1.0, 1.0),  // Diffusion
            0.5,                       // Reflection
            new Couleur(1.0, 1.0, 1.0),  // Specularity
            60                         // Specular power
        );

        MaterielPrincipale magentaMaterial = new MaterielPrincipale(
            new Couleur(1.0, 0.0, 1.0),  // Diffusion
            0.5,                       // Reflection
            new Couleur(1.0, 1.0, 1.0),  // Specularity
            60                         // Specular power
        );

        MaterielPrincipale blackMaterial = new MaterielPrincipale(
            new Couleur(0.01, 0.01, .01), // Diffusion
            1.0,                        // Reflection
            new Couleur(1.0, 1.0, 1.0),   // Specularity
            60                          // Specular power
        );
*/
        // White sphere
       /* spheres.add(new Sphere(
            new Point(400.0, 300.0, 0.0),    // Center
            200.0,                           // Radius
            whiteMaterial                    // MaterielPrincipale
        ));

        // Red sphere
       spheres.add(new Sphere(
            new Point(300.0, 200.0, -350.0), // Center
            100.0,                           // Radius
            redMaterial                      // MaterielPrincipale
        ));

        // Green sphere
        spheres.add(new Sphere(
            new Point(400.0, 240.0, -500.0), // Center
            50.0,                            // Radius
            greenMaterial                    // MaterielPrincipale
        ));

        // Blue sphere
        spheres.add(new Sphere(
            new Point(600.0, 240.0, -350.0), // Center
            100.0,                           // Radius
            blueMaterial                     // MaterielPrincipale
        ));

        // Yellow sphere
        spheres.add(new Sphere(
            new Point(600.0, 400.0, 200.0),  // Center
            75.0,                            // Radius
            yellowMaterial                   // MaterielPrincipale
        ));

        // Cyan sphere
        spheres.add(new Sphere(
            new Point(100.0, 400.0, 0.0),    // Center
            75.0,                            // Radius
            cyanMaterial                     // MaterielPrincipale
        ));

        // Magenta sphere
        spheres.add(new Sphere(
            new Point(300.0, 400.0, -600.0), // Center
            75.0,                            // Radius
            magentaMaterial                  // MaterielPrincipale
        ));

        // Black sphere
        spheres.add(new Sphere(
            new Point(450.0, 300.0, -300.0), // Center
            50.0,                            // Radius
            blackMaterial                    // MaterielPrincipale
        ));

        // Larger yellow sphere
        spheres.add(new Sphere(
            new Point(125.0, 200.0, -600.0), // Center
            120.0,                           // Radius
            yellowMaterial                   // MaterielPrincipale
        ));

        // Larger green sphere
        spheres.add(new Sphere(
            new Point(600.0, 500.0, 0.0),   // Center
            80.0,                           // Radius
            greenMaterial                   // MaterielPrincipale
        ));

        // Lumiere behind the white sphere.
        lumieres.add(new Lumiere(
            new Point(640.0, 240.0, 100.0),   // Origin
            new Couleur(0.2, 0.2, 0.5)          // Intensity
        ));*/

        // Left light
        lumieres.add(new Lumiere(
                new Point(0.0, 0.0, -1100.0),     // Origin
                new Couleur(1.0, 1.0, 1.0)           // Intensity
        ));
/*        sphere{
	<0,2,0>,15
        pigment{
            color rgb <1,0,0>
        }
    }*//*
        MaterielPrincipale temp = new MaterielPrincipale(
                new Couleur(1.0, 1.0, 0.0),  // Diffusion
                0.5,                       // Reflection
                new Couleur(1.0, 1.0, 1.0),  // Specularity
                60                         // Specular power
        );

        spheres.add(new Sphere(
                new Point(300, 200.0, 0.0),   // Center
                80.0,                           // Radius
                temp                   // MaterielPrincipale
        ));*/
        // Lumiere behind the camera
  /*      lumieres.add(new Lumiere(
                new Point(640.0, 240.0, -10000.0), // Origin
                new Couleur(0.4, 0.4, 0.5)           // Intensity
        ));*/
    }
}
