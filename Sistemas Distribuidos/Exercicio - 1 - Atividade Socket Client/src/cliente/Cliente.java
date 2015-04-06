package cliente;

import java.io.*;
import java.net.*;
public class Cliente{
    Socket clienteSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    
    Cliente(){}
    
    void run()
    {
        try{
            //1. Cria o socket para conectar ao servidor
        	clienteSocket = new Socket("localhost", 2004);
            System.out.println("Conectado ao localhost na porta 2004");
            //2. Obtém Input and Output streams
            out = new ObjectOutputStream(clienteSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clienteSocket.getInputStream());
            //3: Comunicando com o servidor
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("Servidor>" + message);
                    //Envia ocabeçalho de resposta 
                    sendMessage("\nHTTP/1.x 200 OK\n"+"Content-type: text/html");
                    message = "tchau"; // para sair do loop                    
                    sendMessage(message);

                }
                catch(ClassNotFoundException classNot){
                    sendMessage("HTTP/1.1 404 Not Found\n"+"Content-type: text/html");                	
                }
            }while(!message.equals("tchau"));
        }
        catch(UnknownHostException unknownHost){
            System.err.println("Voce está tentando se conectar a um host desconhecido!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Encerra a conexão
            try{
                in.close();
                out.close();
                clienteSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    
    public void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("Cliente>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        Cliente client = new Cliente();
        client.run();
    }
}