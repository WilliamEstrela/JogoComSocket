package servidor;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread {

    private static ArrayList<BufferedWriter> clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    /**
     * Método construtor
     */
    public Servidor(Socket con) {
        this.con = con;
        try {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private synchronized void addCliente(BufferedWriter bfw) {
        clientes.add(bfw);
    }
    private synchronized void removeCliente(BufferedWriter bfw) {
        clientes.remove(bfw);
    }
    /**
     * Método run
     */
    public void run() {

        try {

            String msg;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            //chama metodo com modificador synchronized para garantir acesso exclusivo
            this.addCliente(bfw);
            nome = msg = bfr.readLine();

            while (!"Sair".equalsIgnoreCase(msg) && msg != null) {
                msg = bfr.readLine();
                sendToAll(bfw, msg);
                System.out.println(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /***
     * Metodo usado para enviar mensagem para todos os clients
     *
     * @param bwSaida
     *            do tipo BufferedWriter
     * @param msg
     *            do tipo String
     * @throws IOException
     */
    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
        BufferedWriter bwS, bwP = null;

        for (BufferedWriter bw : clientes) {
            bwS =  bw;
            if (!(bwSaida == bwS)) {
                try{
                    bw.write(nome + " -> " + msg + "\r\n");
                    bw.flush();
                }catch(Exception e){
                    bwP=bwS;
                }
            }
        }
        if(bwP !=null){
            this.removeCliente(bwP);
        }
    }

    /***
     * Meodo main
     *
     * @param args
     */
    public static void main(String[] args) {

        try {

            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("12345");

            Object[] texts = { lblMessage, txtPorta };
            JOptionPane.showMessageDialog(null, texts);

            Integer porta = Integer.parseInt(txtPorta.getText());
            server = new ServerSocket(porta);

            clientes = new ArrayList<BufferedWriter>();

            JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText());

            while (true) {
                System.out.println("Aguardando conexao...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new Servidor(con);
                t.start();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }// Fim do metodo main
} // Fim da classe

