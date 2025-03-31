import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.List;

public class test {

    public static void main(String[] args) {
        ArrayList<Account> accounts = JsonInput.deserializeAccounts("./src/accounts.json");
        Game game = Game.getInstance();

        // Start game
        new Login(game);

    }
}

