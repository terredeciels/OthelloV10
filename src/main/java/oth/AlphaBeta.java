package oth;


import java.util.List;

//  players.put(Side.BLACK, new AlphaBetaPlayer(board, Side.BLACK));

public class AlphaBeta {
    static final int INITIAL_DEPTH = 5;
    static final int TIMEOUT_MILISECONDS = 6000;

    int currentDepth;
    Othello.Coups bestMove;
    Othello.Coups globalBestMove;
    long start;
    boolean timeout;
    ///
    Othello o;
    Side side;

    public AlphaBeta(Othello o, Side side) {
        this.o = o;
        this.side = side;
    }

    public Othello.Coups decideMove() {
        timeout = false;
        start = System.currentTimeMillis();

        for (int d = 0; ; d++) {
            if (d > 0) {
                globalBestMove = bestMove;
                System.out.println("Recherche terminée avec profondeur " + currentDepth + ". meilleur mouvement jusqu'à présent: " + globalBestMove);
            }
            currentDepth = INITIAL_DEPTH + d;

            maximizer(currentDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (timeout) {
                System.out.println();
                return globalBestMove;
            }
        }
    }

    ///
    private int maximizer(int depth, int alpha, int beta) {
        if (System.currentTimeMillis() - start > TIMEOUT_MILISECONDS) {
            timeout = true;
            return alpha;
        }

        if (depth == 0) {
            return o.computeRating(Side.BLACK);
        }

        List<Othello.Coups> legalMoves = o.legalmoves(o);

        assert legalMoves != null;
        for (Othello.Coups move : legalMoves) {

            // makeMove(move);
            o.fmove(o.undomove);

            //side = side.opposite();
            o.trait = -o.trait;

            int rating = minimizer(depth - 1, alpha, beta);

            //side = side.opposite();
            o.trait = -o.trait;
            //undoMove(move);
            o.fmove(!o.undomove);

            if (rating > alpha) {
                alpha = rating;

                if (depth == currentDepth) {
                    bestMove = move;
                }
            }

            if (alpha >= beta) {
                return alpha;
            }
        }
        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta) {
        if (depth == 0) {
            return o.computeRating(Side.BLACK);
        }

        List<Move> legalMoves = computeAllLegalMoves();

        for (Move move : legalMoves) {

            makeMove(move);
            side = side.opposite();

            int rating = maximizer(depth - 1, alpha, beta);

            side = side.opposite();
            undoMove(move);

            if (rating <= beta) {
                beta = rating;
            }

            if (alpha >= beta) {
                return beta;
            }
        }
        return beta;
    }

    List<Move> computeAllLegalMoves() {
        return null;
    }

    void undoMove(Move move) {

    }
    ///

    void makeMove(Move move) {

    }

    ///
    public static class Side {
        public static Side BLACK;

        public Side opposite() {
            return null;
        }
    }

    static class Move {
    }


}
