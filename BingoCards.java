import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

class BingoCard {
    List<String[]> cardData;
    String identifier;

    public BingoCard(String identifier) {
        this.cardData = new ArrayList<>();
        this.identifier = identifier;
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
        System.out.println("Card: " + identifier);
        System.out.println("  B    I    N    G    O");
        for (int row = 0; row < cardData.size(); row++) {
            for (int col = 0; col < cardData.get(row).length; col++) {
                System.out.print(cardData.get(row)[col].trim() + "\t");
            }
            System.out.println();
        }
        System.out.println("----");
    }

    public boolean checkBingo() {
        // Check rows
        for (String[] row : cardData) {
            if (isBingoLine(row)) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 5; col++) {
            String[] column = new String[5];
            for (int row = 0; row < 5; row++) {
                column[row] = cardData.get(row)[col];
            }
            if (isBingoLine(column)) {
                return true;
            }
        }

        // Check diagonals
        String[] diagonal1 = new String[5];
        String[] diagonal2 = new String[5];
        for (int i = 0; i < 5; i++) {
            diagonal1[i] = cardData.get(i)[i];
            diagonal2[i] = cardData.get(i)[4 - i];
        }
        return isBingoLine(diagonal1) || isBingoLine(diagonal2);
    }

    private boolean isBingoLine(String[] line) {
        for (String value : line) {
            if (!"XX".equals(value.trim())) {
                return false;
            }
        }
        return true;
    }
}

public class BingoCards {
    private List<BingoCard> cards;
    private String filePath;
    private Random random;
    private Set<String> calledNumbers;

    public BingoCards(String filePath) {
        this.filePath = filePath;
        this.cards = new ArrayList<>();
        this.random = new Random();
        this.calledNumbers = new HashSet<>();
    }

    public void readCardsFromFile() {
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            BingoCard currentCard = null;
            String currentIdentifier = "";

            while (reader.hasNextLine()) {
                String data = reader.nextLine().trim();
                if (data.startsWith("Card")) {
                    currentIdentifier = data;
                    if (currentCard != null) {
                        cards.add(currentCard);
                    }
                    currentCard = new BingoCard(currentIdentifier);
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

    public void randomPlay(int numCards) {
        Collections.shuffle(cards);
        List<BingoCard> selectedCards = cards.subList(0, numCards);

        // Print selected cards
        for (BingoCard card : selectedCards) {
            card.printCardData();
        }

        Scanner scanner = new Scanner(System.in);
        boolean bingoAchieved = false;

        while (!bingoAchieved) {
            String calledNumber = callRandomNumber();
            System.out.println("Called: " + calledNumber);

            for (BingoCard card : selectedCards) {
                System.out.println("Card: " + card.identifier);
                card.printCardData();
                System.out.println("Enter position to mark (e.g., BB) or type 'next' to skip: ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("next")) {
                    continue;
                }

                int rowIndex = "BINGO".indexOf(input.charAt(0));
                int columnIndex = "BINGO".indexOf(input.charAt(1));

                if (rowIndex >= 0 && rowIndex < 5 && columnIndex >= 0 && columnIndex < 5) {
                    card.setValueAt(rowIndex, columnIndex, "XX");
                } else {
                    System.out.println("Invalid position.");
                }

                card.printCardData();
                System.out.println("Enter 'bingo' if you have achieved Bingo, or 'next' to continue: ");
                String userResponse = scanner.nextLine().trim();
                if (userResponse.equalsIgnoreCase("bingo")) {
                    if (card.checkBingo()) {
                        System.out.println("Congratulations! Bingo on card: " + card.identifier);
                        bingoAchieved = true;
                        break;
                    } else {
                        System.out.println("Not a valid Bingo. Continuing game...");
                    }
                }
            }
        }
    }

    public void manualPlay(int numCards) {
        Scanner scanner = new Scanner(System.in);

        // Allow user to select cards by name
        List<BingoCard> selectedCards = new ArrayList<>();
        for (int i = 0; i < numCards; i++) {
            System.out.println("Enter card identifier to select: ");
            String cardIdentifier = scanner.nextLine().trim();
            for (BingoCard card : cards) {
                if (card.identifier.equalsIgnoreCase(cardIdentifier)) {
                    selectedCards.add(card);
                    break;
                }
            }
        }

        // Print selected cards
        for (BingoCard card : selectedCards) {
            card.printCardData();
        }

        boolean bingoAchieved = false;

        while (!bingoAchieved) {
            System.out.println("Enter called number (e.g., B3): ");
            String calledNumber = scanner.nextLine().trim();

            for (BingoCard card : selectedCards) {
                System.out.println("Card: " + card.identifier);
                card.printCardData();
                System.out.println("Enter position to mark (e.g., BB) or type 'next' to skip: ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("next")) {
                    continue;
                }

                int rowIndex = "BINGO".indexOf(input.charAt(0));
                int columnIndex = "BINGO".indexOf(input.charAt(1));

                if (rowIndex >= 0 && rowIndex < 5 && columnIndex >= 0 && columnIndex < 5) {
                    String value = card.getValueAt(rowIndex, columnIndex);
                    if (value != null && value.equals(calledNumber)) {
                        card.setValueAt(rowIndex, columnIndex, "XX");
                    } else {
                        System.out.println("Number does not match. Cannot mark.");
                    }
                } else {
                    System.out.println("Invalid position.");
                }

                card.printCardData();
                System.out.println("Enter 'bingo' if you have achieved Bingo, or 'next' to continue: ");
                String userResponse = scanner.nextLine().trim();
                if (userResponse.equalsIgnoreCase("bingo")) {
                    if (card.checkBingo()) {
                        System.out.println("Congratulations! Bingo on card: " + card.identifier);
                        bingoAchieved = true;
                        break;
                    } else {
                        System.out.println("Not a valid Bingo. Continuing game...");
                    }
                }
            }
        }
    }

    private String callRandomNumber() {
        String[] letters = {"B", "I", "N", "G", "O"};
        while (true) {
            String letter = letters[random.nextInt(letters.length)];
            int number = 0;
            switch (letter) {
                case "B": number = 1 + random.nextInt(15); break;
                case "I": number = 16 + random.nextInt(15); break;
                case "N": number = 31 + random.nextInt(15); break;
                case "G": number = 46 + random.nextInt(15); break;
                case "O": number = 61 + random.nextInt(15); break;
            }
            String calledNumber = letter + number;
            if (!calledNumbers.contains(calledNumber)) {
                calledNumbers.add(calledNumber);
                return calledNumber;
            }
        }
    }

    public static void main(String[] args) {
        BingoCards bingoGame = new BingoCards("path/to/your/bingo_cards.txt");
        bingoGame.readCardsFromFile();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose play mode: 1 for Random Play, 2 for Manual Play");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            System.out.println("Enter number of cards to play: ");
            int numCards = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            bingoGame.randomPlay(numCards);
        } else if (choice == 2) {
            System.out.println("Enter number of cards to play: ");
            int numCards = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            bingoGame.manualPlay(numCards);
        } else {
            System.out.println("Invalid choice. Exiting...");
        }
    }
}2
2