package dag.immutables;

public record Coord2D(double x, double y) {
    /**
     * Sums 2 coordinate pairs
     * @param other second Coord2D
     * @return this + other
     */
    public Coord2D add(Coord2D other) {
        if (other == null) {
            return this;
        }
        return new Coord2D(x + other.x, y + other.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
