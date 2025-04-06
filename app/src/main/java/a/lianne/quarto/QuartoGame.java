package a.lianne.quarto;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class QuartoGame {


    public enum GameState { SELECT_PIECE, PLACE_PIECE }

    private MutableLiveData<QuartoBoard> board;
    private MutableLiveData<Boolean> currentPlayer; // false = player 1, true = player 2
    private MutableLiveData<Piece> chosenPiece;
    private MutableLiveData<GameState> gameState;
    private MutableLiveData<Boolean> gameOver;

    private List<Piece> availablePieces;

    public QuartoGame() {
        board = new MutableLiveData<>(new QuartoBoard());
        currentPlayer = new MutableLiveData<>(false);
        chosenPiece = new MutableLiveData<>(null);
        gameState = new MutableLiveData<>(GameState.SELECT_PIECE);
        gameOver = new MutableLiveData<>(false);

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

    public void play(int row, int col) {

    }
}
