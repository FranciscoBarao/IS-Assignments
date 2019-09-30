// A Java program for a Client
import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Client
{
    private Socket socket		     = null;
    private DataInputStream input    = null;
    private DataOutputStream out	 = null;

    public Client(String address, int port)
    {
        //Connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
            // takes input from terminal
            input = new DataInputStream(System.in);
            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }catch(IOException i)
        {
            System.out.println(i);
        }


        String line = "";
        //ArrayList<Integer> owners_id = new ArrayList<>();
        String output = "";
        while (!line.equals("stop")) {
            try {
                line = input.readLine();
                //owners_id.add(Integer.parseInt(line));
                output = output + "|";
            } catch (IOException i) {
                System.out.println(i);
            }
        }
            try
            {
                out.writeUTF(output);
                //out.writeUTF(owners_id);
            }
            catch(IOException i)
            {
                System.out.println(i);
            }

            //RemoteCallReceive with Shit
            //XMLToObject.ToXml(Shit)


        //Close Connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);
    }
}

