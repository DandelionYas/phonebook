package phonebook.util;

public class SequenceGenerator {
    private static Long sequeceNumber = 0L;

    public static Long getNextID(){
        return sequeceNumber++;
    }
}
