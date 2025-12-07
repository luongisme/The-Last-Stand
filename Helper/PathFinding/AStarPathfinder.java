package Helper.PathFinding;

import java.util.*;

public class AStarPathfinder {
    private PathGrid pathGrid;
    private static final int[][] DIRECTIONS = {
            {0, -1}, // up
            {1, 0},  // right
            {0, 1},  // down
            {-1, 0}  // left
    };

    private static final int MOVE_COST = 10;

    public  AStarPathfinder(PathGrid pathGrid) {
        this.pathGrid = pathGrid;
    }

    /**
     * Tìm đường đi từ (startX, startY) đến (endX, endY)
     * @param startX,startY tọa độ grid X Y bắt đầu
     * @param endX,endY tọa độ grid X Y đích
     * @return List<Node> đường đi từ start đến end, hoặc null nếu không tìm thấy
     */

    // ═════════════════════════════════════════════════════════════════
    // THIS ONE KINDA HARD TO UNDERSTAND SO IM GONNA COMMENT QUITE A LOT
    // ═════════════════════════════════════════════════════════════════

    protected int calculateHeuristic(int x1, int y1, int x2, int y2) {
        // Apply manhattan distance as heuristic
        return (Math.abs(x1 - x2) + Math.abs(y1 - y2)) * MOVE_COST;
    }

    public List<Node> findPath(int startX, int startY, int endX, int endY) {
        // ═════════════════════════════════════════════════════════════════
        // A few setup condition checks before applying the algorithm
        // ═════════════════════════════════════════════════════════════════
        if(!pathGrid.isWalkable(endX, endY)) {
            return null; // Destination is not walkable
        }
        if(!pathGrid.isWalkable(startX, startY)) {
            return null; // Start is not walkable
        }

        //if start equals end
        if(startX==endX&&startY==endY){
            List<Node> path=new ArrayList<>();
            path.add(new Node(startX,startY));// add start node to avoid bugs
            return path;
        }

        //open set to keep track of nodes to be evaluated based on fCost(Min-Heap)
        PriorityQueue<Node> openSet = new PriorityQueue<>();

        //closed set to keep track of evaluated(duyệt qua rồi) nodes
        Set<String> closedSet = new java.util.HashSet<>();

        //check all nodes by their coordinates
        Map<String, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startX, startY);
        startNode.gCost = 0;
        startNode.hCost = calculateHeuristic(startX, startY, endX, endY);
        startNode.calculateFCost();

        openSet.add(startNode);
        allNodes.put(startX + "," + startY, startNode);

        //count interation for debugging
        int iterations = 0;
        int maxIterations = pathGrid.getCols()* pathGrid.getRows(); // to prevent infinite loops

        // ═════════════════════════════════════════════════════════════════
        //                     CORE LOGIC OF THE A* ALGORITHM
        // ═════════════════════════════════════════════════════════════════

         while(!openSet.isEmpty()&&iterations<maxIterations){
             iterations++;
             //get the node with the lowest fCost
             Node currentNode = openSet.poll();
             String currentKey = currentNode.x + "," + currentNode.y;

             if (currentNode.x==endX&&currentNode.y==endY){
                 System.out.println("Path found in " + iterations + " iterations.");
                 return reconstructPath(currentNode);
             }
             //check as evaluated
             closedSet.add(currentKey);

             for(int[]direction:DIRECTIONS){
                 int neigborX=currentNode.x+direction[0];
                 int neigborY=currentNode.y+direction[1];
                 String neighborKey=neigborX+","+neigborY;

                 if(!pathGrid.isWalkable(neigborX,neigborY)||closedSet.contains(neighborKey)){
                     continue; //skip if not walkable or already evaluated
                 }
                 if(closedSet.contains(neighborKey)){
                     continue; //skip if already evaluated
                 }

                 //calculate new cost to neighbor
                 int tentativeGCost=currentNode.gCost+MOVE_COST;

                 // get or create neighbor node
                 Node neighbor = allNodes.get(neighborKey);
                 boolean isNewNode = (neighbor == null);

                 if(isNewNode){
                     neighbor=new Node(neigborX,neigborY);
                     allNodes.put(neighborKey,neighbor);
                 }

                 //if new node or found a cheaper path to neighbor
                 if(tentativeGCost<neighbor.gCost){
                     //update neighbor costs and parent
                    neighbor.gCost=tentativeGCost;
                    neighbor.hCost=calculateHeuristic(neigborX,neigborY,endX,endY);
                    neighbor.calculateFCost();
                    neighbor.parent=currentNode;

                    if(!openSet.contains(neighbor)){
                        openSet.add(neighbor);
                    }
                    else{
                        // Reinsert to update its position in the priority queue
                        openSet.remove(neighbor);
                        openSet.add(neighbor);
                    }
                 }
             }
         }
        System.out.println("No path found after " + iterations + " iterations.");
        return null; // No path found
    }

    private List<Node> reconstructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public void updateGrid(PathGrid newGrid) {
        this.pathGrid = newGrid;
    }
    public PathGrid getPathGrid(){
        return pathGrid;
    }

}
