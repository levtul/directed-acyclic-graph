package dag.vertexes;

import dag.immutables.Coord2D;

import java.util.HashSet;

public final class Space extends NonLeafVertex {
    public Space() {
        position = new Coord2D(0, 0);
        children = new HashSet<>();
    }

    /**
     * @param vector move vector
     * @return new moved Space with same children
     */
    @Override
    protected Space move(Coord2D vector) {
        Space space = new Space();
        space.position = space.position.add(vector);
        return space;
    }

    @Override
    public String toString() {
        return "Space(" +
                "position=" + position +
                ", children=" + children.stream().map((Vertex v) -> v.move(position)).toList() +
                ')';
    }
}
