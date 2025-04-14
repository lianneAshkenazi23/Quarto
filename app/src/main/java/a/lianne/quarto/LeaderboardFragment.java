package a.lianne.quarto;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class LeaderboardFragment extends Fragment {

    private FirebaseFirestore db;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    public static LeaderboardFragment newInstance() {
        LeaderboardFragment fragment = new LeaderboardFragment();
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
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        db.collection("scores").get().addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful())  {
                TableLayout table = view.findViewById(R.id.leaderboardTable);
                for (int i = 0; i < task.getResult().size(); i++) {
                    String email = task.getResult().getDocuments().get(i).getString("email");
//                   String firstName = task.getResult().getDocuments().get(i).getString("firstName");
                    Long score = task.getResult().getDocuments().get(i).getLong("score");
                    TextView row = new TextView(requireContext());
                    row.setText(email + " " + score);
                    table.addView(row);
                }
            }
        });
    }
}