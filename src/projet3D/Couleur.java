/* 
 * Couleur class.
 * Copyright (C) 2009 Susam Pal
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package projet3D;

public class Couleur
{
    private double red;
    private double green;
    private double blue;

    public Couleur(double red, double green, double blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public void ajouter_couleur(Couleur couleur)
    {
        red += couleur.red;
        green += couleur.green;
        blue += couleur.blue;
    }


    public void exposure(double exposure) {
        blue = 1.0 - Math.exp(-blue * exposure);
        red = 1.0 - Math.exp(-red * exposure);
        green = 1.0 - Math.exp(-green * exposure);
    }

    /**
     *Correction Gamma
     * encodage sRGB
     */
    public void correction_gamma() {
        red = correction_gamma(red);
        green = correction_gamma(green);
        blue = correction_gamma(blue);
    }

    /**
     * Conversion des couleurs en bytes
     */
    public byte[] convertBytes() {
        // Afin d'assurer que l'element ne dépasse pas 255, On doit utiliser Byte.
        byte []tab_bytes =new byte[] {(byte) Math.min(blue * 255, 255), (byte) Math.min(green * 255, 255),(byte) Math.min(red * 255, 255)};
        return tab_bytes;
    }

    /*
    transformer les valeurs linéaires en sRGB ==> Codage Gamma
    * si  c <= 0.0031308 ==> 12.92 * c
    *sinon
         1.055 * ((c)^(0.4166667)) - 0.055
    * */
    private static double correction_gamma(double c)
    {
        if (c <= 0.0031308) {
            return 12.92 * c;
        } else {
            return 1.055 * Math.pow(c, 0.4166667) - 0.055;
        }
    }

    // Multiplication de deux couleurs consiste à multiplier  Red*Red, Green*Green etc.
    public static Couleur multiplier_couleurs(Couleur c1, Couleur c2)
    {
        return new Couleur(c1.red * c2.red,
                c1.green * c2.green,
                c1.blue * c2.blue);
    }
    public static Couleur multiplier_couleurs(double c, Couleur color)
    {
        return new Couleur(c * color.red, c * color.green, c * color.blue);
    }

}

