public class Edge implements Comparable<Edge> {
    private int start;
    private int end;
    private int cost;
    public Edge(int start, int end, int cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }
    public Edge(int start, int end) {
        this(start, end, 0);
    }
    public int getStart() {
        return start;
    }
    public int getEnd() {
        return end;
    }
    public int getCost() {
        return cost;
    }
    public void setCost(int i) {
        cost = i;
    }
    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.cost, other.cost);
    }
    @Override
    public String toString() {
        return "(" + start + "-" + end + ", Cost:" + cost + ")";
    }
}