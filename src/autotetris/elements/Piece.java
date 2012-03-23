/*
 * Author: Mengye Ren
 * The Piece Class provide an instance of a single piece.
 * Stores information of type, orientation and the single matrix.
 */
package autotetris.elements;

import autotetris.ATCommon;

public class Piece implements ATCommon {

    private final PieceType type;
    private Orientation orient;
    public byte[][] contour;        //the coordinates of each grid
    private int x, y;               //stores x and y coordinates

    public Piece(PieceType type, Orientation orient) {
        this.type = type;
        this.orient = orient;
        this.contour = CONTOUR[type.value()][orient.value()];
        this.x = XNUM / 2;
        this.y = START_Y[type.value()][orient.value()];
    }

    public Piece(PieceType type, Orientation orient, int x, int y) {
        this.type = type;
        this.orient = orient;
        this.contour = CONTOUR[type.value()][orient.value()];
        this.x = x;
        this.y = y;
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

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //move the piece by input a GameMove object
    //returns false if the piece cannot move
    public synchronized boolean move(GameMove move, Board board) {
        switch (move) {
            case LEFT:
                if (!check_range(x - 1, y)) {
                    return false;
                } else {
                    x--;
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
                    
                    //if drop, moving down until done
                    do { 
                        this.move(GameMove.DOWN, board);
                    } while (!board.check_done(this, GameMove.DOWN));
                }
                break;
            case NULL:
                break;
        }
        return true;
    }

    //check a certain piece is in the display area
    public boolean check_range(int x, int y) {
        for (int i = 0; i <= 3; i++) {
            int dx = contour[i][CONTOUR_DX];
            int dy = contour[i][CONTOUR_DY];
            if ((!check_point_range(x + dx, y + dy))) {
                return false;
            }
        }
        return true;
    }

    //check a certain point is the display area
    public boolean check_point_range(int x, int y) {
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

    @Override
    public Piece clone() {
        return new Piece(type, orient, x, y);
    }

    public boolean equals(Piece x){
        return this.getX()==x.getX() && this.getY()==x.getY() && this.getOrient()==x.getOrient();
    }
}
