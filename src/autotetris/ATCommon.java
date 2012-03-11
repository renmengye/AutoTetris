/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

/**
 *
 * @author MengYe
 */
public interface ATCommon {

    public static final int TWIDTH = 202, THEIGHT = 382,
            GAP = 2, XNUM = 10, YNUM = 19,
            XDIM = TWIDTH / XNUM - GAP, YDIM = THEIGHT / YNUM - 2;
    //public static final int SHAPE_O=0,SHAPE_I=1,SHAPE_S=2,SHAPE_Z=3,SHAPE_J=4,SHAPE_L=5,SHAPE_T=6,SHAPE_NUM=7;
    public static final int[] O_NUM = {1, 2, 2, 2, 4, 4, 4};
    //probablity: not used in this application anymore
    public static final byte[][] PROB = {{10, 15, 15, 15, 10, 10, 25},
                                        {20, 5, 20, 20, 10, 10, 15},
                                        {0, 0, 0, 0, 0, 1, 0},
                                        {0, 0, 0, 0, 1, 0, 0},
                                        {20, 15, 15, 15, 10, 10, 15},
                                        {20, 15, 15, 15, 10, 10, 15},
                                        {15, 30, 15, 15, 5, 5, 15}};
    //contour constants
    public static final int CONTOUR_DX = 0, CONTOUR_DY = 1;
    //SHAPE_MAP(SHAPE,ORIENTATION,GRID,D)
    public static final byte[][][][] CONTOUR = {{{{-1, 0}, {0, 0}, {-1, 1}, {0, 1}}},
                                                {{{-2, 0}, {-1, 0}, {0, 0}, {1, 0}}, {{0, -2}, {0, -1}, {0, 0}, {0, 1}}},
                                                {{{-1, 1}, {0, 1}, {0, 0}, {1, 0}}, {{-1, -1}, {-1, 0}, {0, 0}, {0, 1}}},
                                                {{{-1, 0}, {0, 0}, {0, 1}, {1, 1}}, {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}}},
                                                {{{-1, 0}, {0, 0}, {1, 0}, {1, 1}}, {{0, -1}, {0, 0}, {0, 1}, {-1, 1}}, {{-1, -1}, {-1, 0}, {0, 0}, {1, 0}}, {{1, -1}, {0, -1}, {0, 0}, {0, 1}}},
                                                {{{-1, 1}, {-1, 0}, {0, 0}, {1, 0}}, {{-1, -1}, {0, -1}, {0, 0}, {0, 1}}, {{-1, 0}, {0, 0}, {1, 0}, {1, -1}}, {{0, -1}, {0, 0}, {0, 1}, {1, 1}}},
                                                {{{-1, 0}, {0, 0}, {1, 0}, {0, 1}}, {{-1, 0}, {0, 0}, {0, -1}, {0, 1}}, {{-1, 0}, {0, 0}, {1, 0}, {0, -1}}, {{0, 0}, {1, 0}, {0, -1}, {0, 1}}}};
    //public static final int MOVE_LEFT=0,MOVE_RIGHT=1,MOVE_DOWN=2,MOVE_CW=3,MOVE_CCW=4,DROP=5;
    //public static final int STARTX=5;
    public static final int MAX_DEPTH = 0;
    //Start_pos(shape,orient
    public static final byte[][] START_Y={{0},
                                            {0,2},
                                            {0,1},
                                            {0,1},
                                            {0,1,1,1},
                                            {0,1,1,1},
                                            {0,1,1,1}};
    public static final int[] SCORE={0,1,3,6,10};
}
