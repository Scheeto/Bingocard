import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BingoGame {
    private List<BingoCard> cards;
    private BingoCaller caller;
    private Scanner scanner;

    public BingoGame() {
        cards = new ArrayList<>();
        caller = new BingoCaller();
        scanner = new Scanner(System.in);
    }

    public void loadCardsFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String cardId = fileScanner.nextLine().trim(); // Read card ID (e.g., "Card1")
                int[][] numbers = new int[5][5]; // Initialize a 5x5 grid for the card

                for (int i = 0; i < 5; i++) {
                    String line = fileScanner.nextLine().trim(); // Read a line of numbers
                    String[] parts = line.split(","); // Split the line by commas
                    for (int j = 0; j < 5; j++) {
                        numbers[i][j] = Integer.parseInt(parts[j].trim()); // Parse each number
                    }
                }

                cards.add(new BingoCard(cardId, numbers)); // Add the card to the list
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void playRandomMode() {
        System.out.println("Random Mode Selected.");
        System.out.print("Enter the number of cards to play (1-4): ");
        int numCards = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (numCards < 1 || numCards > 4) {
            System.out.println("Invalid number of cards. Please choose between 1 and 4.");
            return;
        }

        List<BingoCard> selectedCards = new ArrayList<>();
        Set<Integer> selectedIndices = new HashSet<>();
        while (selectedCards.size() < numCards) {
            int randomIndex = (int) (Math.random() * cards.size());
            if (!selectedIndices.contains(randomIndex)) {
                selectedCards.add(cards.get(randomIndex));
                selectedIndices.add(randomIndex);
            }
        }

        for (BingoCard card : selectedCards) {
            card.displayCard();
        }

        while (true) {
            String call = caller.callNumber();
            System.out.println("Caller says: " + call);

            for (BingoCard card : selectedCards) {
                int number = Integer.parseInt(call.substring(1));
                card.markNumber(number);
                card.displayCard();

                if (card.checkBingo()) {
                    System.out.println("BINGO! You win on card: " + card.getId());
                    return;
                }
            }

            System.out.print("Press Enter to continue or type 'Bingo' to claim: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Bingo")) {
                System.out.println("Checking for Bingo...");
                for (BingoCard card : selectedCards) {
                    if (card.checkBingo()) {
                        System.out.println("BINGO! You win on card: " + card.getId());
                        return;
                    }
                }
                System.out.println("No Bingo found. Keep playing!");
            }
        }
    }

    public void playManualMode() {
        System.out.println("Manual Mode Selected.");
        System.out.print("Enter the card ID to play (e.g., Card1): ");
        String cardId = scanner.nextLine();

        BingoCard selectedCard = null;
        for (BingoCard card : cards) {
            if (card.getId().equalsIgnoreCase(cardId)) {
                selectedCard = card;
                break;
            }
        }

        if (selectedCard == null) {
            System.out.println("Card not found.");
            return;
        }

        selectedCard.displayCard();

        while (true) {
            System.out.print("Enter the called number (e.g., B12): ");
            String call = scanner.nextLine();
            if (!call.matches("[BINGO]\\d+")) {
                System.out.println("Invalid format. Please enter in the format B12, I25, etc.");
                continue;
            }
            int number = Integer.parseInt(call.substring(1));

            selectedCard.markNumber(number);
            selectedCard.displayCard();

            if (selectedCard.checkBingo()) {
                System.out.println("BINGO! You win on card: " + selectedCard.getId());
                return;
            }
        }
    }

    public void startGame() {
        System.out.println("Welcome to Bingo!");
        loadCardsFromFile("Cards.txt"); // Load cards from file

        System.out.print("Choose mode (R for Random, M for Manual): ");
        String mode = scanner.nextLine().toUpperCase();

        if (mode.equals("R")) {
            playRandomMode();
        } else if (mode.equals("M")) {
            playManualMode();
        } else {
            System.out.println("Invalid mode selected.");
        }
    }
}