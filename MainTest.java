import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    private BingoCard card;

    // Helper method to create a fresh card for each test
    private BingoCard createCard() {
        String[][] numbers = new String[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                numbers[i][j] = String.valueOf(i * 15 + j + 1);
            }
        }
        return new BingoCard("TestCard", numbers);
    }

    @BeforeEach
    void setUp() {
        card = createCard();
    }

    // Row Pattern Tests
    @Test
    void testP1_FirstRowFullyMarked() {
        for (int j = 0; j < 5; j++) {
            card.markCell(0, j);
        }
        Pattern rowPattern = new RowPattern();
        assertTrue(rowPattern.matches(card));
    }

    @Test
    void testP2_ThirdRowFullyMarked() {
        for (int j = 0; j < 5; j++) {
            card.markCell(2, j);
        }
        Pattern rowPattern = new RowPattern();
        assertTrue(rowPattern.matches(card));
    }

    @Test
    void testP3_LastRowFullyMarked() {
        for (int j = 0; j < 5; j++) {
            card.markCell(4, j);
        }
        Pattern rowPattern = new RowPattern();
        assertTrue(rowPattern.matches(card));
    }
    @Test
    void testP4_FirstRowMissingLastColumn() {
        for (int j = 0; j < 4; j++) {
            card.markCell(0, j);
        }
        Pattern rowPattern = new RowPattern();
        assertFalse(rowPattern.matches(card));
    }
    @Test
    void testP5_FirstColumnFullyMarked() {
        for (int i = 0; i < 5; i++) {
            card.markCell(i, 0);
        }
        Pattern colPattern = new ColumnPattern();
        assertTrue(colPattern.matches(card));
    }

    @Test
    void testP6_FourthColumnFullyMarked() {
        for (int i = 0; i < 5; i++) {
            card.markCell(i, 3);
        }
        Pattern colPattern = new ColumnPattern();
        assertTrue(colPattern.matches(card));
    }
    @Test
    void testP7_LastColumnFullyMarked() {
        for (int i = 0; i < 5; i++) {
            card.markCell(i, 4);
        }
        Pattern colPattern = new ColumnPattern();
        assertTrue(colPattern.matches(card));
    }

    @Test
    void testP8_FirstRowNotAColumn() {
        for (int j = 0; j < 5; j++) {
            card.markCell(0, j);
        }
        Pattern colPattern = new ColumnPattern();
        assertFalse(colPattern.matches(card));
    }
    @Test
    void testP9_SecondColumnMissingSecondRow() {
        for (int i = 0; i < 5; i++) {
            if (i != 1) card.markCell(i, 1);
        }
        Pattern colPattern = new ColumnPattern();
        assertFalse(colPattern.matches(card));
    }

    // Diagonal Pattern Tests
    @Test
    void testP10_TopRightToBottomLeft() {
        for (int i = 0; i < 5; i++) {
            card.markCell(i, 4 - i);
        }
        Pattern diagPattern = new DiagonalPattern();
        assertTrue(diagPattern.matches(card));
    }
    
    @Test
    void testP11_TopLeftToBottomRight() {
        for (int i = 0; i < 5; i++) {
            card.markCell(i, i);
        }
        Pattern diagPattern = new DiagonalPattern();
        assertTrue(diagPattern.matches(card));
    }

    @Test
    void testP12_SecondRowNotDiagonal() {
        for (int j = 0; j < 5; j++) {
            card.markCell(1, j);
        }
        Pattern diagPattern = new DiagonalPattern();
        assertFalse(diagPattern.matches(card));
    }
    @Test
    void testP13_TPatternFullyMarked() {
        int[][] tCoords = {{0,0},{0,1},{0,2},{0,3},{0,4},{1,2},{2,2},{3,2},{4,2}};
        Pattern tPattern = new CustomPattern(tCoords);
        for (int[] coord : tCoords) {
            card.markCell(coord[0], coord[1]);
        }
        assertTrue(tPattern.matches(card));
    }
    @Test
    void testP14_TPatternEntireCardMarked() {
        int[][] tCoords = {{0,0},{0,1},{0,2},{0,3},{0,4},{1,2},{2,2},{3,2},{4,2}};
        Pattern tPattern = new CustomPattern(tCoords);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                card.markCell(i, j);
            }
        }
        assertTrue(tPattern.matches(card));
    }
    @Test
    void testP15_TPatternMissingMiddle() {
        int[][] tCoords = {{0,0},{0,1},{0,2},{0,3},{0,4},{1,2},{2,2},{3,2},{4,2}};
        Pattern tPattern = new CustomPattern(tCoords);
        for (int[] coord : tCoords) {
            if (!(coord[0] == 2 && coord[1] == 2)) {  // Skip middle space
                card.markCell(coord[0], coord[1]);
            }
        }
        assertFalse(tPattern.matches(card));
    }
    @Test
    void testP16_SquarePatternFullyMarked() {
        int[][] squareCoords = {
            {0,0},{0,1},{0,2},{0,3},{0,4},  // Top row
            {4,0},{4,1},{4,2},{4,3},{4,4},  // Bottom row
            {1,0},{2,0},{3,0},              // Left column (excluding corners)
            {1,4},{2,4},{3,4}               // Right column (excluding corners)
        };
        Pattern squarePattern = new CustomPattern(squareCoords);
        for (int[] coord : squareCoords) {
            card.markCell(coord[0], coord[1]);
        }
        assertTrue(squarePattern.matches(card));
    }
}