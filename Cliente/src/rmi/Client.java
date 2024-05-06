/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.SwingUtilities;

/**
 *
 * @author ssama
 */
public class Client extends UnicastRemoteObject implements ClientInterface {

    Client() throws RemoteException {
        super();
    }

    static public void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Client client = new Client();
                    ChessGUI mainFrame = new ChessGUI(client);
                    mainFrame.setSize(1000, 800);
                    mainFrame.setVisible(true);
                } catch (Exception e) {
                }

            }
        });

    }
}
