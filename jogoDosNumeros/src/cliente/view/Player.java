package cliente.view;

import javax.swing.*;

public class Player extends JFrame {

    private JTextField txtIP, txtPorta, txtNome;

    public Player(String nome){
        desenhaTela(nome);
    }

    /**
     * Metodo responsavel por iniciar a configuracao do jogo
     * @param nome Nome do player
     */
    private void desenhaTela(String nome) {
        JLabel lblMessage = new JLabel("Configuracao do jogo!");

        txtIP = new JTextField("127.0.0.1");
        txtPorta = new JTextField("12345");
        txtNome = new JTextField(nome);

        Object[] objects = {lblMessage, txtIP, txtPorta, txtNome};

        JOptionPane.showMessageDialog(null, objects);
    }
}
