import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class BingoCard {
    private String id;
    private String[][] card;
    private boolean[][] marked;
    private List<Pattern> patterns;

    public BingoCard(String id, String[][] card) {
        this.id = id;
        this.card = card;
        this.marked = new boolean[5][5];
        this.patterns = new ArrayList<>();
        patterns.add(new RowPattern());
        patterns.add(new ColumnPattern());
        patterns.add(new DiagonalPattern());
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

    public boolean hasNumber(int number, int col) {
        for (int i = 0; i < 5; i++) {
            if (card[i][col].equals(String.valueOf(number))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNumber(int number) { // Overload the hasNumber function to avoid modifying the older parts of the
                                           // code
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
            int col = -1; // Initialize to an invalid column index
            try {
                if (input.length() > 1) { // Check if input has letter and number
                    char letter = input.toUpperCase().charAt(0);
                    calledNum = Integer.parseInt(input.substring(1));
                    // Translate the letter to a column index.
                    switch (letter) {
                        case 'B':
                            col = 0;
                            break;
                        case 'I':
                            col = 1;
                            break;
                        case 'N':
                            col = 2;
                            break;
                        case 'G':
                            col = 3;
                            break;
                        case 'O':
                            col = 4;
                            break;
                        default:
                            System.out.println(
                                    "Invalid input format. Please enter a letter (B, I, N, G, O) followed by a number");
                            continue; // Go to the next iteration of the loop
                    }

                } else {
                    System.out.println(
                            "Invalid input format. Please enter a letter (B, I, N, G, O) followed by a number");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            // Check if the number has been called
            if (calledNumbers.contains("B" + calledNum) || calledNumbers.contains("I" + calledNum)
                    || calledNumbers.contains("N" + calledNum) || calledNumbers.contains("G" + calledNum)
                    || calledNumbers.contains("O" + calledNum)) {
                System.out.println("This number has already been called.");
                continue;
            }

            if (col != -1 && card.hasNumber(calledNum, col)) { // Use the column to mark the correct one
                for (int row = 0; row < 5; row++) {
                    if (card.getCard()[row][col].equals(String.valueOf(calledNum))) {
                        card.markCell(row, col);
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
                calledNumbers.add(input.toUpperCase()); // Use the letter + number
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
        // No hardcoded B1, I5, etc. We now read from the file directly.

        List<String> allCardData = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            allCardData.add(fileScanner.nextLine());
        }
        fileScanner.close();

        // Determine all possible numbers to be called.
        Set<Integer> allPossibleNumbers = new HashSet<>();
        for (int i = 1; i <= 75; i++) {
            allPossibleNumbers.add(i);
        }
        List<Integer> numbersToCall = new ArrayList<>(allPossibleNumbers); // Convert to list for shuffling
        Collections.shuffle(numbersToCall); // Shuffle to randomize the numbers

        // Process each card
        for (BingoCard card : cards) {
            boolean cardWon = false;
            card.displayInUserFriendlyFormat();

            for (int numberIndex = 0; numberIndex < numbersToCall.size(); numberIndex++) {
                int calledNum = numbersToCall.get(numberIndex);
                char letter = ' ';
                int col = -1;

                // Determine the letter based on the number
                if (calledNum >= 1 && calledNum <= 15) {
                    letter = 'B';
                    col = 0;
                } else if (calledNum >= 16 && calledNum <= 30) {
                    letter = 'I';
                    col = 1;
                } else if (calledNum >= 31 && calledNum <= 45) {
                    letter = 'N';
                    col = 2;
                } else if (calledNum >= 46 && calledNum <= 60) {
                    letter = 'G';
                    col = 3;
                } else if (calledNum >= 61 && calledNum <= 75) {
                    letter = 'O';
                    col = 4;
                }
                String calledNumber = letter + String.valueOf(calledNum); // Build called number string

                if (!calledNumbers.contains(calledNumber)) {
                    calledNumbers.add(calledNumber);
                    System.out.println("Called Number: " + calledNumber);

                    if (col != -1 && card.hasNumber(calledNum, col)) {
                        for (int row = 0; row < 5; row++) {
                            if (card.getCard()[row][col].equals(String.valueOf(calledNum))) {
                                card.markCell(row, col);
                            }
                        }
                        card.displayInUserFriendlyFormat();

                        if (card.checkBingo()) {
                            System.out.println("Bingo! Card " + card.getId() + " wins!");
                            cardWon = true;
                            break; // Card wins, go to next card
                        }
                    } else {
                        // Number not on the card. Continue to the next call.
                    }
                } else {
                    System.out.println("Number " + calledNumber + " already called. Skipping.");
                }
            }

            if (!cardWon) {
                System.out.println("Card " + card.getId() + " did not achieve Bingo.");
            }
        }
        System.out.println("Random game over.");
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
            System.out.print("Enter the number of cards to play (1-" + cards.size() + "): ");
            int numCards = 0;
            try {
                numCards = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Using all cards.");
                numCards = cards.size();
            }

            if (numCards < 1 || numCards > cards.size()) {
                System.out.println("Invalid number of cards.  Max " + cards.size() + ". Using all cards.");
                numCards = cards.size();
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