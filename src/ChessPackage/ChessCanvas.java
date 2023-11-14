package ChessPackage;



/**
 * @filename ChessCanvas.java
 * @author Shane Kelly
 * @date 26 Nov 2011
 */

// -----------------------------------------------------------------------------
// Imports & Definition
// -----------------------------------------------------------------------------

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ChessCanvas extends Canvas implements MouseListener, MouseMotionListener {
// -----------------------------------------------------------------------------
// Fields
// -----------------------------------------------------------------------------

    /**
     * The hex codes for the unicode symbols used to paint the pieces.
     */
    private static final int KING_OUTLINE = 0x2654,
                             QUEEN_OUTLINE = 0x2655,
                             ROOK_OUTLINE = 0x2656,
                             BISHOP_OUTLINE = 0x2657,
                             KNIGHT_OUTLINE = 0x2658,
                             PAWN_OUTLINE = 0x2659,
                             KING_FILLING = 0x265A,
                             QUEEN_FILLING = 0x265B,
                             ROOK_FILLING = 0x265C,
                             BISHOP_FILLING = 0x265D,
                             KNIGHT_FILLING = 0x265E,
                             PAWN_FILLING = 0x265F;

    /**
     * The specific color used for the white squares on the board.
     */
    private static final Color WHITE_SQUARE = new Color(226, 196, 167);

    /**
     * The specific color used for the black squares on the board.
     */
    private static final Color BLACK_SQUARE = new Color(188, 121, 61);
    
    /**
     * The font used for outlines.
     */
    private static Font outline = new Font("Arial Unicode MS", Font.BOLD, 48);

    /**
     * The font used for filling.
     */
    private static Font filling = new Font("Arial Unicode MS", Font.PLAIN, 48);

    /**
     * The Board object used to interact with,
     * and get the data to paint from.
     */
    private Board chessBoard = new Board();
    


    /**
     * Instance variable for DnD operations.
     */
    private Square grabbedPiecePosition;

    private boolean isPieceGrabbed;
    private Point grabPosition;
    private Piece whichPieceIsGrabbed;

// -----------------------------------------------------------------------------
// Getters $ Setters
// -----------------------------------------------------------------------------
    
    /**
     * 
     * @return the chessboard this object is using.
     */
    public Board getBoard() {
        return this.chessBoard;
    }


// -----------------------------------------------------------------------------
// Methods
// -----------------------------------------------------------------------------

    /**
     * The method to draw the state of the Board on screen.
     *
     * @param g the graphics object used for drawing.
     */
    @Override
    public void paint(Graphics g) {
        // If the board has started, draw its state:
        if (chessBoard.isStartedUp()) {
            drawSquares(g);
            drawPieces(g, chessBoard);
            drawDnDPiece(g);

        }
        // else prompt the board to start up with a new
        // Piece[][] array.
        else {
            Piece[][] chessPiecesArray = new Piece[8][8];
            chessBoard.setStartingPosition(chessPiecesArray);
        }
    }

    /**
     *
     * @param g the graphics object to use.
     */
    @Override
    public void update(Graphics g) {
	Graphics offgc;
	Image offscreen = null;
        Dimension d = getSize();
	// create the offscreen buffer and associated Graphics
	offscreen = createImage(d.width, d.height);
	offgc = offscreen.getGraphics();
	// clear the exposed area
	offgc.setColor(getBackground());
	offgc.fillRect(0, 0, d.width, d.height);
	offgc.setColor(getForeground());
	// do normal redraw
	paint(offgc);
	// transfer offscreen to window
	g.drawImage(offscreen, 0, 0, this);
        offscreen.flush();
    }

    /**
     * The procedure to draw the squares of the board itself. It also draws
     * a blue box around the selected square.     *
     *
     * @param g the graphics object used
     */
    private void drawSquares(Graphics g) {
        // -------------- Local variables declertaion: ---------------------- //
        int drawWidth = getSize().width / 8;
        int drawHeight = getSize().height / 8;
        Color squareColor;
        // ------------------------------------------------------------------ //

        for (int rank = 0; rank < 8; rank++) {  // go through the ranks
            for (int file = 0; file < 8; file++) { // go through the files
                // if both rank and file are either even or uneven, it's a black
                // square
                if ((rank % 2 == 0 && file % 2 == 0) || (rank % 2 != 0 && file % 2 != 0))
                    squareColor = WHITE_SQUARE;
                // else it's a black square.
                else squareColor = BLACK_SQUARE;
                // Set the color appropriately and draw the square.
                g.setColor(squareColor);
                g.fillRect(rank * drawWidth, file * drawHeight, drawWidth, drawHeight);
            }
        }
    }

    /**
     *
     * @param g the graphics object used
     * @param chessBoard the chessBoard object used
     */
    private void drawPieces(Graphics g, Board chessBoard) {
        Square tempCoordinates;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                tempCoordinates = new Square(rank, file);
                if (grabbedPiecePosition != null) {
                    if (rank == grabbedPiecePosition.getRank() && file == grabbedPiecePosition.getFile()) {
                    }else {
                    if (chessBoard.getPiece(tempCoordinates) != null) {
                        drawPiece(g, tempCoordinates);
                    }
                }
                } else {
                    if (chessBoard.getPiece(tempCoordinates) != null) {
                        drawPiece(g, tempCoordinates);
                    }
                }
            }
        }
    }

    /**
     * @param g the graphics object used for drawing
     * @param _tempCoordinates the coords where there is a piece on the board.
     */
    private void drawPiece(Graphics g, Square _tempCoordinates) {
        // ---------- Local variables decleration: -------------------------- //
        
        int drawWidth = getSize().width / 8;    // The dimensions of one square.
        int drawHeight = getSize().height / 8;  //
        int widthInset = (int) (drawWidth * 0.1875); // The x,y values to serve
        int heightInset = (int) (drawHeight * 0.625);// as insets for the pieces
        // The positions on each square to start drawing the piece (if there
        // is one).
        int drawX = (_tempCoordinates.getRank() * drawWidth) + widthInset;
        int drawY = (_tempCoordinates.getFile() * drawHeight) + heightInset;

        // The piece to draw.
        Piece pieceToDraw = chessBoard.getPiece(_tempCoordinates);

        Color fillColor;  // important color variables...
        Color outlineColor;

        if (pieceToDraw.getColor() == Board.Color.WHITE) {
            fillColor = Color.WHITE;
            outlineColor = Color.BLACK;
        }
        else {
            fillColor = Color.BLACK;
            outlineColor = Color.WHITE;
        }
        // ------------------------------------------------------------------ //

        // For each type of piece, draw the filling of the type of piece first,
        // and then superimpose the outline on the fill.
        drawPiece(pieceToDraw, g, fillColor, outlineColor, new Point(drawX, drawY));
    }

    private void drawPiece(Piece pieceToDraw, Graphics g, Color fillColor, Color outlineColor, Point drawPosition) {
        switch (pieceToDraw.getType()) {
            case KING:
                g.setColor(fillColor);
                g.setFont(filling);
                g.drawString(Character.toString((char)KING_FILLING), drawPosition.x, drawPosition.y);
                g.setColor(outlineColor);
                g.setFont(outline);
                g.drawString(Character.toString((char)KING_OUTLINE), drawPosition.x - 1, drawPosition.y);
                break;
            case QUEEN:
                g.setColor(fillColor);
                g.setFont(filling);
                g.drawString(Character.toString((char)QUEEN_FILLING), drawPosition.x, drawPosition.y);
                g.setColor(outlineColor);
                g.setFont(outline);
                g.drawString(Character.toString((char)QUEEN_OUTLINE), drawPosition.x - 1, drawPosition.y);
                break;
            case ROOK:
                g.setColor(fillColor);
                g.setFont(filling);
                g.drawString(Character.toString((char)ROOK_FILLING), drawPosition.x, drawPosition.y);
                g.setColor(outlineColor);
                g.setFont(outline);
                g.drawString(Character.toString((char)ROOK_OUTLINE), drawPosition.x - 1, drawPosition.y);
                break;
            case BISHOP:
                g.setColor(fillColor);
                g.setFont(filling);
                g.drawString(Character.toString((char)BISHOP_FILLING), drawPosition.x, drawPosition.y);
                g.setColor(outlineColor);
                g.setFont(outline);
                g.drawString(Character.toString((char)BISHOP_OUTLINE), drawPosition.x - 1, drawPosition.y);
                break;
            case KNIGHT:
                g.setColor(fillColor);
                g.setFont(filling);
                g.drawString(Character.toString((char)KNIGHT_FILLING), drawPosition.x, drawPosition.y);
                g.setColor(outlineColor);
                g.setFont(outline);
                g.drawString(Character.toString((char)KNIGHT_OUTLINE), drawPosition.x - 1, drawPosition.y);
                break;
            case PAWN:
                g.setColor(fillColor);
                g.setFont(filling);
                g.drawString(Character.toString((char)PAWN_FILLING), drawPosition.x, drawPosition.y);
                g.setColor(outlineColor);
                g.setFont(outline);
                g.drawString(Character.toString((char)PAWN_OUTLINE), drawPosition.x - 1, drawPosition.y);
                break;
        }
    }

    /**
     * Input function.
     *
     * @param _e The mouse event used. Contains x;y coordinates.
     */
    public void mousePressed(MouseEvent _e) {
        grabbedPiecePosition = new Square(_e.getX() / (getSize().width / 8), _e.getY() / (getSize().height / 8));
        if (getBoard().getPiece(grabbedPiecePosition) != null) {
            grabPosition = new Point(_e.getX(), _e.getY());
            isPieceGrabbed = true;
            whichPieceIsGrabbed = getBoard().getPiece(grabbedPiecePosition);
        }
        else grabbedPiecePosition = null;
        repaint();
        //doInput(_e);
    }

    public void mouseDragged(MouseEvent _e) {
        if (isPieceGrabbed){
            grabPosition = new Point(_e.getX(), _e.getY());
        }
        repaint();
    }


    public void mouseReleased(MouseEvent _e) {
        if (isPieceGrabbed) {
            Square dropLocation = new Square(_e.getX() / (getSize().width / 8), _e.getY()  / (getSize().height / 8));
            chessBoard.movePiece(whichPieceIsGrabbed, dropLocation);
        }
        isPieceGrabbed = false;
        whichPieceIsGrabbed = null;
        grabPosition = null;
        grabbedPiecePosition = null;
        repaint();
    }
    
    private void drawDnDPiece(Graphics g) {
        if (whichPieceIsGrabbed != null) {
            int drawWidth = getSize().width / 8;    // The dimensions of one square.
            int drawHeight = getSize().height / 8;  //
            int widthInset = (int) (drawWidth * 0.25); // The x,y values to serve
            int heightInset = (int) (drawHeight * 0.25);// as insets for the pieces
            grabPosition.x -=  widthInset;
            grabPosition.y +=  heightInset;
            drawPiece(whichPieceIsGrabbed, grabPosition, g);
        }
    }

    private void drawPiece(Piece whichPieceIsGrabbed, Point grabPosition, Graphics g) {
        Color fillColor;  // important color variables...
        Color outlineColor;

        if (whichPieceIsGrabbed.getColor() == Board.Color.WHITE) {
            fillColor = Color.WHITE;
            outlineColor = Color.BLACK;
        }
        else {
            fillColor = Color.BLACK;
            outlineColor = Color.WHITE;
        }
        drawPiece(whichPieceIsGrabbed, g, fillColor, outlineColor, grabPosition);

    }



    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}  // These aren't used but are
    public void mouseExited(MouseEvent e) {}   // necessary to have.
    public void mouseMoved(MouseEvent e) {}




}