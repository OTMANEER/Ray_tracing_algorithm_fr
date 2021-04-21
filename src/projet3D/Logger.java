package projet3D;

public class Logger {
    private String name;
    public Logger(){}

    public Logger(String name)
    {
        this.name = name;
    }
    public void logPartiel(String message, boolean affiche) {
        if (this.name == null || !affiche)
            System.out.println(message);
        else
            System.out.println(name + ": " + message);
    }
    public void log(String message)
    {
        if (name == null)
            System.out.println(message);
        else
            System.out.println(name + ": " + message);
    }
}