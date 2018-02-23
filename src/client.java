import java.net.*;
import java.io.*;

public class client {
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket clientSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {
            String userInput; String result;
            result = in.readLine();
            System.out.println(result);
            while ((userInput = stdIn.readLine()) != null) {
                userInput = userInput.toLowerCase();
                out.println(userInput);
                //System.out.println("Me: " + userInput);
                result = in.readLine();


                if(result.equals("-5")){
                    stdIn.close();
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                }
                //error code stuff
                switch (result){
                    case "-1":
                        result = "incorrect operation command.";
                        break;
                    case "-2":
                        result = "number of inputs is less than two.";
                        break;
                    case "-3":
                        result = "number of inputs is more than four.";
                        break;
                    case "-4":
                        result = "one or more of the inputs contain(s) non-number(s).";
                        break;
                    case "-5":
                        result = "exit.";
                        break;
                }
                System.out.println("receive: " + result);
            }
            System.out.println("exit");

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
