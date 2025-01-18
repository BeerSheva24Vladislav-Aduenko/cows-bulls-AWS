package telran.games.entities;
import java.time.LocalDateTime;

import jakarta.persistence.*;
@Entity
@Table(name="game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name="date_time")
    LocalDateTime dateTime;
    @Column(name="is_finished")
    boolean isFinished;
    String sequence;  

    @Override
    public String toString() {
        return "Game [id=" + id + ", dateTime=" + dateTime + ", isFinished=" + isFinished + ", sequence=" + sequence
                + "]";
    }

    public void setGameIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Game(String sequence) {
        this.sequence = sequence;
    }
    
    public long getId() {
        return id;
    }

    public void setStartGame(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Game(String sequence, boolean isFinished) {
        this.sequence = sequence;
        this.isFinished = isFinished;
    }

    public String getSequence() {
        return sequence;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Game() {}
}