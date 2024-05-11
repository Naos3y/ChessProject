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

    ArrayList<ClientInterface> users = new ArrayList<>();
    ArrayList<ClientInterface> jogadores = new ArrayList<>();
    ArrayList<ClientInterface> pedidos = new ArrayList<>();

    private String[] letras = {"a", "b", "c", "d", "e", "f", "g", "h"};

    private String[][] board = {
        {"BR1", "BH1", "BB1", "BQ", "BK", "BB2", "BH2", "BR2"},
        {"BP1", "BP2", "BP3", "BP4", "BP5", "BP6", "BP7", "BP8"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"none", "none", "none", "none", "none", "none", "none", "none"},
        {"WP1", "WP2", "WP3", "WP4", "WP5", "WP6", "WP7", "WP8"},
        {"WR1", "WH1", "WB1", "WK", "WQ", "WB2", "WH2", "WR2"}
    };

    private String[] fora = {
        "none", "none", "none", "none", "none", "none", "none", "none",
        "none", "none", "none", "none", "none", "none", "none", "none",
        "none", "none", "none", "none", "none", "none", "none", "none",
        "none", "none", "none", "none", "none", "none", "none", "none",
        "none", "none", "none", "none", "none", "none", "none", "none"
    };

    public static void main(String[] args) {
        try {
            int porto = Integer.parseInt(Scan("Porto do servidor: "));
            Registry reg = LocateRegistry.createRegistry(porto);
            Server_Teste serv = new Server_Teste();
            reg.rebind("Server", serv);
            System.out.println("IP do servidor: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("Porto do servidor: " + porto);
            System.out.println("Servidor Iniciado!");

        } catch (Exception e) {
            System.out.println(e);
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
            {"WR1", "WH1", "WB1", "WK", "WQ", "WB2", "WH2", "WR2"}
        };

        this.fora = new String[]{
            "none", "none", "none", "none", "none", "none", "none", "none",
            "none", "none", "none", "none", "none", "none", "none", "none",
            "none", "none", "none", "none", "none", "none", "none", "none",
            "none", "none", "none", "none", "none", "none", "none", "none",
            "none", "none", "none", "none", "none", "none", "none", "none"
        };

    }

    public synchronized void login(ClientInterface ci) throws RemoteException {
        System.out.println(ci);
        users.add(ci);
        if (users.size() == 0 || users.size() == 1) {
            jogadores.add(ci);
        }
    }

    public synchronized void logout(ClientInterface ci) throws RemoteException {
        users.remove(ci);

    }

    public synchronized void jogada(String peca, String[] inicio, String[] fim, boolean tabToFora, boolean foraToTab) {
        int inicioA = Integer.parseInt(inicio[0]);
        int inicioB = -1;
        int fimA = Integer.parseInt(fim[0]);
        int fimB = -1;

        if (!tabToFora && !foraToTab) {

            for (int i = 0; i < 8; i++) {
                if (inicio[1].equalsIgnoreCase(letras[i])) {
                    inicioB = i;
                }
            }

            for (int i = 0; i < 8; i++) {
                if (fim[1].equalsIgnoreCase(letras[i])) {
                    fimB = i;
                }
            }

            this.board[inicioA][inicioB] = "none";
            if (board[fimA][fimB].equals("none")) {
                this.board[fimA][fimB] = peca;
            } else {
                for (int i = 0; i < fora.length; i++) {
                    if (fora[i].equals("none")) {
                        fora[i] = board[fimA][fimB];
                        board[fimA][fimB] = peca;
                    }
                }
            }
        } else if (tabToFora && !foraToTab) {

            for (int i = 0; i < 8; i++) {
                if (inicio[1].equalsIgnoreCase(letras[i])) {
                    inicioB = i;
                }
            }
            this.board[inicioA][inicioB] = "none";

            for (int i = 0; i < fora.length; i++) {
                if (fora[i].equals("none")) {
                    fora[i] = peca;
                }
            }

        } else {

            for (int i = 0; i < 8; i++) {
                if (inicio[1].equalsIgnoreCase(letras[i])) {
                    inicioB = i;
                }
            }
            this.board[inicioA][inicioB] = peca;

            for (int i = 0; i < fora.length; i++) {
                if (fora[i].equals(peca)) {
                    fora[i] = "none";
                }
            }

        }
    }

    public synchronized void sendMessage(String message) throws RemoteException {
        Iterator<ClientInterface> iterator = users.iterator();
        while (iterator.hasNext()) {
            ClientInterface user = iterator.next();
            try {
                user.recebeMensagem(message);
            } catch (RemoteException e) {
                iterator.remove();
            }
        }

    }

    public synchronized void expetadorParaJogador(ClientInterface ci) throws RemoteException {
        pedidos.add(ci);
    }

    public synchronized void jogadorParaExpetador(ClientInterface ci) throws RemoteException {
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).equals(ci)) {
                jogadores.remove(i);
                try {
                    jogadores.add(pedidos.get(0));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public static String Scan(String aMensagem) {
        System.out.print(aMensagem);
        return (new Scanner(System.in)).nextLine();
    }

}
