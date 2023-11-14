package ChessPackage;

import ChessPackage.Board.Color;

/**
 * @filename Piece.java
 * @author Shane Kelly
 * @date 26 Nov 2011
 */


public class Piece {
// -----------------------------------------------------------------------------
// Fields
// -----------------------------------------------------------------------------

    /**
     * The color of the piece
     */
    private Color color;

    /**
     * The horizontal and vertical position on the board.
     */
    private Square location;

    /**
     * The type of the piece.
     */
    private Type type;

    /**
     * If the type of the piece is a PAWN, this will be used to check if the
     * rules for En passant are met.
     */
    private boolean canEnPassant;

    /**
     * If the type of the piece is a KING, this will be used to check if it
     *  has castled in this game. If it has, it cannot castle again.
     */
    private boolean hasCastled = false;



    /**
     * If the type of the piece is a KING, this will be used to check if the
     * rules for castling are met. This includes:
     *  1.) The king and rook involved in castling must not have previously
     *      moved.
     *      (movesCount == 0)
     *  2.) There must be no pieces between the king and the rook.
     *      ( getPiece(every square between king and rook) == null)
     *  3.) The king may not currently be in check, nor may the king pass
     *      through or end up in a square that is under attack by an enemy
     *      piece (though the rook is permitted to be under attack and to
     *      pass over an attacked square).
     *      (squareUnderAttack(every square between king and rook) == false)
     *  4.) The king and the rook must be on the same rank.
     *      (king.getRank() == rook.getRank())
     */
    private boolean canCastle;

    /**
     * The available types of pieces.
     */
    public enum Type {
        PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
    }





    /**
     * The amount of moves this piece has made - initially 0.
     */
     private int movesCount = 0;
// -----------------------------------------------------------------------------
// Getters $ Setters
// -----------------------------------------------------------------------------

    /**
     * @return the color of the piece.
     */
    public Color getColor() {
        return color;
    }

    /**
     *
     * @return whether the KING can castle.
     */
    public boolean canCastle() {
        if (this.getType() == Type.KING) {
            return this.canCastle;
        }
        return false;
    }

    /**
     * If the king has castled previously, it cannot do so again.
     *
     * @return whether the KING has castled before.
     */
    public boolean hasCastled() {
        if (this.getType() == Type.KING) {
            return this.hasCastled;
        }
        return false;
    }

   
    /**
     * @return canEnPassant
     */
    public boolean getEnPassant() {
        if(this.getType() == Type.PAWN) {
            return canEnPassant;
        }
        return false;
    }

    public void setEnPassant(boolean _canEnPassant) {
        if (this.getType() == Type.PAWN) {
            canEnPassant = _canEnPassant;
        }
        else {
            canEnPassant = false;
        }
    }
    /**
     *
     * @return this piece's location Square.
     */
    public Square getCoordinates() {
        return new Square(location);
    }





    /**
     *
     * @return the amount of moves this piece has made.
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     *
     * @param _movesMade the amount to set movesCount to.
     */
    public void setMovesCount(int _movesMade) {
        this.movesCount = _movesMade;
    }






    /**
     * @return the type of the piece.
     */
    public Type getType() {
        return type;
    }

    /**
     *
     * @param _targetCoordinates the location where it is moving.
     */
    public void setCoordinates(Square _targetCoordinates) {
        this.location = new Square(_targetCoordinates);
    }
    


// -----------------------------------------------------------------------------
// Constructors
// -----------------------------------------------------------------------------

    /**
     * @param _color the color of the piece.
     * @param _type the type of the piece.
     * @param _coordinates the location of this piece.
     */
    public Piece(Color _color, Type _type, Square _coordinates) {
        this.color = _color;
        this.type = _type;
        this.location = _coordinates;
    }

    /**
     * Copy Constructor
     *
     * @param _piece the piece to copy.
     */
    public Piece(Piece _piece) {
        this.color = _piece.getColor();
        this.type = _piece.getType();
        this.location = _piece.getCoordinates();
    }

// -----------------------------------------------------------------------------
// Methods
// -----------------------------------------------------------------------------

    /**
     * Checks whether a piece can move to the destination _coordinates,
     *  according to its specific movement rules. Also checks for
     *  collision and capture. (See ChessMovements.java)
     *
     * @param _coordinates the target location to check for movement.
     * @param _board the board to check for collision and capturing.
     * @return True if it can move there, false if not.
     */
    public boolean isValidMove(Square _coordinates, Board _board) {
        switch (this.getType()) {
            case ROOK:
                return ChessMovement.rookMove(this, _coordinates, _board);
            case KNIGHT:
                return ChessMovement.knightMove(this, _coordinates, _board);
            case BISHOP:
                return ChessMovement.bishopMove(this, _coordinates, _board);
            case QUEEN:
                return ChessMovement.queenMove(this, _coordinates, _board);
            case KING:
                return ChessMovement.kingMove(this, _coordinates, _board);
            case PAWN:
                return ChessMovement.pawnMove(this, _coordinates, _board);
        }
         return false;
    }

    /**
     * Increases the amount of moves done by 1.
     */
    public void increaseMovesCount() {
        this.movesCount += 1;
    }

        /**
     * Checks if this piece is able to capture the target piece
     *
     * @param _targetPiece The piece to check for capturability.
     * @return true if the piece is capturable, false if not.
     */
    public boolean canTakePiece(Piece _targetPiece, Board _board) {
        // A valid move to the target coordinates
        if (this.isValidMove(_targetPiece.getCoordinates(), _board) ) {
            // The color is not the same as this pieces color
            if (this.getColor() != _targetPiece.getColor() ) {
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        String output = "";
        if (this.color == Board.Color.BLACK) output = output + "BLACK";
        else output = output + "WHITE";

        switch (this.type){
            case PAWN:
                output = output + "PAWN";
                break;
            case ROOK:
                output = output + "ROOK";
                break;
            case BISHOP:
                output = output + "BISHOP";
                break;
            case QUEEN:
                output = output + "QUEEN";
                break;
            case KING:
                output = output + "KING";
                break;
            case KNIGHT:
                output = output + "KNIGHT";
                break;
        }
        String rankString = Integer.toString(this.getCoordinates().getRank());
        String fileString = Integer.toString(this.getCoordinates().getFile());
        output = output + "AT ( " +  rankString + " ; " + fileString + " )";
        return output;
    }

}
