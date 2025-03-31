import java.util.ArrayList;
import java.util.SortedSet;

public class Account {
    public Information information;
    private ArrayList<Character> characters;
    private int nrgames;

    public Account(Information info, ArrayList<Character> chars, int nr) {
        this.information = info;
        this.characters = chars;
        this.nrgames = nr;
    }

    public Information getInformation(){
        return this.information;
    }

    public ArrayList<Character> getCharacters(){
        return this.characters;
    }

    public static class Information {
        private Credentials credentials;
        private SortedSet<String> favoriteGames;
        private String name, country;

        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.favoriteGames = builder.favoriteGames;
            this.name = builder.name;
            this.country = builder.country;
        }

        public static class Builder {
            private Credentials credentials;
            private SortedSet<String> favoriteGames;
            private String name;
            private String country;

            public Builder withCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public Builder withFavoriteGames(SortedSet<String> favoriteGames) {
                this.favoriteGames = favoriteGames;
                return this;
            }

            public Builder withName(String name) {
                this.name = name;
                return this;
            }

            public Builder withCountry(String country) {
                this.country = country;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }

        public Credentials getCredentials() {
            return this.credentials;
        }

        public SortedSet<String> getFavoriteGames() {
            return this.favoriteGames;
        }

        public String getName() {
            return this.name;
        }

        public String getCountry() {
            return this.country;
        }

        public void addFavoriteGame(String game) {
            favoriteGames.add(game);
        }

        public void removeFavoriteGame(String game) {
            favoriteGames.remove(game);
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String toString() {
            return "name: " + this.name + "; Country: " + this.country + "; Favorite Games: " + this.favoriteGames;
        }

    }
}
