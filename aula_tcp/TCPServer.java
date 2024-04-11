package aula_tcp;

/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao
 */
import java.net.*;
import java.util.Scanner;
import java.io.*;

public class TCPServer {

    public static void main(String args[]) {
        try {
            int serverPort = 6666; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            serverThread c = new serverThread(clientSocket);
                while (true) {
                    System.out.println("Servidor aguardando conexao ...");

                    /* aguarda conexoes */
                    Socket clientSocket = listenSocket.accept();

                    System.out.println("Cliente conectado ... Criando thread ...");

                    /* cria um thread para atender a conexao */
                    serverThread c = new serverThread(clientSocket);

                    /* inicializa a thread */
                    c.start();
                } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class

/**
 * Classe ClientThread: Thread responsavel pela comunicacao
 * Descricao: Rebebe um socket, cria os objetos de leitura e escrita,
 * aguarda msgs clientes e responde com a msg + :OK
 */
class ClientThread extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection:" + ioe.getMessage());
        } //catch
    } //construtor

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        try {
            String buffer = "";
            while (true) {
                buffer = in.readUTF();   /* aguarda o envio de dados */

                System.out.println("Cliente disse: " + buffer);

                if (buffer.equals("PARAR")) break;

                buffer += ":OK";
                out.writeUTF(buffer);
            }
        } catch (EOFException eofe) {
            System.out.println("EOF: " + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException ioe) {
                System.err.println("IOE: " + ioe);
            }
        }
        System.out.println("Thread comunicação cliente finalizada.");
    } //run
} //class

/*Configurando a thread do servidor*/
class serverThread extends Thread {

    DataInputStream in;
    Scanner reader = new Scanner(System.in); // ler mensagens via teclado
    DataOutputStream out;
    Socket listenSocket;
    
    public serverThread(Socket listenSocket) {
        try {
            this.listenSocket = listenSocket;
            in = new DataInputStream(listenSocket.getInputStream());
            out = new DataOutputStream(listenSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection:" + ioe.getMessage());
        } //catch
    } //construtor

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        try {
            String buffer = "";
            while (true) {
                System.out.print("Mensagem: ");
                buffer = reader.nextLine(); // lê mensagem via teclado
            
                out.writeUTF(buffer);      	// envia a mensagem para o servidor
    
                if (buffer.equals("PARAR")) break;
                
                buffer = in.readUTF();      // aguarda resposta do servidor
                System.out.println("Server disse: " + buffer);
            }
        } catch (EOFException eofe) {
            System.out.println("EOF: " + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                listenSocket.close();
            } catch (IOException ioe) {
                System.err.println("IOE: " + ioe);
            }
        }
        System.out.println("Thread comunicação cliente finalizada.");
    } //run
} //class