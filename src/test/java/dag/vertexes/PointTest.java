package dag.vertexes;

import dag.immutables.BoundBox;
import dag.immutables.Coord2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void nullCreation() {
        assertThrows(NullPointerException.class, () -> new Point(null),
                "creating from null coordinates must throw an exception");
    }

    @Test
    void setPosition() {
        Point point = new Point(1, 2);
        point.setPosition(new Coord2D(3, 4));
        assertEquals(point.getPosition(), new Coord2D(3, 4),
                "position must be same as provided");
        point.setPosition(5, 6);
        assertEquals(point.getPosition(), new Coord2D(5, 6),
                "position must be same as provided");
    }

    @Test
    void setNullPosition() {
        Point point = new Point(1, 2);
        assertThrows(NullPointerException.class, () -> point.setPosition(null),
                "must throw an exception if coordinates are null");
    }

    @Test
    void move() {
        Point point = new Point(1, 2);
        assertEquals(new Coord2D(-2, 7), point.move(new Coord2D(-3, 5)).position, "" +
                "move should act like add in Coord2D");
    }

    @Test
    void moveNull() {
        Point point = new Point(1, 2);
        assertEquals(point.position, point.move(null).position,
                "move null should not change position");
    }

    @Test
    void getBounds() {
        Point point = new Point(1, 2);
        assertEquals(new BoundBox(new Coord2D(1 ,2)), point.getBounds(),
                "getBounds must return boundBox with point's position");
    }

    @Test
    void testToString() {
        assertEquals("Point(1.0, 2.0)", new Point(1, 2).toString());
    }
}