package ChessPackage;

import ChessPackage.Piece.Type;
import ChessPackage.Board.Color;

/**
 * @filename ChessMovement.java
 * @author Shane Kelly
 * @date 27 Nov 2011
 */

// -----------------------------------------------------------------------------
// Imports & Definition
// -----------------------------------------------------------------------------

/**
 *
 * Handles all the checks for the Piece objects.
 */
public class ChessMovement {
    
    /**
     * 
     * Checks if the moving piece is trying to capture an enemy piece. If the
     *  destination location on the board has a piece of the same color or no
     *  piece at all, then it is not a capture move. Else it is.
     * 
     * @param _piece the piece being moved on the board.
     * @param _destinationLocation The location to move the piece.
     * @param _board The board on which the move is occurring.
     * @return whether it was a successful capture or not.
     */
    public static boolean destinationCheck(Piece _piece, Board _board, Square _destinationLocation) {
        if (_board.getPiece(_destinationLocation) != null) {
            if (_piece.getColor() == _board.getPiece(_destinationLocation).getColor()) {
                return false;
            }
        }
        return true;
    }
    /**
     *
     * Move rule for the pawn. If it's moving one up or down (depending on
     *  whether it is white or black) and staying on the same place on the
     *  horizontal plane, and if there is not a piece
     *  at the destination location, then the move is valid. Else, if
     *  it's moving one up or down and either one left or right, and there
     *  is an enemy piece, then it is a valid CAPTURE move. Else, if
     *  it is at starting position (if moveCount < 1), it's NOT moving
     *  on the horizontal plane AND it's moving two up or down, then it
     *  is a valid move. Else it is an invalid move and will not be made.
     * Pawn movements differ from other piece's movements where it has its own
     *  capture movement rules, and does NOT use the normal destinationCheck()
     *  method to do that checking for it.
     *
     * @param _piece the piece being moved on the board.
     * @param _target The location to move the piece.
     * @param _board The board on which the move is occurring.
     * @return whether the move was successful or not.
     */
    public static boolean pawnMove(Piece _piece, Square _target, Board _board) {

        Square _currentLocation = _piece.getCoordinates();
        int rank, file, targetRank, targetFile;
        rank = _piece.getCoordinates().getRank();
        file = _piece.getCoordinates().getFile();
        targetRank = _target.getRank();
        targetFile = _target.getFile();
        Color color = _piece.getColor();
        Piece targetPiece = _board.getPiece(_target);
        Piece pieceLeft = _board.getPiece(new Square(rank - 1, file));
        Piece pieceRight= _board.getPiece(new Square(rank + 1, file));
        int movesMade = _piece.getMovesCount();
        Piece pieceOneUp = _board.getPiece(new Square(_target.getRank(), _target.getFile() - 1));
        Piece pieceOneDown = _board.getPiece(new Square(_target.getRank(), _target.getFile() + 1));
        Piece enPassantLeft = _board.getPiece(new Square(rank - 1, targetFile));
        Piece enPassantRight = _board.getPiece(new Square(rank + 1, targetFile));


        Boolean isMovingOneUpOrDown = ((file == targetFile - 1 && color == Color.WHITE) || (file == targetFile + 1 && color == Color.BLACK));
        Boolean isNotMovingInRank = (_currentLocation.getRank() == _target.getRank());
        Boolean isNoPieceAtTarget = (_board.getPiece(_target) == null);


        Boolean isMovingOneLeftOrRight = (rank == targetRank - 1 || rank == targetRank + 1);
        Boolean isTargetOccupied = (targetPiece != null);
        Boolean isTargetEnemy = (isTargetOccupied && targetPiece.getColor() != _piece.getColor());

        Boolean isPieceLeftPresent = (pieceLeft != null);
        Boolean isPieceLeftIsPawn = (isPieceLeftPresent && pieceLeft.getType() == Type.PAWN);
        Boolean isPieceLeftIsEnemy = (isPieceLeftPresent && pieceLeft.getColor() != color);

        Boolean isPieceRightPresent = (pieceRight != null);
        Boolean isPieceRightIsPawn = (isPieceRightPresent && pieceRight.getType() == Type.PAWN);
        Boolean isPieceRightIsEnemy = (isPieceRightPresent && pieceRight.getColor() != color);

        Boolean isOneLeft = (_target.getRank() == rank - 1);
        Boolean isOneRight = (_target.getRank() == rank + 1);

        Boolean isEnPassant = _piece.getEnPassant();

        Boolean isFirstMove = (movesMade < 1);
        Boolean isMovingTwoUpOrDown = (file == targetFile - 2 && color == Color.WHITE) || (file == targetFile + 2 && color == Color.BLACK);
        Boolean isNothingInPath = (color == Color.WHITE && pieceOneUp == null) || ( color == Color.BLACK && pieceOneDown == null);

        Boolean isEnPassantLeftPresent = (enPassantLeft != null);
        Boolean isEnPassantLeftPawn = (isEnPassantLeftPresent && enPassantLeft.getType() == Type.PAWN);
        Boolean isEnPassantLeftEnemy = (isEnPassantLeftPresent && enPassantLeft.getColor() != color);

        Boolean isEnPassantRightPresent = (enPassantRight != null);
        Boolean isEnPassantRightPawn = (isEnPassantRightPresent && enPassantRight.getType() == Type.PAWN);
        Boolean isEnPassantRightEnemy = (isEnPassantRightPresent && enPassantRight.getColor() != color);



        if (isMovingOneUpOrDown) {
            if (isNotMovingInRank && isNoPieceAtTarget) return true;
            if (isMovingOneLeftOrRight) {
                if (isTargetOccupied && isTargetEnemy) return true;
                else if (isEnPassant) {
                    if (isOneLeft) {
                        if (isPieceLeftPresent && isPieceLeftIsPawn && isPieceLeftIsEnemy) {
                            _piece.setEnPassant(false);
                            _board.killPosition(new Square(rank - 1, file));
                            return true;
                        }
                    }
                    else if (isOneRight) {
                        if (isPieceRightPresent && isPieceRightIsPawn && isPieceRightIsEnemy) {
                            _piece.setEnPassant(false);
                            _board.killPosition(new Square(_currentLocation.getRank() + 1, _currentLocation.getFile()));
                            return true;
                        }
                    }
                }
            }
        }
        else if (isFirstMove) {
            if (isMovingTwoUpOrDown) {
                if (isNotMovingInRank){
                    if (isNothingInPath) {
                        if (isEnPassantLeftPresent && isEnPassantLeftPawn && isEnPassantLeftEnemy) {
                                    _board.activateEnPassantPiece(enPassantLeft.getCoordinates());
                        }
                        if(isEnPassantRightPresent && isEnPassantRightPawn && isEnPassantRightEnemy) {
                                    _board.activateEnPassantPiece(enPassantRight.getCoordinates());
                        }
                        return true;
                    }
                }
            }
        }
        return false;
        }
    

    /**
     *
     * Move rule for the rook. A rook move is assumed to be correct, unless
     * the checks made indicate that it is false. First, it checks the
     * movement of the piece: If it moved in both the horizontal AND the
     * vertical planes, then it is an invalid move. Secondly, it checks for
     * collision. It checks every piece between either its current rank and
     * destination rank, or its current file and destination file, depending
     * on which plane it moved it, and if it gets any piece through the loop
     * then it is an invalid move. Lastly, it checks for a possible capture.
     * (See destinationCheck().) If none of these checks returned false, it's a
     * valid move.
     *
     * @param _piece the piece being moved on the board.
     * @param _destinationLocation The location to move the piece.
     * @param _board The board on which the move is occurring.
     * @return whether the move was successful or not.
     */
    public static boolean rookMove(Piece _piece, Square _destinationLocation, Board _board) {
        Square _currentLocation = _piece.getCoordinates();

        // ---------------- Check Movement ---------------------------------- //
        // Did it move horizontally?
        boolean _changedRank = !(_currentLocation.getRank() == _destinationLocation.getRank());
        // Did it move vertically?
        boolean _changedFile = !(_currentLocation.getFile() == _destinationLocation.getFile());

        // If it moved in both planes, it's an invalid move.
        if (_changedRank && _changedFile){
            return false;
        }
        // ------------------------------------------------------------------ //

        // --------------- Check Collision ---------------------------------- //
        // If it moved horizontally, check for collision on the x-axis.
        if (_changedRank && !(_changedFile)) {
            int currentRank = _currentLocation.getRank();
            int destinationRank = _destinationLocation.getRank();
            // If it moved to the right:
            if (destinationRank > currentRank) {
                // Check every square between board[currentRank, currentFile]
                // and board[destinationRank, destinationFile] if there is
                // a piece.
                for (int loopRank = currentRank + 1; loopRank < destinationRank; loopRank++) {
                    if (_board.getPiece(new Square(loopRank, _currentLocation.getFile())) != null) {
                        return false;
                    }
                }
            }
            // Else if it moved to the left:
            else if (destinationRank < currentRank) {
                // Check every square between board[currentRank, currentFile]
                // and board[destinationRank, destinationFile] if there is
                // a piece.
                for (int loopRank = currentRank - 1; loopRank > destinationRank; loopRank--) {
                    if (_board.getPiece(new Square(loopRank, _currentLocation.getFile())) != null) {
                        return false;
                    }
                }
            }
        }
        // Else if it moved vertically, check for collision on the y-axis.
        else if (!(_changedRank) && _changedFile) {
            int currentFile = _currentLocation.getFile();
            int destinationFile = _destinationLocation.getFile();
            // If it moved downwards:
            if (currentFile > destinationFile) {
                // Check every square between board[currentRank, currentFile]
                // and board[destinationRank, destinationFile] if there is
                // a piece.
                for (int loopFile = currentFile - 1; loopFile > destinationFile; loopFile--) {
                    if (_board.getPiece(new Square( _currentLocation.getRank(), loopFile)) != null) {
                        return false;
                    }
                }
            }
            // Else if it moved upwards:
            else if (currentFile < destinationFile) {
                // Check every square between board[currentRank, currentFile]
                // and board[destinationRank, destinationFile] if there is
                // a piece.
                for (int loopFile = currentFile + 1; loopFile < destinationFile; loopFile++) {
                    if (_board.getPiece(new Square( _currentLocation.getRank(), loopFile)) != null) {
                        return false;
                    }
                }
            }
        }
        // ------------------------------------------------------------------ //
        // ------------- Check Capture -------------------------------------- //
        if (!(destinationCheck(_piece, _board, _destinationLocation))) {
            return false;
        }
        // ------------------------------------------------------------------ //
        return true;
    }

    /**
     *
     * The rule for knight movement. Knights can ONLY move one square in either
     *  plane in any direction, and two squares in the other plane in any
     *  direction, simultaneously. Also, the standard capture check needs to be
     *  made. If none of the checks return true, it's an invalid move.
     *
     * @param _piece the piece being moved on the board.
     * @param _destinationLocation The location to move the piece.
     * @param _board The board on which the move is occurring.
     * @return whether the move was successful or not.
     */
    public static boolean knightMove(Piece _piece, Square _destinationLocation, Board _board) {
        Square _currentLocation = _piece.getCoordinates();
        // ---------------- Check Movement ---------------------------------- //
        boolean movementIsValid = false;
        // If the piece moved one square up or one square down
        if (_currentLocation.getFile() == _destinationLocation.getFile() - 1 ||
            _currentLocation.getFile() == _destinationLocation.getFile() + 1 ) {
            // If the piece moved two squares left or two squares right
            if (_currentLocation.getRank() == _destinationLocation.getRank() - 2 ||
                _currentLocation.getRank() == _destinationLocation.getRank() + 2 ) {
                // Then it's a valid move.
                movementIsValid = true;
            }
        }
        // If the piece moved one square left or one square right
        if (_currentLocation.getRank() == _destinationLocation.getRank() - 1 ||
            _currentLocation.getRank() == _destinationLocation.getRank() + 1 ) {
            // If the piece moved two squares up or two squares down
            if (_currentLocation.getFile() == _destinationLocation.getFile() - 2 ||
                _currentLocation.getFile() == _destinationLocation.getFile() + 2 ) {
                // Then it's a valid move.
                movementIsValid = true;
            }
        }
        // ------------------------------------------------------------------ //

        // ---------------- Capture Checks ---------------------------------- //
        if ((destinationCheck(_piece, _board, _destinationLocation))) {
            if (movementIsValid) {
                return true;
            }
        }
        // ------------------------------------------------------------------ //
        return false;
    }

    /**
     * The rule for bishop movement. A bishop move is assumed to be correct,
     * unless the checks made indicate that it is false. Bishops move in a
     * straight line diagonally. To check this, the change in the horizontal
     * plane is compared to the change in the vertical plane. If they're
     * the same, the movement is legal and the gradient is negative. If
     * their SUM equals 0, the movement is legal and the gradient is positive.
     * The gradients are used specifically for checking collision, to determine
     * which direction to look into.
     *
     * @param _piece the piece being moved on the board.
     * @param _destinationLocation The location to move the piece.
     * @param _board The board on which the move is occurring.
     * @return whether the move was successful or not.
     */
    public static boolean bishopMove(Piece _piece, Square _destinationLocation, Board _board) {
        Square _currentLocation = _piece.getCoordinates();


        // ---------------- Check Collision --------------------------------- //
        // If it has a positive gradient, check both directions for collision.
        // Else if it has a negative gradient, check both of the other
        // directions.
        
        // The amount of squares moved in both directions
        int changeInHorizontalPlane = _currentLocation.getRank() - _destinationLocation.getRank();
        int changeInVerticalPlane = _currentLocation.getFile() - _destinationLocation.getFile();

        // Using variables for the specific values of the coordinate data,
        // because it's easier to read
        int currentRank, currentFile, destinationRank, destinationFile;
        currentRank = _currentLocation.getRank();
        currentFile = _currentLocation.getFile();
        destinationRank = _destinationLocation.getRank();
        destinationFile = _destinationLocation.getFile();

        // Need loop variables for collision detection.
        int loopRank = _piece.getCoordinates().getRank();
        int loopFile = _piece.getCoordinates().getFile();

        // if it's left-down or right-up:
        if ((changeInHorizontalPlane + changeInVerticalPlane) == 0) {
            // if it's left-down:
            if (Integer.signum(currentRank - destinationRank) == 1) {
                while (loopRank > destinationRank + 1 && loopFile < destinationFile - 1) {
                    loopRank -= 1;
                    loopFile += 1;
                    if (_board.getPiece(new Square(loopRank, loopFile)) != null) {
                        return false;
                    }
                }
            }
            // else if it's right-up:
            else if (Integer.signum(currentFile - destinationFile) == 1) {
                while (loopRank < destinationRank - 1 && loopFile > destinationFile + 1) {
                    loopRank += 1;
                    loopFile -= 1;
                    if (_board.getPiece(new Square(loopRank, loopFile)) != null) {
                        return false;
                    }
                }
            }
        }
        // else if it's left-up or right-down:
        else if(changeInHorizontalPlane == changeInVerticalPlane) {
            // if it's left-up:
            if (Integer.signum(currentRank - destinationRank) == -1) {
                while (loopRank < destinationRank - 1 && loopFile < destinationFile - 1) {
                    loopRank += 1;
                    loopFile += 1;
                    if (_board.getPiece(new Square(loopRank, loopFile)) != null) {
                        return false;
                    }
                }
            }
            // else if it's right-down:
            else if (Integer.signum(currentRank - destinationRank) == 1) {
                while (loopRank > destinationRank + 1 && loopFile > destinationFile + 1) {
                    loopRank -= 1;
                    loopFile -= 1;
                    if (_board.getPiece(new Square(loopRank, loopFile)) != null) {
                        return false;
                    }

                }
            }
        }
        else return false;
        // ------------------------------------------------------------------ //

        // ---------------- Check Capture ----------------------------------- //
        // If it lands on a piece of the same color, it's illegal.
            if (_board.getPiece(_destinationLocation) != null) {
                if (_piece.getColor() == _board.getPiece(_destinationLocation).getColor()) {
                    return false;
                }
            }
        // ------------------------------------------------------------------ //
        return true;
    }

    /**
     *
     * The rule for queen movement. Rules move exactly like both bishops and
     * rooks. Therefore, using the bishopMove and rookMove functions with
     * the parameters received by queenMove, I can easily determine
     * whether it's a legal move or not.
     *
     * @param _piece the piece being moved on the board.
     * @param _destinationLocation The location to move the piece.
     * @param _board The board on which the move is occurring.
     * @return whether the move was successful or not.
     */
    public static boolean queenMove(Piece _piece, Square _destinationLocation, Board _board) {
        boolean _isValidBishopMove = bishopMove(_piece, _destinationLocation, _board);
        boolean _isValidRookMove = rookMove(_piece, _destinationLocation, _board);
        return (_isValidBishopMove || _isValidRookMove);
    }

    /**
     *
     * The king's movement rules. Kings can move like queens, except they can
     * only travel one square in both directions. That means they can only reach
     * their immediate surrounding squares.
     *
     * @param _piece the piece being moved on the board.
     * @param _target The location to move the piece.
     * @param _board The board on which the move is occurring.
     * @return whether the move was successful or not.
     */
    public static boolean kingMove(Piece _piece, Square _target, Board _board) {

        // --------- Local Variable Decleration: ---------------------------- //

        // Is this move a castling move? Assumed to be false.
        boolean _isCastlingMove = false;

        // If it is a castling move, is it kingside or queenside?
        boolean _isCastlingKingside = false;

        // Is the movement valid? Assumed to be false.
        boolean movementIsValid = false;

        // Is there a rook to castle with?
        boolean _doesRookExist;

        // Has it moved?
        boolean _rookHasMoved;

        // The rook involved.
        Piece _rookPieceInvolved;

        // Where is the piece moving from?
        Square _currentLocation = _piece.getCoordinates();

        // Temp variables for code elegance.
        int rank, file, targetRank, targetFile;
        rank = _currentLocation.getRank();
        file = _currentLocation.getFile();
        targetRank = _target.getRank();
        targetFile = _target.getFile();
        // ------------------------------------------------------------------ //

        // ---------------- Check Movement ---------------------------------- //
        // Kings can move like queens, except they can only travel to their
        //  immediate surroundings (the potentially 8 squares surrouding it in
        //  all directions). They can also perform a Castle maneuvre.

        
        // if move is vertical
        if (rank == targetRank) {
            // if it's up or down, it's legal.
            if (file == targetFile - 1 || file == targetFile + 1) {
                movementIsValid = true;
            }
        }
        // if move is horizontal
        if (file == targetFile) {
            // if it's left or right, it's legal.
            if (rank == targetRank - 1 || rank == targetRank + 1) {
                movementIsValid = true;
            }
        }
        // if move is diagonal (left or right AND up or down)
        if ((rank == targetRank - 1 || rank == targetRank + 1) &&
            (file == targetFile - 1 || file == targetFile + 1)) {
            // then it's a valid move.
            movementIsValid = true;
        }

        // If a castling move is attempted, it's also a valid move: check if
        // this is a castling move and update the appropriate variables.
        if(file == targetFile){
            if (rank == targetRank - 2) {
                _isCastlingMove = true;
                _isCastlingKingside = false;
                movementIsValid = true;
            }
            else if (rank == targetRank + 2) {
                _isCastlingMove = true;
                _isCastlingKingside = true;
                movementIsValid = true;
            }
        }
        // ------------------------------------------------------------------ //

        // ---------------- Check Capture ----------------------------------- //
        if (movementIsValid) {
            if (!destinationCheck(_piece, _board, _target)) {
                return false;
            }
        }
        // ------------------------------------------------------------------ //

        // ----------------- Check Castling --------------------------------- //
        // Here, multiple checks should be made. Firstly, it should check
        // whether the king and rook involved has previously moved. To do this,
        // it should know which side it is attempting to castle towards.
        // Secondly, it should check if the king is in check or if any of the
        // squares involved are under attack by enemy pieces. Thirdly, it
        // should check whether there are any pieces in between the two pieces.
        // If any of these criteria return true, it's an invalid move. Else
        // it's valid.



        // If it is a castling move, do the necessary checks.
        if (_isCastlingMove) {
            // Firstly, check if the king has moved previously and is in check.
            // If either of these are true, it's an invalid move and no further
            // checking needs to be done.

            // If the king has moved:
            if (_piece.getMovesCount() > 0) {
                return false;
            }
            // If the king is in check:
            if (_board.kingUnderAttack(_piece.getColor(), _currentLocation)) {
                return false;
            }
            // -------------- Queenside -------------------------------------- //
            if (!_isCastlingKingside) {
                // If it's going queenside, check the rook on the queen's side
                // and all the squares between for the necessary criteria.

                // First check if there is a piece, and assign it to a variable.
                if (_board.getPiece(new Square(rank + 4, file)) != null){
                    _rookPieceInvolved = _board.getPiece(new Square(rank + 4, file));
                    _doesRookExist = true;
                }
                else { _rookPieceInvolved = null; _doesRookExist = false;}

                // If the piece exists:
                if (_doesRookExist) {
                    // But it has moved:
                    _rookHasMoved = (_board.getPiece(new Square(rank + 4, file)).getMovesCount() > 0);
                    if (_rookHasMoved) {
                        // Then it's invalid.
                        return false;
                    }
                }
                // else if it doesn't exist, it's also invalid.
                else return false;

                // Now check the squares in between for other pieces and if
                // they're under attack.

                for (int loopRank = rank + 1; loopRank < rank + 2; loopRank++) {
                    // if the square is under attack, it's false.
                    if (_board.squareUnderAttack(_piece.getColor(), new Square(loopRank, file))) {
                        return false;
                    }
                }
                for (int loopRank = rank + 1; loopRank < rank + 3; loopRank++) {
                    // if there is a piece blocking either, it's also false.
                    if (_board.getPiece(new Square(loopRank, file)) != null) {
                        return false;
                    }
                }
            }

             // ---------------------- Kingside ---------------------------- //
            else {
                // If it's going kingside, check the rook on the king's side
                // and all the squares between for the necessary criteria.


                // First check if there is a piece, and assign it to a variable.
//                if (_board.getPiece(new Square(rank - 4, file)) != null){
//                    _rookPieceInvolved = new Piece(_board.getPiece(new Square(rank - 4, file)));
//                }
//                else _rookPieceInvolved = null;

                // If the piece exists:
                _doesRookExist = (_board.getPiece(new Square(rank - 3, file)) != null);
                if (_doesRookExist) {
                    // But it has moved:
                    _rookHasMoved = (_board.getPiece(new Square(rank - 3, file)).getMovesCount() > 0);
                    if (_rookHasMoved) {
                        // Then it's invalid.
                        return false;
                    }
                }
                // else if it doesn't exist, it's also invalid.
                else return false;

                // Now check the squares in between for other pieces and if
                // they're under attack.

                for (int loopRank = rank - 1; loopRank > rank - 2; loopRank--) {
                    Square _currentLoopLocation = new Square(loopRank, file);
                    // if the square is under attack, it's false.
                    if (_board.squareUnderAttack(_piece.getColor(), new Square(loopRank, file))) {
                        return false;
                    }
                }
                for (int loopRank = rank - 1; loopRank > rank - 2; loopRank--) {
                    // if there is a piece blocking it, it's also false.
                    if (_board.getPiece(new Square(loopRank, file)) != null) {
                        return false;
                    }
                }
            }
            // if it reaches this point, the castling move was a success - move
            // the rook as well, though! Else it's not a castle, just a double
            // move.
            if (_isCastlingKingside) {
                _board.killPosition(new Square(rank - 3, file));
                Piece _rookToSpawn;
                _rookToSpawn = new Piece(_piece.getColor(), Type.ROOK, new Square(rank - 1, file));
                _board.spawnPiece(_rookToSpawn);
            }
            else {
                _board.killPosition(new Square(rank + 4, file));
                Piece _rookToSpawn;
                _rookToSpawn = new Piece(_piece.getColor(), Type.ROOK, new Square(rank + 1, file));
                _board.spawnPiece(_rookToSpawn);
            }
        }

        if (!movementIsValid) return false;

        // ------------------------------------------------------------------ //
        return true;
    }
}