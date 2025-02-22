package telran.games.db;

import java.time.*;
import java.util.*;
import org.hibernate.jpa.HibernatePersistenceProvider;
import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceUnitInfo;
import telran.games.MoveResult;

import telran.games.db.config.BullsCowsPersistenceUnitInfo;
import telran.games.entities.Game;
import telran.games.entities.GameGamer;
import telran.games.entities.Gamer;
import telran.games.entities.Move;
import telran.games.exceptions.*;

public class BullsCowsRepositoryImpl implements BullsCowsRepository {
    EntityManager em;

    public BullsCowsRepositoryImpl() {
        PersistenceUnitInfo persistenceUnitInfo = new BullsCowsPersistenceUnitInfo();
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        HibernatePersistenceProvider provider = new HibernatePersistenceProvider();
        EntityManagerFactory emf = provider.createContainerEntityManagerFactory(persistenceUnitInfo, properties);
        em = emf.createEntityManager();
    }

    @Override
    public void createGamer(String username, LocalDate birthdate) {
        if (isGamerExists(username)) {
            throw new GamerAlreadyExistsException(username);
        }
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Gamer gamer = new Gamer(username, birthdate);
            em.persist(gamer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public boolean isGamerExists(String username) {
        Gamer gamer = em.find(Gamer.class, username);
        return gamer != null;
    }

    private Gamer getGamer(String username) {
        Gamer gamer = em.find(Gamer.class, username);
        if (gamer == null) {
            throw new GamerNotFoundException(username);
        }
        return gamer;
    }

    @Override
    public long createGame(String sequence) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = new Game(sequence);
            em.persist(game);
            transaction.commit();
            return game.getId();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Long> findJoinebleGames(String username) {
        TypedQuery<Long> query = em.createQuery(
                "select id from Game where dateTime is null except select game.id from GameGamer where gamer.username = ?1",
                Long.class);
        query.setParameter(1, username);
        return query.getResultList();
    }

    @Override
    public void joinToGame(String username, long gameId) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = getGame(gameId);
            Gamer gamer = getGamer(username);
            checkJoinebleGame(username, gameId);
            GameGamer gameGamer = new GameGamer(game, gamer);
            em.persist(gameGamer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

    }

    private Game getGame(long gameId) {
        Game game = em.find(Game.class, gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }
        return game;
    }

    @Override
    public List<Long> findStartebleGames(String username) {
        TypedQuery<Long> query = em.createQuery(
                "select game.id from GameGamer where gamer.username = ?1 and game.dateTime is null",
                Long.class);
        query.setParameter(1, username);
        return query.getResultList();
    }

    @Override
    public void startGame(String username, long gameId) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = getGame(gameId);
            LocalDateTime dateTime = LocalDateTime.now();
            checkStartebleGame(username, gameId);
            game.setStartGame(dateTime);
            em.persist(game);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void makeMove(String username, long gameId, String sequence, int bulls, int cows) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            checkGameFinished(gameId);
            GameGamer gameGamer = getGameGamer(username, gameId);
            Move move = new Move(gameGamer, bulls, cows, sequence);
            em.persist(move);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public String findWinnerGame(long gameId) {
        TypedQuery<String> query = em.createQuery(
                "SELECT gamer.username FROM GameGamer where game.id = ?1 and isWinner", String.class);
        query.setParameter(1, gameId);
        List<String> list = query.getResultList();
        return list.isEmpty() ? "" : list.get(0);
    }

    @Override
    public void setWinnerAndFinishGame(String username, long gameId, String sequence, int bulls, int cows) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = checkGameFinished(gameId);
            GameGamer gameGamer = getGameGamer(username, gameId);
            Move move = new Move(gameGamer, bulls, cows, sequence);
            em.persist(move);
            game.setGameIsFinished(true);
            em.persist(game);
            gameGamer.setWinner(true);
            em.persist(gameGamer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    private GameGamer getGameGamer(String username, long gameId) {
        TypedQuery<GameGamer> query = em.createQuery(
                "select gameGamer from GameGamer gameGamer where game.id = ?1 and gamer.username = ?2",
                GameGamer.class);
        query.setParameter(1, gameId);
        query.setParameter(2, username);
        List<GameGamer> res = query.getResultList();
        if (res.isEmpty()) {
            throw new GamerNotInGameException(username, gameId);
        }
        return res.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MoveResult> findAllMovesGameGamer(String username, long gameId) {
        Query query = em.createQuery(
                "select sequence, bulls, cows from Move where gameGamer.game.id = ?1 and gameGamer.gamer.username = ?2");
        query.setParameter(1, gameId);
        query.setParameter(2, username);
        List<Object[]> res = query.getResultList();
        return res.stream().map(arr -> new MoveResult((String) arr[0], (int) arr[1], (int) arr[2])).toList();
    }

    @Override
    public List<Long> findPlaybleGames(String username) {
        TypedQuery<Long> query = em.createQuery(
                "select game.id from GameGamer where gamer.username = ?1 and game.dateTime is not null and not game.isFinished",
                Long.class);
        query.setParameter(1, username);
        return query.getResultList();
    }

    private Game checkGameFinished(long gameId) {
        Game game = getGame(gameId);
        if (game.isFinished()) {
            throw new GameAlreadyFinishedException(gameId, findWinnerGame(gameId));
        }
        return game;
    }

    @Override
    public String findSequence(long gameId) {
        Game game = getGame(gameId);
        return game.getSequence();
    }

    private void checkJoinebleGame(String username, long gameId) {
        if (!findJoinebleGames(username).contains(gameId)) {
            throw new GamerCannotJoinToGameException(username, gameId);
        }
    }

    private void checkStartebleGame(String username, long gameId) { 
        if (!findStartebleGames(username).contains(gameId)) {
            throw new GamerCannotStartGameException(username, gameId);
        }  
    }
}
