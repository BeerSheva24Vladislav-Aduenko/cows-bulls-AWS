package telran.games.exceptions;

public class GamerAlreadyExistsException extends IllegalStateException {
    public GamerAlreadyExistsException(String username) {
        super(String.format("Gamer with name %s already exists", username));
    }
}
