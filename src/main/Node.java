/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.List;

class Node {
Node parent;
private int totalCost, depth;
List<List<String>> state;
private Action action;
private int[] emptyTilePosition;

Node(List<List<String>> state, int pathCost, int depth, Node parent) {
this.state = state;
this.depth = depth;
this.totalCost = pathCost;
this.parent = parent;
emptyTilePosition = new int[2];
}

Node(List<List<String>> state, int pathCost, int depth, Node parent, Action action) {
this(state, pathCost, depth, parent);
this.action = action;
}

public void setEmptyTilePosition(int row, int col) {
emptyTilePosition[0] = row;
emptyTilePosition[1] = col;
}

public int[] getEmptyTilePosition() {
return emptyTilePosition;
}

public void setTotalCost(int pathCost) {
this.totalCost = pathCost;
}

public int getTotalCost() {
return totalCost;
}

public int getDepth() {
return depth;
}

public void setDepth(int depth) {
this.depth = depth;
}

public String getAction() {
return action.name();
}
}