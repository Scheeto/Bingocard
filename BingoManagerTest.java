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

}
