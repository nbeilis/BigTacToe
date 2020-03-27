public class TicTacToeLogic {

	public int [][] bigBoard = new int [3][3];
	public int [][] smallBoard = new int [9][9];
	private int numOfPlayers = 2;

	public int winner () {
		for (int j = 0; j < numOfPlayers; j++) {
			for (int i = 0; i < bigBoard.length; i++) {
				if (bigBoard[i][0] == j && bigBoard[i][1] == j && bigBoard[i][2] == j) return j; //player 1/2 wins
				else if (bigBoard[0][0] == j && bigBoard[1][1] == j && bigBoard[2][2] == j) return j;
				else if (bigBoard[2][0] == j && bigBoard[1][1] == j && bigBoard[0][2] == j) return j;
				else if (bigBoard[0][i] == j && bigBoard[1][i] == j && bigBoard[2][i] == j) return j;
			}
		}
		return 0; //no winner
	}

	public void winSection () {
		for (int k = 0; k < smallBoard.length/3; k++) {
			for (int l = 0; l < 3; l++) {
				for (int j = 0; j < numOfPlayers; j++) {
					for (int i = 0; i < bigBoard.length; i++) {
//						if (smallBoard[i][0] == j && smallBoard[i][1] == j && smallBoard[i][2] == j) bigBoard[][] = j; //player 1/2 wins
//						else if (smallBoard[0][0] == j && smallBoard[1][1] == j && smallBoard[2][2] == j) bigBoard[][] = j;
//						else if (smallBoard[2][0] == j && smallBoard[1][1] == j && smallBoard[0][2] == j) bigBoard[][] = j;
//						else if (smallBoard[0][i] == j && smallBoard[1][i] == j && smallBoard[2][i] == j) bigBoard[][] = j;
					}
				}
			}
		}
	}
}