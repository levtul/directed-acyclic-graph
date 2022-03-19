package dag.vertexes;

import dag.immutables.BoundBox;
import dag.immutables.Coord2D;

public final class Point extends Vertex {

    public Point(double x, double y) {
        this.position = new Coord2D(x, y);
    }

    public Point(Coord2D position) {
        if (position == null) {
            throw new NullPointerException("position must be not null");
        }
        this.position = position;
    }

    @Override
    protected Point move(Coord2D vector) {
        return new Point(position.add(vector));
    }

    @Override
    public BoundBox getBounds() {
        return new BoundBox(position);
    }

    @Override
    public String toString() {
        return "Point" + position;
    }
}
