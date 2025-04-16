package a.lianne.quarto;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();

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
        recyclerView = view.findViewById(R.id.leaderboardRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        LeaderboardAdapter adapter = new LeaderboardAdapter(leaderboardEntries);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        db.collection("scores").get().addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful())  {
                // translate firebase data to leaderboard entries
                for (int i = 0; i < task.getResult().size(); i++)
                    leaderboardEntries.add(new LeaderboardEntry(task.getResult().getDocuments().get(i).getString("email"), task.getResult().getDocuments().get(i).getLong("score")));
                // sort leaderboard
                Collections.sort(leaderboardEntries);
                leaderboardEntries.add(0, new LeaderboardEntry("Placeholder", 0));
                // add title entry to the head of the list

                adapter.notifyDataSetChanged();
            }
        });
    }
}