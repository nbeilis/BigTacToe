public class Player {

	public final static String COLOR_1 = "blue", COLOR_2 = "cyan";
	private int playerNum;
	private String color;
	private int playerType;

	Player (int playerNum, int playerType) { //player type 1 - player, 2- computer
		this.playerNum = playerNum;
		this.playerType = playerType;
	}
	
	public int getNum () {
		return playerNum;
	}
	
	public int getType () {
		return playerType;
	}
	
	public String getColor () {
		if (playerNum == 1) color = COLOR_1;
		else color = COLOR_2;
		return color;
	}

}