package dag.vertexes;

import dag.exceptions.DAGConstraintException;
import dag.immutables.BoundBox;
import dag.immutables.Coord2D;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OriginTest {

    @Test
    void createFromNull() {
        assertThrows(NullPointerException.class, () -> new Origin(null),
                "must throw exception if coordinates are null");
    }

    @Test
    void getBounds() {
        try {
            Origin root = new Origin(2, 2);
            assertNull(root.getBounds(), "must return null if no Points provided");
            root.addAll(new Origin(-3, -2), new Origin(10, 10));
            Set<Vertex> children = root.getChildren();
            for (Vertex child : children) {
                if (child instanceof Origin vertex) {
                    vertex.add(new Point(0, 1));
                }
            }
            root.add(new Point(0, 100));
            BoundBox box = new BoundBox(new Coord2D(-1.0, 1.0), new Coord2D(12.0, 102.0));
            assertEquals(box, root.getBounds());
        } catch (DAGConstraintException e) {
            fail("must correctly add children without exceptions");
        }
    }

    @Test
    void getSortedVertexes() throws DAGConstraintException {
        Origin root = new Origin(0, 0);
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Origin o1 = new Origin(3, 3);
        Origin o2 = new Origin(4, 4);
        root.addAll(o1, o2, p1, p2);
        o1.add(p1);
        o2.add(p2);
        o1.add(o2);
        List<Vertex> result = root.getSortedVertexes();
        Set<Vertex> founded = new HashSet<>();
        for (Vertex vertex : result) {
            if (vertex instanceof Origin origin) {
                for (Vertex child : origin.getChildren()) {
                    assertTrue(founded.contains(child),
                            "all origin's children must be already founded");
                }
            }
            founded.add(vertex);
        }
    }

    @Test
    void addNullChild() {
        Origin root = new Origin(1, 2);
        assertThrows(NullPointerException.class, () -> root.add(null),
                "exception should be thrown while adding null");
    }

    @Test
    void addSameChildren() throws DAGConstraintException {
        Origin root = new Origin(1, 2);
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 2);
        Origin o1 = new Origin(10, 10);
        Origin o2 = new Origin(-1, -1);
        root.add(p1);
        root.add(p1);
        assertEquals(1, root.getChildren().size(), "same vertex should not be added again");
        for (Vertex child : root.getChildren()) {
            assertEquals(child, p1, "the point must be same");
        }
        root.addAll(p1, p2, o1, p1, p1, p1);
        root.addAll(List.of(p2, o2, o1, p2));
        assertEquals(4, root.getChildren().size(),
                "set should contain all 4 vertexes and do not contain duplicates");
        Set<Vertex> children = new HashSet<>(List.of(p1, p2, o1, o2));
        for (Vertex child : root.getChildren()) {
            assertTrue(children.contains(child), "all children must be present");
        }
        root.clear();
        assertEquals(0, root.getChildren().size(), "set must be empty");
    }

    @Test
    void removeSame() throws DAGConstraintException {
        Origin root = new Origin(1, 2);
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 2);
        root.addAll(p1, p2);
        root.remove(p2);
        assertEquals(1, root.getChildren().size(), "after the deletion size must be decreased");
        for (Vertex child : root.getChildren()) {
            assertEquals(child, p1, "set must contain only first point");
        }
        root.remove(p2);
        assertEquals(1, root.getChildren().size(), "deleting non-presented element must not change size");
    }

    @Test
    void addWithoutCycle() throws DAGConstraintException {
        Origin root = new Origin(1, 2);
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 2);
        Origin o1 = new Origin(10, 10);
        Origin o2 = new Origin(-1, -1);
        root.addAll(o1, o2, p1, p2);
        // must not fail, no cycles created
        o1.add(p1);
        o2.add(p2);
        o1.add(o2);
    }

    @Test
    void addWithCycle() throws DAGConstraintException {
        Origin looped = new Origin(10, 10);
        assertThrows(DAGConstraintException.class, () -> looped.add(looped),
                "vertex cannot have itself as a child");
        Origin o1 = new Origin(1, 2);
        Origin o2 = new Origin(3, 3);
        assertThrows(DAGConstraintException.class, () -> {
            o1.add(o2);
            o2.add(o1);
        }, "two vertexes cannot have each other as a children");
        Origin first = new Origin(1, 2);
        Origin second = new Origin(0, 0);
        Origin third = new Origin(10, 10);
        Origin fourth = new Origin(-1, -1);
        first.add(second);
        second.add(third);
        third.add(fourth);
        assertThrows(DAGConstraintException.class, () -> fourth.add(first), "cycles are not allowed");
    }

    @Test
    void move() {
        Origin origin = new Origin(1, 2);
        assertEquals(new Coord2D(-3, 5), origin.move(new Coord2D(-4, 3)).position,
                "move should act like add in Coord2D");
    }

    @Test
    void testToString() throws DAGConstraintException {
        Origin origin = new Origin(1, 2);
        Origin child = new Origin(-1, -1);
        child.add(new Point(3, 4));
        origin.addAll(child, new Point(2, 2));
        String string = origin.toString();
        String first = "Origin(position=(1.0, 2.0), children=[" +
                "Point(3.0, 4.0), Origin(position=(0.0, 1.0), children=[Point(3.0, 5.0)])])";
        String second = "Origin(position=(1.0, 2.0), children=[" +
                "Origin(position=(0.0, 1.0), children=[Point(3.0, 5.0)]), Point(3.0, 4.0)])";
        if (!string.equals(first) && !string.equals(second)) {
            fail("must print origin in specified format");
        }
    }
}