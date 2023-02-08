package me.jurassiklizard.microevolution.math;

public class Vector2 {
    public int x, y;

    public final static Vector2 X = new Vector2(1, 0);
    public final static Vector2 Y = new Vector2(0, 1);
    public final static Vector2 Zero = new Vector2(0, 0);

    public Vector2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public com.badlogic.gdx.math.Vector2 toBadLogic(Vector2 vector){
        return new com.badlogic.gdx.math.Vector2(x, y);
    }

    public Vector2 sub(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    public Vector2 straightMul(Vector2 v) {
        return new Vector2(x * v.x, y * v.y);
    }

    public Vector2 straightDiv(Vector2 v) {
        return new Vector2(x / v.x, y / v.y);
    }

    @Override
    public String toString(){
        return "Vector2(" + x + ", " + y + ")";
    }
}
