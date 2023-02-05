package game;

import board.Square;
import player.ChessPlayer;

import java.util.Objects;

public class Move {
    private final Square origin;
    private final Square target;

    private final int hashCode;

    private ChessPlayer player;

    public Move(Square origin, Square target) {
        this.origin = origin;
        this.target = target;
        this.hashCode = Objects.hash(origin, target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Move that = (Move) o;
        return origin.equals(that.origin) && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public Square getOrigin() {
        return origin;
    }

    public Square getTarget() {
        return target;
    }

    public void setPlayer(ChessPlayer player) {
        this.player = player;
    }

    public ChessPlayer getPlayer() {
        return player;
    }
}
