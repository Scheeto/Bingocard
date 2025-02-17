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
        String[] lineArray = line.split(" ");
        cardData.add(lineArray);
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
            BingoCard currentCard = new BingoCard();

            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (data.isEmpty()) {
                    cards.add(currentCard);
                    currentCard = new BingoCard();
                } else {
                    currentCard.addLine(data);
                }
            }
            // Add the last card if file does not end with an empty line
            if (!currentCard.cardData.isEmpty()) {
                cards.add(currentCard);
            }

            reader.close();
            System.out.println("Cards data read successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
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
    }
}
