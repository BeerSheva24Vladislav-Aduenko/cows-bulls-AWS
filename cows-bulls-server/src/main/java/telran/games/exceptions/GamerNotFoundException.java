package telran.games.exceptions;

import java.util.NoSuchElementException;

public class GamerNotFoundException extends NoSuchElementException{
    public GamerNotFoundException(String username) {
        super(String.format("Gamer with name %d not found", username));
    }
}
