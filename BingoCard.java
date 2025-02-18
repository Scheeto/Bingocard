import java.util.Arrays;

public class BingoCard {
    private String id;
    private int[][] numbers;
    private boolean[][] marked;

    public BingoCard(String id, int[][] numbers) {
        this.id = id;
        this.numbers = numbers;
        this.marked = new boolean[5][5];
    }

    public String getId() {
        return id;
    }

    public void markNumber(int number) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (numbers[i][j] == number) {
                    marked[i][j] = true;
                    return;
                }
            }
        }
    }

    public boolean checkBingo() {
        // Check rows
        for (int i = 0; i < 5; i++) {
            if (marked[i][0] && marked[i][1] && marked[i][2] && marked[i][3] && marked[i][4]) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 5; j++) {
            if (marked[0][j] && marked[1][j] && marked[2][j] && marked[3][j] && marked[4][j]) {
                return true;
            }
        }

        // Check diagonals
        if (marked[0][0] && marked[1][1] && marked[2][2] && marked[3][3] && marked[4][4]) {
            return true;
        }
        if (marked[0][4] && marked[1][3] && marked[2][2] && marked[3][1] && marked[4][0]) {
            return true;
        }

        // Check four corners
        if (marked[0][0] && marked[0][4] && marked[4][0] && marked[4][4]) {
            return true;
        }

        return false;
    }

    public void displayCard() {
        System.out.println("Card ID: " + id);
        System.out.println("B   I   N   G   O");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (marked[i][j]) {
                    System.out.print("XX  ");
                } else {
                    System.out.print(numbers[i][j] + "  ");
                }
            }
            System.out.println();
        }
    }
}