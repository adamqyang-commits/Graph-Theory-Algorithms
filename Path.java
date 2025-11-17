import java.util.List;

public class Path {
    private final List<Integer> path;
    private final int cost;
    public Path(List<Integer> path, int cost) {
        this.path = path;
        this.cost = cost;
    }
    public List<Integer> getPath() {
        return path;
    }
    public int getCost() {
        return cost;
    }
    @Override
    public String toString() {
        return "Path: " + path.toString() + "\nCost: " + cost;
    }
}