package rmi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;

public class ChessGUI extends JFrame {

    public Client cliente;
    public String myName;
    public boolean JOGADOR = false;

    private SquarePanel[][] board = new SquarePanel[12][8];
    private SquarePanel[][] player1Square = new SquarePanel[1][1];
    private SquarePanel[][] player2Square = new SquarePanel[1][1];

//    private SquarePanelFora[][] fora = new SquarePanelFora[4][8];
    private JPanel chessPanel = new JPanel();
    private JPanel painelPrincipal = new JPanel();
    private JPanel painelEsquerda = new JPanel();
    private JPanel painelInferior = new JPanel();
    private JPanel painelSuperior = new JPanel();
    private JPanel painelDireita = new JPanel();

    /* Auxiliar Panels*/
    private JPanel userNameP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel serverIPP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel serverPortP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel messageP = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel chatInputs = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel logOutput = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel requests = new JPanel(new GridLayout(2, 1));
    private JPanel boardOptions = new JPanel(new GridLayout(2, 1));
    private JPanel userOptions = new JPanel(new GridLayout(1, 3));
    private JPanel player1 = new JPanel();
    private JPanel player2 = new JPanel();

    private JButton quit = new JButton("Quit");

    /* LABELS */
    JLabel usernameL = new JLabel("Username: ");
    JLabel serverIPL = new JLabel("IP: ");
    JLabel serverPortL = new JLabel("Port: ");
    JLabel messageL = new JLabel("Message: ");

    /* Nomes*/
    TextArea nomePlayer1 = new TextArea();
    TextArea nomePlayer2 = new TextArea();

    /* INPUTS PARA CONEXAO*/
    JTextField userName = new JTextField();
    JTextField serverIP = new JTextField();
    JTextField serverPort = new JTextField();
    JButton startCon = new JButton("Join");
    JButton stopCon = new JButton("Logout");

    /* CHAT*/
    JTextField inputChat = new JTextField();
    JButton sendMessage = new JButton("send");
    JTextArea textArea = new JTextArea(10, 30);
    JScrollPane chat = new JScrollPane(textArea);

    /* LOGS */
    JTextArea textAreaLogs = new JTextArea(10, 30);
    JScrollPane Logs = new JScrollPane(textAreaLogs);

    /* PEDIR PARA SER EXPETADOR */
    JButton pedidoDeJogo = new JButton("Request to Play");

    /* PEDIR PARA SER JOGADOR */
    JButton pedidoEspetador = new JButton("Leave the game");

    /* ARRUMAR & REMOVER PECAS*/
    JButton removePecas = new JButton("Clear Board");
    JButton arrumaPecas = new JButton("Reset Board");

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
    boolean segundaClickFora;

    public ChessGUI(ClientInterface ci, Client cliente) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        atualizaChat t1 = new atualizaChat(cliente);
        verificarJogador t2 = new verificarJogador();
        atualizaTabuleiro t3 = new atualizaTabuleiro(cliente);
        atualizaLogs t4 = new atualizaLogs(cliente);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        this.ci = ci;

        setSize(100, 400);
        setLayout(new BorderLayout());
        chessPanel.setLayout(new GridLayout(8, 8));
        chessPanel.setPreferredSize(new Dimension(550, 550));

        SquarePanel.loadPieceImages();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                SquarePanel sqPanel = new SquarePanel(i, j, this);
                sqPanel.setBackColor((i + j) % 2);
                board[i][j] = sqPanel;
                chessPanel.add(sqPanel);

            }
        }

        board[0][0].setPiece(1, ROOK, "BR1");
        board[0][1].setPiece(1, KNIGHT, "BH1");
        board[0][2].setPiece(1, BISHOP, "BB1");
        board[0][3].setPiece(1, QUEEN, "BQ");
        board[0][4].setPiece(1, KING, "BK");
        board[0][5].setPiece(1, BISHOP, "BB2");
        board[0][6].setPiece(1, KNIGHT, "BH2");
        board[0][7].setPiece(1, ROOK, "BR2");

        for (int i = 0; i < 8; i++) {
            board[1][i].setPiece(1, PAWN, "BP" + (i + 1));
        }

        board[7][0].setPiece(0, ROOK, "WR1");
        board[7][1].setPiece(0, KNIGHT, "WH1");
        board[7][2].setPiece(0, BISHOP, "WB1");
        board[7][3].setPiece(0, KING, "WK");
        board[7][4].setPiece(0, QUEEN, "WQ");
        board[7][5].setPiece(0, BISHOP, "WB2");
        board[7][6].setPiece(0, KNIGHT, "WH2");
        board[7][7].setPiece(0, ROOK, "WR2");

        for (int i = 0; i < 8; i++) {
            board[6][i].setPiece(0, PAWN, "WP" + (i + 1));
        }

        /*Indicador dos jogadores*/
        SquarePanel sqPanelPlayer1 = new SquarePanel(0, 0, this);
        SquarePanel sqPanelPlayer2 = new SquarePanel(0, 0, this);

        player1Square[0][0] = sqPanelPlayer1;
        player2Square[0][0] = sqPanelPlayer2;

        player1Square[0][0].setPiece(0, QUEEN, "WQ");
        player2Square[0][0].setPiece(1, QUEEN, "BQ");

        sqPanelPlayer1.setBackColor(0);
        player1.add(sqPanelPlayer1);
        player2.add(sqPanelPlayer2);
        /* Estado dos botoes
         */
        userName.setEnabled(true);
        serverIP.setEnabled(true);
        serverPort.setEnabled(true);
        startCon.setEnabled(true);
        sendMessage.setEnabled(false);
        textArea.setEnabled(false);
        stopCon.setEnabled(false);
        pedidoDeJogo.setEnabled(false);
        pedidoEspetador.setEnabled(false);
        removePecas.setEnabled(false);
        arrumaPecas.setEnabled(false);

        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.add(chessPanel, BorderLayout.CENTER);

        /* Painel do Direita*/
        painelDireita.setLayout(new BorderLayout());
        painelDireita.setBackground(Color.LIGHT_GRAY);
        inputChat.setPreferredSize(new Dimension(200, 25));

        chatInputs.add(inputChat);
        chatInputs.add(sendMessage);
        messageP.add(messageL);
        messageP.add(chatInputs);
        painelDireita.add(chat);
        painelDireita.add(messageP);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        painelDireita.add(chat, BorderLayout.CENTER);
        painelDireita.add(messageP, BorderLayout.SOUTH);
        painelPrincipal.add(painelDireita, BorderLayout.EAST);

        /* Painel das Esquerda*/
        painelEsquerda.setLayout(new FlowLayout());
        painelEsquerda.setBackground(Color.LIGHT_GRAY);
        painelEsquerda.setPreferredSize(new Dimension(200, 500));

        // Adicionando painéis do tabuleiro ao painel esquerdo
        for (int i = 8; i < 12; i++) {
            for (int j = 0; j < 8; j++) {
                SquarePanel sqPanel = new SquarePanel(i, j, this);
                sqPanel.setBackColor((i + j));
                board[i][j] = sqPanel;
                painelEsquerda.add(sqPanel);
            }
        }

        // Configurando text area de logs
        textAreaLogs.setLineWrap(true);
        textAreaLogs.setWrapStyleWord(true);
        textAreaLogs.setEditable(false);
        painelEsquerda.add(Logs);
        painelPrincipal.add(painelEsquerda, BorderLayout.WEST);

        /* Painel do Inferior*/
        painelInferior.setBackground(Color.LIGHT_GRAY);

        userOptions.add(player1);
        userOptions.add(nomePlayer1)
        userOptions.add(player2);
        userOptions.add(nomePlayer2);
        painelInferior.add(userOptions);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        /* Painel Superior*/
        painelSuperior.setBackground(Color.LIGHT_GRAY);

        userName.setPreferredSize(new Dimension(200, 25));
        serverIP.setPreferredSize(new Dimension(200, 25));
        serverPort.setPreferredSize(new Dimension(200, 25));

        // painelSuperior.add(quit);
        userNameP.add(usernameL);
        userNameP.add(userName);
        painelSuperior.add(userNameP);

        serverIPP.add(serverIPL);
        serverIPP.add(serverIP);
        painelSuperior.add(serverIPP);

        serverPortP.add(serverPortL);
        serverPortP.add(serverPort);
        painelSuperior.add(serverPortP);

        painelSuperior.add(startCon);
        painelSuperior.add(stopCon);
        requests.add(pedidoEspetador);
        requests.add(pedidoDeJogo);
        painelSuperior.add(requests);

        boardOptions.add(removePecas);
        boardOptions.add(arrumaPecas);

        painelSuperior.add(boardOptions);
//        painelSuperior.add(pedidoEspetador);
//        painelSuperior.add(pedidoDeJogo);
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
                                pedidoEspetador.setEnabled(false);

                                if (JOGADOR) {
                                    sendMessage.setEnabled(true);
                                    pedidoEspetador.setEnabled(true);
                                    removePecas.setEnabled(true);
                                    arrumaPecas.setEnabled(true);
                                    pedidoDeJogo.setEnabled(false);

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
                    pedidoEspetador.setEnabled(false);
                    pedidoEspetador.setEnabled(false);
                    removePecas.setEnabled(false);
                    arrumaPecas.setEnabled(false);

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

        arrumaPecas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    chess.resetBoardCliente();

                } catch (Exception eExparaJogador) {
                }
            }
        }
        );

        removePecas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    chess.clearBoardCliente();
                } catch (Exception eExparaJogador) {
                }
            }
        }
        );

    }

    public void selected(int x, int y, Piece p) {
        if (JOGADOR) {
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
                try {
                    inicio[0] = x;
                    inicio[1] = y;
                    peca = p.getID();
                    System.out.println("mouse pressed at:" + p.toString() + " at " + x + " : " + y);
                    segundaClick = true;
                } catch (Exception e2) {

                }
            }
        }

    }

    public char numberToLetter(int n) {
        switch (n) {
            case 0:
                return 'a';
            case 1:
                return 'b';

            case 2:
                return 'c';

            case 3:
                return 'd';

            case 4:
                return 'e';

            case 5:
                return 'f';

            case 6:
                return 'g';

            case 7:
                return 'h';

            default:
                return 'a';

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

    public class atualizaLogs extends Thread {

        private Client cliente;

        public atualizaLogs(Client cliente) {
            this.cliente = cliente;
        }

        public void run() {
            while (true) {
                if (this.cliente.getNewLog()) {
                    String mensagem = this.cliente.getLogs();
                    textAreaLogs.append(mensagem);
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
                        pedidoEspetador.setEnabled(true);
                        removePecas.setEnabled(true);
                        arrumaPecas.setEnabled(true);
                        pedidoDeJogo.setEnabled(false);

                    }
                    Thread.sleep(5000);
                } catch (Exception e1) {
                }
            }

        }
    }

    public class atualizaTabuleiro extends Thread {

        private Client cliente;
        int type;
        int apeca;
        int[] pecaFinal = new int[2];

        public atualizaTabuleiro(Client cliente) {
            this.cliente = cliente;

        }

        public int[] tipoPeca(char tipo, char peca) {
            int[] resultado = new int[2];

            if (Character.toUpperCase(tipo) == 'B') {
                resultado[0] = 1;
                switch (Character.toUpperCase(peca)) {
                    case 'R':
                        resultado[1] = 3;
                        break;
                    case 'H':
                        resultado[1] = 1;
                        break;
                    case 'B':
                        resultado[1] = 2;
                        break;

                    case 'Q':
                        resultado[1] = 4;
                        break;
                    case 'K':
                        resultado[1] = 5;
                        break;
                    case 'P':
                        resultado[1] = 0;
                        break;
                    default:
                        resultado[1] = -1;
                        break;
                }
            } else if (Character.toUpperCase(tipo) == 'W') {
                resultado[0] = 0;
                switch (Character.toUpperCase(peca)) {
                    case 'R':
                        resultado[1] = 3;
                        break;
                    case 'H':
                        resultado[1] = 1;
                        break;
                    case 'B':
                        resultado[1] = 2;
                        break;
                    case 'Q':
                        resultado[1] = 4;
                        break;
                    case 'K':
                        resultado[1] = 5;
                        break;
                    case 'P':
                        resultado[1] = 0;
                        break;
                    default:
                        resultado[1] = -1;
                        break;
                }
            } else {
                resultado[1] = -1;
            }

            return resultado;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            pecaFinal = tipoPeca(cliente.board[i][j].charAt(0), cliente.board[i][j].charAt(1));
                            if (pecaFinal[1] != -1) {
                                board[i][j].setPiece(pecaFinal[0], pecaFinal[1], cliente.board[i][j]);
                            } else {
                                board[i][j].removePiece();
                            }
                        }
                    }

                    for (int i = 8; i < 12; i++) {
                        for (int j = 0; j < 8; j++) {
                            pecaFinal = tipoPeca(cliente.board[i][j].charAt(0), cliente.board[i][j].charAt(1));
                            if (pecaFinal[1] != -1) {
                                board[i][j].setPiece(pecaFinal[0], pecaFinal[1], cliente.board[i][j]);
                            } else {
                                board[i][j].removePiece();
                            }
                        }
                    }
                    chessPanel.repaint();
                    painelEsquerda.repaint();
                    Thread.sleep(100);

                } catch (Exception e1) {
                }
            }
        }
    }
}
