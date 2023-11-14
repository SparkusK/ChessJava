package ChessPackage;

/**
 * @filename Square.java
 * @author Shane Kelly
 * @date 26 Nov 2011
 */

// -----------------------------------------------------------------------------
// Imports & Definition
// -----------------------------------------------------------------------------

public class Square {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /**
     * The horizontal position on the chess board
     */
    private int rank;

    /**
     * The vertical position on the chess board
     */
    private int file;


    // -------------------------------------------------------------------------
    // Getters $ Setters
    // -------------------------------------------------------------------------

    /**
     * @return the vertical position on the board
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * @return the horizontal position on the board
     */
    public int getFile() {
        return this.file;
    }
    /**
     * @param _rank the rank
     */
    public void setRank(int _rank) {
        this.rank = _rank;
    }
    /**
     * @param _file the file.
     */
    public void setFile(int _file) {
        this.file = _file;
    }

    @Override
    public String toString() {
        return Integer.toString(this.getRank()) + ";" + Integer.toString(this.getFile());
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    /**
     * @param _rank The horizontal position on the board.
     * @param _file The vertical position on the board.
     */
    public Square(int _rank, int _file) {
        this.rank = _rank;
        this.file = _file;
    }

    /**
     *
     * Copy Constructor
     *
     * @param _coordinates The coordinates object to copy.
     */
    public Square(Square _coordinates) {
        this.rank = _coordinates.getRank();
        this.file = _coordinates.getFile();
    }


    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    /**
     * Checks if two Coordinate objects have the same rank and file.
     *
     * @param _coordinatesToCompare The Square object to compare with.
     * @return true if the Square are the same, false if they're not.
     */
    public boolean equals(Square _coordinatesToCompare) {

        boolean rankTheSame = ( this.file == _coordinatesToCompare.getRank() );
        boolean fileTheSame = ( this.file == _coordinatesToCompare.getFile() );

        if ( fileTheSame && rankTheSame ) {
            return true;
        }
        return false;
    }
}
