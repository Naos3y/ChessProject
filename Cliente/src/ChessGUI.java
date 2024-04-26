
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ChessGUI extends JFrame {

    private SquarePanel[][] board = new SquarePanel[8][8];

    private JPanel chessPanel = new JPanel();
    private JPanel painelPrincipal = new JPanel();
    private JPanel propriedades = new JPanel();
    private JPanel jogador1 = new JPanel();
    private JPanel jogador2 = new JPanel();
    private JPanel pecas = new JPanel();
    private JButton quit = new JButton("Quit");
    private JPanel topPanel = new JPanel();

    public ChessGUI() {
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

        /* Painel do propriedades*/
        propriedades.setBackground(Color.red);
        propriedades.setPreferredSize(new Dimension(400, 100));
        painelPrincipal.add(propriedades, BorderLayout.EAST);

        /* Painel do jogador 1*/
        jogador1.setBackground(Color.blue);
        painelPrincipal.add(pecas, BorderLayout.WEST);

        /* Painel do jogador 2*/
        jogador2.setBackground(Color.green);
        jogador2.add(quit);

        painelPrincipal.add(jogador1, BorderLayout.SOUTH);


        /* Painel das pecas*/
        pecas.setBackground(Color.pink);
        painelPrincipal.add(jogador2, BorderLayout.NORTH);

        add(painelPrincipal, BorderLayout.CENTER);

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Confirmar saída", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

    }

    public void selected(int x, int y) {
        System.out.printf("mouse pressed at: %d - %d\n", x, y);
    }

    static public void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChessGUI mainFrame = new ChessGUI();
                mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                mainFrame.setUndecorated(true);
//                mainFrame.setResizable(false);
                mainFrame.setVisible(true);

            }
        });

    }
}
