/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

import java.awt.List;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 *
 * @author ssama
 */
public class Client extends UnicastRemoteObject implements ClientInterface {

    Client() throws RemoteException {
        super();
    }

    public String mensagem;
    public boolean nova = false;

    static public void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Client client = new Client();
                    ChessGUI mainFrame = new ChessGUI(client, client);
                    mainFrame.setSize(1000, 800);
                    mainFrame.setVisible(true);
                } catch (Exception e) {
                }

            }
        });

    }

    public synchronized void recebeMensagem(String msg) throws RemoteException {
        this.mensagem = msg+"\n";
        this.nova = true;

    }

    public synchronized String getMensagens() {
        this.nova = false;
        return this.mensagem;

    }
    
    public synchronized boolean getNova(){
        return this.nova;
    }


}