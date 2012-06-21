package tk.ericwieser.rfw;

public abstract class GameComponent {
	protected Game game;
	public Game getGame() { return game; }
	public void setGame(Game game) { this.game = game; }
	
	public RFWManager getPlugin() {
		return game.getPlugin();
	}
	
	public GameComponent(Game game) {setGame(game); }
}
