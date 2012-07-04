import java.awt.Dimension;

public interface GameFactory {
	GameModel newGameModel();

	Dimension getDimension();
}
