package StandardClasses;

public class CircleCollider {
    private Vector2 pos;
    private final double radius;

    public CircleCollider(Vector2 pos, double radius) {
        this.pos = pos;
        this.radius = radius;
    }

    public Vector2 getPos() {
        return new Vector2(pos);
    }

    public void setPos(Vector2 newPos) {
        pos = new Vector2(newPos);
    }

    public double getRadius() {
        return radius;
    }
}
