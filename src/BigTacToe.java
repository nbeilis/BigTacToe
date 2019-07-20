import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class BigTacToe 
{
	public static int [][] bigBoard = new int [3][3];
	public static int [][] smallBoard = new int [9][9];
	private static int numOfPlayers = 0;
	public static int section = -1; //numbers sections from 0-8, first row is 012 then 345 then 678

	private static int row = 0, col = 0; //initializes variables
	private static Coordinate loc = new Coordinate(row, col); //creates coordinate object
	private static Random r = new Random();

	public static int numOfClicks = 0;
	public static StringBuffer help = new StringBuffer("");
	public static File instructions = new File("instructions");
	public static ImageIcon welcomeImage = new ImageIcon("tictactoe.png");	

	@SuppressWarnings("resource")
	public static void main(String[] args) 
	{
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

		String[] buttons = { "Help", "1-Player", "2-Player"}; 
		int returnValue = 0;
		do {
			returnValue = JOptionPane.showOptionDialog(null, "Welcome to Big Tac Toe! Press to begin!", "Main Menu",
					JOptionPane.PLAIN_MESSAGE, 0, welcomeImage, //icon
					buttons, buttons); //1 player is 2, 2 player is 1, help is 0 

			if (returnValue == 0) JOptionPane.showMessageDialog(null, help);
			numOfPlayers = returnValue;
		} while (numOfPlayers == 0);

		Board b = new Board(9, 9); //creates board
		if (numOfPlayers == 1) {
			while (winner() == 0) {
				player(b, 1, "yellow");
				computer(b);
			}
		}
		if (numOfPlayers == 2) {
			while (winner() == 0) {
				player(b, 1, "yellow");
				player(b, 2, "green");
			}
		}
		System.out.println(winner());
	}

	public static int winner() {
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
		for (int j = 0; j< numOfPlayers; j++) {
			for (int l = 0; l < 7; l+=3) {
				for (int k = 0; k < 7; k+=3) {
					for (int i = 0; i < bigBoard.length; i++) {
						if (smallBoard[i + l][0 + k] == j && smallBoard[i + l][1 + k] == j && smallBoard[i + l][2 + k] == j) bigBoard[l/3][k/3]=j; //player 1/2 wins	//horizontal
						else if (smallBoard[0 + l][0 + k] == j && smallBoard[1 + l][1 + k] == j && smallBoard[2 + l][2 + k] == j) bigBoard[l/3][k/3]=j;	//diagonal
						else if (smallBoard[2 + l][0 + k] == j && smallBoard[1 + l][1 + k] == j && smallBoard[0 + l][2 + k] == j) bigBoard[l/3][k/3]=j;	//other diagonal
						else if (smallBoard[0 + l][i + k] == j && smallBoard[1 + l][i + k] == j && smallBoard[2 + l][i + k] == j) bigBoard[l/3][k/3]=j;	//vertical
					}
				}
			}
		}
	}

	public boolean fullSection() {
		for (int l = 0; l < 7; l+=3) {
			for (int k = 0; k < 7; k+=3) {
				for (int i = 0; i < bigBoard.length; i++) {
					if (smallBoard[0 + l][0 + k] !=0 && smallBoard[0 + l][1 + k] != 0 && smallBoard[0 + l][2 + k] != 0 &&
							smallBoard[1 + l][0 + k] !=0 && smallBoard[1 + l][1 + k] != 0 && smallBoard[1 + l][2 + k] != 0 &&
							smallBoard[2 + l][2 + k] !=0 && smallBoard[2 + l][1 + k] != 0 && smallBoard[2 + l][2 + k] != 0) return true;
				}
			}
		}
		return false;
	}

	public static void highlight (Board b) {
		int addToRow = 0, addToCol = 0;
		switch (section) {
		case 0: addToRow = 0; addToCol = 0; break;
		case 1: addToCol = 3; addToRow = 0; break;
		case 2: addToCol = 6; addToRow = 0; break;
		case 3: addToRow = 3; addToCol = 0; break;
		case 4: addToCol = 3; addToRow = 3; break;
		case 5: addToRow = 3; addToCol = 6; break;
		case 6: addToRow = 6; addToCol = 0; break;
		case 7: addToCol = 3; addToRow = 6; break;
		case 8: addToCol = 6; addToRow = 6; break;
		//default: addToRow = 0; addToCol = 0;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (smallBoard [i + addToRow][j + addToCol] == 0) {
					b.putPeg("red", i + addToRow, j + addToCol);
					smallBoard[i + addToRow][j + addToCol] = 3;
				}
			}
		}
	}
	
	public static void removeHighlight (Board b) {
		for (int i = 0; i < smallBoard.length; i++) {
			for (int j = 0; j < smallBoard[i].length; j++) {
				if (smallBoard[i][j] == 3) {
					b.removePeg(i, j);
					smallBoard[i][j] = 0;
				}
			}
		}
	}

	public static void player(Board b, int playerNum, String colour) {
		boolean hasGone = false;
		//if (section > 0) {
		//	highlight(b);
		//}
		do {
			loc = b.getClick(); //gets click
			col = loc.getCol(); //takes out row and column
			row = loc.getRow();
			int checkSection = (row/3)*3 + col/3;
			System.out.println("p"+section+" and "+checkSection);
			if (smallBoard[row][col] == 0 && (section == checkSection || section < 0)) {
				smallBoard[row][col] = playerNum;
				b.putPeg(colour, row, col); //puts a peg
				hasGone = true;
			}
		} while (!hasGone);
		//removeHighlight(b);
		section = (row%3)*3 + col%3; //dividing by three gives which row it's in and multiplied by three to add previous sections, dividing col by three and adding 1 gives which col it's in
	}

	public static void computer(Board b) {
		try
		{
			Thread.sleep(200);
		}
		catch(InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
		boolean hasGone = false;
		do {
			row = r.nextInt(3) + (section/3)*3; 
			col = r.nextInt(3) + (section%3)*3; //initializes variables
			if (smallBoard[row][col] == 0) {
				smallBoard[row][col] = 2;
				b.putPeg("green", row, col); //puts a peg
				hasGone = true;
			}
		} while (!hasGone);
		section = (row%3)*3 + col%3;
		System.out.println("comp "+section);
	}

}