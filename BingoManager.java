import java.util.ArrayList;
import java.util.List;

class BingoManager {
    private List<Pattern> patterns;

    public BingoManager() {
        this.patterns = new ArrayList<>();
    }

    public void addPattern(Pattern pattern) {
        patterns.add(pattern);
    }

    public int countBingos(BingoCard card) {
        int bingoCount = 0;
        for (Pattern pattern : patterns) {
            if (pattern.matches(card)) {
                bingoCount++;
            }
        }
        return bingoCount;
    }
}