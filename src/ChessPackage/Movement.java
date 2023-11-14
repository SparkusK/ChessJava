package ChessPackage;

/**
 * @filename Movement.java
 * @author Shane Kelly
 * @date 14 Nov 2012
 */

// -----------------------------------------------------------------------------
// Imports & Definition
// -----------------------------------------------------------------------------
import ChessPackage.Piece.Type;
import ChessPackage.Board.Color;

public class Movement {
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
        Piece pieceLeft = new Piece(_board.getPiece(new Square(rank - 1, file)));
        Piece pieceRight= new Piece(_board.getPiece(new Square(rank + 1, file)));
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
        Boolean isTargetEnemy = (targetPiece.getColor() != _piece.getColor());

        Boolean isPieceLeftPresent = (pieceLeft != null);
        Boolean isPieceLeftIsPawn = (pieceLeft.getType() == Type.PAWN);
        Boolean isPieceLeftIsEnemy = (pieceLeft.getColor() != color);

        Boolean isPieceRightPresent = (pieceRight != null);
        Boolean isPieceRightIsPawn = (pieceRight.getType() == Type.PAWN);
        Boolean isPieceRightIsEnemy = (pieceRight.getColor() != color);

        Boolean isOneLeft = (_target.getRank() == rank - 1);
        Boolean isOneRight = (_target.getRank() == rank + 1);

        Boolean isEnPassant = _piece.getEnPassant();

        Boolean isFirstMove = (movesMade < 1);
        Boolean isMovingTwoUpOrDown = (file == targetFile - 2 && color == Color.WHITE) || (file == targetFile + 2 && color == Color.BLACK);
        Boolean isNothingInPath = (color == Color.WHITE && pieceOneUp == null) || ( color == Color.BLACK && pieceOneDown == null);

        Boolean isEnPassantLeftPresent = (enPassantLeft != null);
        Boolean isEnPassantLeftPawn = (enPassantLeft.getType() == Type.PAWN);
        Boolean isEnPassantLeftEnemy = (enPassantLeft.getColor() != color);

        Boolean isEnPassantRightPresent = (enPassantRight != null);
        Boolean isEnPassantRightPawn = (enPassantRight.getType() == Type.PAWN);
        Boolean isEnPassantRightEnemy = (enPassantRight.getColor() != color);


        if (isTargetOccupied && isTargetEnemy) return true;
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
                            _board.killPosition(new Square(rank + 1, file));
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
}
