package ChessPackage;

import ChessPackage.Piece.Type;
import ChessPackage.Piece.Type;
import ChessPackage.Piece.Type;
import javax.swing.JOptionPane;

/**
 * @filename Board.java
 * @author Shane Kelly
 * @date 26 Nov 2011
 */

public class Board {

// -----------------------------------------------------------------------------
// Fields
// -----------------------------------------------------------------------------

    /**
     *  The current player's turn.
     */
    private Color currentMovePlayer;

    /**
     * The pieces on the board. The index is used to maintain their positions.
     */
    private Piece[][] chessPiecesArray;

    /**
     * The boolean value indicating whether the board is initialized in the
     * normal starting position.
     */
    private boolean startedUp;

    /**
     * The integer value indicating how many moves have been made since
     * en passant has been enabled.
     */
    private int movesMadeSinceEnPassant;

    /**
     * The boolean value indicating whether an En Passant move can be made.
     */
    private boolean canEnPassant;


    /**
     * The amount of ranks - always 8.
     */
    private final int RANK_SIZE = 8;

    /**
     * The amount of files - always 8.
     */
    private final int FILE_SIZE = 8;

    /**
     *  Used for piece colors, player colors and square colors.
     */
    public enum Color {
        WHITE, BLACK
    }
// <editor-fold defaultstate="collapsed" desc="comment">
// -----------------------------------------------------------------------------
// Getters $ Setters
// -----------------------------------------------------------------------------


    /**
     * Activates the En Passant rule for the piece at the target square.
     *
     * @param _coordinates the target square to activate En Passant for.
     */
    public void activateEnPassantPiece(Square _coordinates) {
        this.chessPiecesArray[_coordinates.getRank()][_coordinates.getFile()].setEnPassant(true);
        this.movesMadeSinceEnPassant = 0;
        this.canEnPassant = true;
    }

    /**
     * Deactivates the En Passant rule for the piece at the target square.
     *
     * @param _coordinates the target square to deactivate En Passant for.
     */
    public void deactivateEnPassantPiece(Square _coordinates) {
        this.chessPiecesArray[_coordinates.getRank()][_coordinates.getFile()].setEnPassant(false);
        this.movesMadeSinceEnPassant = 1;
        this.canEnPassant = false;
    }

    /**
     * @return how many moves has been made since En Passant has been activated.
     */
    public int getMovesMadeSinceEnPassant() {
        return this.movesMadeSinceEnPassant;
    }

    /**
     * @return the current player's turn.
     */
    public Color getCurrentMovePlayer() {
        return currentMovePlayer;
    }

    /**
     *
     * @param _color the player that should move now.
     */
    public void setCurrentMovePlayer(Color _color) {
        this.currentMovePlayer = _color;
    }

    /**
     *
     * @return startedUp, indicator for if board was initialized in normal
     *  starting position.
     */
    public boolean isStartedUp() {
        return this.startedUp;
    }

    /**
     *
     * @param _isStartedUp the boolean to set startedUp to.
     */
    public void setStartedUp(boolean _isStartedUp) {
        this.startedUp = _isStartedUp;

    }

    /**
     * @return whether an en passant move can be made or not.
     */
    public boolean getEnPassant() {
        // En passant can only occur immediately after the trigger is activated.
        if (this.movesMadeSinceEnPassant == 0) {
            return this.canEnPassant;
        }
        return false;
    }

    /**
     * @return the amount of ranks this board has.
     */
    public int getRankSize() {
        return this.RANK_SIZE;
    }

    /**
     * @return the amount of files this board has.
     */
    public int getFileSize() {
        return this.FILE_SIZE;
    }


    /**
     *
     * @param _coordinates the point where the piece should be returned from.
     * @return the piece at _coordinates ( if there is one ).
     */
    public Piece getPiece(Square _coordinates) throws NullPointerException {
        int x = _coordinates.getRank();
        int y = _coordinates.getFile();
        if (x >= 0 &&  y >= 0 && x < 8 && y < 8) {
//            if (chessPiecesArray[x][y] != null) {
            return chessPiecesArray[x][y];
//            }
        }
        return null;
    }

    /**
     *
     * Takes a Piece[][] array and sets it up in starting position.
     *
     * @param _chessPieces the array of chess pieces to take for itself
     *  and set in starting position.
     */
    public final void setStartingPosition(Piece[][] _chessPieces) {

        // first, clear the board.
        clearBoard(_chessPieces);
        // Do the pawns.
        for (int rank = 0; rank < 8; rank++) {
            _chessPieces[rank][6] = new Piece(Color.BLACK, Type.PAWN, new Square(rank, 6));
            _chessPieces[rank][1] = new Piece(Color.WHITE, Type.PAWN, new Square(rank, 1));
        }
        // Now, all the other pieces.
        _chessPieces[0][0] = new Piece(Color.WHITE, Type.ROOK, new Square(0, 0));
        _chessPieces[0][7] = new Piece(Color.BLACK, Type.ROOK, new Square(0, 7));
        _chessPieces[1][0] = new Piece(Color.WHITE, Type.KNIGHT, new Square(1, 0));
        _chessPieces[1][7] = new Piece(Color.BLACK, Type.KNIGHT, new Square(1, 7));
        _chessPieces[2][0] = new Piece(Color.WHITE, Type.BISHOP, new Square(2, 0));
        _chessPieces[2][7] = new Piece(Color.BLACK, Type.BISHOP, new Square(2, 7));
        _chessPieces[3][0] = new Piece(Color.WHITE, Type.KING, new Square(3, 0));
        _chessPieces[3][7] = new Piece(Color.BLACK, Type.KING, new Square(3, 7));
        _chessPieces[4][0] = new Piece(Color.WHITE, Type.QUEEN, new Square(4, 0));
        _chessPieces[4][7] = new Piece(Color.BLACK, Type.QUEEN, new Square(4, 7));
        _chessPieces[5][0] = new Piece(Color.WHITE, Type.BISHOP, new Square(5, 0));
        _chessPieces[5][7] = new Piece(Color.BLACK, Type.BISHOP, new Square(5, 7));
        _chessPieces[6][0] = new Piece(Color.WHITE, Type.KNIGHT, new Square(6, 0));
        _chessPieces[6][7] = new Piece(Color.BLACK, Type.KNIGHT, new Square(6, 7));
        _chessPieces[7][0] = new Piece(Color.WHITE, Type.ROOK, new Square(7, 0));
        _chessPieces[7][7] = new Piece(Color.BLACK, Type.ROOK, new Square(7, 7));

        this.chessPiecesArray = _chessPieces;
        setStartedUp(true);
    }



// </editor-fold>
// -----------------------------------------------------------------------------
// Constructors
// -----------------------------------------------------------------------------

    /**
     * Copy Constructor.
     *  
     * @param _board the Board to copy.
     */
    public Board(Board _board) {
        this.canEnPassant = _board.getEnPassant();
        System.out.println(Boolean.toString(this.canEnPassant));
        this.chessPiecesArray = (_board.getChessPiecesArray());
        this.currentMovePlayer = _board.getCurrentMovePlayer();
        this.movesMadeSinceEnPassant = _board.getMovesMadeSinceEnPassant();
        this.startedUp = _board.isStartedUp();

    }


    /**
     *
     */
    public Board() {
        this.currentMovePlayer = Color.WHITE;
        this.chessPiecesArray = new Piece[8][8];
        setStartingPosition(this.chessPiecesArray);
        this.movesMadeSinceEnPassant = 0;
        this.canEnPassant = false;
    }




// -----------------------------------------------------------------------------
// Methods
// -----------------------------------------------------------------------------

    /**
     * Checks whether a king of player _color at target location _coordinates is
     * under attack.
     *
     * @param _color the player to check.
     * @param _coordinates The target location of the king.
     * @return whether the king is under attack or not.
     */
    public boolean kingUnderAttack(Color _color, Square _coordinates) {
        if (squareUnderAttack(_color, getKingPosition(_color))) {
            return true;
        }
        return false;
    }

    /**
     * Promotes a pawn piece using JOptionPane.showOptionDialog() for input.
     *
     * @param _piece the pawn to promote.
     * @param _destinationLocation The location of the pawn.
     */
    private void promotePawn(Piece _piece, Square _destinationLocation) {
        String[] pawnPromotionChoices = {"Queen", "Rook", "Bishop", "Knight"};
        this.killPosition(_destinationLocation);
        int response = JOptionPane.showOptionDialog(
                null,
                "What would you like to promote to?",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                pawnPromotionChoices,
                "Queen");
        switch (response) {
            case 0:
                this.spawnPiece(new Piece(_piece.getColor(), Type.QUEEN, _destinationLocation));
                break;
            case 1:
                this.spawnPiece(new Piece(_piece.getColor(), Type.ROOK, _destinationLocation));
                break;
            case 2:
                this.spawnPiece(new Piece(_piece.getColor(), Type.BISHOP, _destinationLocation));
                break;
            case 3:
                this.spawnPiece(new Piece(_piece.getColor(), Type.KNIGHT, _destinationLocation));
                break;
            default:
                this.spawnPiece(new Piece(_piece.getColor(), Type.QUEEN, _destinationLocation));
                break;
        }
    }

    /**
     * Spawns a Piece of type _chessPiece at _chessPiece.getCoordinates().
     *
     * @param _chessPiece the Piece to spawn.
     */
    public void spawnPiece(Piece _chessPiece) {
        int x = _chessPiece.getCoordinates().getRank();
        int y = _chessPiece.getCoordinates().getFile();
        this.chessPiecesArray[x][y] = _chessPiece;
    }

    /**
     * Scans the board for a KING of Color _color until it finds one. If it is
     * unable to find one, it returns null.
     *
     * @param _color The color of the king.
     * @return The Coordinate position of the king.
     */
    public Square getKingPosition(Color _color) {
        boolean pieceExists, isSameColor, isKing;

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++)
            {
                pieceExists = this.getPiece(new Square(rank, file)) != null;
                if (pieceExists) {
                    isSameColor = this.getPiece(new Square(rank, file)).getColor() == _color;
                    isKing = this.getPiece(new Square(rank, file)).getType() == Type.KING;
                    if (isSameColor && isKing) {
                                return new Square(rank, file);
                    }
                }
            } //end for
        }
        return null;
    }

    /**
     * Checks whether a specified square of color _color at target location
     *  _coordinates is under attack. To do this, it checks every square
     *  on the board for a piece of the enemy color - if there is a piece,
     *  and if it is of the enemy color, AND if it can move to the target
     *  location validly. As soon as it finds the first piece to meet
     *  these criteria, it returns true. Else if it finds none, it returns
     *  false.
     *
     * @param _color The color of the square under attack.
     * @param _coordinates The target location of the square under attack.
     * @return whether the square is under attack or not.
     */
    public boolean squareUnderAttack(Color _color, Square _coordinates) {
        boolean _isOppositeColor;
        boolean _canMove;
        Piece _tempPiece;

        for (int rank = 0; rank < 8; rank++) {      // Loop through ranks and
            for (int file = 0; file < 8; file++) {  // files for pieces
                if (this.getPiece(new Square(rank, file)) != null) {
                    _isOppositeColor = (this.getPiece(new Square(rank, file)).getColor() != _color);
                    _canMove = (this.getPiece(new Square(rank, file)).isValidMove(_coordinates, this));
                    if (_isOppositeColor && _canMove) {
                        return true;
                    }
                }
            }// end file
        } // end rank

        return false;
    }

    /**
     * Deactivates En Passant globally for a player specified by _color.
     *
     * @param _color the player to deactivate En Passant for.
     */
    private void deactivateEnPassant(Color _color) {
        boolean pieceExists;
        boolean isPawn, isSameColor;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                pieceExists = this.getPiece(new Square(rank, file)) != null;
                if (pieceExists) {
                    isPawn = this.getPiece(new Square(rank, file)).getType() == Type.PAWN;
                    isSameColor = this.getPiece(new Square(rank, file)).getColor() == _color;
                    if (isPawn && isSameColor) {
                        this.chessPiecesArray[rank][file].setEnPassant(false);
                    }
                }
            }
        }
    }

    /**
     *
     */
    private Piece[][] getChessPiecesArray() {
        Piece[][] piecesArray = new Piece[this.getRankSize()][this.getFileSize()];

        for (int rank = 0; rank < this.getRankSize(); rank++) {
            for (int file = 0; file < this.getFileSize(); file++) {
                if (this.getPiece(new Square(rank, file)) != null) {
                    piecesArray[rank][file] = new Piece(this.getPiece(new Square(rank, file)));
                }
            }
        }
        return piecesArray;
    }


    /**
     * Clears the target square at _coordinates of any ChessPieces.
     *
     * @param _coordinates the target square to clear.
     */
    public void killPosition(Square _coordinates) {
        this.chessPiecesArray[_coordinates.getRank()][_coordinates.getFile()] = null;
    }


    /**
     * @param _color The color of the player.
     * @param _piece The Piece to compare with the color of the player.
     * @return whether the Piece belongs to the player _color.
     */
    public boolean pieceBelongsToPlayer(Color _color, Piece _piece) {
        if (_piece.getColor() == _color) {
            return true;
        }
        return false;
    }

    /**
     * Clears an entire 2D array of Piece objects.
     *
     * @param _chessPieces the array of Piece objects to clear.
     */
    private void clearBoard(Piece[][] _chessPieces) {
        for (int rank = 0; rank < 8; rank++){
            for (int file = 0; file < 8; file++){
                _chessPieces[rank][file] = null;
            }
        }
    }

    /**
     * Checks a piece for if it has any legal moves to make.
     *
     * @param _thisPiece the piece to check if a legal move can be made for.
     * @return whether this piece can make a legal move.
     */
    private boolean checkForMove(Piece _thisPiece) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Square _loopLocation = new Square(rank,file);
                if (_thisPiece.isValidMove(_loopLocation, this)) {
                    if (!(this.doesApplyCheck(_thisPiece, _loopLocation))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player has any legal moves to do. Go through all pieces
     *  belonging to _color, check if they have any moves left to do by using
     *  doesApplyCheck() to filter them out. As soon as it detects something,
     *  return false. else return true.
     *
     * @param _color the player to check for stalemate.
     * @return whether the players are in stalemate or not.
     */
    private boolean staleMateDetection(Color _color) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Square _loopLocation = new Square(rank, file);
                if (this.getPiece(_loopLocation) != null) {
                    Piece _thisPiece = this.getPiece(_loopLocation);
                    if (_thisPiece.getColor() == _color) {
                        if (this.checkForMove(_thisPiece)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * This function goes through
     * all the board's squares, looking for moves that _thisPiece can make,
     * and if the move is valid, it checks if it takes the king OUT of check.
     * As soon as it detects a move that gets the player out of check, it returns
     * true. If it detects nothing, then it returns false.
     *
     * @param _thisPiece the piece to check for a valid move.
     * @param _board The board to iterate through.
     * @return True if there is a move that takes the player out of check; false
     * if not.
     */
    private boolean checkForValidMove(Piece _thisPiece) {
        if (this.kingUnderAttack(_thisPiece.getColor(), this.getKingPosition(_thisPiece.getColor()))) {
            for (int rank = 0; rank < 8; rank++) {
                for (int file = 0; file < 8; file++) {
                    Square _loopLocation = new Square(rank,file);
                    if (_thisPiece.isValidMove(_loopLocation, this)) {
                        if (this.doesRemoveCheck(_thisPiece, _loopLocation)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * From en.wikipedia.org/wiki/Rules_of_chess
     * "If a player's king is placed in check and there is no legal move that
     * player can make to escape check, then the king is said to be checkmated,
     * the game ends, and that player loses."
     *
     * This means, I have to go through all the player's pieces' moves and
     * as soon as it detects something that removes it from check, it is NOT
     * in checkmate.
     *
     * @param _color the player to check if in checkmate.
     * @param _board the board to check on.
     * @return whether the player is in checkmate or not.
     */
    private boolean checkMateDetection(Color _color) {
        // Firstly set up the loop so that it goes through the entire board,
        // looking for pieces belonging to _color. As soon as it picks up
        // that a piece at (rank, file) is a piece belonging to
        // _color, use go through all of that piece's moves to see if any of
        // them take the player out of check.
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Square _loopLocation = new Square(rank,file);
                //Firstly check if there is a piece here.
                if (this.getPiece(_loopLocation) != null) {
                    // There is a piece - check its color.
                    if (this.getPiece(_loopLocation).getColor() == _color) {
                        // it belongs to _color. Check its moves for a valid move.
                        Piece _thisPiece = this.getPiece(_loopLocation);
                        if (this.checkForValidMove(_thisPiece)) {
                            return false;
                        }
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(null, _color.toString() + " is in checkmate!");
        return true;
    }

    /**
     * Manual move function working with the ChessPieces array that simply moves
     * a piece from _piece.getCoordinates to _destinationLocation.
     * 
     * @param _piece The piece to move.
     * @param _destinationLocation The target location to move it to.
     */
    private void setPiecePosition(Piece _piece, Square _destinationLocation) {
        Square _currentLocation = new Square(_piece.getCoordinates());
        this.chessPiecesArray[_currentLocation.getRank()][_currentLocation.getFile()] = null;
        this.chessPiecesArray[_destinationLocation.getRank()][_destinationLocation.getFile()] = _piece;
    }

    /**
     *
     * If the moving player's king is in check (_piece.getColor() for
     *  determining the player to check;
     *  _board.kingInCheck(_piece.getColor()) for checking if the king is
     *  in check), do the move on a virtual, identical board.
     *  If the king is still in check, it's an invalid move.
     *  It is assumed that this function will only be called when
     *  all the appropriate checks have already been made.
     *
     * @param _piece the piece to be moved.
     * @param _destinationLocation the target destination to move towards.
     * @return whether this removes check status from the moving player's king.
     *
     */
    public boolean doesRemoveCheck(Piece _piece, Square _destinationLocation) {
        if (this.kingUnderAttack(_piece.getColor(), this.getKingPosition(_piece.getColor()))) {
            Board _virtualBoard = new Board(this);
            _virtualBoard.setPiecePosition(_piece, _destinationLocation);
            if(!(_virtualBoard.kingUnderAttack(_piece.getColor(), _virtualBoard.getKingPosition(_piece.getColor())))) {
                return true;
            }
        }
        return false;
    }

    /**
     * The inverse of doesRemoveCheck(). Checks if the move made puts the
     * moving player in check. If it does, it's an invalid move. Assume false.
     *
     *
     * @param _piece the piece to be moved.
     * @param _destinationLocation the target destination to move towards.
     * @return whether this applies check status from the moving player's king.
     *
     */
    public boolean doesApplyCheck(Piece _piece, Square _destinationLocation) {
        if (!this.kingUnderAttack(_piece.getColor(), this.getKingPosition(_piece.getColor()))) {

            Board _virtualBoard = new Board(this);
            _virtualBoard.setPiecePosition(_piece, _destinationLocation);
            if((_virtualBoard.kingUnderAttack(_piece.getColor(), _virtualBoard.getKingPosition(_piece.getColor())))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Firstly checks all the necessary criteria for if a move can be made or
     * not, including if the destination location is actually on the board or
     * not, if it's a valid move for the piece, if the move gets the player
     * in check or not, if it gets the player OUT of check or not, if it sets
     * the opposing player in checkmate or stalemate etc. Then attempts to move
     * the piece on the board to the destination location.
     *
     * @param _piece The piece to move.
     * @param _board The board on which to move it.
     * @param _destinationLocation The location towards which to move it.
     * @return Whether the move was successful or not.
     */
    public boolean movePiece(Piece _piece, Square _destinationLocation) {
        // A) Is the destination on the board?
        if (_destinationLocation.getRank() < RANK_SIZE && _destinationLocation.getFile() < FILE_SIZE) {

            // B) Is the destination within the movement/capture rules
            //    for the piece, and is there something in the way?
            if (_piece.isValidMove(_destinationLocation, this)) {

                // C) Does the piece being moved belong to the player
                //    whose move it currently is?
                if (pieceBelongsToPlayer(getCurrentMovePlayer(), _piece)){

                    // If so, attempt to move the piece and make it the other
                    //  player's turn. However, if the moving player's king
                    // is in check, then first look if the attempted move
                    // removes it from check. If it does, it's a valid move.
                    // if not, then it's invalid and should not be done.
                    // if it does not remove check, the board should be prompted
                    // to check for Checkmate and/or Stalemate.

                    if (this.kingUnderAttack(_piece.getColor(), this.getKingPosition(_piece.getColor()))) {

                        if (this.doesRemoveCheck(_piece, _destinationLocation)) {
                            
                            this.chessPiecesArray[_piece.getCoordinates().getRank()][_piece.getCoordinates().getFile()] = null;
                            _piece.setCoordinates(new Square(_destinationLocation.getRank(), _destinationLocation.getFile()));
                            this.chessPiecesArray[_destinationLocation.getRank()][_destinationLocation.getFile()] = _piece;
                            this.chessPiecesArray[_destinationLocation.getRank()][_destinationLocation.getFile()].increaseMovesCount();
                            // Pawn promotion for white
                            if (_destinationLocation.getFile() == 7) {
                                if (_piece.getType() == Type.PAWN) {
                                    if (_piece.getColor() == Color.WHITE) {
                                        promotePawn(_piece, _destinationLocation);
                                    }
                                }
                            }
                            // Pawn promotion for black
                            else if(_destinationLocation.getFile() == 0) {
                                if (_piece.getType() == Type.PAWN) {
                                    if (_piece.getColor() == Color.BLACK) {
                                        promotePawn(_piece, _destinationLocation);
                                    }
                                }
                            }

                            if (getCurrentMovePlayer() == Color.WHITE) {
                                setCurrentMovePlayer(Color.BLACK);
                                this.deactivateEnPassant(Color.WHITE);
                            }
                            else {
                                setCurrentMovePlayer(Color.WHITE);
                                this.deactivateEnPassant(Color.BLACK);

                            }
                            return true;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, _piece.getColor().toString()
                                    + " You are in check, checkmate or stalemate - you cannot move there!");
                        }
                    }
                    else {

                        if (!(this.doesApplyCheck(_piece, _destinationLocation))) {
                            this.chessPiecesArray[_piece.getCoordinates().getRank()][_piece.getCoordinates().getFile()] = null;
                            _piece.setCoordinates(new Square(_destinationLocation.getRank(), _destinationLocation.getFile()));
                            this.chessPiecesArray[_destinationLocation.getRank()][_destinationLocation.getFile()] = _piece;
                            this.chessPiecesArray[_destinationLocation.getRank()][_destinationLocation.getFile()].increaseMovesCount();
                            // Pawn promotion for white
                            if (_destinationLocation.getFile() == 7) {
                                if (_piece.getType() == Type.PAWN) {
                                    if (_piece.getColor() == Color.WHITE) {
                                        promotePawn(_piece, _destinationLocation);
                                    }
                                }
                            }
                            // Pawn promotion for black
                            else if(_destinationLocation.getFile() == 0) {
                                if (_piece.getType() == Type.PAWN) {
                                    if (_piece.getColor() == Color.BLACK) {
                                        promotePawn(_piece, _destinationLocation);
                                    }
                                }
                            }

                            if (this.kingUnderAttack(Color.WHITE, this.getKingPosition(Color.WHITE))) {
                                if (this.checkMateDetection(Color.WHITE)) {
                                        JOptionPane.showMessageDialog(null, "White is in checkmate! Black wins.");
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "White is in check!");
                                }
                            }
                            else {
                                if (this.staleMateDetection(_piece.getColor())) {
                                    JOptionPane.showMessageDialog(null, "A stalemate has occurred! Both players tie.");
                                }
                            }

                            if (this.kingUnderAttack(Color.BLACK, this.getKingPosition(Color.BLACK))) {
                                if (this.checkMateDetection(Color.BLACK)) {
                                        JOptionPane.showMessageDialog(null, "Black is in checkmate! White wins.");
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Black is in check!");
                                }
                            }
                            else {
                                if (this.staleMateDetection(_piece.getColor())) {
                                    JOptionPane.showMessageDialog(null, "A stalemate has occurred! Both players tie.");
                                }
                            }

                            if (getCurrentMovePlayer() == Color.WHITE) {
                                setCurrentMovePlayer(Color.BLACK);
                                this.deactivateEnPassant(Color.WHITE);
                            }
                            else {
                                setCurrentMovePlayer(Color.WHITE);
                                this.deactivateEnPassant(Color.BLACK);

                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}