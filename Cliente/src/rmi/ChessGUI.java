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
    public String myName;
    public boolean JOGADOR = false;

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
    JButton pedidoDeJogo = new JButton("Request to Play");

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
        /* 
        Estado dos botoes
         */
        userName.setEnabled(true);
        serverIP.setEnabled(true);
        serverPort.setEnabled(true);
        startCon.setEnabled(true);
        sendMessage.setEnabled(false);
        textArea.setEnabled(false);
        stopCon.setEnabled(false);

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
        painelSuperior.add(pedidoDeJogo);
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);

        add(painelPrincipal, BorderLayout.CENTER);

        startCon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {

                    if (userName.getText().length() != 0 && serverIP.getText().length() != 0 && serverPort.getText().length() != 0) {
                        String ip = serverIP.getText();
                        int porto;

                        try {
                            porto = stringParaInt(serverPort.getText());

                        } catch (Exception ePorto) {

                            JOptionPane.showMessageDialog(null, "O porto está incorreto!", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        try {
                            Registry reg = LocateRegistry.getRegistry(ip, porto);
                            chess = (ChessInterface) reg.lookup("Server");
                            chess.login(ci);

                            JOGADOR = chess.souJogador(ci);

                        } catch (Exception eReg) {
                            JOptionPane.showMessageDialog(null, "O endereço IP está incorreto!", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        /* Bloqueia os botoes Join & inputs */
                        userName.setEnabled(false);
                        serverIP.setEnabled(false);
                        serverPort.setEnabled(false);
                        startCon.setEnabled(false);
                        if (JOGADOR) {
                            sendMessage.setEnabled(true);
                        }
                        textArea.setEnabled(true);
                        stopCon.setEnabled(true);
                        /* -------------------------- */

                        myName = userName.getText();
                    } else {
                        JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception eLogin) {
                    System.out.println(eLogin);
                    JOptionPane.showMessageDialog(null, "O endereço IP e porto estão incorretos!", "VAMOS!", JOptionPane.ERROR_MESSAGE);

                }
            }

        }
        );

        stopCon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    chess.logout(ci);

                    /* Bloqueia os botoes Leave*/
                    userName.setEnabled(true);
                    serverIP.setEnabled(true);
                    serverPort.setEnabled(true);
                    startCon.setEnabled(true);
                    sendMessage.setEnabled(false);
                    textArea.setEnabled(false);
                    stopCon.setEnabled(false);

                } catch (Exception eLogout) {
                    System.out.println(eLogout);
                }
            }
        });

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Confirmar saída", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        sendMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    chess.sendMessage(myName + ": " + inputChat.getText());
                } catch (Exception eLogout) {
                    System.out.println(eLogout);
                }
            }
        });

        pedidoDeJogo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    chess.expetadorParaJogador(ci);
                    try {
                        JOGADOR = chess.souJogador(ci);
                    } catch (Exception e1) {
                        System.out.println(e1);
                    }
                    if (JOGADOR) {
                        System.out.println("OK");
                        sendMessage.setEnabled(true);
                    }
                } catch (Exception eExparaJogador) {
                    System.out.println(eExparaJogador);
                }
            }
        }
        );

    }

    public void selected(int x, int y) {
        System.out.printf("mouse pressed at: %d - %d\n", x, y);
    }

    private int stringParaInt(String aMensagem) {

        return Integer.parseInt(aMensagem);
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
