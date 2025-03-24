abstract class Pattern {
    // Abstract method that checks if the pattern is matched on the given BingoCard
    abstract boolean matches(BingoCard card);
}

class RowPattern extends Pattern {
    @Override
    boolean matches(BingoCard card) {
        boolean[][] marked = card.getMarked();
        for (int i = 0; i < 5; i++) {
            boolean rowComplete = true;
            for (int j = 0; j < 5; j++) {
                if (!marked[i][j]) {
                    rowComplete = false;
                    break;
                }
            }
            if (rowComplete) {
                return true;
            }
        }
        return false;
    }
}

class ColumnPattern extends Pattern {
    @Override
    boolean matches(BingoCard card) {
        boolean[][] marked = card.getMarked();
        for (int j = 0; j < 5; j++) {
            boolean colComplete = true;
            for (int i = 0; i < 5; i++) {
                if (!marked[i][j]) {
                    colComplete = false;
                    break;
                }
            }
            if (colComplete) {
                return true;
            }
        }
        return false;
    }
}