package StandardClasses;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

public class PathFinder {

    public static List<Position> findShortestPath(Position start, Position target, Set<Position> isBlocked) {
        return findShortestPath(start, target, isBlocked, 50, 50);
    }

    public static List<Position> findShortestPath(Position start, Position target, Set<Position> isBlocked, int maxX, int maxY) {
        return findShortestPath(start,
                target,
                pos -> !isBlocked.contains(pos)
                        && !(pos.getX() < 0
                            || pos.getX() > maxX
                            || pos.getY() < 0
                            || pos.getY() > maxY)
        );
    }

    public static List<Position> findShortestPath(Position start, Position target, Predicate<Position> isAllowed) {
        final long startTime = System.nanoTime();
        //try {
            Map<Position, Position> cache = new HashMap<>();
            Queue<Position> candidates = new PriorityQueue<>();
            candidates.add(new Position(start.x, start.y, null, target, 0));

            while (!candidates.isEmpty()) {
                final Position candidate = candidates.poll();
                final Position alreadyReached = cache.get(candidate);
                if (alreadyReached == null || alreadyReached.steps > candidate.steps) {
                    cache.put(candidate, candidate);
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx != 0 || dy != 0) {
                                Position nextPosition = new Position(candidate.x + dx, candidate.y + dy, candidate, target, candidate.steps + 1);
                                if (!cache.containsKey(nextPosition) && isAllowed.test(nextPosition)) {
                                    candidates.add(nextPosition);
                                }
                            }
                        }
                    }
                }
                if (cache.containsKey(target)) {
                    return findPathReverse(start, target, cache);
                }
            }

            return cache.values().stream()
                    .min((a,b) -> Float.compare(a.distance, b.distance))
                    .map(pos -> findPathReverse(start, pos, cache))
                    .orElse(Collections.singletonList(start));

        //} finally {
            //final long duration = System.nanoTime() - startTime;
            //System.out.println("Duration: " + duration / 1_000_000L + "ms " + duration % 1_000_000L + "ns");
        //}
    }

    private static List<Position> findPathReverse(Position start, Position target, Map<Position, Position> cache) {
        Position pos = cache.get(target);
        final List<Position> result = new ArrayList<>();
        while (pos.previousPosition != null) {
            result.add(pos);
            pos = pos.previousPosition;
        }
        result.add(pos);
        Collections.reverse(result);
        return result;
    }

    public static class Position implements Comparable<Position>, Serializable {
        private final int x;
        private final int y;
        private final int steps;
        private final float distance;
        private final Position previousPosition;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
            this.previousPosition = null;
            this.steps = 0;
            this.distance = Float.MAX_VALUE;
        }

        private Position(int x, int y, Position previousPosition, Position target, int steps) {
            this.x = x;
            this.y = y;
            this.previousPosition = previousPosition;
            this.steps = steps;
            this.distance = this.distanceTo(target);
        }

        private float distanceTo(Position target) {
            final float dx = target.x - this.x;
            final float dy = target.y - this.y;
            return (float) Math.sqrt(dx*dx + dy*dy);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public int compareTo(Position o) {
            return this.steps != o.steps
                    ? Integer.compare(this.steps, o.steps)
                    : this.distance != o.distance
                        ? Float.compare(this.distance, o.distance)
                        : Integer.compare(System.identityHashCode(this), System.identityHashCode(o));
        }

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    ", steps=" + steps +
                    '}';
        }

        public Vector2 toVector2() {
            return new Vector2(x, y);
        }
    }

    public static void main(String[] args) {
        System.out.println(PathFinder.findShortestPath(new Position(1,1), new Position(10,12),
                pos -> true));
    }

}
