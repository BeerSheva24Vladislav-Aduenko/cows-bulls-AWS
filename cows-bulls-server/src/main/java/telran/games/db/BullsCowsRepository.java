package telran.games.db;

import java.time.LocalDate;
import java.util.List;

import telran.games.MoveResult;
public interface BullsCowsRepository {
    public boolean isGamerExists(String username);

    public void createGamer(String username, LocalDate birthdate);

    public long createGame(String sequence);

    public List<Long> findJoinebleGames(String username);

    public void joinToGame(String username, long gameId);

    public List<Long> findStartebleGames(String username);

    public void startGame(String username, long gameId);

    public List<Long> findPlaybleGames(String username);

    public void makeMove(String username, long gameId, String sequence, int bulls, int cows);

    public String findWinnerGame(long gameId);

    public void setWinnerAndFinishGame(String username, long gameId, String sequence, int bulls, int cows);

    public List<MoveResult> findAllMovesGameGamer(String username, long gameId);

    public String findSequence(long gameId);
}