import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args) throws IOException {
        String hn = "unknown host address: ";

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            hn = hostname + ": ";
        } catch (UnknownHostException e) {
            System.out.print("unknown host address: ");
        }

        if (args.length > 0) {
            String text = args[0];
            String result = printString(text, hn);
            System.out.println("Result: " + result);
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            boolean exitFlag = false;

            while (!exitFlag) {
                System.out.println("Enter a number (type 'exit' to quit)");
                System.out.print(hn);
                String txt = br.readLine();

                if (txt.equalsIgnoreCase("exit")) {
                    exitFlag = true;
                } else {
                    String result = printString(txt, hn);
                    System.out.println("Result: " + result);
                }
            }
        }
    }

    private static String printString(String text, String hn) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(new String[0], "client.cfg")) {
            com.zeroc.Ice.ObjectPrx base = communicator.propertyToProxy("Service.Proxy");
            Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(base);

            if (printer == null) {
                throw new Error("Invalid proxy");
            }

            return printer.printString(text, hn);
        }
    }
}
