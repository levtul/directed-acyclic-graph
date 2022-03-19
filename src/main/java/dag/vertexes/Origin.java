package dag.vertexes;

import dag.immutables.Coord2D;

import java.util.*;

public class Origin extends NonLeafVertex {
    public Origin(Coord2D position) {
        if (position == null) {
            throw new NullPointerException("position must be not null");
        }
        this.position = position;
        this.children = new HashSet<>();
    }

    public Origin(double x, double y) {
        this.position = new Coord2D(x, y);
        this.children = new HashSet<>();
    }

    /**
     * @param vector move vector
     * @return moved Origin with same children
     */
    @Override
    protected Origin move(Coord2D vector) {
        Origin res = new Origin(this.position.add(vector));
        res.children = children;
        return res;
    }

    @Override
    public String toString() {
        return "Origin(" +
                "position=" + position +
                ", children=" + children.stream().map((Vertex v) -> v.move(position)).toList()
                + ')';
    }
}

