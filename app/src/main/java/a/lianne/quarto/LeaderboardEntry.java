package a.lianne.quarto;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    private String email;
    private long score;

    public LeaderboardEntry(String email, long score) {
        this.email = email;
        this.score = score;
    }


    public String getEmail() {
        return email;
    }

    public long getScore() {
        return score;
    }


    @Override
    public int compareTo(LeaderboardEntry o) {
        if (this.score == o.getScore()) // if scores are equal, sort by email
            return this.email.compareTo(o.getEmail());
        return Long.compare(o.getScore(), this.score);
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "email='" + email + '\'' +
                ", score=" + score;
    }
}
