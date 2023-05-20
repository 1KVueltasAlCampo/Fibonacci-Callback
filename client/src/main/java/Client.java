import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

import Demo.CallbackPrx;
import Demo.PrinterPrx;

public class Client {
    static com.zeroc.Ice.Communicator communicator;

    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();
        communicator = com.zeroc.Ice.Util.initialize(args, "client.cfg", extraArgs);
        Demo.PrinterPrx server = serverConfiguration();
        Demo.CallbackPrx client = clientConfiguration();
        if (server == null || client == null)
            throw new Error("Invalid proxy");

        int argsN = args.length;
        if (argsN > 0) {
            String text = args[0];
            System.out.println("Sending: " + text);
            String result = runProgram(server, client, text);
            argsN=0;
        } else {
            runProgram(server, client);
        }

        communicator.shutdown();
        communicator.destroy();
    }

    public static void runProgram(PrinterPrx server, CallbackPrx client) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            boolean exitFlag = false;
            while (!exitFlag) {
                System.out.println("What do you want to do?  \n 1. Calculate fibonacci (enter a number) \n 2. List available hosts (list clients) \n 3. Send a message to everyone (bc <<msg>>) \n 4. Send a message to someone (to <<host:msg>>) \n 5. exit ");
                String message = br.readLine();

                if (message.equalsIgnoreCase("exit")) {
                    exitFlag = true;
                } else {
                    server.printString(message,hostname, client);
                    String result = client.waitForResult(); // Esperar el resultado del servidor
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void showTime(long elapsed) {
        long elapsedMillis = TimeUnit.MILLISECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
        long elapsedSecs = TimeUnit.SECONDS.convert(elapsed, TimeUnit.NANOSECONDS);
        System.out.println("Time: " + (elapsedMillis) + " ms, " + (elapsedSecs) + " s");
    }

    public static Demo.CallbackPrx clientConfiguration() {
        ObjectAdapter adapter = communicator.createObjectAdapter("Callback.Client");
        com.zeroc.Ice.Object obj = new CallbackI();
        ObjectPrx objectPrx = adapter.add(obj, Util.stringToIdentity("callback"));
        adapter.activate();
        return Demo.CallbackPrx.uncheckedCast(objectPrx);
    }

    public static Demo.PrinterPrx serverConfiguration() {
        Demo.PrinterPrx twoway = Demo.PrinterPrx
                .checkedCast(communicator.propertyToProxy("Printer.Proxy")).ice_twoway().ice_secure(false);
        return twoway.ice_twoway();
    }


    public static String runProgram(PrinterPrx server, CallbackPrx client, String message) {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            server.printString(message,hostname, client);
            return client.waitForResult();
            
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
