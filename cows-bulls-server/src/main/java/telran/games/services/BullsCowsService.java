package telran.games.services;

import java.time.LocalDate;
import java.util.List;
import telran.games.MoveResult;

public interface BullsCowsService {
    public void register(String username, LocalDate birthdate);

    public void login(String username);

    public long createGame();

    public List<Long> getListJoinebleGames(String username);

    public void joinToGame(String username, long gameId);

    public List<Long> getListStartebleGames(String username);

    public void startGame(String username, long gameId);
    
    public List<Long> getListPlaybleGames(String username);

    public List<MoveResult> makeMove(String username, long gameId, String sequence);
}