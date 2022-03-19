package dag.utils;

import dag.exceptions.DAGConstraintException;
import dag.exceptions.UnknownClassNameException;
import dag.exceptions.WrongGraphStructureException;
import dag.exceptions.WrongNumerationException;
import dag.vertexes.Origin;
import dag.vertexes.Point;
import dag.vertexes.Space;
import dag.vertexes.Vertex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DAGUtilsTest {

    @Test
    void exportAsString() throws DAGConstraintException, UnknownClassNameException, WrongNumerationException, WrongGraphStructureException {
        Space root = new Space();
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Origin o1 = new Origin(3, 3);
        Origin o2 = new Origin(4, 4);
        root.addAll(o1, o2, p1, p2);
        o1.add(p1);
        o2.add(p2);
        o1.add(o2);
        Space newRoot = DAGUtils.importFromString(DAGUtils.exportAsString(root));
        assertEquals(root.getBounds(), newRoot.getBounds());
    }

    @Test
    void importFromString() throws DAGConstraintException, UnknownClassNameException, WrongNumerationException, WrongGraphStructureException {
        String correctInput = """
                0. Point(position=(2.0, 2.0))
                1. Origin(position=(4.0, 4.0), [0,])
                2. Point(position=(1.0, 1.0))
                3. Origin(position=(3.0, 3.0), [1,2,])
                4. Space(position=(0.0, 0.0), [3,1,2,0,])
                """;
        Vertex v = DAGUtils.importFromString(correctInput);
        String incorrectInput = """
                0. Point(position=(2.0, 2.0))
                1. Origin(position=(4.0, 4.0), [0,])
                2. Point(position=(1.0, 1.0))
                3. Origin(position=(3.0, 3.0), [1,2,])
                """;
        assertThrows(WrongGraphStructureException.class,
                () -> DAGUtils.importFromString(incorrectInput), "no space in end");
        String incorrectNumber = """
                2. Point(position=(2.0, 2.0))
                1. Origin(position=(4.0, 4.0), [0,])
                2. Point(position=(1.0, 1.0))
                3. Origin(position=(3.0, 3.0), [1,2,])
                4. Space(position=(0.0, 0.0), [3,1,2,0,])
                """;
        assertThrows(WrongNumerationException.class,
                () -> DAGUtils.importFromString(incorrectNumber), "wrong number 2");
        String incorrectChildren = """
                0. Origin(position=(4.0, 4.0), [10,])
                """;
        assertThrows(WrongNumerationException.class,
                () -> DAGUtils.importFromString(incorrectChildren), "wrong child -1");
        String incorrectClassName = """
                0. ABC(position=(4.0, 4.0), [])
                """;
        assertThrows(UnknownClassNameException.class,
                () -> DAGUtils.importFromString(incorrectClassName), "wrong class name ABC");
    }

}