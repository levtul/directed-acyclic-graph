package dag.vertexes;

import dag.exceptions.DAGConstraintException;
import dag.immutables.BoundBox;

import java.util.*;

public abstract class NonLeafVertex extends Vertex {
    protected Set<Vertex> children;

    /**
     * @return unmodifiable set of vertex's children
     */
    public Set<Vertex> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public BoundBox getBounds() {
        BoundBox current = null;
        for (Vertex object : children) {
            BoundBox box = object.move(position).getBounds();
            if (current == null) {
                current = box;
            } else {
                if (box != null) {
                    current = BoundBox.of(box, current);
                }
            }
        }
        return current;
    }

    /**
     * @param vertex current vertex
     * @param container container where we contain vertexes in sorted order
     * @param used set that shows us if vertex was visited
     */
    private void sortTopologically(Vertex vertex, List<Vertex> container, Set<Vertex> used) {
        used.add(vertex);
        if (vertex instanceof NonLeafVertex origin) {
            for (Vertex child : origin.getChildren()) {
                if (!used.contains(child)) {
                    sortTopologically(child, container, used);
                }
            }
        }
        container.add(vertex);
    }

    /**
     * @return topological sort array
     */
    public List<Vertex> getSortedVertexes() {
        List<Vertex> result = new ArrayList<>();
        Set<Vertex> used = new HashSet<>();
        sortTopologically(this, result, used);
        return result;
    }

    private boolean hasCycle(Vertex vertex, Map<Vertex, Integer> used) {
        used.put(vertex, 1);
        boolean foundCycle = false;
        // only Origin has children, so we ignore Points
        if (vertex instanceof Origin origin) {
            for (Vertex child : origin.children) {
                if (used.getOrDefault(child, 0) == 0) {
                    foundCycle = foundCycle || hasCycle(child, used);
                } else if (used.getOrDefault(child, 0) == 1) {
                    foundCycle = true; // founded cycle
                }
            }
        }
        used.put(vertex, 2);
        return foundCycle;
    }

    public void add(Vertex child) throws DAGConstraintException {
        if (child == null) {
            throw new NullPointerException("child must be not null");
        }
        if (!children.contains(child)) {
            children.add(child);
            if (hasCycle(child, new HashMap<>())) {
                children.remove(child);
                throw new DAGConstraintException(String.format("Cannot add %s to %s because of cycle\n", child, this));
            }
        }
    }

    public void addAll(Vertex... elements) throws DAGConstraintException {
        for (Vertex element : elements) {
            add(element);
        }
    }

    public void addAll(Collection<? extends Vertex> elements) throws DAGConstraintException {
        for (Vertex element : elements) {
            add(element);
        }
    }

    public void remove(Vertex child) {
        children.remove(child);
    }

    public void clear() {
        children.clear();
    }
}
