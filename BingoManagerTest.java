import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BingoManagerTest {
    private BingoCard card;
    private BingoManager manager;

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
        manager = new BingoManager();
    }

    @Test
    void testBM1_FirstRowFullyMarked() {
        manager.addPattern(new RowPattern());
        for (int j = 0; j < 5; j++) {
            card.markCell(0, j);
        }
        assertEquals(1, manager.countBingos(card));
    }

    @Test
    void testBM2_FirstThirdLastRowsFullyMarked() {
        manager.addPattern(new RowPattern());
        for (int j = 0; j < 5; j++) {
            card.markCell(0, j); // First row
            card.markCell(2, j); // Third row
            card.markCell(4, j); // Last row
        }
        assertEquals(3, manager.countBingos(card));
    }

    @Test
    void testBM3_RowsAndColumnMarked() {
        manager.addPattern(new RowPattern());
        for (int j = 0; j < 5; j++) {
            card.markCell(0, j); // First row
            card.markCell(2, j); // Third row
            card.markCell(4, j); // Last row
        }
        for (int i = 0; i < 5; i++) {
            card.markCell(i, 4); // Last column
        }
        assertEquals(3, manager.countBingos(card));
    }

    @Test
    void testBM6_FullCardWithAllPatterns() {
        int[][] tCoords = { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 }, { 1, 2 }, { 2, 2 }, { 3, 2 }, { 4, 2 } };
        int[][] squareCoords = {
                { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 },
                { 4, 0 }, { 4, 1 }, { 4, 2 }, { 4, 3 }, { 4, 4 },
                { 1, 0 }, { 2, 0 }, { 3, 0 },
                { 1, 4 }, { 2, 4 }, { 3, 4 }
        };

        manager.addPattern(new RowPattern());
        manager.addPattern(new ColumnPattern());
        manager.addPattern(new DiagonalPattern());
        manager.addPattern(new CustomPattern(tCoords));
        manager.addPattern(new CustomPattern(squareCoords));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                card.markCell(i, j);
            }
        }
        // 5 rows + 5 columns + 2 diagonals + 1 T + 1 Square = 14
        assertEquals(14, manager.countBingos(card));
    }

    @Test
    void testBM7_FullCardMinusTopRight() {
        int[][] tCoords = { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 }, { 1, 2 }, { 2, 2 }, { 3, 2 }, { 4, 2 } };
        int[][] squareCoords = {
                { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 },
                { 4, 0 }, { 4, 1 }, { 4, 2 }, { 4, 3 }, { 4, 4 },
                { 1, 0 }, { 2, 0 }, { 3, 0 },
                { 1, 4 }, { 2, 4 }, { 3, 4 }
        };

        manager.addPattern(new RowPattern());
        manager.addPattern(new ColumnPattern());
        manager.addPattern(new DiagonalPattern());
        manager.addPattern(new CustomPattern(tCoords));
        manager.addPattern(new CustomPattern(squareCoords));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!(i == 0 && j == 4)) { // Skip top-right
                    card.markCell(i, j);
                }
            }
        }
        // 4 rows (row 0 incomplete) + 4 columns (col 4 incomplete) + 1 diagonal + 0 T +
        // 0 Square = 9
        assertEquals(9, manager.countBingos(card));
    }

}
