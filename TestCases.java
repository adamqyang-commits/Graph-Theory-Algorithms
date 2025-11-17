public class TestCases {
    public static void main(String[] args) {
        int M = Integer.MAX_VALUE;

        int[][] matrix1 = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        Graph graph1 = new Graph(matrix1, false);

        System.out.println("--- Test Graph 1 (Undirected, Complete) ---");

        try {
            System.out.println("\nNearest Neighbor (start 0):");
            System.out.println(Algorithms.nearestNeighbor(graph1, 0));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nBest Edge:");
            System.out.println(Algorithms.bestEdge(graph1));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nDijkstra's (0 to 3):");
            System.out.println(Algorithms.dijkstra(graph1, 0, 3));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nKruskal's MST:");
            System.out.println(Algorithms.kruskal(graph1));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        int[][] matrix2 = {
            {0, 3, 2, M, M, M},
            {M, 0, M, 4, M, M},
            {M, M, 0, M, 2, M},
            {M, M, M, 0, M, 3},
            {M, M, M, M, 0, 5},
            {M, M, M, M, M, 0}
        };
        Graph graph2 = new Graph(matrix2, true);
        
        System.out.println("\n--- Test Graph 2 (Directed, Acyclic) ---");

        try {
            System.out.println("\nDijkstra's (0 to 5):");
            System.out.println(Algorithms.dijkstra(graph2, 0, 5));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nCritical Path (start 0):");
            System.out.println(Algorithms.criticalPath(graph2));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nKruskal's (should fail):");
            System.out.println(Algorithms.kruskal(graph2));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        int[][] matrix3 = {
            {0, 1, M, M},
            {1, 0, M, M},
            {M, M, 0, 1},
            {M, M, 1, 0}
        };
        Graph graph3 = new Graph(matrix3, false);
        
        System.out.println("\n--- Test Graph 3 (Undirected, Disconnected) ---");

        try {
            System.out.println("\nNearest Neighbor (should fail):");
            System.out.println(Algorithms.nearestNeighbor(graph3, 0));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nKruskal's (should fail):");
            System.out.println(Algorithms.kruskal(graph3));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            System.out.println("\nDijkstra's (should fail):");
            System.out.println(Algorithms.dijkstra(graph3, 0, 3));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}