package Engine;

import java.util.Random;

/**
 * Created by shpatnaik on 8/5/14.
 */
public class Utilities {

    public static class Vector {

        public double x;
        public double y;

        public Vector() {}
        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double magnitude() {
            return Math.sqrt((x * x) + (y * y));
        }

        public Vector unit() {
            return new Vector((x / magnitude()), (y / magnitude()));
        }

        public Vector add(Vector b) {
            return new Vector(this.x + b.x, this.y + b.y);
        }

        public Vector multiply(double s) {
            return new Vector(s * x, s * y);
        }

        public double dotProduct(Vector b) {
            return (this.x * b.x) + (this.y * b.y);
        }

        public double angleBetweenVectors(Vector b) {
            return Math.acos(this.dotProduct(b) / (this.magnitude() * b.magnitude()));
        }

        public String toString() {
            return "(" + this.x + ", " + this.y + ")";
        }
    }

    public static int randomNumber(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
