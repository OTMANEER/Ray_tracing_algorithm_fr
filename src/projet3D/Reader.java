package projet3D;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {
    private BufferedReader lecteur;
    private final String nom;

    public Reader(String nom){
        this.nom  = nom;
    }

    public BufferedReader creerLecteur(String nom) throws FileNotFoundException {
        InputStream inputStream = Reader.class.getResourceAsStream("/projet3D/" +nom);
        assert inputStream != null;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    /*lit le fichier et affiche ligne par ligne */
    public void lireTout() throws IOException{
        this.lecteur = this.creerLecteur(this.nom); //initialisation du bufferredReader
        String line; 				// on declare une variable de type String qui vas recevoir le contenu de ligne parcouru par le BufferredReader
        while ((line = this.lecteur.readLine()) != null) //Le BufferedReader lit la ligne et ajoute son contenu a la variable de type string
        {												 // temps que le contenu n'est pas nul
            System.out.println (line);				//Afficher le contenu de la ligne dans la console
        }
        this.lecteur.close(); //on ferme le bufferedReader
    }

    /*
     * Le but de cette classe est de recuperer le nom des elements presents dans le fichier pov
     * ces noms sont stockes dans une liste de String qui est retourne a la fin de la fonction
     */
    public ArrayList<String> lireElement() throws IOException{
        this.lecteur = this.creerLecteur(this.nom);
        String line; // Contenu de la ligne
        ArrayList<String> mainObjects = new ArrayList<String>();
        while ((line = this.lecteur.readLine()) != null)
        {
            if(line.contains("{")) {//un element d'un fichier .pov commence par son nom suivi de son contenu entre {} je recupere donc la ligne contenant "{"
                if(this.getNom(line).length() != 0){
                    mainObjects.add(this.getNom(line)); //j'appel la fonction getNom qui prend en entree une ligne contenant le nom et me renvoie seulement nom
                }
            }
        }
        this.lecteur.close(); // on ferme le bufferedReader
        return mainObjects; // on retourne la liste contenant les noms
    }

    /*
     * Le but de cette fonction est d'extraire le nom d'un element contenu dans une ligne
     */
    public String getNom(String chaine){
        String[] chaine1 = chaine.split("\\\\");
        if(chaine1[0].length() != 0){
            String nom = chaine1[0].substring(0, chaine1[0].length()-1);
            return nom;
        }
        return "";
    }

    public String getLigne(String nomenclature) throws IOException{
        this.lecteur = this.creerLecteur(this.nom);
        String line = null;
        StringBuilder contenu = new StringBuilder(" "); //initialisation d'une variable de type String qui vas stocker le contenu de l'element
        while((line = this.lecteur.readLine()) != null && !(line.contains(nomenclature))) {
        }
        assert line != null;
        if(line.contains(nomenclature)) {
            while(!(line.contains("}"))) { // tant que la ligne ne contient pas l'acolade fermee qui signifie la fin du contenu
                contenu.append(line); //on ajoute la ligne a la variable stockant le contenu
                line = this.lecteur.readLine(); // on passe a la ligne suivant
            }
            contenu.append(line);
        }
        this.lecteur.close();
        return contenu.toString(); //on retourne la variable stockant le contenu
    }


    public ArrayList<Paire>  builTokens() throws Exception {
        ArrayList<Paire> paires = new ArrayList<>();
        ArrayList<String> strings = this.lireElement();
        String name;
        Paire tempPaire,tempPaire1;
        String[] positions=null;
        int []t;
        double []t1;
        Matcher matcher;
        Pattern pattern = Pattern.compile("<(.*?)>");
        for (String s:strings){
            String[] tab = (this.getLigne(s).trim()).split(" ");
            if(tab[0].equals("camera{")){
                name = "camera";
                 matcher = pattern.matcher(s);
                String temp = null;
                if (matcher.find())
                {
                    temp = matcher.group(1);
                    t =new int[3];
                    positions =  temp.split(",");
                    t[0]=Integer.parseInt(positions[0]);
                    t[1]=Integer.parseInt(positions[1]);
                    t[2]=Integer.parseInt(positions[2]);

                    tempPaire = new Paire(name,t,null);
                    paires.add(tempPaire);
                }
            }else if(tab[0].equals("light_source{")){
                name ="light_source";
                 matcher = pattern.matcher(tab[1]);
                t = new int[3];
                t1 = new double[3];
                String temp = null;
                if (matcher.find())
                {
                    temp = matcher.group(1);
                    positions =  temp.split(",");
                    t[0]=Integer.parseInt(positions[0]);
                    t[1]=Integer.parseInt(positions[1]);
                    t[2]=Integer.parseInt(positions[2]);
                }

                 Matcher matcher1 = pattern.matcher(tab[4]);
                String temp1 = null;
                if (matcher1.find())
                {
                    temp1 = matcher1.group(1);
                    positions =  temp1.split(",");
                    t1[0]=Double.parseDouble(positions[0]);
                    t1[1]=Double.parseDouble(positions[1]);
                    t1[2]=Double.parseDouble(positions[2]);
                }
               tempPaire1 = new Paire(name,t,t1);
               paires.add(tempPaire1);
            }else if(tab[0].equals("background{")){
                name = "background";
                Matcher matcher1 = pattern.matcher(s);
                String temp1 = null;
                if (matcher1.find())
                {
                    temp1 = matcher1.group(1);
                    t1 = new double[3];
                    positions =  temp1.split(",");
                    t1[0]=Double.parseDouble(positions[0]);
                    t1[1]=Double.parseDouble(positions[1]);
                    t1[2]=Double.parseDouble(positions[2]);
                    tempPaire1 = new Paire(name,null,t1);
                    paires.add(tempPaire1);
                }
            }else if(tab[0].equals("sphere{")){
                name = "sphere";
                Matcher matcher1 = pattern.matcher(tab[1]);
                String temp = null;
                t = new int[3];
                if (matcher1.find())
                {
                    temp = matcher1.group(1);

                    positions =  temp.split(",");
                    t[0]=Integer.parseInt(positions[0]);
                    t[1]=Integer.parseInt(positions[1]);
                    t[2]=Integer.parseInt(positions[2]);
                }
                Matcher matcher2= pattern.matcher(tab[6]);
                String temp1 = null;
                t1 = new double[3];
                if (matcher2.find())
                {
                    temp1 = matcher2.group(1);

                    positions =  temp1.split(",");
                    t1[0]=Double.parseDouble(positions[0]);
                    t1[1]=Double.parseDouble(positions[1]);
                    t1[2]=Double.parseDouble(positions[2]);
                }
                 tempPaire1 = new Paire(name,t,t1);
                tempPaire1.setRayon(Integer.parseInt(tab[2]));
                paires.add(tempPaire1);
            }
        }
        return paires;
    }

}