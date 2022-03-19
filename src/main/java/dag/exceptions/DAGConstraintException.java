package dag.exceptions;

/**
 * Exception that shows error in DAG structure, for example loop
 */
public class DAGConstraintException extends Exception {
    /**
     * Primary constructor with empty message
     */
    public DAGConstraintException() {
        this("");
    }

    /**
     * @param message Exception message
     */
    public DAGConstraintException(String message) {
        super(message);
    }
}
