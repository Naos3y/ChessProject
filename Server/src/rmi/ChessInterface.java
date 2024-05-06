/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rmi;

import java.rmi.Remote;
import java.rmi.*;

/**
 *
 * @author ssama
 */
public interface ChessInterface extends Remote {

    public void resetBoard() throws RemoteException;

    public void login(ClientInterface ci) throws RemoteException;
    
    public void logout(ClientInterface ci) throws RemoteException;

    public void jogada(String peca, String[] inicio, String[] fim, boolean tabToFora, boolean foraToTab) throws RemoteException;

    public void sendMessage(String message) throws RemoteException;

    public void expetadorParaJogador(ClientInterface ci) throws RemoteException;

    public void jogadorParaExpetador(ClientInterface ci) throws RemoteException;
}
