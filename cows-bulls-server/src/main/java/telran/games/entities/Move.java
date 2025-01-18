package telran.games.entities;

import jakarta.persistence.*;

@Table(name = "move")
@Entity
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int bulls;
    int cows;
    String sequence;
    @ManyToOne
    @JoinColumn(name = "game_gamer_id")
    GameGamer gameGamer;

    @Override
    public String toString() {
        return "Move [id=" + id + ", bulls=" + bulls + ", cows=" + cows + ", sequence=" + sequence + ", gameGamer="
                + gameGamer.id + "]";
    }

    public Move(GameGamer gameGamer, int bulls, int cows, String sequence) {
        this.gameGamer = gameGamer;
        this.bulls = bulls;
        this.cows = cows;
        this.sequence = sequence;
    }

    public Move() {}
}