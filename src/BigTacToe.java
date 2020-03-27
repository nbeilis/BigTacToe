import java.awt.Image;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class BigTacToe 
{
	public final static String COLOR_SPOT = "yellow";
	//first player's color, second player's color, color of options for player to go
	private final static int EMPTY = 0, PLAYER_1 = 1, PLAYER_2 = 2, VALID_LOCATION = 3, FULL_SPOT = 4;
	private final static int PLAYER = 1, COMPUTER = 2;

	public static int [][] bigBoard = new int [3][3];
	public static int [][] smallBoard = new int [9][9];
	public static int section = -1; //numbers sections from 0-8, first row is 012 then 345 then 678

	public static int row = 0, col = 0; //initializes variables
	public static Coordinate loc = new Coordinate(row, col); //creates coordinate object	

	@SuppressWarnings("resource")
	public static void main(String[] args) 
	{
		StringBuffer help = new StringBuffer("");
		File instructions = new File("instructions");
		ImageIcon welcomeImage = new ImageIcon("tictactoe.png");

		try	//sets Scanner up for document reading and etc.
		{
			//This is for reading the instructions/help document
			Scanner input = new Scanner(instructions);
			while(input.hasNext())
				help.append(input.nextLine() + "\n");//reads an entire whole line	

			//This is for re-scaling the welcome image
			Image temp = welcomeImage.getImage();
			welcomeImage = new ImageIcon(temp.getScaledInstance(125, 111, java.awt.Image.SCALE_SMOOTH));
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("Exception :(((");
			System.exit(1);
		}

		PlayMusic();

		int playAgain;
		int numOfPlayers;
		do {
			playAgain = -1;
			numOfPlayers = start(welcomeImage, help);
			Board b = new Board(9, 9); //creates board
			newGame(b, numOfPlayers);
			playAgain = endGame(welcomeImage, playAgain, numOfPlayers);
			Board.f.setVisible(false);
			wipe();
		} while (playAgain == 0);
		StopMusic();
	}
	//Music stuff:
	static Clip clip;
	static void PlayMusic() 
	{
		try 
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Kalimba.wav"));	//Kalimba.wav
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			clip.loop(10);	//this should be enough for the user. The music stops to remind them to stop playing too

		} catch(Exception e) 
		{
			System.out.println("Error w/ sound playing");	//tells us about the error (for debugging)
			e.printStackTrace();
		}
	}
	static void StopMusic() 
	{
		clip.stop();
	}
	//End of Music stuff

	public static void wipe() {
		for (int i = 0; i < bigBoard.length; i++) {
			for (int j = 0; j < bigBoard[i].length; j++) {
				bigBoard[i][j] = EMPTY;
			}
		}
		for (int i = 0; i < smallBoard.length; i++) {
			for (int j = 0; j < smallBoard[i].length; j++) {
				smallBoard[i][j] = EMPTY;
			}
		}
		section = -1;
	}

	public static int start(ImageIcon welcomeImage, StringBuffer help) {
		String[] buttons = {"Help", "1-Player", "2-Player"}; 
		int returnValue = 0;
		do {
			returnValue = JOptionPane.showOptionDialog(null, "Welcome to Big Tac Toe! Press to begin!", "Main Menu",
					JOptionPane.PLAIN_MESSAGE, 0, welcomeImage, //icon
					buttons, buttons); //1 player is 2, 2 player is 1, help is 0 

			if (returnValue == 0) JOptionPane.showMessageDialog(null, help);
		} while (returnValue == 0);
		return returnValue;
	}

	public static void newGame(Board b, int numOfPlayers) {
		if (numOfPlayers == 1) {
			Player player1 = new Player(PLAYER_1, PLAYER);
			Player comp = new Player(PLAYER_2, COMPUTER);
			while (winner() == 0) {
				player(b, player1);
				if (winner() != 0) break;
				player(b, comp);
			}
		}
		if (numOfPlayers == 2) {
			Player player1 = new Player(PLAYER_1, PLAYER);
			Player player2 = new Player(PLAYER_2, PLAYER);
			while (winner() == 0) {
				player(b, player1);
				if (winner() != 0) break;
				player(b, player2);
			}
		}
	}

	public static int endGame(ImageIcon welcomeImage, int playAgain, int numOfPlayers) {
		String[] endButtons = {"Replay", "Quit"}; //replay == 0, quit == 1
		if (numOfPlayers == 2) {
			playAgain = JOptionPane.showOptionDialog(null, "Player " + winner() + " has won!", "Winner", JOptionPane.PLAIN_MESSAGE, 0, welcomeImage, endButtons, null);
		}
		else if (numOfPlayers == 1 && winner() == PLAYER_1) {
			playAgain = JOptionPane.showOptionDialog(null, "Player " + winner() + " has won!", "Winner", JOptionPane.PLAIN_MESSAGE, 0, welcomeImage, endButtons, null);
		}
		else if (numOfPlayers == 1 && winner() == PLAYER_2){
			playAgain = JOptionPane.showOptionDialog(null, "Computer has won!", "Winner", JOptionPane.PLAIN_MESSAGE, 0, welcomeImage, endButtons, null);
		}
		else if (winner() == 3) { //no winner but no valid spots left
			playAgain = JOptionPane.showOptionDialog(null, "Game Over", "Winner", JOptionPane.PLAIN_MESSAGE, 0, welcomeImage, endButtons, null);
		}
		return playAgain;
	}

	//Function for winner for overall game. Note: if the whole board is full, will have a 'tie' pop-up method later.
	public static int winner() {
		for (int j = 1; j <= 2; j++) {
			for (int i = 0; i < bigBoard.length; i++) {
				if (bigBoard[i][0] == j && bigBoard[i][1] == j && bigBoard[i][2] == j) return j; //player 1/2 wins
				else if (bigBoard[0][0] == j && bigBoard[1][1] == j && bigBoard[2][2] == j) return j;
				else if (bigBoard[2][0] == j && bigBoard[1][1] == j && bigBoard[0][2] == j) return j;
				else if (bigBoard[0][i] == j && bigBoard[1][i] == j && bigBoard[2][i] == j) return j;
			}
		}
		wonSection();
		for (int i = 0; i < smallBoard.length; i++) {
			for (int j = 0; j < smallBoard[i].length; j++) {
				if (smallBoard[i][j] == EMPTY) {
					return 0; //no winner for game yet and not full
				}
			}
		}
		return 3; //board is full
	}

	public static void winSection () {
		for (int j = 1; j <= 2; j++) {
			for (int l = 0; l < 7; l+=3) {
				for (int k = 0; k < 7; k+=3) {
					for (int i = 0; i < bigBoard.length; i++) {
						if (smallBoard[i + l][0 + k] == j && smallBoard[i + l][1 + k] == j && smallBoard[i + l][2 + k] == j) {
							bigBoard[l/3][k/3]=j; //player 1/2 wins	//horizontal
							//System.out.println(bigBoard[l/3][k/3] + "" + i + "" + j);
						}
						else if (smallBoard[0 + l][0 + k] == j && smallBoard[1 + l][1 + k] == j && smallBoard[2 + l][2 + k] == j) {
							bigBoard[l/3][k/3]=j;	//diagonal
							//System.out.println(bigBoard[l/3][k/3]+ "" + i + "" + j);
						}
						else if (smallBoard[2 + l][0 + k] == j && smallBoard[1 + l][1 + k] == j && smallBoard[0 + l][2 + k] == j) {
							bigBoard[l/3][k/3]=j;	//other diagonal
							//System.out.println(bigBoard[l/3][k/3]+ "" + i + "" + j);
						}
						else if (smallBoard[0 + l][i + k] == j && smallBoard[1 + l][i + k] == j && smallBoard[2 + l][i + k] == j) {
							bigBoard[l/3][k/3]=j;	//vertical
							//System.out.println(bigBoard[l/3][k/3]+ "" + i + "" + j);
						}
					}
				}
			}
		}
	}

	public static void fillSection() {
		int addToRow = 0, addToCol = 0;
		switch (section) {
		case 1: addToCol = 3; break;
		case 2: addToCol = 6; break;
		case 3: addToRow = 3; break;
		case 4: addToCol = 3; addToRow = 3; break;
		case 5: addToRow = 3; addToCol = 6; break;
		case 6: addToRow = 6; break;
		case 7: addToCol = 3; addToRow = 6; break;
		case 8: addToCol = 6; addToRow = 6; break;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (smallBoard [i + addToRow][j + addToCol] == EMPTY) {
					smallBoard[i + addToRow][j + addToCol] = FULL_SPOT;
				}
			}
		}
	}

	//returns whether or not a section is won (true/false)
	public static boolean wonSection() {
		winSection();
		int bigSectionRow = 0, bigSectionCol = 0;
		switch (section) {
		case 1: bigSectionCol = 1; break;
		case 2: bigSectionCol = 2; break;
		case 3: bigSectionRow = 1; break;
		case 4: bigSectionRow = 1; bigSectionCol = 1; break;
		case 5: bigSectionRow = 1; bigSectionCol = 2; break;
		case 6: bigSectionRow = 2; break;
		case 7: bigSectionRow = 2; bigSectionCol = 1; break;
		case 8: bigSectionRow = 2; bigSectionCol = 2; break;
		}
		if (bigBoard[bigSectionRow][bigSectionCol]==EMPTY) {
			return false;
		}
		else {
			fillSection();
			return true;
		}
	}

	//returns whether or not a section is full (true/false)
	public static boolean fullSection() {
		int addToRow = 0, addToCol = 0;
		switch (section) {
		case 1: addToCol = 3; break;
		case 2: addToCol = 6; break;
		case 3: addToRow = 3; break;
		case 4: addToCol = 3; addToRow = 3; break;
		case 5: addToRow = 3; addToCol = 6; break;
		case 6: addToRow = 6; break;
		case 7: addToCol = 3; addToRow = 6; break;
		case 8: addToCol = 6; addToRow = 6; break;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (smallBoard [i + addToRow][j + addToCol] == EMPTY || (smallBoard [i + addToRow][j + addToCol] == VALID_LOCATION)) {
					return false;
				}
			}
		}
		return true;
	}

	//puts red pegs in available empty spaces where the player can go
	public static void highlight (Board b) {
		if (!fullSection()) {
			int addToRow = 0, addToCol = 0;
			switch (section) {
			case 1: addToCol = 3; break;
			case 2: addToCol = 6; break;
			case 3: addToRow = 3; break;
			case 4: addToCol = 3; addToRow = 3; break;
			case 5: addToRow = 3; addToCol = 6; break;
			case 6: addToRow = 6; break;
			case 7: addToCol = 3; addToRow = 6; break;
			case 8: addToCol = 6; addToRow = 6; break;
			}
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (smallBoard [i + addToRow][j + addToCol] == EMPTY) {
						b.putPeg(COLOR_SPOT, i + addToRow, j + addToCol);
						smallBoard[i + addToRow][j + addToCol] = VALID_LOCATION;
					}
				}
			}
		}
		else if (fullSection()||wonSection())
		{
			for (int i = 0; i < smallBoard.length; i++) {
				for (int j = 0; j < smallBoard[i].length; j++) {
					if (smallBoard[i][j] == EMPTY) {
						b.putPeg(COLOR_SPOT, i, j);
						smallBoard[i][j] = VALID_LOCATION;
					}
				}
			}
		}
	}

	//removes the red-peg highlights
	public static void removeHighlight (Board b) {
		for (int i = 0; i < smallBoard.length; i++) {
			for (int j = 0; j < smallBoard[i].length; j++) {
				if (smallBoard[i][j] == VALID_LOCATION) {
					b.removePeg(i, j);
					smallBoard[i][j] = EMPTY;
				}
			}
		}
	}

	public static void player(Board b, Player player) {
		if (player.getType() == 1) {
			b.displayMessage("Player " + player.getNum() + "'s Turn");
			boolean hasGone = false;
			if (section >= 0) {
				highlight(b);
			}
			do {
				loc = b.getClick(); //gets click
				col = loc.getCol(); //takes out row and column
				row = loc.getRow();
				//int checkSection = (row/3)*3 + col/3;
				//System.out.println("p"+section+" and "+checkSection);
				if (smallBoard[row][col] == VALID_LOCATION || section < 0) {
					smallBoard[row][col] = player.getNum();
					b.putPeg(player.getColor(), row, col); //puts a peg
					hasGone = true;
				}
			} while (!hasGone);
			removeHighlight(b);
			wonSection();
			section = (row%3)*3 + col%3; //dividing by three gives which row it's in and multiplied by three to add previous sections, dividing col by three and adding 1 gives which col it's in
		}
		if (player.getType() == 2) {
			b.displayMessage("Computer's Turn");
			try
			{
				Thread.sleep(200);
			}
			catch(InterruptedException ex)
			{
				Thread.currentThread().interrupt();
			}
			boolean hasGone = false;
			Random r = new Random();
			if (!fullSection() && !wonSection()) {
				do {
					row = r.nextInt(3) + (section/3)*3; 
					col = r.nextInt(3) + (section%3)*3; //initializes variables
					if (smallBoard[row][col] == EMPTY) {
						smallBoard[row][col] = player.getNum();
						b.putPeg(player.getColor(), row, col); //puts a peg
						hasGone = true;
					}
				} while (!hasGone);
			}
			else if (fullSection() || wonSection()) {
				do {
					row = r.nextInt(9); 
					col = r.nextInt(9); //initializes variables
					section = (row/3)*3 + col/3;
					if (smallBoard[row][col] == EMPTY && !fullSection() && !wonSection()) {
						smallBoard[row][col] = player.getNum();
						b.putPeg(player.getColor(), row, col); //puts a peg
						hasGone = true;
					}
				} while (!hasGone);
			}
			wonSection();
			section = (row%3)*3 + col%3;
			//System.out.println("comp "+section);
		}

	}
}
