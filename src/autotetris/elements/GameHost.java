package autotetris.elements;

import autotetris.ATCommon;
import autotetris.ai.Player;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmy
 */
public class GameHost extends Thread implements ATCommon {

    private Board board;
    private Piece piece;
    private Random random;
    private int score;
    private Player player;
    private int reaction;

    public GameHost(int reaction) {
        board = new Board();
        random = new Random();
        piece = initPiece();
        score = 0;
        this.reaction=reaction;
        setPriority(MIN_PRIORITY);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void start() {
        super.start();
        if (player != null) {
            player.start();
        }
    }

    public Board getBoard() {
        return board;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getScore() {
        return score;
    }
    
    public void setReact(int reaction){
        this.reaction=reaction;
    }

    //initialize piece
    private Piece initPiece() {

        //generate random piece type
        int type = random.nextInt(7);

        //generate random orientation according to type
        int orient = random.nextInt(O_NUM[type]);

        //create a new instance of piece
        Piece new_piece = new Piece(PieceType.get(type), Orientation.get(orient));

        return new_piece;
    }

    //initiate a new game
    public void new_game() {
        random = new Random();
        score = 0;
        initPiece();
        board = new Board();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (board.check_done(piece, GameMove.DOWN)) {
                    //the piece become history
                    board.bindPiece(piece);
                    //check if there is any score gained
                    score += board.checkFull();
                    //initialize a new piece
                    piece = initPiece();
                    //tcanvas.setPiece(piece);
                    if (player != null) {
                        player.update();
                    }
                    if (board.check_done(piece, GameMove.DOWN)) {
                        //tcanvas.setStatus(GameStatus.DEAD);
                        //this.wait();
                        if (player != null) {
                            player.end_game();
                        }
                        break;
                        //System.out.println("piece dead");
                    }
                } else {
                    piece.move(GameMove.DOWN, board);
                }
                sleep(reaction);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameHost.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
