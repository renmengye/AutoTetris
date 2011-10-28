/*
 * Author: Mengye Ren
 * The Piece Class provide an instance of a single piece.
 * Stores information of type, orientation and the single matrix.
 */
package autotetris;

public class Piece implements ATCommon {

    private final PieceType type;
    private Orientation orient;
    //protected byte[] range; //the maximum of left, right, top and bottom
    protected byte[][] contour; //the coordinates of each grid
    private int x, y; //stores x and y coordinates
    //private Board board; //stores the grid matrix of the single piece //obsolete

    public Piece(PieceType type, Orientation orient) {
        this.type = type;
        this.orient = orient;
        //this.range = RANGE[type.value()][orient.value()];
        this.contour = CONTOUR[type.value()][orient.value()];
        this.x = XNUM / 2;
        this.y = START_Y[type.value()][orient.value()];
        //System.out.printf("x:%d y:%d t:%s o:%s",x,y,type,orient);
        //this.y = START_Y[type.value()][orient.value()];
        //this.board = new Board();
        //genBoard();
    }

    public Piece(PieceType type, Orientation orient, int x, int y) {
        this.type = type;
        this.orient = orient;
        //this.range = RANGE[type.value()][orient.value()];
        this.contour = CONTOUR[type.value()][orient.value()];
        this.x = x;
        this.y = y;
        //this.board = new Board();
        //genBoard();
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

    public int getContour(int i, int xy) {
        return contour[i][xy];
    }

    //return the piece's single grid matrix //obsolete
    /**public Board getBoard() {
    return board;
    }
    
    //generate a new single grid matrix according to current x,y
    //obsolete, in minimal use
    
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
    }*/
    //manually set piece's x,y
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //move the piece by input a GameMove object
    public boolean move(GameMove move, Board board) {
        switch (move) {
            case LEFT:
                if (!check_range(x - 1, y)) {
                    return false;
                } else {
                    x--;
                    //genBoard();
                }
                break;
            case RIGHT:
                if (!check_range(x + 1, y)) {
                    return false;
                } else {
                    x++;
                }
                break;
            case DOWN:
                if (!check_range(x, y + 1)) {
                    return false;
                } else {
                    y++;
                }
                break;
            case UP:
                if (!check_range(x, y - 1)) {
                    return false;
                } else {
                    y--;
                }
                break;
            case CW:
                Orientation new_orient = Orientation.next(type, orient.value());
                Piece test_piece = new Piece(type, new_orient, x, y);
                if (!test_piece.check_range(x, y)) {
                    return false;
                } else {
                    orient = new_orient;
                    this.contour = CONTOUR[type.value()][orient.value()];
                }
                break;
            case CCW:
                Orientation new_orient2 = Orientation.prev(type, orient.value());
                Piece test_piece2 = new Piece(type, new_orient2, x, y);
                if (!test_piece2.check_range(x, y)) {
                    return false;
                } else {
                    orient = new_orient2;
                    this.contour = CONTOUR[type.value()][orient.value()];
                }
                break;
            case DROP:
                if (board.check_done(this, GameMove.DOWN)) {
                    return false;
                } else {
                    do { //if drop, moving down until done
                        this.move(GameMove.DOWN, board);
                    } while (!board.check_done(this, GameMove.DOWN));
                }
                break;
            case NULL:
                break;
        }
        return true;
    }

    public boolean check_range(int x, int y) { //check a certain piece is in the display area
        for (int i = 0; i <= 3; i++) {
            int dx = contour[i][CONTOUR_DX];
            int dy = contour[i][CONTOUR_DY];
            if ((!check_point_range(x + dx, y + dy))) {
                return false;
            }
        }
        return true;
    }

    public boolean check_point_range(int x, int y) { //check a certain point is the display area
        if ((y >= 0 && x >= 0) && (y < YNUM && x < XNUM)) {
            return true;
        } else {
            return false;
        }
    }

    //move the pirce by a series of GameMove
    public boolean moves(GameMove[] moves, Board board) {
        for (GameMove move : moves) {
            if (!move(move, board)) {
                return false;
            }
        }
        return true;
    }

    //counter-move action, used in recursive routing
    public boolean revmove(GameMove move, Board board) {
        switch (move) {
            case LEFT:
                return move(GameMove.RIGHT, board);
            case RIGHT:
                return move(GameMove.LEFT, board);
            case DOWN:
                return move(GameMove.UP, board);
            case CW:
                return move(GameMove.CCW, board);
            case CCW:
                return move(GameMove.CW, board);
            case NULL:
                break;
        }
        return true;
    }

    @Override
    public Piece clone() {
        return new Piece(type, orient, x, y);
    }
}
