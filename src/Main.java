public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify whether you want to run the server or the client.");
            return;
        }

        if (args[0].equals("server")) {
            Server.Main.main(args);
        } else if (args[0].equals("client")) {
            Client.Main.main(args);
        } else {
            System.out.println("Invalid argument. Please specify whether you want to run the server or the client.");
        }
    }
}
