package telran.games.exceptions;

public class GameAlreadyFinishedException extends IllegalStateException {
    public GameAlreadyFinishedException(long gameId, String winner) {
        super(" Game with id " + gameId + " already finished. Winner is " + winner);
    }
}
