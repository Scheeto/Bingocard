import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BingoCards {
    private List<String> cardsData;
    private String filePath;

    public BingoCards(String filePath) {
        this.filePath = filePath;
        this.cardsData = new ArrayList<>();
    }

    public void readCardsFromFile() {
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                cardsData.add(data);
            }

            reader.close();
            System.out.println("Cards data read successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }

    public void printCardsData() {
        for (String data : cardsData) {
            System.out.println(data);
        }
    }

    public static void main(String[] args) {
        BingoCards bingoCards = new BingoCards("Cards.txt");
        bingoCards.readCardsFromFile();
        bingoCards.printCardsData();
    }
}
