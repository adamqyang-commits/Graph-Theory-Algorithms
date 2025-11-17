public class UnionFind {
    private int[] parent;
    private int[] size;
    public UnionFind(int num) { // creates num sets each with size 1
        parent = new int[num];
        size = new int[num];
        for (int i = 0; i < num; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }
    public int find(int i) { // recursively find root of the set for element i
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]); // also sets all nodes in the same path to the same parent
    }
    public void union(int i, int j) {
        int iroot = find(i); // root of i
        int jroot = find(j); // root of j
        if (iroot == jroot) { // already in same set
            return;
        }
        // to attach the smaller tree to the larger tree
        if (size[iroot] < size[jroot]) { // i's tree is smaller, so add to j's tree
            parent[iroot] = jroot;
            size[jroot] += size[iroot]; // j's tree is now larger
        } else { // j's tree is smaller or equal, so add to i's tree
            parent[jroot] = iroot;
            size[iroot] += size[jroot]; // i's tree is now larger
        }
    }
    public boolean areConnected(int i, int j) { // returns true if i and j are in the same set.
        return find(i) == find(j);
    }
}