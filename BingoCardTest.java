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

class DiagonalPattern extends Pattern {
    @Override
    boolean matches(BingoCard card) {
        boolean[][] marked = card.getMarked();

        // Check top-left to bottom-right diagonal
        boolean mainDiagonal = true;
        for (int i = 0; i < 5; i++) {
            if (!marked[i][i]) {
                mainDiagonal = false;
                break;
            }
        }

        // Check top-right to bottom-left diagonal
        boolean antiDiagonal = true;
        for (int i = 0; i < 5; i++) {
            if (!marked[i][4 - i]) {
                antiDiagonal = false;
                break;
            }
        }

        return mainDiagonal || antiDiagonal;
    }
}
class CustomPattern extends Pattern {
    private int[][] coordinates;
    
    public CustomPattern(int[][] coordinates) {
        this.coordinates = coordinates;
    }
    
    @Override
    boolean matches(BingoCard card) {
        boolean[][] marked = card.getMarked();
        
        for (int[] coord : coordinates) {
            int row = coord[0];
            int col = coord[1];
            
            // Check if coordinates are valid and marked
            if (row < 0 || row >= 5 || col < 0 || col >= 5 || !marked[row][col]) {
                return false;
            }
        }
        return true;
    }
}