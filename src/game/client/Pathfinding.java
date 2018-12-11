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

    public static void main(String[] args) {
        Pathfinding finder = new Pathfinding();
        finder.execute();
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter Number of rows: ");
            nRows = scanner.nextInt();
            System.out.print("Enter Number of cols: ");
            nCols = scanner.nextInt();
            System.out.print("Enter Difficulty ( 0.1 = low, 0.9 = high ): ");
            delta = scanner.nextDouble();
            //nodes = new Node[nRows][nCols];

            // showAllPaths();
            System.out.print("Show Again (y/n): ");
            String answer = scanner.next();
            if (!answer.startsWith("y")) {
                break;
            }
        }

    }
    private boolean isFound = false;
    private String savedPath = "";

    public void showAllPaths(Tile[][] tiles, Tile tile) {
        this.nodes = tiles;
        tile.setContents("S");
        List<String> paths = new ArrayList<>();
        int tries = 0;
        // createGrid();
        while (++tries < 500) {
            reset();
            find(tile, '0', "", paths);

        }
        if (paths.isEmpty()) {
        //     printGrid("");
            System.out.println("No Path found");
            return;
        }
        String minPath = paths.get(0);
        System.out.println("Paths: ");
        for (String path : paths) {
            System.out.println(path);
            if (path.length() < minPath.length()) {
                minPath = path;
            }
        }
    }

    /*
    public void showPath() {
        List<String> paths = new ArrayList<>();
        int tries = 0;
        createGrid();
        while (++tries < 100) {
            reset();
            find(nodes[0][0], '0', "", paths);

        }
        if (paths.isEmpty()) {
            printGrid("");
            System.out.println("No Path found");
            return;
        }
        String minPath = paths.get(0);
        System.out.println("Paths: ");
        for (String path : paths) {
            System.out.println(path);
            if (path.length() < minPath.length()) {
                minPath = path;
            }
        }
        printGrid(minPath);
    }
    */
    public void find(Tile node, char last, String path, List<String> paths) {
        if (node != null && !node.visited) {
            if (path.length() >= 10) {
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

    /*
    public void createGrid() {
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                nodes[row][col] = new Node(" " + (nCols * row + col), row, col);
            }
        }
        nodes[0][0].contents = "S";
        nodes[nRows - 1][nCols - 1].contents = "E";
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                int up = row - 1;
                int down = row + 1;
                int left = col - 1;
                int right = col + 1;
                if (up >= 0 && Math.random() > delta) {
                    nodes[row][col].up = nodes[up][col];
                    nodes[up][col].down = nodes[row][col];
                }
                if (down < nRows && Math.random() > delta) {
                    nodes[row][col].down = nodes[down][col];
                    nodes[down][col].up = nodes[row][col];
                }
                if (left >= 0 && Math.random() > delta) {
                    nodes[row][col].left = nodes[row][left];
                    nodes[row][left].right = nodes[row][col];
                }
                if (right < nCols && Math.random() > delta) {
                    nodes[row][col].right = nodes[row][right];
                    nodes[row][right].left = nodes[row][col];
                }
            }
        }
    }
    */
    /*
    public void printGrid(String path) {
        char t = '+';
        char[][] display = new char[3 * nRows][3 * nCols];
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                Node node = nodes[row][col];
                int r = 3 * row;
                int c = 3 * col;
                display[r + 0][c + 0] = t;
                display[r + 0][c + 1] = node.up == null ? t : ' ';
                display[r + 0][c + 2] = t;
                display[r + 1][c + 0] = node.left == null ? t : ' ';
                display[r + 1][c + 1] = node.contents.charAt(0);
                display[r + 1][c + 2] = node.right == null ? t : ' ';
                display[r + 2][c + 0] = t;
                display[r + 2][c + 1] = node.down == null ? t : ' ';
                display[r + 2][c + 2] = t;
            }
        }
        if (!"".equals(path)) {
            Node current = nodes[0][0];
            for (int ii = 0; ii < path.length(); ii++) {
                int r = 3 * current.row;
                int c = 3 * current.col;
                char ch = path.charAt(ii);
                display[r + 1][c + 1] = current.contents.equals("S") ? 'S' : ch;

                switch (ch) {
                    case 'u':
                        current = current.up;
                        break;
                    case 'd':
                        current = current.down;
                        break;
                    case 'l':
                        current = current.left;
                        break;
                    case 'r':
                        current = current.right;
                        break;

                }
            }
        }
        System.out.println();
        System.out.println();
        String last = "";
        for (int r = 0; r < nRows * 3; r++) {
            String line = "";
            for (int c = 0; c < 3 * nCols; c++) {
                line += display[r][c];
            }
            if (!line.equals(last)) {
                System.out.println(line);
            } else {
                System.out.println();
            }
            last = "";
        }
    }
    */
    public void reset() {
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                nodes[row][col].visited = false;
            }
        }
    }
}
