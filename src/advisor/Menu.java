package advisor;

import java.util.Scanner;

/**
 * @author Fulkin
 * Created on 25.01.2022
 */

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static boolean isConnect = false;
    private static ConnectServer connectServer;

    private Menu() {
    }

    public static void start(String[] args) {
        connectServer = ConnectServer.getInstance();
        getResources(args);
        while (isConnect) {
            String[] s = getLine();
            if ("exit".equals(s[0])) {
                isConnect = false;
                break;
            }
            String result = connectServer.takeAdvice(s);
            System.out.print(result);
        }
        System.out.println("---GOODBYE!---");
        System.exit(0);
    }

    private static void getResources(String[] args) {
        String accessServer = "";
        String resourceServer = "";
        String page ="";
        if (args.length > 1) {
            for (int i = 0; i < args.length; i++) {
                if ("-access".equals(args[i])) {
                    accessServer = args[i + 1];
                }
                if ("-resource".equals(args[i])) {
                    resourceServer = args[i + 1];
                }
                if ("-page".equals(args[i])) {
                    page = args[i + 1];
                }
            }
        }
        connectApp(accessServer);
        if (isConnect) {
            connectServer.setResourceServer(resourceServer);
            connectServer.setPage(page);
        }
    }

    private static void connectApp(String accessServer) {
        while (true) {
            String s = getLine()[0];
            if ("auth".equals(s) || "exit".equals(s)) {
                if ("auth".equals(s)) {
                    connectServer.getAccess(accessServer);
                    isConnect = true;
                }
                break;
            } else {
                System.out.println("Please, provide access for application.");
            }
        }
    }

    private static String[] getLine() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sc.nextLine().split(" ");
    }
}
