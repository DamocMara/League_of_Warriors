public class InvalidCommandException extends Exception {
    public InvalidCommandException(String message) {
        super(message); // Mesajul va fi transmis constructorului clasei de bază
    }
}
