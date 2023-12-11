package edu.yu.introtoalgs;

public class QuestForOil extends QuestForOilBase{
    private char[][] map;

    /** Constructor supplies the map.
     *
     * @param map a non-null, N by M (not necessarily a square!), two-dimensional
     * matrix in which each element is either an 'S' (safe) or a 'U' (unsafe) to
     * walk on. It's the client's responsibility to ensure that the matrix isn't
     * "jagged". The client relinquishes ownership to the implementation.
     */
    public QuestForOil(char[][] map) {
        super(map);
        if(map == null){
            throw new IllegalArgumentException();
        }
        // check that each element is either an S or a U
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j] != 'S' || map[i][j] != 'U'){
                    throw new IllegalArgumentException();
                }
            }
        }
        this.map = map;
        // no-op implementation
    }

    /** Specifies the initial "start the search" square, explore the map to find
     * the maximum number of squares contiguous to that square (including the
     * "start the search" square itself).
     *
     * Note: the client is allowed to repeatedly invoke this method, e.g., with
     * different start search squares, on the same QuestForOil instance.
     *
     * @param row the row of the initial "start the search" square, 0..N-1
     * indexing.
     * @param column the column of the initial "start the search" square, 0..M-1
     * indexing.
     * @return the maximum number of squares contiguous to the inital square.
     */
    @Override
    public int nContiguous(int row, int column) {
        return 0;
    }
}
