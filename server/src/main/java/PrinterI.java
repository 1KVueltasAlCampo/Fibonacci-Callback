import java.math.BigInteger;

public class PrinterI implements Demo.Printer
{
    public void printString(String s,String hostname, com.zeroc.Ice.Current current)
    {
		if(s.matches("[0-9]+")){
			long num = Long.parseLong(s);
        System.out.print(hostname);
		fibonacci(num);
		}
		else{
			System.out.println(0);
		}

		System.out.println("");
    	
    }


    private void fibonacci(long posicion) {
        
        BigInteger siguiente = BigInteger.valueOf(1), actual = BigInteger.valueOf(0), temporal = BigInteger.valueOf(0);
		for (long x = 1; x <= posicion; x++) {
			temporal = actual;
			actual = siguiente;
			siguiente = siguiente.add(temporal);
			System.out.print((x == posicion)?actual.toString(0):(actual.toString(0)+","));
		}
		// Si no quieres imprimirla, comenta la siguiente línea:
		
		
	}
}