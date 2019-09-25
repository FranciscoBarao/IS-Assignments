// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server
{
    //initialize socket and input stream
    private Socket		 socket = null;
    private ServerSocket server = null;
    private DataInputStream in	 = null;

    private ArrayList<Owner> owners;
    // constructor with port
    public Server(int port)
    {
        try
        {
            //Server Connection
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));



            //Read Owner/car from "dAtAbAsE"
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("database.txt"));
                owners = (ArrayList<Owner>) ois.readObject();
            }catch(Exception e){
                System.out.println(e);
            }

            // Wait for client answer with IDs to search
            //Message from client serialized already?

            //Deserialize XML -> find -> serialize -> send
            // ??? OR ???
            // String -> Find -> Serialize -> send


            //Assuming Server only receives a string with "ID|ID|ID|ID" for now
            String answer = "1|2|3";
            String[] ans = answer.split("|");
            ArrayList<Owner> correct_owners = findOwners(ans);
            //ObjectToXml.ToXml(correct_owners); -> Creates XML
            //Convert that XML to transfer? Binary or some shit
            //RemoteCallClient(Shit)


            String line = "";
            while (!line.equals("stop"))
            {
                try
                {
                    line = in.readUTF();
                    System.out.println(line);
                    //Implement Code mentioned Above HERE
                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");

            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        Server server = new Server(5000);
    }

    private void printOwners(){
        for(Owner o : owners){
            System.out.println(o.toString());
        }
    }

    private ArrayList<Owner> findOwners(String[] ans){
        //Function to Retrieve Correct Owners

        ArrayList<Owner> own = new ArrayList<>();
        for(String a : ans){
            for(Owner o : owners){
                //Same ID
                if(o.getId() == Integer.parseInt(a)){
                    own.add(o);
                    break;
                }
            }
        }
        return own;
    }
}
