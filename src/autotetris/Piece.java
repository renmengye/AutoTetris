/*
 * Author: Mengye Ren
 * The Piece Class provide an instance of a single piece.
 * Stores information of type, orientation and the single matrix.
 */
package autotetris;

public class Piece implements ATCommon {

    private final PieceType type;
    private Orientation orient;
    protected byte[] range; //the maximum of left, right, top and bottom
    protected byte[][] contour; //the coordinates of each grid
    private int x, y; //stores x and y coordinates
    private Board board; //stores the grid matrix of the single piece

    public Piece(PieceType type, Orientation orient) {
        this.type = type;
        this.orient = orient;
        this.range = RANGE[type.value()][orient.value()];
        this.contour = CONTOUR[type.value()][orient.value()];
        this.x = XNUM / 2;
        this.y = START_Y[type.value()][orient.value()];
        this.board = new Board();
        genBoard();
    }

    public Piece(PieceType type, Orientation orient, int x, int y) {
        this.type = type;
        this.orient = orient;
        this.range = RANGE[type.value()][orient.value()];
        this.contour = CONTOUR[type.value()][orient.value()];
        this.x = x;
        this.y = y;
        this.board = new Board();
        genBoard();
    }

    //return current x
    public int getX() {
        return x;
    }

    //return current y
    public int getY() {
        return y;
    }

    //return the piece's type
    public PieceType getType() {
        return type;
    }

    //return the piece's orientation
    public Orientation getOrient() {
        return orient;
    }

    //return the piece's single grid matrix
    public Board getBoard() {
        return board;
    }

    //generate a new single grid matrix according to current x,y
    private boolean genBoard() {
        int dx, dy;
        byte[][] map = new byte[YNUM][XNUM];
        //try {
        for (int i = 0; i <= 3; i++) {
            dx = contour[i][CONTOUR_DX];
            dy = contour[i][CONTOUR_DY];
            //System.out.printf("x: %d, y: %d, dx: %d, dy: %d, shape: %d, ori: %d",x,y,dx,dy,shape,orient);
            if ((y + dy >= 0 && x + dx >= 0) && (y + dy < YNUM && x + dx < XNUM)) {
                map[y + dy][x + dx] = 1;
            }
        }
        //} catch (Exception e) {
        //  return false;
        //}
        board.setBoard(map);
        return true;
    }

    //manually set piece's x,y
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        genBoard();
    }

    //move the piece by input a GameMove object
    public boolean move(GameMove move) {
        switch (move) {
            case LEFT:
                if (x - 1 < range[0]) {
                    return false;
                } else {
                    x--;
                    genBoard();
                }
                break;
            case RIGHT:
                if (x + 1 > range[1]) {
                    return false;
                } else {
                    x++;
                    genBoard();
                }
                break;
            case DOWN:
                if (y + 1 > range[3]) {
                    return false;
                } else {
                    y++;
                    genBoard();
                }
                break;
            case CW:
                Orientation new_orient = Orientation.next(type, orient.value());
                byte[] new_range = RANGE[type.value()][new_orient.value()];
                if (x < new_range[0] || x > new_range[1] || x < new_range[2] || x > new_range[3]) {
                    return false;
                } else {
                    orient = new_orient;
                    this.range = RANGE[type.value()][orient.value()];
                    this.contour = CONTOUR[type.value()][orient.value()];
                }
                genBoard();
                break;
            case CCW:
                Orientation new_orient2 = Orientation.prev(type, orient.value());
                byte[] new_range2 = RANGE[type.value()][new_orient2.value()];
                if (x < range[0] || x > range[1] || x < range[2] || x > range[3]) {
                    return false;
                } else {
                    orient = new_orient2;
                    this.range = RANGE[type.value()][orient.value()];
                    this.contour = CONTOUR[type.value()][orient.value()];
                }
                genBoard();
                break;
            case DROP:
                break;
            case NULL:
                break;
        }
        return true;
    }

    //move the pirce by a series of GameMove
    public boolean moves(GameMove[] moves) {
        for (GameMove move : moves) {
            if (!move(move)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Piece clone() {
        return new Piece(type, orient, x, y);
    }
}
