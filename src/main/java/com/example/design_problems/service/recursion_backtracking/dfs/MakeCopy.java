package com.example.design_problems.service.recursion_backtracking.dfs;

import java.util.Arrays;
import java.util.List;
class Node{
    String val;
    public List<Node> neighbors;
    Node(String val, List<Node> nodes){
        this.val= val;
        this.neighbors= nodes;
    }
}
class Graph{
    List<Node> nodes;
    Graph(List<Node> node){
        this.nodes= node;
    }
}
public class MakeCopy {

    public static void main(String[] args) {
        Node node= new Node("a", null);
        Node node2= new Node("b", null);
        Node node3= new Node("c", null);
        node.neighbors= Arrays.asList(new Node[]{node2});
        node2.neighbors= Arrays.asList(new Node[]{node3});
        Graph g = new Graph(Arrays.asList(new Node[]{node}));
        //task Graph g2;
    }

}
