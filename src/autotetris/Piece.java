/*
 * Author: Mengye Ren
 * The Piece Class provide an instance of a single piece.
 * Stores information of type, orientation and the single matrix.
 */
package autotetris;

public class Piece implements ATCommon {

    private final PieceType type;
    private final Orientation orient;
    private final byte[] range; //the maximum of left, right, top and bottom
    private final byte[][] contour; //the coordinates of each grid
    private int x, y; //stores x and y coordinates
    private Board board; //stores the grid matrix of the single piece

    public Piece(PieceType type, Orientation orient) {
        this.type = type;
        this.orient = orient;
        this.range = RANGE[type.value()][orient.value()];
        this.contour = CONTOUR[type.value()][orient.value()];
        this.x = XNUM/2;
        this.y = START_Y[type.value()][orient.value()];
        this.board=new Board();
        genBoard();
    }

    //return current x
    protected int getX() {
        return x;
    }

    //return current y
    protected int getY() {
        return y;
    }

    //return the piece's type
    protected PieceType getType() {
        return type;
    }

    //return the piece's orientation
    protected Orientation getOrient() {
        return orient;
    }

    //return the piece's single grid matrix
    protected Board getBoard(){
        return board;
    }

    //generate a new single grid matrix according to current x,y
    private boolean genBoard() {
        int dx, dy;
        byte[][] map = new byte[YNUM][XNUM];
        try {
            for (int i = 0; i <= 3; i++) {
                dx = contour[i][CONTOUR_DX];
                dy = contour[i][CONTOUR_DY];
                //System.out.printf("x: %d, y: %d, dx: %d, dy: %d, shape: %d, ori: %d",x,y,dx,dy,shape,orient);
                map[y + dy][x + dx] = 1;
            }
        } catch (Exception e) {
            return false;
        }
        board.setBoard(map);
        return true;
    }

    //manually set piece's x,y
    protected void setXY(int x, int y) {
        this.x=x;
        this.y=y;
        genBoard();
    }

    //move the piece by input a GameMove object
    protected void move(GameMove move) {
        switch (move) {
            case LEFT:
                break;
            case RIGHT:
                break;
            case DOWN:
                break;
            case CW:
                break;
            case CCW:
                break;
            case DROP:
                break;
        }
    }

    //move the pirce by a series of GameMove
    protected void moves(GameMove[] moves) {
        for (GameMove move : moves) {
            move(move);
        }
    }
}
