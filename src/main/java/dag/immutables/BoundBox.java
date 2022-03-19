package dag.immutables;


/**
 * Immutable class which represents rectangle defined by it's
 * lower left and upper right points
 */
public record BoundBox(Coord2D lowerLeft, Coord2D upperRight) {

    /**
     * Constructor from two points. <p>
     * Note: If points are not correct rectangle corners, constructor creates
     *       minimum possible rectangle which fits provided points
     * @param lowerLeft lower left point of rectangle
     * @param upperRight upper right point of rectangle
     * <p></p>
     *
     */
    public BoundBox(Coord2D lowerLeft, Coord2D upperRight) {
        double left_x = Math.min(lowerLeft.x(), upperRight.x());
        double right_x = Math.max(lowerLeft.x(), upperRight.x());
        double lower_y = Math.min(lowerLeft.y(), upperRight.y());
        double upper_y = Math.max(lowerLeft.y(), upperRight.y());
        this.lowerLeft = new Coord2D(left_x, lower_y);
        this.upperRight = new Coord2D(right_x, upper_y);
    }

    /**
     * Constructor from one point, creates BoundBox with same corner points
     * @param point point which would be set as corner point
     */
    public BoundBox(Coord2D point) {
        this(point, point);
    }

    /**
     * Creates new BoundBox from two other <p>
     * Note: if one of boxes is null, the other is returned
     * @param first first BoundBox
     * @param second second BoundBox
     * @return
     */
    public static BoundBox of(BoundBox first, BoundBox second) {
        if (first == null) {
            return second;
        } else if (second == null) {
            return first;
        }
        double left_x = Math.min(first.lowerLeft.x(), second.lowerLeft.x());
        double right_x = Math.max(first.upperRight.x(), second.upperRight.x());
        double lower_y = Math.min(first.lowerLeft.y(), second.lowerLeft.y());
        double upper_y = Math.max(first.upperRight.y(), second.upperRight.y());
        return new BoundBox(new Coord2D(left_x, lower_y), new Coord2D(right_x, upper_y));
    }

    /**
     * Generates string representing BoundBox
     * @return string, format: `BoundBox(lowerLeft=[lowerLeft], upperRight=[upperRight]
     * @see Coord2D#toString()
     */
    @Override
    public String toString() {
        return "BoundBox(lowerLeft=" + lowerLeft + ", upperRight=" + upperRight + ')';
    }
}
