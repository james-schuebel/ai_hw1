
package main;


import java.util.*;

public class Main {

    
   
private boolean goalTest(List<List<String>> state) {
for(int i = 0; i < 8; i++) {
String inputTile = state.get(i/3).get(i%3);
String goalTile = String.valueOf(i+1);
if(!inputTile.equals(goalTile))
return false;

}
return true;
}

private List<List<String>> cloneParentState(List<List<String>> parentState) {
List<List<String>> childState = new ArrayList<>();
for(List<String> row: parentState) {
childState.add(new ArrayList<>(row));
}
return childState;
}

private Node getChildNode(Node parent, Action action) {
int[] emptyTilePosition = parent.getEmptyTilePosition();
if((emptyTilePosition[0] == 0 && action == Action.UP) || (emptyTilePosition[0] == 2 && action == Action.DOWN)
|| (emptyTilePosition[1] == 0 && action == Action.LEFT) || (emptyTilePosition[1] == 2 && action == Action.RIGHT)) {
return null;
}
int childEmptyTileRow, childEmptyTileCol;
List<List<String>> parentState = parent.state;
List<List<String>> childState = cloneParentState(parentState);
String tile;
if(action == Action.UP) {
tile = parentState.get(emptyTilePosition[0]-1).get(emptyTilePosition[1]);
childState.get(emptyTilePosition[0]-1).set(emptyTilePosition[1], "*");
childEmptyTileRow = emptyTilePosition[0]-1;
childEmptyTileCol = emptyTilePosition[1];
} else if(action == Action.DOWN) {
tile = parentState.get(emptyTilePosition[0]+1).get(emptyTilePosition[1]);
childState.get(emptyTilePosition[0]+1).set(emptyTilePosition[1], "*");
childEmptyTileRow = emptyTilePosition[0]+1;
childEmptyTileCol = emptyTilePosition[1];
} else if(action == Action.LEFT) {
tile = parentState.get(emptyTilePosition[0]).get(emptyTilePosition[1]-1);
childState.get(emptyTilePosition[0]).set(emptyTilePosition[1]-1, "*");
childEmptyTileRow = emptyTilePosition[0];
childEmptyTileCol = emptyTilePosition[1]-1;
} else {
tile = parentState.get(emptyTilePosition[0]).get(emptyTilePosition[1]+1);
childState.get(emptyTilePosition[0]).set(emptyTilePosition[1]+1, "*");
childEmptyTileRow = emptyTilePosition[0];
childEmptyTileCol = emptyTilePosition[1]+1;
}
childState.get(emptyTilePosition[0]).set(emptyTilePosition[1], tile);
Node child = new Node(childState, parent.getTotalCost()+1, parent.getDepth()+1, parent, action);
child.setEmptyTilePosition(childEmptyTileRow, childEmptyTileCol);
return child;
}

private boolean isStatePresent(List<List<String>> childState, Deque<Node> frontier) {
for(Node nd: frontier) {
if(childState.equals(nd.state))
return true;
}
return false;
}

private void printPath(Node goalNode) {
if(goalNode == null)
return;

printPath(goalNode.parent);
List<List<String>> parentState = goalNode.state;
if(goalNode.parent != null)
System.out.println("Action: "+goalNode.getAction());
System.out.println("------------------------");
for(List<String> row: parentState) {
for(String tile: row) {
System.out.print(tile+" ");
}
System.out.println();
}
}

private void printOuptut(Node goalNode) {
System.out.println("Total number of moves "+goalNode.getDepth());
System.out.println();
System.out.println("Path from input to goal state is");
printPath(goalNode);
}

private HashMap<String, List<Integer>> getGoalState() {
HashMap<String, List<Integer>> goalState = new HashMap<>();
for(int i = 0; i < 8; i++) {
List<Integer> tile = new ArrayList<>();
tile.add(i/3);
tile.add(i%3);
goalState.put(String.valueOf(i+1), tile);
}
return goalState;
}

private int heuristic1(List<List<String>> gameState) {
int misplacedTiles = 0;
String inputTile;
for(int i = 0; i < 8; i++) {
inputTile = gameState.get(i/3).get(i%3);
if(!inputTile.equals("*") && !inputTile.equals(String.valueOf(i+1))) {
misplacedTiles++;
}
}
inputTile = gameState.get(2).get(2);
if(!inputTile.equals("*")) {
misplacedTiles++;
}
return misplacedTiles;
}

private int heuristic2(List<List<String>> gameState) {
int manhattanDist = 0;
HashMap<String, List<Integer>> goalState = getGoalState();
for(int i = 0; i < 9; i++) {
String inputTile = gameState.get(i/3).get(i%3);
if(!inputTile.equals("*")) {
List<Integer> goalTilePosition = goalState.get(inputTile);
manhattanDist += Math.abs(goalTilePosition.get(0)-i/3) + Math.abs(goalTilePosition.get(1)-i%3);
}
}
return manhattanDist;
}

private int astar(Node input, String algorithm) {
if(goalTest(input.state)) {
printOuptut(input);
return 1;
}
int statesEnqueued = 0;
PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost));
pq.add(input);
int optimalCost = Integer.MAX_VALUE;
int cost;
Node optGoalNode = null;
do {
Node temp = pq.remove();
cost = temp.getTotalCost();
if(cost > optimalCost)
break;
if(temp.getDepth() < 10) {
statesEnqueued++;
for(Action action: Action.values()) {
Node child = getChildNode(temp, action);
if (child != null) {
int pathCost = child.getDepth();
int heuristicCost = algorithm.equals("astar1")? heuristic1(child.state): heuristic2(child.state);
child.setTotalCost(pathCost+heuristicCost);
if (goalTest(child.state)) {
optimalCost = child.getTotalCost();
optGoalNode = child;
} else {
pq.add(child);
}
}
}
}
} while(pq.size() != 0);
if(optGoalNode != null) {
System.out.println("Number of states enqueued is "+statesEnqueued);
printOuptut(optGoalNode);
return 1;
}

return -1;
}

private void astar1(Node input) {
input.setTotalCost(heuristic1(input.state));
if(astar(input, "astar1") == -1)
System.out.println("Failed to find out the goal state with astar1 search");
}

private void astar2(Node input) {
input.setTotalCost(heuristic2(input.state));
if(astar(input, "astar2") == -1)
System.out.println("Failed to find out the goal state with astar2 search");
}

private Node depthLimitedSearch(Node input, int depth, int[] statesEnqueued) {
if(goalTest(input.state)) {
return input;
}
else if(input.getDepth() == depth) {
return null;
}
else {
statesEnqueued[0]++;
for(Action action: Action.values()) {
Node child = getChildNode(input, action);
Node temp = null;
if(child != null)
temp = depthLimitedSearch(child, depth, statesEnqueued);
if(temp != null)
return temp;
}
return null;
}
}


private int ids(Node input) {
int statesEnqueued[] = new int[1];
for(int i = 0; i < 11; i++) {
System.out.println("Calling Depth Limited Search with depth "+i);
Node result = depthLimitedSearch(input, i, statesEnqueued);
if(result != null) {
System.out.println("Number of states enqueued is "+statesEnqueued[0]);
printOuptut(result);
return 1;
}
}
System.out.println("Failed to find out the goal state with dfs search");
return -1;
}

private int bfs(Node input) {
if(goalTest(input.state)) {
printOuptut(input);
return 1;
}
Deque<Node> frontier = new LinkedList<>();
frontier.add(input);
HashSet<List<List<String>>> explored = new HashSet<>();
while(!frontier.isEmpty()) {
Node temp = frontier.poll();
if(temp.getDepth() < 10) {
explored.add(temp.state);
for(Action action: Action.values()) {
Node child = getChildNode(temp, action);
if (child != null && !explored.contains(child.state) && !isStatePresent(child.state, frontier)) {
if (goalTest(child.state)) {
System.out.println("Number of states enqueued is "+explored.size());
printOuptut(child);
return 1;
}
frontier.add(child);
}
}
}
}
System.out.println("Failed to find out the goal state with bfs search");
return -1;
}

public static void main(String[] args) {
if(args.length == 0) // If argument is not specified
{
System.out.println("Provide one of the following alorithms as first argument\n" +
"1. bfs\n" +
"2. ids\n" +
"3. astar1\n" +
"4. astar2\n");
return;
}
Scanner in = new Scanner(System.in);
List<List<String>> input = new ArrayList<>();
System.out.println("Enter the input state of 8 puzzle problem row by row");
int emptyTileRow = 0, emptyTileCol = 0;
for(int i = 0; i < 3; i++) {
List<String> row = new ArrayList<>();
for(int j = 0; j < 3; j++) {
String tile = in.next();
if(tile.equals("*")) {
emptyTileRow = i;
emptyTileCol = j;
}
row.add(tile);
}
input.add(row);
}
Node start = new Node(input, 0, 0, null);
start.setEmptyTilePosition(emptyTileRow, emptyTileCol);
Main m = new Main();
switch (args[0]) {
case "bfs":
m.bfs(start);
break;
case "ids":
m.ids(start);
break;
case "astar1":
m.astar1(start);
break;
case "astar2":
m.astar2(start);
break;
default:
System.out.println("Incorrect Argument!");

}
}
}