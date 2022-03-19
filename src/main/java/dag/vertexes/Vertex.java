package dag.vertexes;

import dag.immutables.BoundBox;
import dag.immutables.Coord2D;

public abstract class Vertex {
    protected Coord2D position;

    abstract protected Vertex move(Coord2D vector);

    abstract public BoundBox getBounds();

    public void setPosition(Coord2D position) {
        if (position == null) {
            throw new NullPointerException("position must be not null");
        }
        this.position = position;
    }

    public void setPosition(double x, double y) {
        this.position = new Coord2D(x, y);
    }

    public Coord2D getPosition() {
        return position;
    }
}
