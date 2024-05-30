/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

/**
 *
 * @author ssama
 */
public class Piece {

    private int color;
    private int type;
    private String ID;

    public Piece(int color, int type, String ID) {
        this.color = color;
        this.type = type;
        this.ID = ID;
    }

    public int getColor() {
        return color;
    }

    public int getType() {
        return type;
    }

    public String getID() {
        return ID;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toString() {
        String typeName;
        switch (type) {
            case 0:
                typeName = "Pawn";
                break;
            case 1:
                typeName = "Knight";
                break;
            case 2:
                typeName = "Bishop";
                break;
            case 3:
                typeName = "Rook";
                break;
            case 4:
                typeName = "Queen";
                break;
            case 5:
                typeName = "King";
                break;
            default:
                typeName = "Unknown";
                break;
        }

        String colorName = (color == 0) ? "White" : "Black";
        return colorName + typeName;
    }

}
