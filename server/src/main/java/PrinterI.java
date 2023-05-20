import com.zeroc.Ice.Current;

import Demo.CallbackPrx;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PrinterI implements Demo.Printer {

    @Override
    public String printString(String message, String hostname, CallbackPrx proxy, Current current) {
        Handler handler = Handler.getInstance();
        Logic logic = new Logic(hostname+":"+message, proxy, Handler.getInstance());
        handler.addClient((m) -> m.split(":", 2)[0], message, proxy);
        Future<String> future = handler.executeTask(logic);
        try {
            return future.get(); // Espera y obtiene el resultado de la tarea
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "Error al ejecutar la tarea"; // Manejo de error
    }
}
