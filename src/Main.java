import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Main extends JFrame
{
	
	private String		player				= "X";
	static String[][]	board					= new String[3][3];
	JPanel						outline				= new JPanel();
	JButton[]					tiles					= new JButton[9];
	static boolean		aiPlay, aiiPlay, aiTurn, aiiTurn = false;
	
	// blank panel
	JPanel						blank					= new JPanel();
	// win counters
	JLabel						p1Wins				= new JLabel("Wins: 0");
	JLabel						p2Wins				= new JLabel("Wins: 0");
	JLabel						ties					= new JLabel("Ties: 0");
	// panel for the reset and new game buttons
	JPanel						resetPanel		= new JPanel();
	JButton						reset					= new JButton("Reset Stats");
	JButton						newGame				= new JButton("New Game");
	// panel for the player labels and AI selection
	JPanel						user					= new JPanel();
	JPanel						first					= new JPanel();
	JPanel						second				= new JPanel();
	JLabel						p1						= new JLabel("Player 1 (X)");
	JLabel						p2						= new JLabel("Player 2 (O)");
	// need a different AI and difficulty box for each player
	JCheckBox					ai						= new JCheckBox("AI");
	JCheckBox					aii						= new JCheckBox("AI");
	String[]					difficulties	= { "novice", "intermediate", "expert",
			"legendary"								};
	JComboBox<String>	difficulty		= new JComboBox<String>(difficulties);
	JComboBox<String>	difficultyy		= new JComboBox<String>(difficulties);
	// panel at the bottom that displays the winner
	JPanel						w							= new JPanel();
	JLabel						winLabel			= new JLabel(" ");
	// button that stores the last move
	JButton						last					= new JButton();
	
	public static void main(String[] args)
	{
		Main frame = new Main();
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
// while(aiPlay){System.out.println(true);}
// while(!aiPlay){System.out.println(false);}
	}
	
	public Main()
	{
		
		difficulty.setEnabled(false);
		difficultyy.setEnabled(false);
		user.setLayout(new GridLayout(5, 1));
		first.setLayout(new GridLayout(1, 3));
		first.add(p1);
		first.add(ai);
		first.add(difficulty);
		user.add(first);
		user.add(p1Wins);
		second.setLayout(new GridLayout(1, 3));
		second.add(p2);
		second.add(aii);
		second.add(difficultyy);
		user.add(second);
		user.add(p2Wins);
		resetPanel.setLayout(new GridLayout(1, 3));
		resetPanel.add(ties);
		resetPanel.add(newGame);
		resetPanel.add(reset);
		user.add(resetPanel);
		add(user, BorderLayout.EAST);
		outline.setLayout(new GridLayout(3, 3));
		outline.setPreferredSize(new Dimension(180, 180));
		w.add(winLabel, BorderLayout.CENTER);
		add(w, BorderLayout.SOUTH);
		
		// sets up the buttons
		for (int i = 0; i < 9; i++)
		{
			tiles[i] = new JButton(" ");
			tiles[i].setSize(80, 80);
			outline.add(tiles[i]);
			tiles[i].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton b = (JButton) e.getSource();
					if (b.getText() == " ")
					{
						b.setText(player);
						player = player == "X" ? "O" : "X";
					}
					b.setForeground(Color.red);
					last.setForeground(Color.black);
					last = b;
					update();
					setWin();
					aiTurn = true;
					aiiTurn = true;
					if (tiles[0].isEnabled())
					{
						moveAI();
					}
					AI.evaluate();
					
					// if(difficultyy.isEnabled() &&
					// difficultyy.getSelectedItem() == "novice"){
				}
			});
			
		}
		add(outline, BorderLayout.CENTER);
		
		// listener for the AI checkbox
		ai.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				difficulty.setEnabled(!difficulty.isEnabled());
			}
		});
		aii.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				difficultyy.setEnabled(!difficultyy.isEnabled());
			}
		});
		
		// listener for new game button
		newGame.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for (JButton b : tiles)
				{
					b.setText(" ");
					b.setEnabled(true);
					player = "X";
				}
				update();
				winLabel.setText(" ");
				aiPlay = difficulty.isEnabled() ? true : false;
				aiiPlay = difficultyy.isEnabled() ? true : false;
				if (aiPlay) aiTurn = true;
				aiiTurn = false;
				moveAI();
			}
		});
		
		// listener for the reset button
		reset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setStats(ties, true);
				setStats(p1Wins, true);
				setStats(p2Wins, true);
			}
		});
	}
	
	// sets the buttons to the board array (for AI)
	public void update()
	{
		int y = 0;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				// tiles[y] = new JButton(board.getTile(i, j));
				board[i][j] = tiles[y].getText();
// System.out.print("[" + board[i][j] + "] ");
				y++;
			}
		}
	}
	
	// checks if there is a winner
	public static String winCheck(String[][] board)
	{
		if (board[1][1] != " ")
		{
			String middle = board[1][1];
			if (middle == board[0][0] && middle == board[2][2]) return board[1][1];
			if (middle == board[0][2] && middle == board[2][0]) return board[1][1];
			if (middle == board[0][1] && middle == board[2][1]) return board[1][1];
			if (middle == board[1][0] && middle == board[1][2]) return board[1][1];
		}
		if (board[0][0] == board[0][1] && board[0][1] == board[0][2]
				&& board[0][0] != " ") return board[0][0];
		if (board[2][0] == board[2][1] && board[2][1] == board[2][2]
				&& board[2][0] != " ") return board[2][0];
		if (board[0][0] == board[1][0] && board[1][0] == board[2][0]
				&& board[2][0] != " ") return board[0][0];
		if (board[0][2] == board[1][2] && board[1][2] == board[2][2]
				&& board[2][2] != " ") return board[0][2];
		for (String[] s : board)
		{
			for (String b : s)
			{
				if (b == " ") return "";
			}
		}
		return " ";
	}
	
	public void setWin()
	{
		String winner = winCheck(board);
		if (winner != "")
		{
			for (JButton button : tiles)
			{
				button.setEnabled(false);
			}
			if (winner == " ")
			{
				winLabel.setText("The game is a tie!");
				setStats(ties, false);
			}
			else
			{
				player = player == "X" ? "O" : "X";
				winLabel.setText(player + " is the winner!");
				if (winner == "X")
				{
					setStats(p1Wins, false);
				}
				else
				{
					setStats(p2Wins, false);
				}
			}
		}
	}
	
	public void moveAI()
	{
		if (aiiPlay && aiiTurn)
		{
//			System.out.println(difficultyy.getSelectedItem());
			if (difficultyy.getSelectedItem() == "novice")
			{
				tiles[AI.novice()].setText("O");
			}
			else if (difficultyy.getSelectedItem() == "intermediate")
			{
				tiles[AI.intermediate("O")].setText("O");
			}
			else if (difficultyy.getSelectedItem() == "expert")
			{
				tiles[AI.expert("O")].setText("O");
			}
			else
			{
				tiles[AI.legendary("O")].setText("O");
			}
			player = "X";
			aiiTurn = false;
			update();
			setWin();
			if (aiPlay && tiles[0].isEnabled())
			{
				aiTurn = true;
				moveAI();
			}
		}
		
		if (aiPlay && aiTurn)
		{
//			System.out.println(difficultyy.getSelectedItem());
			if (difficulty.getSelectedItem() == "novice")
			{
				tiles[AI.novice()].setText("X");
			}
			else if (difficulty.getSelectedItem() == "intermediate")
			{
				tiles[AI.intermediate("X")].setText("X");
			}
			else if (difficulty.getSelectedItem() == "expert")
			{
				tiles[AI.expert("X")].setText("X");
			}
			else
			{
				tiles[AI.legendary("X")].setText("X");
			}
			player = "O";
			aiTurn = false;
			update();
			setWin();
			if (aiiPlay && tiles[0].isEnabled())
			{
				aiiTurn = true;
				moveAI();
			}
		}
	}
	
	// sets the win stats for player 1, player 2, ties
	public void setStats(JLabel textName, boolean reset)
	{
		String a[] = textName.getText().trim().split(":");
		a[1] = a[1].trim();
		int i = reset ? 0 : Integer.parseInt(a[1]) + 1;
		textName.setText(a[0] + ": " + i);
	}
	
	public String getPlayer()
	{
		return player;
	}
	
	public boolean setPlayer(String player)
	{
		if (player == "X" || player == "O")
		{
			this.player = player;
			return true;
		}
		return false;
	}
}
