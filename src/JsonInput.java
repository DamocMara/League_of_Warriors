import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.io.FileReader;

public class JsonInput {
    public static ArrayList<Account> deserializeAccounts(String filePath) {
        ArrayList<Account> accounts = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(new FileReader(filePath));
            JSONArray accountsArray = (JSONArray) obj.get("accounts");

            for (Object accountObj : accountsArray) {
                JSONObject accountJson = (JSONObject) accountObj;
                Account account = deserializeAccount(accountJson);
                if (account != null) {
                    accounts.add(account);
                }
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public static Account deserializeAccount(JSONObject accountJson) {
        try {
            // Extract account information
            String name = (String) accountJson.get("name");
            String country = (String) accountJson.get("country");
            int gamesNumber = Integer.parseInt((String) accountJson.get("maps_completed"));

            Credentials credentials = null;
            try {
                JSONObject credentialsJson = (JSONObject) accountJson.get("credentials");
                String email = (String) credentialsJson.get("email");
                String password = (String) credentialsJson.get("password");
                credentials = new Credentials(email, password);
            } catch (Exception e) {
                System.out.println("! This account doesn't have all credentials !");
            }

            SortedSet<String> favoriteGames = new TreeSet<>();
            try {
                JSONArray games = (JSONArray) accountJson.get("favorite_games");
                for (Object game : games) {
                    favoriteGames.add((String) game);
                }
            } catch (Exception e) {
                System.out.println("! This account doesn't have favorite games !");
            }

            ArrayList<Character> characters = new ArrayList<>();
            try {
                JSONArray charactersListJson = (JSONArray) accountJson.get("characters");
                characters = deserializeCharacters(charactersListJson);
            } catch (Exception e) {
                System.out.println("! This account doesn't have characters !");
            }

            Account.Information information = new Account.Information.Builder()
                    .withCredentials(credentials)
                    .withFavoriteGames(favoriteGames)
                    .withName("John")
                    .withCountry("USA")
                    .build();
            return new Account(information, characters, gamesNumber);

        } catch (Exception e) {
            System.err.println("Error deserializing account: " + e.getMessage());
        }
        return null;
    }


    private static ArrayList<Character> deserializeCharacters(JSONArray charactersJson) {
        ArrayList<Character> characters = new ArrayList<>();

        for (Object obj : charactersJson) {
            try {
                JSONObject charJson = (JSONObject) obj;
                String charname = (String) charJson.get("name");
                String profession = (String) charJson.get("profession");

                if (charname == null || profession == null) {
                    System.err.println("Error: Missing name or profession for character.");
                    continue;
                }

                int level = parseJsonInt(charJson.get("level"));
                int experience = parseJsonInt(charJson.get("experience"));
                int charisma = parseJsonInt(charJson.get("charisma"));
                int dexterity = parseJsonInt(charJson.get("dexterity"));

                Character character = null;
                switch (profession.toLowerCase()) {
                    case "warrior":
                        character = new Warrior(charname, 100, 100, 100, charisma, dexterity,
                                level, experience);
                        break;
                    case "mage":
                        character = new Mage(charname, 100, 100, 100, charisma, dexterity,
                                level, experience);
                        break;
                    case "rogue":
                        character = new Rogue(charname, 100, 100, 100, charisma, dexterity,
                                level, experience);
                        break;
                    default:
                        System.err.println("Unknown profession: " + profession + " for character: " + charname);
                        continue;
                }

                if (character != null) {
                    character.setLevel(level);
                    character.setExperience(experience);
                    characters.add(character);
                }
            } catch (Exception e) {
                System.err.println("Error deserializing character: " + e.getMessage());
            }
        }
        return characters;
    }

    private static int parseJsonInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        throw new IllegalArgumentException("Invalid integer value: " + value);
    }
}