import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class Graph {
    private boolean isDigraph;
    private final int[][] adjMatrix;
    public Graph(int[][] presetMatrix, boolean isDigraph) {
        // Validation to ensure the data is safe
        if (presetMatrix == null || presetMatrix.length == 0 || presetMatrix.length != presetMatrix[0].length) {
            throw new IllegalArgumentException("Matrix must be a non-empty square.");
        }
        this.adjMatrix = presetMatrix;
        this.isDigraph = isDigraph;
    }
    public Graph(int size, boolean isDigraph) { // creates an "empty" adjMatrix (all infinity)
        this.adjMatrix = new int[size][size];
        this.isDigraph = isDigraph;
        for (int[] row : this.adjMatrix) { // initialize all paths to "infinity" (no edge)
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        for (int i = 0; i < size; i++) { // path from a node to itself is 0
            this.adjMatrix[i][i] = 0;
        }
    }
    public int getSize() {
        return this.adjMatrix.length;
    }
    public boolean isDigraph() {
        return isDigraph;
    }
    public int getCost(int start, int end) {
        return adjMatrix[start][end];
    }
    public void setCost(int cost, int start, int end) {
        adjMatrix[start][end] = cost;
        if (!isDigraph) { // ensure symmetry
            adjMatrix[end][start] = cost;
        }
    }
    public List<Edge> sortedEdges() { // returns List of edges in sorted order
        int size = this.adjMatrix.length;
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < size; i++) { // assuming undirected graph
            for (int j = i + 1; j < size; j++) {
                int weight = this.getCost(i, j);
                if (weight != Integer.MAX_VALUE) { // only add edges that actually exist
                    edges.add(new Edge(i, j, weight));
                }
            }
        }
        Collections.sort(edges);
        return edges;
    }
}