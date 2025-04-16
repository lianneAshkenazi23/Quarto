package a.lianne.quarto;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>{
    private List<LeaderboardEntry> leaderboardEntries;

    public LeaderboardAdapter(List<LeaderboardEntry> entries) {
        this.leaderboardEntries = entries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.rankTextView.setText("Rank");
            holder.emailTextView.setText("Email");
            holder.scoreTextView.setText("Score");
            return;
        }
        LeaderboardEntry entry = leaderboardEntries.get(position);
        holder.rankTextView.setText(String.valueOf(position));
        holder.emailTextView.setText(entry.getEmail());
        holder.scoreTextView.setText(String.valueOf(entry.getScore()));
    }

    @Override
    public int getItemCount() {
        return leaderboardEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rankTextView;
        public TextView emailTextView;
        public TextView scoreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.tvRank);
            emailTextView = itemView.findViewById(R.id.tvEmail);
            scoreTextView = itemView.findViewById(R.id.tvScore);
        }
    }


}
