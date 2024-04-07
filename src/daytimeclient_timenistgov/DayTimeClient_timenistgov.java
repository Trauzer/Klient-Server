package daytimeclient_timenistgov;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DayTimeClient_timenistgov {

    public static void main(String[] args) {
        String hostname = "time.nist.gov";
        try
        {
            Socket s = new Socket(hostname,13);
            InputStream timeStream = s.getInputStream();
            StringBuffer time = new StringBuffer();
            int c;
            while((c = timeStream.read())!=-1)
                time.append((char) c);
            String timeString = time.toString().trim();
            System.out.println("Jest "+timeString+" na "+hostname);
            timeStream.close();
            s.close();
        }
        catch (UnknownHostException e) { System.out.println("Nieznany host"); }
        catch (IOException e) { System.out.println("Błąd"); }
    }
}