package telran.games.exceptions;

import java.util.NoSuchElementException;

public class GameNotFoundException extends NoSuchElementException{
    public GameNotFoundException(long gameId) {
        super(String.format("Game with id %d not found", gameId));
    }
}
