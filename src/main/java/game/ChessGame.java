package game;

import static game.GameStatus.*;

import board.*;

import static game.InputValidator.parseMove;
import static game.InputValidator.validateMove;
import static pieces.PieceType.*;
import static general.Alliance.*;

import pieces.ChessPiece;
import pieces.PieceType;
import player.ChessPlayer;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class ChessGame implements IChessGame {
    private ChessPlayer whitePlayer;
    private ChessPlayer blackPlayer;
    private final CheckerBoard<ChessPiece> board;
    private ChessPlayer currentPlayer;
    private GameStatus gameStatus;
    private final Scanner scanner;

    private int numberOfMovesWithNoCapture;

    public ChessGame(){
        this.board = new ChessBoard();
        this.scanner = new Scanner(System.in);
        this.numberOfMovesWithNoCapture = 0;
    }

    @Override
    public void start() {
        initializeGame();
        while (gameIsActive()){
            try {
                boardCheck();
                System.out.println(currentPlayer + "'s turn now:");
                String input = scanner.nextLine();
                validateMove(input, currentPlayer, board);
                checkInactiveMove(parseMove(input));
                currentPlayer.makeAMove(parseMove(input));
                switchTurns();
                BoardViewer.view(board);
            } catch (Exception e){
                examineGameEnd();
                if (!gameIsActive())
                    break;
                System.out.println(e.getMessage() + "\nPlease try again...");
                BoardViewer.view(board);
            }
        }
        printGameResult();
        scanner.close();
    }

    @Override
    public void initializeGame(){
        BoardSetter.initialize(board);
        setGameStatus(ACTIVE);
        System.out.println("Enter the white player's name: ");
        String whiteName = scanner.nextLine();
        System.out.println("Enter the black player's name: ");
        String blackName = scanner.nextLine();
        whitePlayer = new ChessPlayer(whiteName, WHITE, board);
        blackPlayer = new ChessPlayer(blackName, BLACK, board);
        currentPlayer = whitePlayer;
        BoardViewer.view(board);
        boardCheck();
        System.out.println("White player starts first.");
    }

    public boolean gameIsActive(){
        return this.gameStatus == ACTIVE;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void boardCheck(){
        generateValidMoves();
        whitePlayer.getKing().checkSurroundings();
        blackPlayer.getKing().checkSurroundings();
        generateValidMoves();
        if (whitePlayer.isCheckMated() || blackPlayer.isCheckMated() || isInDraw())
            throw new IllegalArgumentException("Checkmate or draw detected");
    }

    public void checkInactiveMove(Move move) {
        Square origin = move.getOrigin();
        Square target = move.getTarget();
        boolean noPawnMovement = board.getPieceAt(origin).getType() != PAWN;
        boolean noCapture = target.isEmpty();
        if (noPawnMovement && noCapture)
            numberOfMovesWithNoCapture++;
        else numberOfMovesWithNoCapture = 0;
        if (numberOfMovesWithNoCapture >= 100){
            setGameStatus(DRAW);
            throw new IllegalArgumentException("50 moves detected without pawn movement or capture...");
        }
    }

    public boolean isInDraw() {
        if (whitePlayer.isStaleMated() || blackPlayer.isStaleMated())
            return true;
        return playersDoNotHaveSufficientMaterialToForceCheckmate();
    }

    public boolean playersDoNotHaveSufficientMaterialToForceCheckmate() {
        List<PieceType> sufficientPieces = Arrays.asList(ROOK, QUEEN, PAWN);
        List<ChessPiece> allPieces = board.getAllPieces();
        allPieces.removeIf(piece -> piece.getType() == KING);

        int sumOfRemainingWhitePieces = 0;
        int sumOfRemainingBlackPieces = 0;
        for (ChessPiece piece: allPieces) {
            if (sufficientPieces.contains(piece.getType()))
                return false;
            sumOfRemainingBlackPieces += piece.getAlliance() == BLACK? 1 : 0;
            sumOfRemainingWhitePieces += piece.getAlliance() == WHITE? 1 : 0;
        }

        return sumOfRemainingWhitePieces < 2 && sumOfRemainingBlackPieces < 2;
    }

    public void generateValidMoves(){
        for (ChessPiece piece : board.getAllPieces()) {
            piece.updateSquareSets();
        }
    }

    public void examineGameEnd(){
        if (whitePlayer.isCheckMated())
            setGameStatus(BLACK_WIN);
        if (blackPlayer.isCheckMated())
            setGameStatus(WHITE_WIN);
        if (isInDraw())
            setGameStatus(DRAW);
    }

    @Override
    public void switchTurns(){
        if (currentPlayer.equals(whitePlayer)){
            currentPlayer = blackPlayer;
        } else {
            currentPlayer = whitePlayer;
        }
    }

    @Override
    public void printGameResult() {
        String winMessage = "Game ends with a checkmate!";
        if (gameStatus == WHITE_WIN)
            System.out.println(winMessage + "\n" + whitePlayer + " is the Winner.");
        else if (gameStatus == BLACK_WIN)
            System.out.println(winMessage + "\n" + blackPlayer + " is the Winner.");
        else if (gameStatus == DRAW)
            System.out.println("Game ends with a draw!");
    }

}
