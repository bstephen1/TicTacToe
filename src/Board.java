import javax.swing.*;

public class Board {
	
	private String[][] tiles = new String[3][3];
	
	public Board(){}
	
	public void setBoard(JButton[] buttons){
		int y = 0;
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				tiles[i][j] = buttons[y].getText();
				y++;
			}
		}
	}

	public String getTile(int row, int col){
		return tiles[row][col];
	}
	
	public void setTile(String tile, int row, int col){
		tiles[row][col] = tile;
	}

	public void novice(){
		int row = (int)Math.floor(Math.random()*3);
		int col = (int)Math.floor(Math.random()*3);
		while(tiles[row][col] == ""){
			row = (int)Math.floor(Math.random()*3);
			col = (int)Math.floor(Math.random()*3);
		}
		setTile("o", row, col);
	}
	
}
