package a.lianne.quarto;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class QuartoGameViewModel extends ViewModel {


    public enum GameState {
        SELECT_PIECE, PLACE_PIECE
    }

    private MutableLiveData<QuartoBoard> board;
    private MutableLiveData<Boolean> currentPlayer; // false = player 1, true = player 2
    private MutableLiveData<Piece> chosenPiece;
    private MutableLiveData<GameState> gameState;
    private MutableLiveData<Boolean> gameOver;

    private MutableLiveData<String> winner;

    private List<Piece> availablePieces;

    public QuartoGameViewModel() {
        board = new MutableLiveData<>(new QuartoBoard());
        currentPlayer = new MutableLiveData<>(true);
        chosenPiece = new MutableLiveData<>(null);
        gameState = new MutableLiveData<>(GameState.SELECT_PIECE);
        gameOver = new MutableLiveData<>(false);
        winner = new MutableLiveData<>("");

        availablePieces = new ArrayList<>();
        initializePieces();
    }

    private void initializePieces() {
        availablePieces.add(new Piece(false, false, false, false));
        availablePieces.add(new Piece(false, false, false, true));
        availablePieces.add(new Piece(false, false, true, false));
        availablePieces.add(new Piece(false, false, true, true));
        availablePieces.add(new Piece(false, true, false, false));
        availablePieces.add(new Piece(false, true, false, true));
        availablePieces.add(new Piece(false, true, true, false));
        availablePieces.add(new Piece(false, true, true, true));
        availablePieces.add(new Piece(true, false, false, false));
        availablePieces.add(new Piece(true, false, false, true));
        availablePieces.add(new Piece(true, false, true, false));
        availablePieces.add(new Piece(true, false, true, true));
        availablePieces.add(new Piece(true, true, false, false));
        availablePieces.add(new Piece(true, true, false, true));
        availablePieces.add(new Piece(true, true, true, false));
        availablePieces.add(new Piece(true, true, true, true));
    }



    public void selectPiece(Piece p) {
        if (p == null || !availablePieces.contains(p) || gameState.getValue() != GameState.SELECT_PIECE) return;
        chosenPiece.setValue(p);
        currentPlayer.setValue(!currentPlayer.getValue());
        gameState.setValue(GameState.PLACE_PIECE);
        availablePieces.remove(p);
    }

    public boolean placePiece(int row, int col) {
        if (chosenPiece.getValue() == null || gameState.getValue() != GameState.PLACE_PIECE) return false;
        boolean result = board.getValue().placePiece(row, col, chosenPiece.getValue());
        if (!result) return false;
        board.setValue(board.getValue());
//        currentPlayer.setValue(!currentPlayer.getValue());
        chosenPiece.setValue(null);
        gameState.setValue(GameState.SELECT_PIECE);
        if (board.getValue().didGameEnd()) {
            gameOver.setValue(true);
            if (board.getValue().checkWin() == false) { // TIE
                winner.setValue("tie");
            } else { // player x won
                winner.setValue("player " + (currentPlayer.getValue() ? 2 : 1));
            }
        }
        return true;
    }

    public MutableLiveData<QuartoBoard> getBoard() {
        return board;
    }

    public MutableLiveData<Boolean> getCurrentPlayer() {
        return currentPlayer;
    }

    public MutableLiveData<Piece> getChosenPiece() {
        return chosenPiece;
    }

    public MutableLiveData<GameState> getGameState() {
        return gameState;
    }

    public MutableLiveData<Boolean> getGameOver() {
        return gameOver;
    }
    public MutableLiveData<String> getWinner() {
        return winner;
    }

    public List<Piece> getAvailablePieces() {
        return availablePieces;
    }
}
