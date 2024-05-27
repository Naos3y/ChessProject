package rmi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javax.swing.*;

public class ChessGUI extends JFrame {

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
    public ChessInterface chess;
    public ClientInterface ci;

    /* PECAS */
    private static final int PAWN = 0;
    private static final int KNIGHT = 1;
    private static final int BISHOP = 2;
    private static final int ROOK = 3;
    private static final int QUEEN = 4;
    private static final int KING = 5;

    /* Auxiliar para a jogada */
    String peca;
    int[] inicio = new int[2];
    int[] fim = new int[2];
    boolean tabToFora;
    boolean foraToTab;
    boolean segundaClick;

    public ChessGUI(ClientInterface ci, Client cliente) {

        atualizaChat t1 = new atualizaChat(cliente);
        verificarJogador t2 = new verificarJogador();
        t1.start();
        t2.start();
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

//        board[0][0].setPiece(0, ROOK, "BR1");
        board[0][0] = null;
        board[0][1].setPiece(0, KNIGHT, "BH1");
        board[0][2].setPiece(0, BISHOP, "BB1");
        board[0][3].setPiece(0, QUEEN, "BQ");
        board[0][4].setPiece(0, KING, "BK");
        board[0][5].setPiece(0, BISHOP, "BB2");
        board[0][6].setPiece(0, KNIGHT, "BH2");
        board[0][7].setPiece(0, ROOK, "BR2");

        for (int i = 0; i < 8; i++) {
            board[1][i].setPiece(0, PAWN, "BP" + (i + 1));
        }

        // Set white pieces
        board[7][0].setPiece(1, ROOK, "WR1");
        board[7][1].setPiece(1, KNIGHT, "WH1");
        board[7][2].setPiece(1, BISHOP, "WB1");
        board[7][3].setPiece(1, KING, "WK");
        board[7][4].setPiece(1, QUEEN, "WQ");
        board[7][5].setPiece(1, BISHOP, "WB2");
        board[7][6].setPiece(1, KNIGHT, "WH2");
        board[7][7].setPiece(1, ROOK, "WR2");

        for (int i = 0; i < 8; i++) {
            board[6][i].setPiece(1, PAWN, "WP" + (i + 1));
        }

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
        pedidoDeJogo.setEnabled(false);

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
                            myName = userName.getText();
                            if (chess.verificaNome(myName)) {
                                chess.login(ci, myName);
                                JOGADOR = chess.souJogador(ci);

                                /* Bloqueia os botoes Join & inputs */
                                userName.setEnabled(false);
                                serverIP.setEnabled(false);
                                serverPort.setEnabled(false);
                                startCon.setEnabled(false);
                                pedidoDeJogo.setEnabled(true);

                                if (JOGADOR) {
                                    sendMessage.setEnabled(true);
                                }
                                textArea.setEnabled(true);
                                stopCon.setEnabled(true);
                                /* -------------------------- */
                            } else {
                                JOptionPane.showMessageDialog(null, "O nome já existe!", "Error!", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                        } catch (Exception eReg) {
                            JOptionPane.showMessageDialog(null, "O endereço IP está incorreto!", "Error!", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

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
                    pedidoDeJogo.setEnabled(false);

                } catch (Exception eLogout) {
                    System.out.println(eLogout + "AAA");
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
                } catch (Exception eExparaJogador) {
                    System.out.println(eExparaJogador);
                }
            }
        }
        );

    }

    public void selected(int x, int y, Piece p) {
        if (segundaClick) {
            fim[0] = x;
            fim[1] = y;
            System.out.println("mouse pressed at:" + x + " : " + y);
            try {
                System.out.println("" + inicio[0] + inicio[1] + fim[0] + fim[1]);
                chess.jogada(peca, inicio, fim, false, false);

            } catch (Exception e1) {

            } finally {
                segundaClick = !segundaClick;

            }
        } else {
            inicio[0] = x;
            inicio[1] = y;
            peca = p.getID();
            System.out.println("mouse pressed at:" + p.toString() + " at " + x + " : " + y);
            segundaClick = true;
        }

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

    public class verificarJogador extends Thread {

        public verificarJogador() {

        }

        public void run() {
            while (true) {
                try {
                    JOGADOR = chess.souJogador(ci);
                    System.out.println(ci);
                    if (JOGADOR) {
                        sendMessage.setEnabled(true);
                    }
                    Thread.sleep(5000);
                } catch (Exception e1) {
                }
            }

        }
    }

    public class atualizaTabuleiro extends Thread {

        public atualizaTabuleiro() {

        }

        public void run() {
            while (true) {
                try {

                } catch (Exception e1) {
                }
            }

        }
    }

}
