import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BingoCaller {
    private Set<String> calledNumbers;
    private Random random;

    public BingoCaller() {
        calledNumbers = new HashSet<>();
        random = new Random();
    }

    public String callNumber() {
        String call;
        do {
            int letterIndex = random.nextInt(5);
            char letter = "BINGO".charAt(letterIndex);
            int number = random.nextInt(15) + 1 + (letterIndex * 15);
            call = letter + Integer.toString(number);
        } while (calledNumbers.contains(call));
        calledNumbers.add(call);
        return call;
    }
}