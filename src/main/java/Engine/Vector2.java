package Engine;

/**
* Created by shpatnaik on 8/8/14.
*/
public class Vector2 {

    public double x;
    public double y;

    public Vector2() {}
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double magnitude() {
        return Math.sqrt((x * x) + (y * y));
    }

    public Vector2 unit() {
        return new Vector2((x / magnitude()), (y / magnitude()));
    }

    public Vector2 add(Vector2 b) {
        return new Vector2(this.x + b.x, this.y + b.y);
    }

    public Vector2 multiply(double s) {
        return new Vector2(s * x, s * y);
    }

    public double dotProduct(Vector2 b) {
        return (this.x * b.x) + (this.y * b.y);
    }

    public double angleBetweenVectors(Vector2 b) {
        return Math.acos(this.dotProduct(b) / (this.magnitude() * b.magnitude()));
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
