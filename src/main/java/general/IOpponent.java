package general;

public interface IOpponent {
    Alliance getAlliance();
    boolean isEnemyOf(IOpponent other);
    boolean isAllyOf(IOpponent other);
}
