public class AI
{
	
	private static String[][]	board	= Main.board;
	
	public static int novice()
	{
		int row, col = 0;
		do
		{
			row = (int) (Math.random() * 3);
			col = (int) (Math.random() * 3);
		}
		while (board[row][col] != " ");
		
		return row * 3 + col;
	}
	
	public static int badIntermediate(String player)
	{
		String temp;
		String notPlayer = player == "X" ? "O" : "X";
		for (int row = 0; row < board.length; row++)
		{
			for (int col = 0; col < board[row].length; col++)
			{
				temp = board[row][col];
				if (board[row][col] == " ")
				{
					board[row][col] = player;
					if (Main.winCheck(board) == player)
					{
						return row * 3 + col;
					}
				}
				board[row][col] = temp;
			}
			
		}
		for (int row = 0; row < board.length; row++)
		{
			for (int col = 0; col < board[row].length; col++)
			{
				temp = board[row][col];
				if (board[row][col] == " ")
				{
					board[row][col] = notPlayer;
					if (Main.winCheck(board) == notPlayer)
					{
						return row * 3 + col;
					}
				}
				board[row][col] = temp;
			}
		}
		return novice();
	}
	
//Uses the heuristic evaluation, with a seed of 1
	public static int intermediate(String player)
	{
		int[] move = {0,0,0};
		move = bestMove(player, 1);
//		System.out.println("Best move: "+ move[0]+", "+move[1]+", "+move[2]);
		return move[1]*3 + move[2];
	}
	
	public static int expert(String player)
	{
		int[] move = {0,0,0};
		move = bestMove(player, 2);
//		System.out.println("Best move: "+ move[0]+", "+move[1]+", "+move[2]);
		return move[1]*3 + move[2];
	}
	
	public static int legendary(String player)
	{
		int[] move = {0,0,0};
		move = bestMove(player, 3);
		System.out.println("Best move: "+ move[0]+", "+move[1]+", "+move[2]);
		return move[1]*3 + move[2];
	}
	
	public static int evaluate()
	{
		return evaluate(board, "X");
	}
	
	// heuristic board evaluation (determines the net score of the board)
	// 10, 100, 1000 for 1, 2, 3 in a row (negative for opposing player)
	public static int evaluate(String[][] board, String player)
	{
		String notPlayer = player == "X" ? "O" : "X";
		int score = 0;
		int p = 0;
		int np = 0;
//		System.out.println("Horizontal");
		for (int row = 0; row < board.length; row++)
		{
			for (int col = 0; col < board[row].length; col++)
			{
				if (board[row][col] == " ")
				{
					continue;
				}
				if (board[row][col] == player)
				{
					p++;
				}
				if (board[row][col] == notPlayer)
				{
					np++;
				}
				if (p != 0 && np != 0)
				{
					p = 0;
					np = 0;
					break;
				}
			}
			
//			System.out.print(p + ": " + np);
			score += heurFoo(p) - heurFoo(np);
//			System.out.println(": " + score);
			p = 0;
			np = 0;
		}
//		System.out.println("Vertical");
		for (int col = 0; col < board.length; col++)
		{
			for (int row = 0; row < board[col].length; row++)
			{
				if (board[row][col] == " ")
				{
					continue;
				}
				if (board[row][col] == player)
				{
					p++;
				}
				if (board[row][col] == notPlayer)
				{
					np++;
				}
				if (p != 0 && np != 0)
				{
					p = 0;
					np = 0;
					break;
				}
			}
//			System.out.print(p + ": " + np);
			score += heurFoo(p) - heurFoo(np);
//			System.out.println(": " + score);
			p = 0;
			np = 0;
		}
		
//		System.out.println("Diagonal (L/R)");
		for (int n = 0; n < board.length; n++)
		{
			if (board[n][n] == " ")
			{
				continue;
			}
			if (board[n][n] == player)
			{
				p++;
			}
			if (board[n][n] == notPlayer)
			{
				np++;
			}
			if (p != 0 && np != 0)
			{
				p = 0;
				np = 0;
				break;
			}
		}
//		System.out.print(p + ": " + np);
		score += heurFoo(p) - heurFoo(np);
//		System.out.println(": " + score);
		p = 0;
		np = 0;
		
//		System.out.println("Diagonal (R/L)");
		for (int n = 0, j = 2; n < board.length; n++, j--)
		{
			if (board[j][n] == " ")
			{
				continue;
			}
			if (board[j][n] == player)
			{
				p++;
			}
			if (board[j][n] == notPlayer)
			{
				np++;
			}
			if (p != 0 && np != 0)
			{
				p = 0;
				np = 0;
				break;
			}
		}
//		System.out.print(p + ": " + np);
		score += heurFoo(p) - heurFoo(np);
//		System.out.println(": " + score);
		p = 0;
		np = 0;
		
//		System.out.println("Score: " + score);
		return score;
	}
	
	// maps 0,1,2,3 to 0,1,10,100 for heuristic evaluation
	// former is the amount of same-player pieces the row, latter is the allocated
	// points
	public static int heurFoo(int n)
	{
		double x = n;
		return (int) (Math.round(73. / 6 * (Math.pow(x, 3)) - 65. / 2
				* (Math.pow(x, 2)) + 64. / 3 * (x)));
	}
	
//evaluate the board and place the piece on the best possible spot. Seed is how deep the recursion goes.
//neg parameter is for switching the evaluation to negative for an opposing player's move
	public static int[] bestMove(String player, int seed)
	{
		String notPlayer = player == "X" ? "O" : "X";
		int tempScore = 0; 
//	array to return. Contains score, row, column
		int[] results = {-1000,0,0};
		int[] recResult = {0,0,0};
		for(int row = 0; row < board.length; row++)
		{
			for(int col = 0; col < board[row].length; col++)
			{
				
				if(board[row][col] == " ")
				{
					board[row][col] = player;
					tempScore = evaluate(board, player);
//					System.out.println("-Score "+ row +", "+ col +": "+ tempScore);
					if(seed > 1)
					{
						recResult = bestMove(notPlayer, seed-1);
						tempScore -= recResult[0];
					}
					if(tempScore > results[0]) {results[0] = tempScore; results[1] = row; results[2] = col;}
					else if(tempScore == results[0] && Math.random() > .5) {results[0] = tempScore; results[1] = row; results[2] = col;}
					board[row][col] = " ";
				}
			}
		}
//		System.out.println("Best move: "+ results[0]+", "+results[1]+", "+results[2]+", "+ player+", "+ seed +", "+ neg);
		return results;
	}
}
