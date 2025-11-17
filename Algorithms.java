import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Collections;

public class Algorithms {
    public static Path nearestNeighbor(Graph graph, int start) {
        if (graph.isDigraph()) {
            throw new IllegalArgumentException("Nearest neighbor algorithm only works on undirected graphs.");
        }
        int size = graph.getSize();
        int cost = 0; // cost of the solution
        boolean[] visited = new boolean[size]; // stores nodes already visited
        visited[start] = true;
        List<Integer> path = new ArrayList<>(); // stores the solution
        path.add(start);
        int currNode = start;
        for (int i = 1; i < size; i++) { // loops through each node
            int toNode = -1;
            int min = Integer.MAX_VALUE;
            for (int end = 0; end < size; end++) { // checks costs to other connected nodes
                int weight = graph.getCost(currNode, end);
                if (!visited[end] && weight < min) { // find cheapest node that hasn't been visited
                    min = weight;
                    toNode = end;
                }
            }
            if (toNode == -1) { // safeguard for incomplete graphs
                throw new RuntimeException("Graph is not complete.");
            }
            path.add(toNode);
            cost += min;
            visited[toNode] = true;
            currNode = toNode;
        }
        // safeguard for incomplete graphs
        if (graph.getCost(currNode, start) == Integer.MAX_VALUE) {
            throw new RuntimeException("Graph is not complete.");
        }
        path.add(start); // return to start node
        cost += graph.getCost(currNode, start);
        return new Path(path, cost);
    }
    public static Path bestEdge(Graph graph) {
        if (graph.isDigraph()) {
            throw new IllegalArgumentException("Best edge algorithm only works on undirected graphs.");
        }
        int size = graph.getSize();
        int cost = 0; // cost of the solution
        int[] degrees = new int[size]; // stores degree of each node
        List<Edge> allEdges = graph.sortedEdges(); // stores all edges of the graph
        List<Edge> solEdges = new ArrayList<>(); // stores the edges of the solution
        UnionFind uf = new UnionFind(size);
        for (Edge currEdge : allEdges) {
            if (solEdges.size() == size) { // edges of solution have been found
                break;
            }
            int start = currEdge.getStart();
            int end = currEdge.getEnd();
            if (degrees[start] < 2 && degrees[end] < 2) { // first check for if a degree 3 node will be created
                boolean isCircuit = uf.areConnected(start, end);
                boolean lastEdge = (solEdges.size() == size - 1);
                if (!isCircuit || lastEdge) { // only add if not a circuit or if the edge is the final edge
                    solEdges.add(currEdge);
                    cost += currEdge.getCost();
                    uf.union(start, end);
                    degrees[start]++;
                    degrees[end]++;
                }
            }
        }
        if (solEdges.size() < size) { // safeguard against incomplete solution
            throw new RuntimeException("Could not find a valid circuit for this graph.");
        }
        return new Path(pathFromEdges(solEdges), cost);
    }
    public static Path dijkstra(Graph graph, int start, int end) {
        int size = graph.getSize();
        int[] prev = new int[size]; // prev[i] is the previous node of node i
        int[] distance = new int[size]; // distances from other nodes to start

        // nodeCosts[0] holds the node, nodeCosts[1] holds the cost
        // this queue is organized by the cost
        PriorityQueue<int[]> nodeCosts = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        
        // initialize all distances to "infinity" and previous nodes to -1 (unknown)
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        distance[start] = 0; // distance from the start node to itself
        nodeCosts.add(new int[]{start, 0}); // add the start node to the queue
        while (!nodeCosts.isEmpty()) {
            int[] entry = nodeCosts.poll();
            int node = entry[0]; // the node
            int dist = entry[1]; // distance of node
            if (dist > distance[node]) { // check if better path already exists
                continue;
            }
            if (node == end) { // reached end node
                break; 
            }
            for (int i = 0; i < size; i++) { // checking all neighbors "i"
                int edgeCost = graph.getCost(node, i);
                if (edgeCost != Integer.MAX_VALUE) { // edge existence check
                    int newDist = distance[node] + edgeCost;
                    if (newDist < distance[i]) { // found shorter path
                        distance[i] = newDist;
                        prev[i] = node;
                        nodeCosts.add(new int[]{i, newDist});
                    }
                }
            }
        }
        if (distance[end] == Integer.MAX_VALUE) { // safeguard for incomplete solution
            throw new RuntimeException("No path found from " + start + " to " + end);
        }
        List<Integer> path = new ArrayList<>();
        int curr = end;
        while (curr != -1) { // creates a backwards path
            path.add(curr);
            curr = prev[curr];
        }
        Collections.reverse(path); // reverses to correct order
        return new Path(path, distance[end]);
    }
    public static MST kruskal(Graph graph) {
        if (graph.isDigraph()) {
            throw new IllegalArgumentException("Kruskal's algorithm only works on undirected graphs.");
        }
        int size = graph.getSize();
        int cost = 0; // cost of the solution
        List<Edge> allEdges = graph.sortedEdges(); // stores all edges of the graph
        List<Edge> solEdges = new ArrayList<>(); // stores the edges of the solution
        UnionFind uf = new UnionFind(size);
        for (Edge currEdge : allEdges) {
            if (solEdges.size() == size - 1) { // edges of solution have been found
                break;
            }
            int start = currEdge.getStart();
            int end = currEdge.getEnd();
            boolean isCircuit = uf.areConnected(start, end);
            if (!isCircuit) { // only add if not a circuit
                solEdges.add(currEdge);
                cost += currEdge.getCost();
                uf.union(start, end);
            }
        }
        if (solEdges.size() < size - 1) { // safeguard against incomplete solution
            throw new RuntimeException("Could not build a full MST (graph is not connected).");
        }
        return new MST(solEdges, cost);
    }
    public static Path criticalPath(Graph graph) {
        if (!graph.isDigraph()) {
            throw new IllegalArgumentException("Critical Path algorithm only works on directed graphs.");
        }
        int size = graph.getSize();

        // Forward pass (Find Earliest Completion Times)
        int[] earliest = new int[size];
        boolean changed = true;
        for (int k = 0; k < size && changed; k++) { // Loop at most 'size' times
            changed = false;
            for (int u = 0; u < size; u++) {
                for (int v = 0; v < size; v++) {
                    int weight = graph.getCost(u, v);
                    if (weight != Integer.MAX_VALUE) { // edge existence check
                        if (earliest[u] + weight > earliest[v]) {
                            earliest[v] = earliest[u] + weight;
                            changed = true;
                        }
                    }
                }
            }
        }

        // Find total cost
        int totalCost = 0;
        for (int time : earliest) {
            if (time > totalCost) {
                totalCost = time;
            }
        }

        // Backward pass (Find Latest Completion Times)
        int[] latest = new int[size];
        Arrays.fill(latest, totalCost);
        changed = true;
        for (int k = 0; k < size && changed; k++) {
            changed = false;
            for (int u = 0; u < size; u++) {
                for (int v = 0; v < size; v++) {
                    int weight = graph.getCost(u, v);
                    if (weight != Integer.MAX_VALUE) {
                        if (latest[v] - weight < latest[u]) {
                            latest[u] = latest[v] - weight;
                            changed = true;
                        }
                    }
                }
            }
        }
        List<Integer> path = new ArrayList<>();
        findCriticalPath(graph, 0, earliest, latest, path);
        return new Path(path, totalCost);
    }
    private static void findCriticalPath(Graph graph, int u, int[] earliest, int[] latest, List<Integer> path) {
        path.add(u); // Add the current node to our path
        for (int v = 0; v < graph.getSize(); v++) { // Look at all neighbors "v"
            if (u == v) continue; // ignore self-loops
            int weight = graph.getCost(u, v);
            if (weight != Integer.MAX_VALUE) {
                // check for critical edge
                boolean isNodeUCritical = (earliest[u] == latest[u]);
                boolean isNodeVCritical = (earliest[v] == latest[v]);
                boolean isEdgeTimingCritical = (earliest[u] + weight == earliest[v]);
                if (isNodeUCritical && isNodeVCritical && isEdgeTimingCritical) { // is critical edge
                    findCriticalPath(graph, v, earliest, latest, path);
                    // this break assumes only one critical path
                    // remove break for all critical paths
                    break;
                }
            }
        }
    }
    private static List<Integer> pathFromEdges(List<Edge> edgeList) {
        // create map for neighbors of each node
        Map<Integer, List<Integer>> neighbors = new HashMap<>();
        for (Edge edge : edgeList) {
            int start = edge.getStart();
            int end = edge.getEnd();
            // adds end as a neighbor to start
            neighbors.computeIfAbsent(start, k -> new ArrayList<>()).add(end);
            // adds start as a neighbor to end
            neighbors.computeIfAbsent(end, k -> new ArrayList<>()).add(start);
        }
        // generate path
        int size = edgeList.size();
        int startNode = edgeList.get(0).getStart();
        int currNode = startNode;
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[size];
        path.add(currNode);
        visited[currNode] = true;
        while (path.size() < size) {
            for (int i = 0; i < 2; i++) { // check both neighbors
                int neighbor = neighbors.get(currNode).get(i);
                if (!visited[neighbor]) { // goes to neighbor if not already visited
                    path.add(neighbor);
                    visited[neighbor] = true;
                    currNode = neighbor;
                    break;
                }
            }
        }
        path.add(startNode); // return to start node
        return path;
    }
}