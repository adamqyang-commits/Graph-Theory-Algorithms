import java.util.List;

public class MST {
    private final List<Edge> edges;
    private final int cost;
    public MST(List<Edge> edges, int cost) {
        this.edges = edges;
        this.cost = cost;
    }
    public List<Edge> getTree() {
        return edges;
    }
    public int getCost() {
        return cost;
    }
    @Override
    public String toString() {
        return "Edges: " + edges.toString() + "\nCost: " + cost;
    }
}