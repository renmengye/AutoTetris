package autotetris.ai;

import autotetris.ATCommon;
import autotetris.elements.Board;
import autotetris.elements.GameMove;
import autotetris.elements.Piece;
import java.util.List;

/**
 *
 * @author rmy
 */
public class Router implements ATCommon {

    private Board board;
    private Piece end_point;
    private Piece start_point;
    //private float[][][] trend;

    //private static final float DECAY=0.8f;
    public Router(Board board, Piece start_point, Piece end_point) {
        this.board = board;
        this.start_point = start_point;
        this.end_point = end_point;
    }

    //a recursive method to route the piece to the desired position
    /*public List<GameMove> possible_route2(Piece piece, List<GameMove> moves, boolean dropped) {


    //if the piece is already at desired position then return what we've got
    if (piece.getX() == target.getX() && piece.getY() == target.getY() && piece.getOrient() == target.getOrient()) { //reaches target, recursive ends
    return moves;
    }

    //if there is still vertical distance till the target
    if (target.getY() < piece.getY()) {

    //we need to first calculate the horizontal distance and the rotational distance
    int dx = target.getX() - piece.getX();
    int dr = target.getOrient().value() - piece.getOrient().value();

    //if the piece have not yet go through the reverse dropping test, then do it now
    if (!dropped) {
    for (int j = piece.getY() - target.getY() - Math.abs(dx) - Math.abs(dr); j > 0; j--) { //repeat from the max drop to the min drop
    Piece test = piece.clone();
    boolean possible_drop = true;
    for (int i = 0; i <= j; i++) {

    //regard dropping as moving piece up
    //if the piece can be move up once more
    if (!board.check_done(test, GameMove.UP)) {

    test.move(GameMove.reverse(GameMove.DOWN), board);

    } //otherwise break the loop and indicate that the drop is impossible.
    else {
    possible_drop = false;
    //return null;
    break;
    }
    }

    //have a new list to store the test moves
    List<GameMove> tmoves;

    //if the drop is possible, then add drop action
    if (possible_drop) {
    moves.add(0, GameMove.DROP);
    } else {

    test = piece;
    //TODO preliminary try of making things nicer
    //return null;
    }

    //recursion occurs here
    tmoves = possible_route2(test, moves, true);


    //if got a non empty set of moves then return
    if (tmoves != null) {
    return tmoves;
    }
    }

    } //if the object is already dropped, then search horizontally and rotationally (only one)
    else {
    //search all possible moves in GameMove
    for (int i = 2; i <= 6; i++) {
    GameMove move = GameMove.get(i);
    Piece test = piece.clone();

    //if the piece can move down as game host would always do, and also can do the move
    //or if the piece is in the initial move then only check the reverse move
    if (((!moves.isEmpty() && !board.check_done(test, GameMove.UP))
    || (moves.isEmpty()))
    && !board.check_done(test, GameMove.reverse(move))) {

    //if not initial move then move reverse of down by default
    if (!moves.isEmpty()) {
    test.move(GameMove.UP, board);
    }

    //move the designated action
    test.move(GameMove.reverse(move), board);

    //add the action to the list
    moves.add(0, move);


    //recursion occurs here
    List<GameMove> tmoves = possible_route2(test, moves, true);
    if (tmoves != null) {
    return tmoves;
    } else {
    moves.remove(0);
    }

    }
    }
    }

    }

    //if already at same vertical level but still not placed rightly, then give up
    return null;
    }*/
    //a recursive method to route the piece to the desired position
    public boolean possible_route() {
        Piece test = end_point.clone();

        while (start_point.getY() > test.getY()) {

            if (!board.check_done(test, GameMove.UP)) {
                test.move(GameMove.UP, board);
            } else {
                return false;
            }
        }
        return true;
    }

    //a preliminary model would simply calculate neighbouring shortest "distance"
    public GameMove next() {

        GameMove max_choice = GameMove.DROP;
        float max_score = -1 / 0f;
        float score;

        for (int i = 0; i <= 6; i++) {

            //first consider drop
            if (i == 0) {
                Piece test = start_point.clone();
                test.move(GameMove.get(i), board);
                if (test.equals(end_point)) {
                    max_score = 100;
                    max_choice = GameMove.DROP;
                }
            } //then consider other neighbouring moves
            else if (!board.check_done(start_point, GameMove.get(i))) {
                Piece test = start_point.clone();
                test.move(GameMove.get(i), board);
                score = 100 - norm(test, end_point);
                if (score > max_score) {
                    max_score = score;
                    max_choice = GameMove.get(i);
                }

            }
        }
        //System.out.println(max_choice+": "+max_score);
        return max_choice;
    }

    private float norm(final Piece a, final Piece b) {

        int x = a.getX() - b.getX();
        int y = a.getY() - b.getY();
        int o = a.getOrient().value() - b.getOrient().value();
        return (float) Math.sqrt(100 * x * x + y * y + 100 * o * o);
    }
    /*private void update_trend(Piece piece){

    int x=piece.getX();
    int y=piece.getY();
    int z=piece.getOrient().value();

    for(int i=3;i<=7;i++){
    GameMove move=GameMove.get(i);
    Piece test = piece.clone();
    if(test.move(move, board)){

    int x1=test.getX();
    int y1=test.getY();
    int z1=test.getOrient().value();

    if(trend[y1][x1][z1]<trend[y][x][z]*DECAY){
    //trend[y1][x1][z1];
    }
    }
    }

    }

    private void clean_trend(){
    for(int j=0;j<YNUM;j++){
    for(int i=0;i<XNUM;i++){
    for(int k=0;k<O_NUM[target.getType().value()];k++){
    trend[j][i][k]=0f;
    }
    }
    }
    }*/
}
