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