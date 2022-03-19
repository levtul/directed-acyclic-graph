package dag.utils;

import dag.exceptions.DAGConstraintException;
import dag.exceptions.UnknownClassNameException;
import dag.exceptions.WrongGraphStructureException;
import dag.exceptions.WrongNumerationException;
import dag.vertexes.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DAGUtils {
    /**
     * Method that serializes Space to String, by format <p>
     * `[number]. [classType](position=([x], [y]), [[firstChildNumber], ...])`<p>
     * Array in the end is optional. <p>
     * Examples:<p>
     * 0. Point(position=(2.0, 2.0))<p>
     * 1. Origin(position=(4.0, 4.0), [0,])<p>
     * 2. Point(position=(1.0, 1.0))<p>
     * 3. Origin(position=(3.0, 3.0), [1,2,])<p>
     * 4. Space(position=(0.0, 0.0), [3,1,2,0,])<p>
     *
     * @param space serializable vertex
     * @return serialized string
     */
    static public String exportAsString(Space space) {
        List<Vertex> vertexes = space.getSortedVertexes();
        Map<Vertex, Integer> numeration = new HashMap<>();
        int currentNumber = 0;
        StringBuilder result = new StringBuilder();
        for (Vertex vertex : vertexes) {
            result.append(String.format("%d. ", currentNumber));
            numeration.put(vertex, currentNumber);
            result.append(String.format("%s(position=%s",
                    vertex.getClass().getSimpleName(), vertex.getPosition()));
            if (vertex instanceof NonLeafVertex node) {
                result.append(", [");
                for (Vertex child : node.getChildren()) {
                    result.append(numeration.get(child).toString()).append(',');
                }
                result.append("]");
            }
            result.append(")\n");
            currentNumber++;
        }
        return result.toString();
    }

    /**
     * Does the backward operation to exportAsString, unserializes string to Space, string format
     * is still the same, regex is provided in code
     * @param inputString serialized vertex string
     * @return unserialized Space
     * @throws WrongNumerationException
     * @throws UnknownClassNameException
     * @throws DAGConstraintException
     * @throws WrongGraphStructureException
     */
    public static Space importFromString(String inputString) throws WrongNumerationException, UnknownClassNameException, DAGConstraintException, WrongGraphStructureException {
        Scanner scanner = new Scanner(inputString);
        int currentNumber = 0;
        Map<Integer, Vertex> vertexes = new HashMap<>();
        // regex string that matches our serializable format
        String regex = "(\\d+)\\. (\\w+)\\(position=\\((\\d.\\d), (\\d.\\d)\\)(, (\\[(\\d*,)*\\]))?\\)";
        Pattern pat = Pattern.compile(regex);
        Vertex vertex = null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = pat.matcher(line);
            if (matcher.find()) {
                int number = Integer.parseInt(matcher.group(1));
                String className = matcher.group(2);
                double x = Double.parseDouble(matcher.group(3));
                double y = Double.parseDouble(matcher.group(3));
                if (number != currentNumber) {
                    throw new WrongNumerationException(String.format("expected %d, got %d", currentNumber, number));
                }
                vertex = switch (className) {
                    case "Point" -> new Point(x, y);
                    case "Space" -> new Space();
                    case "Origin" -> new Origin(x, y);
                    default -> throw new UnknownClassNameException(className);
                };
                if (vertex instanceof NonLeafVertex origin) {
                    String children = matcher.group(6);
                    children = children.substring(1, children.length() - 1);
                    List<Integer> list = Arrays.stream(children.split(",")).map(Integer::parseInt).toList();
                    for (Integer num : list) {
                        if (!vertexes.containsKey(num)) {
                            throw new WrongNumerationException("no such vertex: " + num.toString());
                        }
                        origin.add(vertexes.get(num));
                    }
                }
                vertexes.put(currentNumber, vertex);
            }
            currentNumber++;
        }
        if (vertex instanceof Space space) {
            return space;
        } else {
            throw new WrongGraphStructureException("last vertex should be Space");
        }
    }
}
