import javax.sound.midi.Soundbank;
import java.net.*;
import java.io.*;

public class server extends Thread{

    private static boolean listening = true;
    private static int clientCount = 0;
    private static String inputLine = null;
    private static String outputLine = null;

    private static final int incorrectOperationCommand = -1;
    private static final int lessThanTwoInputs = -2;
    private static final int moreThanFourInputs = -3;
    private static final int invalidInputs = -4;
    private static final int exitCode = -5;

    private static int doMath(String inputPrompt){

        int result = 0;
        String input[] = inputPrompt.split(" ");
        if(!inputPrompt.contains("add") && !inputPrompt.contains("subtract") && !inputPrompt.contains("multiply")){
            //System.out.println("Incorrect operation command!");
            return incorrectOperationCommand;
        }
        if(input.length < 3){
            //System.out.println("Less than two inputs given");
            return lessThanTwoInputs;
        }
        if(input.length > 5){
            //System.out.println("More than 4 inputs given");
            return moreThanFourInputs;
        }

        int[] numbers = new int[input.length];
        for(int i = 1; i < input.length; i++) {
            try{
                numbers[i] = Integer.parseInt(input[i]);
                //System.out.println("Numbers[" + i + "] = " + numbers[i]);
            }
            catch(Exception e){
                //System.out.println("NumberFormatException " + input[i] + " is not a valid number.");
                return invalidInputs;
            }
        }

        switch(input[0]){
            case "add":
                //System.out.println("Add");
                for(int i = 1; i < numbers.length; i++){
                    //System.out.println("Numbers[" + i + "] = " + numbers[i]);
                    result += numbers[i];
                }
                break;
            case "subtract":
                result = numbers[1];
                //System.out.println("Sub");
                for(int i = 2; i < numbers.length; i++){
                    //System.out.println("Numbers[" + i + "] = " + numbers[i]);
                    result -= numbers[i];
                }
                break;
            case "multiply":
                result = 1;
                //System.out.println("Mult");
                for(int i = 1; i < numbers.length; i++){
                    //System.out.println("Numbers[" + i + "] = " + numbers[i]);
                    result *= numbers[i];
                }
                break;
        }
        return result;
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java server <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(portNumber)){

            while (listening) {
                //System.out.println("Accepting Newbies. Currently have had " + clientCount + " connect to me.");
                Socket clientSocket = serverSocket.accept();
                //System.out.println("We hooked one!");
                clientCount++;

                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("get connection from " + clientSocket.getInetAddress().getHostName());
                out.println("Hello!");
                while ((inputLine = in.readLine()) != null) {
                    inputLine = inputLine.toLowerCase();
                    System.out.print("get: " + inputLine);
                    if(inputLine.equals("bye")){
                        out.println(exitCode);
                        listening = true;
                        in.close();
                        out.close();
                        clientSocket.close();
                        break;
                    }
                    else if(inputLine.equals("terminate")){
                        out.println(exitCode);
                        listening = false;
                        in.close();
                        out.close();
                        clientSocket.close();
                        break;
                    }

                    outputLine = Integer.toString(doMath(inputLine));
                    System.out.println(", return " + outputLine);
                    out.println(outputLine);
                }
                //System.out.println("Cya later dude!");
            }
            System.out.println(", return -5");
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}