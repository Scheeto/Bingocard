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
}