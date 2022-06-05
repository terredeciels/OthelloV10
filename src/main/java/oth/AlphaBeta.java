package oth;


import java.util.List;

import static oth.Othello.noir;
import static oth.Othello.*;

public class AlphaBeta {
    static final int INITIAL_DEPTH = 5;
    static final int TIMEOUT_MILISECONDS = 6000;
    int trait;
    int currentDepth;
    Coups bestMove;
    Coups globalBestMove;
    long start;
    boolean timeout;
    Othello o;

    public AlphaBeta(Othello o, int trait) {
        this.o = o;
        this.trait = trait;
    }

    public Coups decideMove() {
        timeout = false;
        start = System.currentTimeMillis();

        for (int d = 0; ; d++) {
            if (d > 0) {
                globalBestMove = bestMove;
                System.out.println("Fin de recherche avec profondeur: " + currentDepth + " meilleur mouvement jusqu'a present: " + globalBestMove);
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
            o.trait=noir;
            return o.computeRating(noir);
        }

        List<Othello.Coups> legalMoves = o.legalmoves();

        assert legalMoves != null;
        for (Othello.Coups move : legalMoves) {
            o.move=move;

            o.fmove(o.undomove);
            o.trait = -o.trait;

            int rating = minimizer(depth - 1, alpha, beta);

            o.trait = -o.trait;
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
            o.trait=noir;
            return o.computeRating(noir);
        }

        List<Othello.Coups> legalMoves = o.legalmoves();

        for (Othello.Coups move : legalMoves) {
            o.move=move;

            o.fmove(!o.undomove);
            o.trait = -o.trait;

            int rating = maximizer(depth - 1, alpha, beta);

            o.trait = -o.trait;
            o.fmove(!o.undomove);

            if (rating <= beta) {
                beta = rating;
            }

            if (alpha >= beta) {
                return beta;
            }
        }
        return beta;
    }

}
