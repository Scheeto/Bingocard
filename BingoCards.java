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
        String[] lineArray = line.split(",");
        cardData.add(lineArray);
    }

    public String getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < cardData.size() && columnIndex >= 0 && columnIndex < cardData.get(rowIndex).length) {
            return cardData.get(rowIndex)[columnIndex].trim();
        }
        return null;
    }

    public void setValueAt(int rowIndex, int columnIndex, String value) {
        if (rowIndex >= 0 && rowIndex < cardData.size() && columnIndex >= 0 && columnIndex < cardData.get(rowIndex).length) {
            cardData.get(rowIndex)[columnIndex] = value;
        }
    }

    public void printCardData() {
        for (String[] line : cardData) {
            for (String element : line) {
                System.out.print("[" + element.trim() + "] ");
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
                    if (currentCard != null) {
                        cards.add(currentCard);
                    }
                    currentCard = new BingoCard();
                } else if (!data.isEmpty() && currentCard != null) {
                    currentCard.addLine(data);
                }
            }
            if (currentCard != null && !currentCard.cardData.isEmpty()) {
                cards.add(currentCard);
            }

            reader.close();
            System.out.println("Cards data read successfully.");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        }
    }

    public boolean searchValueInColumn(String input) {
        if (input.length() < 2) {
            return false;
        }

        char columnLabel = input.charAt(0);
        int columnIndex;
        switch (columnLabel) {
            case 'B': columnIndex = 0; break;
            case 'I': columnIndex = 1; break;
            case 'N': columnIndex = 2; break;
            case 'G': columnIndex = 3; break;
            case 'O': columnIndex = 4; break;
            default: return false;
        }

        int rowIndex;
        try {
            rowIndex = Integer.parseInt(input.substring(1)) - 1;
        } catch (NumberFormatException e) {
            System.err.println("Invalid row number: " + input.substring(1));
            return false;
        }

        if (rowIndex < 0 || rowIndex >= 5) {
            System.err.println("Row index out of range: " + rowIndex);
            return false;
        }

        for (BingoCard card : cards) {
            String cardValue = card.getValueAt(rowIndex, columnIndex);
            System.out.println("Checking card value: " + cardValue + " against input: " + input.substring(1));
            if (cardValue != null && cardValue.equals(input.substring(1))) {
                card.setValueAt(rowIndex, columnIndex, "null");
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
                System.out.println(input + " was found and replaced with 'null'!");
            } else {
                System.out.println(input + " was not found.");
            }
        }

        scanner.close();
    }
}
