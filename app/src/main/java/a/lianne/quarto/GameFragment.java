package a.lianne.quarto;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class GameFragment extends Fragment {

    private QuartoGameViewModel game;
    private QuartoBoardView boardView;
    private TextView chosenPieceLabel;
    private PieceThumbView pieceView;
    private Button actionBtn;

    private AlertDialog picker;

    public GameFragment() {
        // Required empty public constructor
    }
public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        game = new ViewModelProvider(this).get(QuartoGameViewModel.class);
        boardView = view.findViewById(R.id.boardView);
        chosenPieceLabel = view.findViewById(R.id.chosenPieceLabel);
        pieceView = view.findViewById(R.id.pieceView);
        actionBtn = view.findViewById(R.id.actionBtn);

        boardView.setOnCellClickListener((row, col) -> {
            game.placePiece(row, col);
        });
        game.getBoard().observe(getViewLifecycleOwner(), board -> boardView.setBoard(board));

        game.getGameState().observe(getViewLifecycleOwner(), state -> {
            if (state == QuartoGameViewModel.GameState.SELECT_PIECE) {

            }
        });

        game.getWinner().observe(getViewLifecycleOwner(), winner -> {
            // Show dialog of game over
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Game Over");
            if (winner.equals("tie")) {
                builder.setMessage("It's a tie!");
            } else if (winner.equals("player 1")) {
                builder.setMessage("Player 1 wins!");
            } else if (winner.equals("player 2")) {
                builder.setMessage("Player 2 wins!");
            }
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
            });
            if (!winner.isEmpty())
                builder.show();
        });

        game.getGameState().observe(getViewLifecycleOwner(), gameState -> {
            updateGameInstruction(view, game.getCurrentPlayer().getValue(), gameState);
        });


        game.getChosenPiece().observe(getViewLifecycleOwner(), piece -> {
            if (piece != null) {
                chosenPieceLabel.setVisibility(View.VISIBLE);
                pieceView.setVisibility(View.VISIBLE);
                pieceView.setPiece(piece);
            } else {
                chosenPieceLabel.setVisibility(View.GONE);
                pieceView.setVisibility(View.GONE);
                actionBtn.setClickable(true);
            }
        });

        actionBtn.setOnClickListener(v -> {
            Log.d("GameActivity", "Size: " + game.getAvailablePieces().size());
            Log.d("GameActivity", game.getBoard().getValue().toString());

            View dialog = getLayoutInflater().inflate(R.layout.dialog_piece_picker, null);
            GridLayout grid = dialog.findViewById(R.id.pieceGrid);
            for (Piece p : game.getAvailablePieces()) {
                Log.d("GameActivity", "Available piece: " + p.toString());
                PieceThumbView thumbView = new PieceThumbView(requireContext());
                thumbView.setPiece(p);
                thumbView.setOnClickListener(v1 -> {
                    game.selectPiece(p);
                    picker.dismiss();
                    actionBtn.setClickable(false);
                });
                grid.addView(thumbView);
            }

            picker = new AlertDialog.Builder(requireContext())
                    .setTitle("Select a piece")
                    .setCancelable(false)
                    .setView(dialog)
                    .show();
        });
    }

    private void updateGameInstruction(View view, Boolean currentPlayer, QuartoGameViewModel.GameState gameState) {
        Log.d("GameActivity", "Updating game instruction: " + currentPlayer + " " + gameState);
        if(gameState == QuartoGameViewModel.GameState.SELECT_PIECE) {
            ((TextView) view.findViewById(R.id.infoLabel)).setText("Player " + (currentPlayer ? 2 : 1) + " – choose a piece for opponent");
        } else {
            ((TextView) view.findViewById(R.id.infoLabel)).setText("Player " + (currentPlayer ? 2 : 1) + " – place the piece");
        }
    }
}