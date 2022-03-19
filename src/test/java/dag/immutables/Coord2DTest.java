package dag.immutables;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Coord2DTest {

    @Test
    void add() {
        Coord2D first = new Coord2D(1, 2);
        Coord2D second = new Coord2D(-2, 4);
        Coord2D sum = first.add(second);
        assertEquals(new Coord2D(-1, 6), sum, "add should sum coordinates");
        assertEquals(-1, sum.x(), "add should sum coordinates");
        assertEquals(6, sum.y(), "add should sum coordinates");
        assertEquals(first.add(second), second.add(first), "сложение должно быть коммутативно");
    }

    @Test
    void addNull() {
        Coord2D first = new Coord2D(5, 5);
        assertEquals(first, first.add(null), "adding null must be the same point");
    }

    @Test
    void checkEquality() {
        assertEquals(new Coord2D(1, 2), new Coord2D(1, 2), "must check equality by coords equality");
        assertEquals(new Coord2D(1,1).add(new Coord2D(3, 2)),
                new Coord2D(-1, 2).add(new Coord2D(5, 1)));
    }

    @Test
    void testToString() {
        assertEquals("(1.0, 2.0)", new Coord2D(1, 2).toString(),
                "must show coords in round braces");
    }
}