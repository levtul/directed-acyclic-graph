package dag.vertexes;

import dag.exceptions.DAGConstraintException;
import dag.immutables.Coord2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceTest {

    @Test
    void move() {
        Space space = new Space();
        assertEquals(new Coord2D(1, 2), space.move(new Coord2D(1, 2)).getPosition(), "" +
                "move should act like add in Coord2D\"");
    }

    @Test
    void testToString() throws DAGConstraintException {
        Space space = new Space();
        space = space.move(new Coord2D(1, 2));
        Origin child = new Origin(-1, -1);
        child.add(new Point(3, 4));
        space.addAll(child, new Point(2, 2));
        String string = space.toString();
        String first = "Space(position=(1.0, 2.0), children=[" +
                "Point(3.0, 4.0), Origin(position=(0.0, 1.0), children=[Point(3.0, 5.0)])])";
        String second = "Space(position=(1.0, 2.0), children=[" +
                "Origin(position=(0.0, 1.0), children=[Point(3.0, 5.0)]), Point(3.0, 4.0)])";
        if (!string.equals(first) && !string.equals(second)) {
            fail("must print origin in specified format, waited:\n" +
                    first + "\nor\n" + second + "\ngot:\n" + string);
        }
    }
}