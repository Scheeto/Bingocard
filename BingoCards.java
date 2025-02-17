import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class BingoCard {
    List<String[]> cardData;

    public BingoCard() {
        this.cardData = new ArrayList<>();
    }

    public void addLine(String line) {
        // Split the line into individual numbers using commas
        String[] lineArray = line.split(",");
        cardData.add(lineArray);
    }

    public String getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < cardData.size() && columnIndex >= 0 && columnIndex < cardData.get(rowIndex).length) {
            return cardData.get(rowIndex)[columnIndex].trim(); // Trim to remove extra spaces
        }
        return null;
    }

    public void printCardData() {
        for (String[] line : cardData) {
            for (String element : line) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }
}

public class BingoCards {
    private List<BingoCard> cards;
    private String filePath;

    public BingoCards(String filePath) {
        this.filePath = filePath;
        this.cards = new ArrayList<>();
    }

    public void readCardsFromFile() {
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            BingoCard currentCard = null;

            while (reader.hasNextLine()) {
                String data = reader.nextLine().trim();
                if (data.startsWith("Card")) {
                    // Start a new card
                    if (currentCard != null) {
                        cards.add(currentCard);
                    }
                    currentCard = new BingoCard();
                } else if (!data.isEmpty() && currentCard != null) {
                    // Add the line to the current card
                    currentCard.addLine(data);
                }
            }
            // Add the last card if file does not end with an empty line
            if (currentCard != null && !currentCard.cardData.isEmpty()) {
                cards.add(currentCard);
            }

            reader.close();
            System.out.println("Cards data read successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    public boolean searchValueInColumn(String input) {
        if (input.length() < 2) {
            return false;
        }

        // Extract the column label (e.g., 'B' from 'B3')
        char columnLabel = input.charAt(0);

        // Map the column label to a column index
        int columnIndex;
        switch (columnLabel) {
            case 'B': columnIndex = 0; break;
            case 'I': columnIndex = 1; break;
            case 'N': columnIndex = 2; break;
            case 'G': columnIndex = 3; break;
            case 'O': columnIndex = 4; break;
            default: return false; // Invalid column label
        }

        // Extract the row number (e.g., '3' from 'B3') and convert to zero-based index
        int rowIndex;
        try {
            rowIndex = Integer.parseInt(input.substring(1)) - 1; // Convert to zero-based index
        } catch (NumberFormatException e) {
            return false; // Invalid row number
        }

        // Check if the row and column indices are valid
        if (rowIndex < 0 || rowIndex >= 5) { // Assuming 5 rows in a bingo card
            return false;
        }

        // Iterate through all cards and check the specified row and column for the value
        for (BingoCard card : cards) {
            String cardValue = card.getValueAt(rowIndex, columnIndex);
            if (cardValue != null && cardValue.equals(input.substring(1))) {
                return true;
            }
        }

        return false;
    }

    public void printCardsData() {
        for (BingoCard card : cards) {
            card.printCardData();
            System.out.println("----");
        }
    }

    public static void main(String[] args) {
        BingoCards bingoCards = new BingoCards("Cards.txt");
        bingoCards.readCardsFromFile();
        bingoCards.printCardsData();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a value to search (e.g., B3) or type 'exit' to quit: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            boolean found = bingoCards.searchValueInColumn(input);
            if (found) {
                System.out.println(input + " was found!");
            } else {
                System.out.println(input + " was not found.");
            }
        }

        scanner.close();
    }
}