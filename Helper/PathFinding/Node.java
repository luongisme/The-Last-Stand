package Helper.PathFinding;

public class Node implements  Comparable<Node> {
    public int x;
    public int y;
    public int gCost;
    public int hCost;
    public int fCost;
    public Node parent;

    public Node(int x, int y) {
        this.gCost = Integer.MAX_VALUE;// set this shit inf at first
        this.hCost = hCost;
        this.parent = parent;
        this.fCost=Integer.MAX_VALUE;
        this.x = x;
        this.y = y;
    }

    public void calculateFCost(){
        this.fCost=this.gCost+this.hCost;
    }

    @Override
    public int compareTo(Node other){
        int fCompare=Integer.compare(this.fCost,other.fCost);

        //prior to heuristic if f is equal
        if(fCompare==0){
            return Integer.compare(this.hCost,other.hCost);
        }
        return fCompare;
    }

    @Override
    public int hashCode(){
        return 31*x+y;// store this one to check if i have run after it or not
    }

    //debug String
    @Override
    public String toString() {
        return "Node{" +
                "fCost=" + fCost +
                ", x=" + x +
                ", y=" + y +
                ", gCost=" + gCost +
                ", hCost=" + hCost +
                ", parent=" + parent +
                '}';
    }
}
