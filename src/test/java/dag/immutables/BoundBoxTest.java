package dag.immutables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class BoundBoxTest {

    @Test
    void fromSameBoundBox() {
        // boundBox of 2 same boundBoxes is same boundBox
        BoundBox first = new BoundBox(new Coord2D(1, 1), new Coord2D(1, 1));
        BoundBox firstToo = new BoundBox(new Coord2D(1,1));
        assertEquals(first, firstToo, "bound box from 2 same point must be equal to box from 1 point");
        assertEquals(first, BoundBox.of(first, first));
        assertEquals(first.lowerLeft(), BoundBox.of(first, first).lowerLeft());
        assertEquals(first.upperRight(), BoundBox.of(first, first).upperRight());
    }

    @Test
    void fromDifferentBoundBoxes() {
        BoundBox first = new BoundBox(new Coord2D(3, 3), new Coord2D(1, 1));
        assertEquals(new Coord2D(1, 1), first.lowerLeft(), "constructor should fix wrong order");
        assertEquals(new Coord2D(3, 3), first.upperRight(), "constructor should fix wrong order");
        BoundBox second = new BoundBox(new Coord2D(-1, 2), new Coord2D(2,1));
        assertEquals(new Coord2D(-1, 1), second.lowerLeft(),
                "constructor must change coords if second point isn't greater than first");
        BoundBox created = BoundBox.of(first, second);
        assertEquals(new Coord2D(-1, 1), created.lowerLeft(), "must correctly build box from 2 other");
        assertEquals(new Coord2D(3, 3), created.upperRight(), "must correctly build box from 2 other");
    }

    @Test
    void fromNullBox() {
        BoundBox first = new BoundBox(new Coord2D(1, 1), new Coord2D(2, 2));
        assertEquals(first, BoundBox.of(first, null), "adding null must be same bound box");
        assertEquals(first, BoundBox.of(null, first), "adding null must be same bound box");
    }

    @Test
    void testToString() {
        BoundBox box = new BoundBox(new Coord2D(1, 2), new Coord2D(2 ,4));
        assertEquals(box.toString(), "BoundBox(lowerLeft=(1.0, 2.0), upperRight=(2.0, 4.0))");
    }
}