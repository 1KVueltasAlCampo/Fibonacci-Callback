import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client
{
    public static void main(String[] args) throws IOException
    {
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"client.cfg"))
        {
            com.zeroc.Ice.ObjectPrx base = communicator.propertyToProxy("Service.Proxy");
            Demo.PrinterPrx printer = Demo.PrinterPrx.checkedCast(base);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            if(printer == null)
            {
                throw new Error("Invalid proxy");
            }
            String hn = "unknown host adress: ";

            try{
                InetAddress addr;
                addr = InetAddress.getLocalHost();
                String hostname = addr.getHostName();
                hn = hostname+": ";
            }catch(UnknownHostException e){
                System.out.print("unknown host adress: ");
            }

            Boolean exitFlag = false;

            

            while (!exitFlag){
                System.out.println("Enter a number");
                System.out.print(hn);
                String txt = br.readLine();

                if (txt.equalsIgnoreCase("exit")){
                    exitFlag = true;
                }else{
                    
                    printer.printString(txt,hn);
                }
            }
            
                
            
            

            
            
        }
    }
}