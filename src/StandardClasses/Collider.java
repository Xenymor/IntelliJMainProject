package StandardClasses;

import java.awt.*;

import static java.lang.Math.abs;

public class Collider {
    public static boolean isTouchingRectRect(Rectangle rect, Rectangle rect1) {
        Rectangle intersection = rect1.intersection(rect);
        return intersection.height > 0 && intersection.width > 0;
    }

    public static boolean intersects(CircleCollider circle, Rectangle rect) {
        double circleDistanceX = abs(circle.getPos().getX() - rect.x);
        double circleDistanceY = abs(circle.getPos().getY() - rect.y);

        if (circleDistanceX > (rect.width / 2 + circle.getRadius())) {
            return false;
        }

        if (circleDistanceY > (rect.height / 2 + circle.getRadius())) {
            return false;
        }


        if (circleDistanceX <= (rect.width / 2)) {
            return true;
        }

        if (circleDistanceY <= (rect.height / 2)) {
            return true;
        }


        double cornerDistance_sq = Math.pow(circleDistanceX - rect.width / 2, 2) +
                Math.pow(circleDistanceY - rect.height / 2, 2);

        return (cornerDistance_sq <= (Math.pow(circle.getRadius(), 2)));
    }
}
