package telran.games.entities;
import java.time.LocalDate;

import jakarta.persistence.*;
@Entity
@Table(name="gamer")
public class Gamer {
    @Id
    String username;
    LocalDate birthdate;
    @Override
    public String toString() {
        return "Gamer [username=" + username + ", birthdate=" + birthdate + "]";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthdate = birthdate;
    }

    public Gamer (String username, LocalDate birthdate) {
        this.username = username;
        this.birthdate = birthdate;
    }
 
    public Gamer() {
        
    }
}
