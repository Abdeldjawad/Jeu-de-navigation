package Model;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard {
    private static List<ScoreEntry> scores = new ArrayList<>();

    public static List<ScoreEntry> getScores() {
        return scores;
    }

    public static void addScore(String username, int score) {
        scores.add(new ScoreEntry(username, score));
        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
    }

    public static class ScoreEntry {
        private String username;
        private int score;

        public ScoreEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }

        public String getUsername() {
            return username;
        }

        public int getScore() {
            return score;
        }
    }
}
