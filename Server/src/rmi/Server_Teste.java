/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rmi;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.rmi.registry.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import javax.crypto.Cipher;
import javax.swing.text.html.HTML;

public class Server_Teste extends UnicastRemoteObject implements ChessInterface {

    public static HashMap<ClientInterface, String> users = new HashMap<>();
    public static ClientInterface[] jogadores = new ClientInterface[2];

    public static ArrayList<ClientInterface> pedidos = new ArrayList<>();

    private String[] letras = {"a", "b", "c", "d", "e", "f", "g", "h"};

    private String[][] board = {
        {"BR1", "BH1", "BB1", "BQ", "BK", "BB2", "BH2", "BR2"},
        {"BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "BP7", "BP8"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"WP1", "WP2", "WP3", "WP4", "WP5", "WP6", "WP7", "WP8"},
        {"WR1", "WH1", "WB1", "WK", "WQ", "WB2", "WH2", "WR2"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"}

    };

    private String[][] fora = {
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"}
    };

    public static void main(String[] args) {
        try {
            jogadores[0] = null;
            jogadores[1] = null;
            
            int porto = Integer.parseInt(Scan("Porto do servidor: "));
            Registry reg = LocateRegistry.createRegistry(porto);
            Server_Teste serv = new Server_Teste();
            reg.rebind("Server", serv);
            System.out.println("IP do servidor: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("Porto do servidor: " + porto);
            System.out.println("Servidor Iniciado!");

            validaJogadoresEmEspera t1 = new validaJogadoresEmEspera();
            t1.start();

        } catch (Exception e) {
        }
    }

    Server_Teste() throws RemoteException {
        super();
    }

    public void resetBoard() throws RemoteException {
        this.board = new String[][]{
            {"BR1", "BH1", "BB1", "BQ", "BK", "BB2", "BH2", "BR2"},
            {"BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "BP7", "BP8"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"WP1", "WP2", "WP3", "WP4", "WP5", "WP6", "WP7", "WP8"},
            {"WR1", "WH1", "WB1", "WK", "WQ", "WB2", "WH2", "WR2"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"}
        };
    }

    public void removePecas() throws RemoteException {
        this.board = new String[][]{
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"none", "none", "none", "none", "none", "none", "none", "none"},
            {"WP1", "WP2", "WP3", "WP4", "WP5", "WP6", "WP7", "WP8"},
            {"WR1", "WH1", "WB1", "WK", "WQ", "WB2", "WH2", "WR2"},
            {"BR1", "BH1", "BB1", "BQ", "BK", "BB2", "BH2", "BR2"},
            {"BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "BP7", "BP8"},};
    }

    public synchronized void login(ClientInterface ci, String nome) throws RemoteException {

        users.put(ci, nome);
        System.out.println("Entrou jogador " + nome);
        if (jogadores[0] == null) {
            jogadores[0] = ci;
        } else if (jogadores[1] == null) {
            jogadores[1] = ci;
        }

        Iterator<ClientInterface> iterator = users.keySet().iterator();
        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();
            try {
                key.atualizaTab(board);
            } catch (Exception e) {
                iterator.remove();
            }

        }
    }

    public synchronized void logout(ClientInterface ci) throws RemoteException {

        Iterator<ClientInterface> iterator = users.keySet().iterator();

        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();
            if (key.equals(ci)) {
                System.out.println("Saiu jogador " + users.get(key));
                iterator.remove();
                try {
                    for (int j = 0; j < jogadores.length; j++) {
                        if (jogadores[j].equals(ci)) {
                            jogadores[j] = null;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

    }
    
    
    public synchronized void jogadorParaExpetador(ClientInterface ci) throws RemoteException {
        for (int i = 0; i < jogadores.length; i++) {
            try {
                if (jogadores[i].equals(ci)) {
                    jogadores[i] = null;
                    try {
                        jogadores[i] = (pedidos.get(0));
                    } catch (Exception e) {
                        jogadores[i] = null;
                    }
                }
            } catch (Exception e2) {
            }
        }

    }

    public synchronized void printChessBoard(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + "\t");
            }
        }
    }

    public synchronized void printForaBoard(String[] fora) {
        for (int i = 0; i < fora.length; i++) {
        }
    }

    public synchronized void jogada(String peca, int[] inicio, int[] fim, boolean tabToFora, boolean foraToTab) {
        int inicioA = inicio[0];
        int inicioB = inicio[1];
        int fimA = fim[0];
        int fimB = fim[1];
        boolean flag = false;

        System.out.println("Jogada: [" + numberToLetter(inicio[0]) + inicio[1] + "] para [" + numberToLetter(fim[0]) + fim[1] + "]");

//        printChessBoard(board);
        if (inicioA == fimA && inicioB == fimB) {
            return;
        }

        if (board[fimA][fimB].equals("none")) {
            board[fimA][fimB] = peca;
            board[inicioA][inicioB] = "none";
        } else {
            for (int i = 8; i < 12; i++) {
                if (flag) {
                    break;
                }
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].equals("none")) {
                        board[i][j] = board[fimA][fimB];
                        board[fimA][fimB] = peca;
                        board[inicioA][inicioB] = "none";
                        flag = true;
                        break;
                    }

                }
            }
        }
        Iterator<ClientInterface> iterator = users.keySet().iterator();
        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();
            try {
                key.atualizaTab(board);
                key.atualizaLog("- Peça " + peca + " movida para [" + numberToLetter(fim[0]) + (fim[1] + 1) + "]");

            } catch (Exception e) {
                iterator.remove();
            }

        }

    }

    public synchronized void sendMessage(String message) throws RemoteException {
        Iterator<ClientInterface> iterator = users.keySet().iterator();
        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();
            try {
                key.recebeMensagem(message);
            } catch (Exception e) {
                iterator.remove();
            }

        }
//        Iterator<ClientInterface> iterator = users.keySet().iterator();
//        while (iterator.hasNext()) {
//            ClientInterface user = iterator.next();
//            try {
//                user.recebeMensagem(message);
//            } catch (RemoteException e) {
//                iterator.remove();
//            }
//        }

    }

    public synchronized boolean souJogador(ClientInterface ci) throws RemoteException {
        for (int i = 0; i < jogadores.length; i++) {
            if (jogadores[i].equals(ci)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void expetadorParaJogador(ClientInterface ci) throws RemoteException {

        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).equals(ci)) {
                return;
            }
        }
        pedidos.add(ci);

        for (int i = 0; i < jogadores.length; i++) {
            try {
                jogadores[i].validar();
            } catch (Exception validarPedido) {
                jogadores[i] = pedidos.get(0);
                pedidos.remove(0);
                System.out.println("Entrou novo jogador");
                return;
            }
        }
        
        

    }

    public synchronized boolean verificaNome(String nome) throws RemoteException {
        Iterator<ClientInterface> iterator = users.keySet().iterator();
        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();
            try {
                if (users.get(key).equalsIgnoreCase(nome)) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }

        }
        return true;
    }


    public static String Scan(String aMensagem) {
        System.out.print(aMensagem);
        return (new Scanner(System.in)).nextLine();
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

    public synchronized void resetBoardCliente() throws RemoteException {
        resetBoard();
        Iterator<ClientInterface> iterator = users.keySet().iterator();
        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();
            try {
                key.atualizaTab(board);
            } catch (Exception e) {
                iterator.remove();
            }

        }
        System.out.println("O tabuleiro foi resetado");
    }

    public synchronized void clearBoardCliente() throws RemoteException {
        removePecas();
        Iterator<ClientInterface> iterator = users.keySet().iterator();
        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();
            try {
                key.atualizaTab(board);
            } catch (Exception e) {
                iterator.remove();
            }

        }
        System.out.println("O tabuleiro foi limpo");
    }

    public synchronized String[] getPlayers() throws RemoteException {

        Iterator<ClientInterface> iterator = users.keySet().iterator();
        String[] result = {"Player 1", "Player 2"};

        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();

            try {
                if (key.equals(jogadores[0])) {
                    result[0] = users.get(key);
                } else if (key.equals(jogadores[1])) {
                    result[1] = users.get(key);

                }
            } catch (Exception e) {
                iterator.remove();
            }

        }
        //System.out.println(result[0] + "" + result[1]);
        return result;
    }

    public synchronized ArrayList<String> getNames() throws RemoteException {
        ArrayList<String> result = new ArrayList<>();
        Iterator<ClientInterface> iterator = users.keySet().iterator();

        while (iterator.hasNext()) {
            ClientInterface key = iterator.next();

            try {
                result.add(users.get(key));
            } catch (Exception e) {
            }

        }
        return result;
    }

    public synchronized void trocaPosicao(boolean flag) throws RemoteException {
        try {
            //System.out.println(jogadores[0] + "-" + jogadores[1]);
//        if (jogadores[0] != null && jogadores[1] != null) {
            System.out.println("Jogadores trocaram de posicao");
            ClientInterface temp = jogadores[0];
            jogadores[0] = jogadores[1];
            jogadores[1] = temp;
//        }
        } catch (Exception e) {
            //System.out.println(e);
        }
    }

    public static class validaJogadoresEmEspera extends Thread {

        public validaJogadoresEmEspera() {

        }

        public void run() {
            while (true) {
                try {
                    for (int i = 0; i < jogadores.length; i++) {
                        try {
                            jogadores[i].validar();
                        } catch (Exception validarPedido) {

                            jogadores[i] = pedidos.get(0);
                            pedidos.remove(0);
                            return;
                        }
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {

                }
            }

        }
    }

}
