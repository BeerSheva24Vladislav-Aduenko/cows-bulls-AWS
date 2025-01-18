package telran.games.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import telran.games.MoveResult;
import telran.games.db.*;
import telran.games.exceptions.*;

public class BullsCowsServiceImpl implements BullsCowsService {
    private static final long N_DIGITS = 4;
     BullsCowsRepository repo = new BullsCowsRepositoryImpl();
    String username = "";

    @Override
    public void register(String username, LocalDate birthdate) {
        repo.createGamer(username, birthdate);
    }

    @Override
    public void login(String username) {
        if (!repo.isGamerExists(username)) {
            throw new GamerNotFoundException(username);      
        }
    }

        @Override
    public long createGame() {
        return repo.createGame(generateSequence());
    }

    public String generateSequence() {
        return new Random().ints(0, 10).distinct().limit(N_DIGITS).boxed()
                .map(i -> i.toString()).collect(Collectors.joining());
    }

    @Override
    public List<Long> getListJoinebleGames(String username) {
        checkLogin(username);
        return repo.findJoinebleGames(username);
    }

    @Override
    public void joinToGame(String username, long gameId) {
        checkLogin(username);
        repo.joinToGame(username, gameId);
    }

    @Override
    public List<Long> getListStartebleGames(String username) {
        checkLogin(username);
        return repo.findStartebleGames(username);
    }

    @Override
    public void startGame(String username, long gameId) {
        checkLogin(username);
        repo.startGame(username, gameId);
    }

    @Override
    public List<Long> getListPlaybleGames(String username) {
        checkLogin(username);
        return repo.findPlaybleGames(username);
    }

    @Override
    public List<MoveResult> makeMove(String username, long gameId, String sequence) {
        checkLogin(username);
        String gameSequence = repo.findSequence(gameId);
        MoveResult res = calculateMove(sequence, gameSequence);
        if (res.bulls() == 4) {
            repo.setWinnerAndFinishGame(username, gameId, sequence, res.bulls(), res.cows());
        } else {
            repo.makeMove(username, gameId, sequence, res.bulls(), res.cows());
        }
        return repo.findAllMovesGameGamer(username, gameId);
    }

    public MoveResult calculateMove(String sequence, String gameSequence) {
        int bulls = 0;
        int cows = 0;
        System.out.println(gameSequence + "gameSequence");
        System.out.println(sequence);
        for (int i = 0; i < N_DIGITS; i++) {
            if (gameSequence.charAt(i) == sequence.charAt(i)) {
                bulls++;
            } else {
                if (gameSequence.indexOf(sequence.charAt(i)) > -1) {
                    cows++;
                }
            }
        }
        return new MoveResult(sequence, bulls, cows);
    }

    private void checkLogin(String username) { 
        if (username.equals("NoName")) {
            throw new GamerIsNotLoginException();
        }
    }
    
}
