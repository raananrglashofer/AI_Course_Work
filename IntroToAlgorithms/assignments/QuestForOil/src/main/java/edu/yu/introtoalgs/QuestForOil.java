package edu.yu.introtoalgs;
import java.util.*;

public class QuestForOil extends QuestForOilBase {
    private char[][] map;
    private boolean[][] visited;
    private int count;

    public QuestForOil(char[][] map) {
        super(map);
        if (map == null) {
            throw new IllegalArgumentException();
        }

        this.map = map;
        this.visited = new boolean[map.length][map[0].length];
        this.count = 0;
    }

    @Override
    public int nContiguous(int row, int column) {
        if (this.map[row][column] == 'U') {
            return 0;
        }

        // Reset the visited array, count, and use a Queue for BFS
        resetVisited();
        count = 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{row, column});
        visited[row][column] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentColumn = current[1];

            if (map[currentRow][currentColumn] == 'S') {
                count++;

                List<int[]> neighbors = getNeighbors(currentRow, currentColumn);
                for (int[] neighbor : neighbors) {
                    int newRow = neighbor[0];
                    int newColumn = neighbor[1];

                    if (!visited[newRow][newColumn]) {
                        queue.offer(new int[]{newRow, newColumn});
                        visited[newRow][newColumn] = true;
                    }
                }
            }
        }

        return count;
    }

    private void resetVisited() {
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j] = false;
            }
        }
    }

    private List<int[]> getNeighbors(int row, int column) {
        List<int[]> neighbors = new ArrayList<>();
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];

            if (isValidPosition(newRow, newColumn)) {
                neighbors.add(new int[]{newRow, newColumn});
            }
        }

        return neighbors;
    }

    private boolean isValidPosition(int row, int column) {
        return row >= 0 && row < map.length && column >= 0 && column < map[0].length && map[row][column] == 'S';
    }
}



//import java.util.*;
//
//public class QuestForOil extends QuestForOilBase{
//    private char[][] map;
//    private boolean[][] marked;
//    private int count;
//
//    /** Constructor supplies the map.
//     *
//     * @param map a non-null, N by M (not necessarily a square!), two-dimensional
//     * matrix in which each element is either an 'S' (safe) or a 'U' (unsafe) to
//     * walk on. It's the client's responsibility to ensure that the matrix isn't
//     * "jagged". The client relinquishes ownership to the implementation.
//     */
//    public QuestForOil(char[][] map) {
//        super(map);
//        if(map == null){
//            throw new IllegalArgumentException();
//        }
//        // check that each element is either an S or a U
//        // don't think i need to validate - but should double check
////        for(int i = 0; i < map.length; i++){
////            for(int j = 0; j < map[i].length; j++){
////                if(map[i][j] != 'S' || map[i][j] != 'U'){
////                    throw new IllegalArgumentException();
////                }
////            }
////        }
//        this.map = map;
//        this.marked = new boolean[map.length][map[0].length];
//        this.count = 0;
//        // no-op implementation
//    }
//
//    /** Specifies the initial "start the search" square, explore the map to find
//     * the maximum number of squares contiguous to that square (including the
//     * "start the search" square itself).
//     *
//     * Note: the client is allowed to repeatedly invoke this method, e.g., with
//     * different start search squares, on the same QuestForOil instance.
//     *
//     * @param row the row of the initial "start the search" square, 0..N-1
//     * indexing.
//     * @param column the column of the initial "start the search" square, 0..M-1
//     * indexing.
//     * @return the maximum number of squares contiguous to the inital square.
//     */
//    // DFS
//    @Override
//    public int nContiguous(int row, int column) {
//        // check if starting square is safe
//        if(this.map[row][column] == 'U') {
//            return 0;
//        }
//        // reset boolean array
//        this.marked = new boolean[this.map.length][this.map[0].length];
//        this.count = 0;
//        int maxCount = 0;
//        dfs(row, column);
//        maxCount = Math.max(maxCount, count);
//        return maxCount;
//    }
//
//    private void dfs(int row, int column){
//        this.marked[row][column] = true;
//        if(map[row][column] == 'S'){
//            count++;
//        }
//        List<int[]> neighbors = getNeighbors(row, column);
//        for (int[] neighbor : neighbors) {
//            int newRow = neighbor[0];
//            int newColumn = neighbor[1];
//
//            if (!marked[newRow][newColumn]) {
//                dfs(newRow, newColumn);
//            }
//        }
//    }
//    private List<int[]> getNeighbors(int row, int column) {
//        List<int[]> neighbors = new ArrayList<>();
//
//        // Iterate over the eight possible directions (four cardinal and four ordinal)
//        int[][] directions = {
//                {-1, 0}, {1, 0}, // Up, Down
//                {0, -1}, {0, 1}, // Left, Right
//                {-1, -1}, {-1, 1}, // Diagonal Up-Left, Diagonal Up-Right
//                {1, -1}, {1, 1} // Diagonal Down-Left, Diagonal Down-Right
//        };
//
//        for (int[] direction : directions) {
//            int newRow = row + direction[0];
//            int newColumn = column + direction[1];
//
//            // Check if the new position is within the bounds of the map
//            if (isValidPosition(newRow, newColumn)) {
//                neighbors.add(new int[]{newRow, newColumn});
//            }
//        }
//
//        return neighbors;
//    }
//
//    private boolean isValidPosition(int row, int column) {
//        // Check if the position is within the bounds of the map
//        // If within bounds and S then return true, otherwise false
//        if(row >= 0 && row < map.length && column >= 0 && column < map[0].length){
//            if(this.map[row][column] == 'S'){
//                return true;
//            }
//        }
//        return false;
//    }
//}
