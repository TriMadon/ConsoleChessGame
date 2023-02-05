package board;

import pieces.ChessPiece;

public final class BoardViewer {

    private BoardViewer(){
        throw new AssertionError();
    }

    public static void view(CheckerBoard<ChessPiece> board){
        String green =  "\u001B[42m";
        String brightGreen = "\u001B[102m";
        String black =  "\u001B[30m";
        String reset = "\u001B[0m";
        String space = "     ";
        System.out.println("    a     b     c     d     e     f     g     h  ");
        for (int i = 8; i >= 1; i--) {
            System.out.print(i + " ");
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPieceAt(Square.getSquare(j,i));
                if (piece == null) {
                    if ((i + j) % 2 == 0)
                        System.out.print(brightGreen + space + reset + " ");
                    else
                        System.out.print(green + space + reset + " ");
                }
                else {
                    if ((i + j) % 2 == 0)
                        System.out.print(brightGreen + black + piece + reset + " ");
                    else
                        System.out.print(green + black + piece + reset + " ");
                }
            }
            System.out.println(i);
        }
        System.out.println("    a     b     c     d     e     f     g     h  ");
    }

}
