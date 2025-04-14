package a.lianne.quarto;

public class QuartoBoard {
    public static final int BOARD_SIZE = 4;
    private Piece[][] cells = new Piece[BOARD_SIZE][BOARD_SIZE];

    public QuartoBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j] = null;
            }
        }
    }

    public boolean isOccupied(int row, int col) {
        if (!(row >=0 && col >= 0 && row < BOARD_SIZE && col < BOARD_SIZE)) return true;
        if (cells[row][col] == null) return false;
        return true;
    }

    public boolean placePiece(int row, int col, Piece p) {
        if(!(isOccupied(row, col))) {
            cells[row][col] = p;
            return true;
        }
        return false;
    }

    public Piece getPiece(int row, int col) {
        return cells[row][col];
    }

    public boolean didGameEnd() {
        if (checkWin()) return true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cells[i][j] == null) return false;
            }
        }
        return true; // TIE
    }

    public boolean checkWin() {
       for(int i = 0; i < BOARD_SIZE; i++) {
           if(checkLine(cells[i][0], cells[i][1], cells[i][2], cells[i][3])) return true;
       }
       for(int i = 0; i < BOARD_SIZE; i++) {
           if(checkLine(cells[0][i], cells[1][i], cells[2][i], cells[3][i])) return true;
       }
       // Check diagonals
        if(checkLine(cells[0][0], cells[1][1], cells[2][2], cells[3][3]))
            return true;
        if(checkLine(cells[0][3], cells[1][2], cells[2][1], cells[3][0]))
            return true;
        return false;
    }

    public boolean checkLine(Piece p1, Piece p2, Piece p3, Piece p4) {
        if (p1 == null || p2 == null || p3 == null || p4 == null) return false;
        return ((p1.isTall == p2.isTall && p1.isTall == p3.isTall && p1.isTall == p4.isTall) ||
            (p1.isDark == p2.isDark && p1.isDark == p3.isDark && p1.isDark == p4.isDark) ||
            (p1.isHollow == p2.isHollow && p1.isHollow == p3.isHollow && p1.isHollow == p4.isHollow) ||
            (p1.isSquare == p2.isSquare && p1.isSquare == p3.isSquare && p1.isSquare == p4.isSquare));
    }

    public String toString() {
        String s = "";

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cells[i][j] == null) {
                    s += "null ";
                } else {
                    s += cells[i][j].toString() + " ";
                }
            }
            s += "\n";
        }
        return s;
    }
}
