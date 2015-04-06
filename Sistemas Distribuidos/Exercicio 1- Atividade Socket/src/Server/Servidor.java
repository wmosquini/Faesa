package Server;

import java.io.*;
import java.net.*;
public class Servidor{
    ServerSocket servidorSocket;
    Socket conexao = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    
    Servidor(){}
    
    void run()
    {
        try{
            //1. criando o server socket
        	servidorSocket = new ServerSocket(2004, 10);
            //2. Aguardando conexão
            System.out.println("Aguardando Conexão");
            conexao = servidorSocket.accept();
            System.out.println("Conexão recebida de " + conexao.getInetAddress().getHostName());
            //3. Obtém Input and Output streams
            out = new ObjectOutputStream(conexao.getOutputStream());
            out.flush();
            in = new ObjectInputStream(conexao.getInputStream());
            sendMessage("Conexão Sucedida");
            //4. As duas partes se comunicam via the input and output streams
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("Cliente>" + message);
                    if (message.equals("tchau"))
                        sendMessage("tchau");
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Dados recebidos em um formato desconhecido");
                }
            }while(!message.equals("tchau"));
        }
        catch(IOException ioException){
        	System.out.println(ioException.getMessage());
            ioException.printStackTrace();
        }
        finally{
            //4: Encerra Conexão
            try{
                in.close();
                out.close();
                servidorSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("Servidor>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
        Servidor server = new Servidor();
        while(true){
            server.run();
        }
    }
}