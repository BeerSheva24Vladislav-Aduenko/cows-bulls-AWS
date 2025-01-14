package telran.games;

import telran.games.services.BullsCowsServiceImpl;

import telran.net.*;

import java.util.Scanner;;

public class Main {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        BullsCowsServiceImpl service = new BullsCowsServiceImpl();
        Protocol protocol = new BullsCowsProtocol(service);
        TcpServer server = new TcpServer(protocol, PORT);
        Thread threadTcpServer = new Thread(server);
        threadTcpServer.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("To shutdown server input \"shutdown\":");
            String command = scanner.nextLine();
            if (command.equals("shutdown")) {
                server.shutdown();
                break;
            }
        }
        scanner.close();
    }
}