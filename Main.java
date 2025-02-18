import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class BingoCard {
    private String id;
    private String[][] card;
    private boolean[][] marked;

    public BingoCard(String id, String[][] card) {
        this.id = id;
        this.card = card;
        this.marked = new boolean[5][5];
    }

    public String getId() {
        return id;
    }

    public String[][] getCard() {
        return card;
    }

    public boolean[][] getMarked() {
        return marked;
    }

    public void markCell(int row, int col) {
        marked[row][col] = true;
    }

    public void displayCard() {
        System.out.println("Card ID: " + id);
        System.out.println("B   I   N   G   O");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (marked[i][j]) {
                    System.out.print("XX\t");
                } else {
                    System.out.print(card[i][j] + "\t");
                }
            }
            System.out.println();
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
        return false;
    }

    public boolean hasNumber(int number) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (card[i][j].equals(String.valueOf(number))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValid() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!hasNumber(i * 15 + j + 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void displayInUserFriendlyFormat() {
        System.out.println("Card ID: " + id);
        System.out.println("B   I   N   G   O");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (marked[i][j]) {
                    System.out.print("XX\t");
                } else {
                    System.out.print(card[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }
}

class BingoGame {
    private List<BingoCard> cards;
    private Set<String> calledNumbers;
    private boolean isManualMode;
    private Scanner scanner;

    public BingoGame(List<BingoCard> cards, boolean isManualMode) {
        this.cards = cards;
        this.calledNumbers = new HashSet<>();
        this.isManualMode = isManualMode;
        this.scanner = new Scanner(System.in);
    }

    public void startGame() {
        if (isManualMode) {
            manualPlay();
        } else {
            randomPlay();
        }
    }

    private void manualPlay() {
        while (!cards.isEmpty()) {
            BingoCard currentCard = chooseCard();
            if (currentCard == null) {
                System.out.println("No card selected. Exiting manual play.");
                return;
            }
            playCardManually(currentCard);
            if (!cards.contains(currentCard)) { // If the card was removed, go to the next card.
                continue;
            }
            String continueChoice;
            do {
                System.out.print("Do you want to play another card? (yes/no): ");
                continueChoice = scanner.nextLine().trim().toLowerCase();
                if (!continueChoice.equals("yes") && !continueChoice.equals("no")) {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            } while (!continueChoice.equals("yes") && !continueChoice.equals("no"));

            if (continueChoice.equals("no")) {
                break;
            }
        }
        System.out.println("Manual game over.");
    }

    private BingoCard chooseCard() {
        System.out.println("Available Cards:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ". " + cards.get(i).getId());
        }

        int cardChoice;
        do {
            System.out.print("Choose a card by entering its number (or 0 to exit): ");
            try {
                cardChoice = Integer.parseInt(scanner.nextLine());
                if (cardChoice == 0) {
                    return null; // Exit
                }
                if (cardChoice < 1 || cardChoice > cards.size()) {
                    System.out.println("Invalid card number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                cardChoice = -1; // Ensure cardChoice is invalid
            }
        } while (cardChoice < 1 || cardChoice > cards.size());

        return cards.get(cardChoice - 1);
    }


    private void playCardManually(BingoCard card) {
        boolean bingoAchieved = false;
        while (!bingoAchieved) {
            card.displayInUserFriendlyFormat();
            System.out.print("Enter the number called (or 'quit' to exit the card): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Exiting current card play.");
                return;
            }

            int calledNum;
            try {
                calledNum = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            // Check if the number has been called
            if (calledNumbers.contains("B" + calledNum) || calledNumbers.contains("I" + calledNum) || calledNumbers.contains("N" + calledNum) || calledNumbers.contains("G" + calledNum) || calledNumbers.contains("O" + calledNum)) {
                System.out.println("This number has already been called.");
                continue;
            }


            if (card.hasNumber(calledNum)) {
                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < 5; col++) {
                        if (card.getCard()[row][col].equals(String.valueOf(calledNum))) {
                            card.markCell(row, col);
                        }
                    }
                }
                card.displayInUserFriendlyFormat();

                if (card.checkBingo()) {
                    System.out.print("BINGO! Verify? (yes/no): ");
                    String verify = scanner.nextLine().trim().toLowerCase();
                    if (verify.equals("yes")) {
                        System.out.println("Congratulations! You win with card " + card.getId() + ".");
                        cards.remove(card);
                        bingoAchieved = true;
                    } else {
                        System.out.println("Incorrect Bingo. Card is discarded.");
                        cards.remove(card);
                        return;
                    }
                }
                calledNumbers.add("B" + calledNum);
            } else {
                System.out.println("Number not found on this card.");
                card.displayInUserFriendlyFormat();
            }
        }
    }

    private void randomPlay() {
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(new File("Cards.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Could not find the file");
            return;
        }
        int counter = 1;
        String calledNumber;

        while (fileScanner.hasNextLine()) {
            if (counter == 1) {
                System.out.println("First number will be B1");
                calledNumber = "B1";
            } else if (counter == 5) {
                System.out.println("Fifth number will be I5");
                calledNumber = "I5";
            } else if (counter == 10) {
                System.out.println("Tenth number will be N0");
                calledNumber = "N0";
            } else if (counter == 15) {
                System.out.println("Fifteenth number will be G5");
                calledNumber = "G5";
            } else if (counter == 20) {
                System.out.println("Twentieth number will be O0");
                calledNumber = "O0";
            } else {
                // Read the called number from the file (if it has a next line)
                if (!fileScanner.hasNextLine()) {
                    System.out.println("End of file.  No more numbers to call.");
                    break; // Exit if no more numbers to call
                }
                String nextLine = fileScanner.nextLine();
                if (nextLine.contains("Card")) {
                    continue; // Skip card id and read numbers from the input file
                }
                calledNumber = "B" + nextLine;
                counter++;
            }

            // Extract the number from the called number (e.g., "B12" -> 12)
            int calledNum;
            try {
                calledNum = Integer.parseInt(calledNumber.substring(1));
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format in called number: " + calledNumber);
                continue; // Skip this number and go to the next
            }

            calledNumbers.add(calledNumber);
            System.out.println("Called Number: " + calledNumber);

            List<BingoCard> cardsToRemove = new ArrayList<>();

            for (BingoCard card : cards) {
                if (card.hasNumber(calledNum)) {
                    // Mark the card based on the called number
                    for (int row = 0; row < 5; row++) {
                        for (int col = 0; col < 5; col++) {
                            if (card.getCard()[row][col].equals(String.valueOf(calledNum))) {
                                card.markCell(row, col);
                            }
                        }
                    }
                    card.displayInUserFriendlyFormat();
                    if (card.checkBingo()) {
                        System.out.println("Bingo! Card " + card.getId() + " wins!");
                        cardsToRemove.add(card);
                    }
                }
            }
            cards.removeAll(cardsToRemove);
            if (cards.isEmpty()) {
                System.out.println("No cards left. Game over.");
                break; // Exit the game
            }

            counter++; // increment counter
        } // end while
        if (fileScanner != null) {
            fileScanner.close(); // close the file scanner.
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<BingoCard> cards = new ArrayList<>();

        // Read cards from input file and print the contents
        try {
            File file = new File("Cards.txt");
            Scanner fileScanner = new Scanner(file);
            String currentCardId = null;
            List<String> cardDataLines = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.contains("Card")) {
                    // New card found
                    if (currentCardId != null) {
                        // Process the previous card
                        cards.add(createBingoCard(currentCardId, cardDataLines));
                        cardDataLines.clear();
                    }
                    currentCardId = line;
                } else if (line.trim().length() > 0) {
                    // Collect the lines of card data
                    cardDataLines.add(line);
                }
            }
            // Process the last card (if any)
            if (currentCardId != null) {
                cards.add(createBingoCard(currentCardId, cardDataLines));
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found.");
            return;
        }

        System.out.print("Select game mode (manual/random): ");
        String mode = scanner.nextLine().trim().toLowerCase();

        boolean isManualMode = mode.equals("manual");

        List<BingoCard> selectedCards;
        if (isManualMode) {
            selectedCards = cards; // All cards for manual mode
        } else {
            System.out.print("Enter the number of cards to play (1-9): ");
            int numCards = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (numCards < 1 || numCards > cards.size()) {
                System.out.println("Invalid number of cards.  Max " + cards.size());
                return;
            }
            selectedCards = new ArrayList<>(cards.subList(0, numCards));
        }


        BingoGame game = new BingoGame(selectedCards, isManualMode);
        game.startGame();
        scanner.close();
    }

    public static BingoCard createBingoCard(String cardId, List<String> cardDataLines) {
        String[][] cardData = getCardData(cardDataLines);
        return new BingoCard(cardId, cardData);
    }

    public static String[][] getCardData(List<String> cardDataLines) {
        String[][] cardData = new String[5][5];
        for (int i = 0; i < 5; i++) {
            String[] rowData = cardDataLines.get(i).split(",");
            for (int j = 0; j < 5; j++) {
                cardData[i][j] = rowData[j].trim(); // Remove any extra spaces.
            }
        }
        return cardData;
    }
}