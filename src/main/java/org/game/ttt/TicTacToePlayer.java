package org.game.ttt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TicTacToePlayer implements ActionListener
{

    private static final int ROWS = 3;  // number of rows
    private static final int COLS = 3;  // number of columns
    private static final int OPPONENT = 0;
    private static final int ME = 1;

    public int[][] board = new int[][] {{ -1, -1, -1 },  { -1, -1, -1 }, { -1, -1, -1 }};


    /** Recursive minimax at level of depth for either maximizing or minimizing player.
     Return int[3] of {score, row, col}  */
    private int[] minimax(int depth, int player, int[][] board) {
        // Generate possible next moves in a List of int[2] of {row, col}.
        List<int[]> nextMoves = generateMoves(board);
        // mySeed is maximizing; while oppSeed is minimizing
        int bestScore = player == ME ? Integer.MIN_VALUE : Integer.MAX_VALUE;;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            // Gameover or depth reached, evaluate score
            bestScore = evaluate(board);
        } else {
            for (int[] move : nextMoves) {
                // Try this move for the current "player"
                board[move[0]][move[1]] = player;
                if (player == ME) {  // mySeed (computer) is maximizing player
                    currentScore = minimax(depth - 1, OPPONENT, board)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {  // oppSeed is minimizing player
                    currentScore = minimax(depth - 1, ME, board)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                board[move[0]][move[1]] = -1;
             }
        }
        return new int[] {bestScore, bestRow, bestCol};
    }

    /** Find all valid next moves.
     Return List of moves in int[2] of {row, col} or empty list if gameover */
    private List<int[]> generateMoves(int[][] board) {
        List<int[]> nextMoves = new ArrayList<>(); // allocate List

        // If gameover, i.e., no next move
        if (hasWon(ME, board) || hasWon(OPPONENT, board)) {
            return nextMoves;   // return empty list
        }

        // Search for empty cells and add to the List
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == -1) {
                    nextMoves.add(new int[] {row, col});
                }
            }
        }
        return nextMoves;
    }

    /** The heuristic evaluation function for the current board
     @Return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer.
     -100, -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent.
     0 otherwise   */
    private int evaluate(int[][] board) {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2, board);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2, board);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2, board);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0, board);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1, board);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2, board);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2, board);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0, board);  // alternate diagonal
        return score;
    }

    /** The heuristic evaluation function for the given line of 3 cells
     @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     0 otherwise */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3, int[][] board) {
        int score = 0;

        // First cell
        if (board[row1][col1] == ME) {
            score = 1;
        } else if (board[row1][col1] == OPPONENT) {
            score = -1;
        }

        // Second cell
        if (board[row2][col2] == ME) {
            if (score == 1) {   // cell1 is mySeed
                score = 10;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (board[row2][col2] == OPPONENT) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (board[row3][col3] == ME) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (board[row3][col3] == OPPONENT) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    /** Returns true if thePlayer wins */
    private boolean hasWon(int player, int[][] board) {
        return     (board[0][0] == player && board[0][1] == player &&board[0][2] == player)
                || (board[1][0] == player && board[1][1] == player &&board[1][2] == player)
                || (board[2][0] == player && board[2][1] == player &&board[2][2] == player)
                || (board[0][0] == player && board[1][0] == player &&board[2][0] == player)
                || (board[0][1] == player && board[1][1] == player &&board[2][1] == player)
                || (board[0][2] == player && board[1][2] == player &&board[2][2] == player)
                || (board[0][0] == player && board[1][1] == player &&board[2][2] == player)
                || (board[0][2] == player && board[1][1] == player &&board[2][0] == player);
    }

    private void endGame(JPanel panel, int[][] board) {
        String s = "Draw!";
        if (hasWon(ME, board)) {
            s = "I won";
        }
        if (hasWon(OPPONENT, board)) {
            s = "You won";
        }
        JLabel jlabel = new JLabel(s);
        panel.setLayout(new GridLayout(4, 3));
        panel.setSize(500, 600);
        panel.add(jlabel);
        panel.updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //   interpret user move:
       char x = ((Button) e.getSource()).getName().charAt(0);
       int y = ((Button) e.getSource()).getName().charAt(1)-'0';
       JPanel panel = ((JPanel) ((Button) e.getSource()).getParent());
       board[x=='A'?0:x=='B'?1:2][y-1] = OPPONENT;
       if (generateMoves(board).isEmpty()) {
           endGame(panel, board);
       }

        //   calculate my move:
       int[] move = minimax(2, ME, board);
       board[move[1]][move[2]] = ME;
       Button button = ((Button) panel.getComponent(move[1]*3+move[2])) ;
       button.setLabel("O");

       if (generateMoves(board).isEmpty()) {
           endGame(panel, board);
       }

    }
}
