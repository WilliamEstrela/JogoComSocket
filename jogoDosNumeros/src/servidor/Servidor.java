package servidor;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread {

    private static int ptsJogador1 = 0, ptsJogador2 = 0;
    private static JogoServidor jogoServidor;
    private String nome;
    private int player;
    private static ArrayList<BufferedWriter> clientes;
    private static ServerSocket server;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    /**
     * método construtor, que recebe um objeto socket como parâmetro e cria um objeto do tipo BufferedReader,
     * que aponta para o stream do cliente socket.
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
     * Toda vez que um cliente novo chega ao servidor, esse método é acionado e alocado numa Thread
     * e também fica verificando se existe alguma mensagem nova.
     * Caso exista, esta será lida e o evento “sentToAll” será acionado para enviar a mensagem para os demais usuários conectados no chat.
     */
    public void run() {
        jogoServidor = new JogoServidor();

        try {

            String msg;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);

            //chama metodo com modificador synchronized para garantir acesso exclusivo
            this.addCliente(bfw);

            nome = msg = bfr.readLine();
            System.out.println("Nome jogador =" + nome);

            while (!"Sair".equalsIgnoreCase(msg) && msg != null) {

                msg = bfr.readLine();

                System.out.println("SIZE" + clientes.size());

                if (clientes.size() == 2){
                    System.out.println("Dois clientes conectados");
                    String msg1 = "jogook,";
                    msg1 += jogoServidor.getNumero1()+",";
                    msg1 += jogoServidor.getNumero2()+",";

                    System.out.println(msg1 +jogoServidor.getResultado());

                    if(Integer.parseInt(msg) == jogoServidor.getResultado()){

                        String ganhador = "reposta correta";

                        if(nome == "1"){
                            String nome = "jogador 1";
                            ganhador = nome;
                            ptsJogador1++;
                        }else{
                            if(nome == "2"){
                                String nome = " jogador 2";
                                ganhador = nome;
                                ptsJogador2++;
                            }
                        }

                        ganhador = " pontos=" +ptsJogador1;

                        sendToAll(bfw, ganhador);

                    }else{

                    }

                    sendToAll(bfw, msg1);
                }
                System.out.println("Mensagem: "+ msg + " clientes="+clientes.size());

                sendToAll(bfw, msg);

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }



    /***
     * Quando um cliente envia uma mensagem, o servidor recebe e manda esta para todos os outros clientes conectados.
     * Veja que para isso é necessário percorrer a lista de clientes e mandar uma cópia da mensagem para cada um.
     *
     * @param bwSaida do tipo BufferedWriter
     * @param msg do tipo String
     * @throws IOException
     */
    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
        BufferedWriter bwS, bwP = null;

        for (BufferedWriter bw : clientes) {
            bwS =  bw;

                try{
                    bw.write(nome + " -> " + msg + "\r\n");
                    bw.flush();
                }catch(Exception e){
                    bwP=bwS;
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

            byte[] b = InetAddress.getByName("localhost").getAddress();
            String ipMaquina = (b[0] + "." + b[1] + "." + b[2] + "." + b[3]);

            JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPorta.getText() + " IP maquina: " +ipMaquina);

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

