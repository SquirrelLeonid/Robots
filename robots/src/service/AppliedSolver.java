package service;

public class AppliedSolver {

    public static double distance(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public static double asNormalizedRadians(double angle) {
        while (angle < -2 * Math.PI) {
            if (angle + 2 * Math.PI > 0)
                break;
            angle += 2 * Math.PI;
        }

        while (angle >= 2 * Math.PI) {
            if (angle - 2 * Math.PI < 0)
                break;
            angle -= 2 * Math.PI;
        }

        return angle;
    }

    public static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }
}
