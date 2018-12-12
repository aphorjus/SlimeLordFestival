package game.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Pathfinding {

    private int nRows;
    private int nCols;
    private double delta = 0.5;
    private Tile[][] nodes;
    private int numSteps;

    public class Node {

        public String contents;
        public int row;
        public int col;
        public boolean visited;
        public Node up;
        public Node down;
        public Node left;
        public Node right;

        public Node(String contents, int row, int col) {
            this.contents = contents;
            this.row = row;
            this.col = col;
        }
    }

    public Pathfinding(Tile[][] nodes){
        this.nodes = nodes;
    }
    private boolean isFound = false;
    private String savedPath = "";

    public List<String> showAllPaths(Tile tile, int numSteps) {
        this.numSteps = numSteps;
        tile.visited = false;
        List<String> paths = new ArrayList<>();
        int tries = 0;
        while (++tries < 500) {
            reset();
            find(tile, '0', "", paths);

        }
        return paths;
    }

    public void find(Tile node, char last, String path, List<String> paths) {
        if (node != null && !node.visited) {
            if (path.length() == numSteps || node.getContents().equals("T")) {
                if ( !paths.contains(path) ) {
                    paths.add(path);
                }
                return;
            }
            if (!node.getContents().equals("S")) {
                node.visited = true;
            }
            List<String> directions = new ArrayList<>();
            Random random = new Random();
            directions.add("l");
            directions.add("r");
            directions.add("u");
            directions.add("d");
            if (last == 'l') {
                directions.remove("r");
            } else if (last == 'r') {
                directions.remove("l");
            } else if (last == 'u') {
                directions.remove("d");
            } else if (last == 'd') {
                directions.remove("u");
            }
            while (!directions.isEmpty()) {
                String direction = directions.remove(random.nextInt(directions.size()));
                switch (direction) {
                    case "l":
                        find(node.left, 'u', path + 'l', paths);
                        break;
                    case "r":
                        find(node.right, 'r', path + 'r', paths);
                        break;
                    case "u":
                        find(node.up, 'u', path + 'u', paths);
                        break;
                    case "d":
                        find(node.down, 'd', path + 'd', paths);
                        break;
                }
            }
        }
    }
    public void reset() {
        if(nodes == null){
            return;
        }
        for (int row = 0; row < nodes.length; row++) {
            for (int col = 0; nodes[row] != null && col < nodes[row].length; col++) {
                if(nodes[row][col] != null){
                    nodes[row][col].visited = false;
                }
            }
        }
    }
}
