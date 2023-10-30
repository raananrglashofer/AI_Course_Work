package edu.yu.introtoalgs;

import java.util.*;

public class WordLayout extends WordLayoutBase{
    private List<LocationBase> locations = new ArrayList<>();
    private List<String> words;
    private HashMap<String, List<LocationBase>> map = new HashMap<>(); // in wordLayout constructor when filling each word going to create a new instance of the list<locationbase> and addto the map
    private Grid grid;
    private Set<LocationBase> usedLocations = new HashSet<>();
    private int rows;
    private int columns;
    /**
     * Creates a grid with the specified number of rows and columns such that
     * every one of the supplied words are successfully laid out on the grid.
     * Conceptually, a Grid instance is created (with random letters assigned to
     * all Grid locations), and then overlaid with the list of words to create a
     * valid layout.  The rules for a valid layout are specified in the
     * requirements document.
     *
     * @param nRows    number of rows in 0..n-1 representation, must be a
     *                 non-negative integer.
     * @param nColumns number of columns in 0..n-1 representation, must be a
     *                 non-negative integer.
     * @param words    a non-null, non-empty list of words.  Client maintains ownership.
     * @throws IllegalArgumentException if it's impossible to layout the words in
     *                                  the specified grid or if the supplied parameters violate the specified
     *                                  requirements.
     */

    // I am thinking about making two grids and one will just tell me if it that spot has been used yet or not
    // I am thinking about making the whole grid the letter A and if a spot was used then it becomes B
    // Then while laying out the words I can check if that spot is an A or B
    public WordLayout(int nRows, int nColumns, List<String> words) {
        super(nRows, nColumns, words); // what is this (came from ide)
        if(nRows < 0 || nColumns < 0 || words.isEmpty() || words == null){ // check if meet requirements (0 or 1 for column size)
            throw new IllegalArgumentException();
        }
        this.rows = nRows;
        this.columns = nColumns;
        this.words = words;
        this.grid = new Grid(nRows, nColumns);

        // checking to see if words are within the parameters and will fit
        earlySizeChecks(words, nRows, nColumns);

        // place all words in the grid, if they can't fit then will throw IAE
        placeWords(words);
    }

    /** Returns the grid locations that specify how a word is laid out on the
     * grid.  The locations must be sorted in ascending row coordinate, breaking
     * ties if necessary, by sorting in ascending column coordinate.
     *
     * @return List of locations that specify how the word is laid out on the
     * grid.
     * @throws IllegalArgumentException if the word is not an element of the List
     * supplied in the constructor.
     */
    @Override
    public List<LocationBase> locations(String word) {
        if(!words.contains(word)){
            throw new IllegalArgumentException();
        }
        return this.map.get(word); // will return the list of coordinates for the word
    }

    /** Returns the Grid after it has been filled in with all words
     *
     * @returns Grid instance.
     */
    @Override
    public Grid getGrid() {
        return this.grid;
    }

    private void earlySizeChecks(List<String> words, int nRows, int nColumns){
        int totalLength = 0;
        for(String word : words){
            if(word == null || word.isEmpty()){
                throw new IllegalArgumentException();
            }
            totalLength += word.length();
            // check if word is too long for grid, null, or empty (double check if have to check individual words for null and empty
            if(word.length() > nRows && word.length() > nColumns){
                throw new IllegalArgumentException();
            }
        }
        // check if not enough spaces on the grid
        if(totalLength > nRows * nColumns){
            throw new IllegalArgumentException();
        }
    }

    private void placeWords(List<String> words){
        for(String word : words){
            boolean placed = placeWord(word);
            if(!placed){
                throw new IllegalArgumentException();
            }
        }
    }

    private boolean placeWord(String word){
        boolean wordPlaced = false;
        int currentRow = 0;
        int currentColumn = 0;
        // check if word can be  placed
        while(!wordPlaced){
            wordPlaced = checkHorizontal(word, currentRow, currentColumn);
            if(!wordPlaced) {
                wordPlaced = checkVertical(word, currentRow, currentColumn);
            }
            if(!wordPlaced) {
                wordPlaced = checkDiagonal(word, currentRow, currentColumn);
            }
            currentColumn++;

            // if reached the end of the row, move to the next row
            if(currentColumn == columns){
                currentRow++;
                currentColumn = 0;
            }
            // loop has gone through the whole grid and there is no place to put the word --> return false
            if(currentRow == rows && currentColumn == columns){
                return false;
            }
        }
        return true;
    }

    private void addLocations(List<LocationBase> locations, String word){
        if(!locations.isEmpty()) {
            map.put(word, locations);
            usedLocations.addAll(locations);
        }
    }

    private boolean checkHorizontal(String word, int currentRow, int currentColumn){
        int wordLength = word.length();
        List<LocationBase> currentWordLocations = new ArrayList<>();
        if(wordLength + currentColumn > rows) { // the word does not fit horizontally
            return false;
        }
        for(int i = currentColumn; i < wordLength + currentColumn; i++){
            LocationBase location = new LocationBase(currentRow, i);
            if(usedLocations.contains(location)){
//                for(LocationBase lb : currentWordLocations){
//                    currentWordLocations.remove(lb);
//                }
                return false; // this word can't be placed horizontally
            }
            this.grid.grid[currentRow][i] = word.charAt(i - currentColumn); // 0 indexed now --> I think I can return any random letters for non-used spaces
            currentWordLocations.add(location);
        }
        addLocations(currentWordLocations, word);
        return true;
    }

    private boolean checkVertical(String word, int currentRow, int currentColumn){
        int wordLength = word.length();
        List<LocationBase> currentWordLocations = new ArrayList<>();
        if(wordLength + currentRow > columns) { // the word does not fit vertically
            return false;
        }
        for(int i = currentRow; i < wordLength + currentRow; i++){
            LocationBase location = new LocationBase(i, currentColumn);
            if(usedLocations.contains(location)){
//                for(LocationBase lb : currentWordLocations){
//                    currentWordLocations.remove(lb);
//                }
                return false; // this word can't be placed vertically
            }
            this.grid.grid[i][currentColumn] = word.charAt(i - currentRow); // 0 indexed now --> I think I can return any random letters for non-used spaces
            currentWordLocations.add(location);
        }
        addLocations(currentWordLocations, word);
        return true;
    }

    private boolean checkDiagonal(String word, int currentRow, int currentColumn){
        int wordLength = word.length();
        List<LocationBase> currentWordLocations = new ArrayList<>();
        if(wordLength + currentRow > rows || wordLength + currentColumn > columns) { // the word does not fit diagonally
            return false;
        }
        for(int i = currentColumn; i < wordLength + currentColumn; i++){
            LocationBase location = new LocationBase(currentRow, currentColumn);
            if(usedLocations.contains(location)){
//                for(LocationBase lb : currentWordLocations){
//                    currentWordLocations.remove(lb);
//                }
                return false; // this word can't be placed diagonally
            }
            this.grid.grid[currentRow + i][currentColumn + 1] = word.charAt(i - currentColumn + 1); // 0 indexed now --> I think I can return any random letters for non-used spaces
            currentWordLocations.add(location);
        }
        // might be worthwhile putting an if statement here to check that the whole word was placed in --> check that wordLength == currentWordLocations.size()
        addLocations(currentWordLocations, word);
        return true;
    }
}

