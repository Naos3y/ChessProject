package rmi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javax.swing.*;

public class ChessGUI extends JFrame {

    public ClientInterface ci;
    public Client cliente;

    private SquarePanel[][] board = new SquarePanel[8][8];

    private JPanel chessPanel = new JPanel();
    private JPanel painelPrincipal = new JPanel();
    private JPanel painelEsquerda = new JPanel();
    private JPanel painelInferior = new JPanel();
    private JPanel painelSuperior = new JPanel();
    private JPanel painelDireita = new JPanel();

    private JButton quit = new JButton("Quit");

    /* INPUTS PARA CONEXAO*/
    JTextField userName = new JTextField();
    JTextField serverIP = new JTextField();
    JTextField serverPort = new JTextField();
    JButton startCon = new JButton("Join");
    JButton stopCon = new JButton("Leave");

    /* CHAT*/
    JPanel chatInputs = new JPanel();
    JTextField inputChat = new JTextField();
    JButton sendMessage = new JButton("send");
    JTextArea textArea = new JTextArea(10, 30);
    JScrollPane chat = new JScrollPane(textArea);

    /* PEDIR PARA SER EXPETADOR */
 /* PEDIR PARA SER JOGADOR */

 /* PECAS DE FORA */
 /* Interface */
    ChessInterface chess;

    public ChessGUI(ClientInterface ci, Client cliente) {

        atualizaChat thread = new atualizaChat(cliente);
        thread.start();

        this.ci = ci;

        setSize(100, 400);
        setLayout(new BorderLayout());

        chessPanel.setLayout(new GridLayout(8, 8));

        SquarePanel.loadPieceImages();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                SquarePanel sqPanel = new SquarePanel(i, j, this);
                sqPanel.setBackColor((i + j) % 2);
                board[i][j] = sqPanel;
                chessPanel.add(sqPanel);

            }
        }

        board[7][0].setPiece(0, 3);
        board[7][1].setPiece(0, 1);
        board[7][2].setPiece(0, 2);
        board[7][3].setPiece(0, 4);
        board[7][4].setPiece(0, 5);
        board[6][0].setPiece(0, 0);

        board[0][0].setPiece(1, 3);
        board[0][1].setPiece(1, 1);
        board[0][2].setPiece(1, 2);
        board[0][3].setPiece(1, 4);
        board[0][4].setPiece(1, 5);
        board[1][0].setPiece(1, 0);

        //board[7][3].removePiece();
        //board[0][0].setBackColor(1);
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.add(chessPanel, BorderLayout.CENTER);

        /* Painel do Direita*/
        painelDireita.setLayout(new GridLayout(2, 1));
        painelDireita.setBackground(Color.red);
        inputChat.setPreferredSize(new Dimension(200, 25));

        chatInputs.add(inputChat);
        chatInputs.add(sendMessage);
        painelDireita.add(chat);
        painelDireita.add(chatInputs);

        painelPrincipal.add(painelDireita, BorderLayout.EAST);

        /* Painel das Esquerda*/
        painelEsquerda.setBackground(Color.pink);
        painelPrincipal.add(painelEsquerda, BorderLayout.WEST);

        /* Painel do Inferior*/
        painelInferior.setBackground(Color.blue);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        /* Painel Superior*/
        painelSuperior.setBackground(Color.green);

        userName.setPreferredSize(new Dimension(200, 25));
        serverIP.setPreferredSize(new Dimension(200, 25));
        serverPort.setPreferredSize(new Dimension(200, 25));

        painelSuperior.add(quit);
        painelSuperior.add(userName);
        painelSuperior.add(serverIP);
        painelSuperior.add(serverPort);
        painelSuperior.add(startCon);
        painelSuperior.add(stopCon);
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);

        add(painelPrincipal, BorderLayout.CENTER);

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Confirmar saída", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        startCon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String ip = serverIP.getText();
                    Registry reg = LocateRegistry.getRegistry(ip, Integer.parseInt(serverPort.getText()));
                    chess = (ChessInterface) reg.lookup("Server");
                    chess.login(ci);
                    System.out.println("OK");
                } catch (Exception eLogin) {
                    System.out.println(eLogin);
                }
            }

        });

        stopCon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    chess.logout(ci);
                } catch (Exception eLogout) {
                    System.out.println(eLogout);
                }
            }
        });

        sendMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    chess.sendMessage(inputChat.getText());
                } catch (Exception eLogout) {
                    System.out.println(eLogout);
                }
            }
        });

    }

    public void selected(int x, int y) {
        System.out.printf("mouse pressed at: %d - %d\n", x, y);
    }

    public class atualizaChat extends Thread {

        private Client cliente;

        public atualizaChat(Client cliente) {
            this.cliente = cliente;
        }

        public void run() {
            while (true) {
                if (this.cliente.getNova()) {
                    String mensagem = this.cliente.getMensagens();
                    textArea.append(mensagem);
                }
            }

        }
    }
}
