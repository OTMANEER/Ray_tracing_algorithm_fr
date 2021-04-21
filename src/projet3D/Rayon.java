package projet3D;

public class Rayon {
    public Point origine;
    public Vecteur direction;

    public Rayon(Point origine, Vecteur direction)
    {
        this.origine = origine;
        this.direction = direction;
    }

    public Point getOrigine() {
        return origine;
    }

    public void setOrigine(Point origine) {
        this.origine = origine;
    }

    public Vecteur getDirection() {
        return direction;
    }

    public void setDirection(Vecteur direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Rayon{" +
                "origine=" + origine +
                ", direction=" + direction +
                '}';
    }
}

