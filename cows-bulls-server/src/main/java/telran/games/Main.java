package telran.games;

import telran.games.db.config.BullsCowsPersistenceUnitInfo;
import telran.net.TcpServer;
import telran.view.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.hibernate.jpa.HibernatePersistenceProvider;

import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceUnitInfo;

public class Main {
    static InputOutput io = new StandardInputOutput();
    static EntityManager em;
    

    public static void main(String[] args) {
        createEntityManager();
    }

    private static void createEntityManager() {
        HashMap<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        PersistenceUnitInfo persistenceUnit = new BullsCowsPersistenceUnitInfo();
        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        EntityManagerFactory emf = hibernatePersistenceProvider.createContainerEntityManagerFactory(persistenceUnit,
                hibernateProperties);
        em = emf.createEntityManager();
    }

    TcpServer tcpServer = new TcpServer(new CompanyProtocol(company), PORT);
    new Thread(tcpServer).start();
    Scanner scanner = new Scanner(System.in);
    while (true) {
        System.out.println("enter shutdown for stopping server");
        String line = scanner.nextLine();
        if (line.equals(
                "shutdown")) {
            tcpServer.shutdown();
            break;
        }
    }
}