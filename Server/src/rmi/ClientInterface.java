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
public interface ClientInterface extends Remote {

    public void recebeMensagem(String msg) throws RemoteException;

    public void validar() throws RemoteException;

    public void atualizaTab(String[][] board) throws RemoteException;

    public void atualizaLog(String log) throws RemoteException;

}
