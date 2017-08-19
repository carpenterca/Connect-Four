package connect_four;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * The board the game takes place on.
 * 
 * @author Cory Carpenter
 */

public class GameBoard {
	
	private Token[][] tokens = new Token[6][7];
	
	private int[][] result; // Needed for isConsecutiveFour
	
	private BufferedImage gameBoardImg;
	
	private BufferedImage gameBoardBackgroundImg;

	private boolean blueVictory;

	private boolean redVictory;
	
	private char playerTurn = 'B';
	
	private int gameTurns;
	
    public int x;

    public int y;
    
    public int gameBoardImgWidth;
    
    public int gameBoardImgHeight;
	
	
	public GameBoard(){
		Initialize();
        LoadContent();
	}
	
	
	private void Initialize(){
		
		//double tokenXPercentage = 0.020;
		//double tokenYPercentage = 0.045;
		int tokenXTotal = 0;
		int tokenYTotal = 0;
		for(int i = 0; i < tokens.length; i++){
			for(int j = 0; j < tokens[i].length; j++){
				//int tokenX = (int)(Framework.frameWidth * tokenXPercentage);
				//int tokenY = (int)(Framework.frameHeight * tokenYPercentage);
				int tokenX = tokenXTotal;
				int tokenY = tokenYTotal;
				tokens[i][j] = new Token(tokenX, tokenY);
				//tokenXPercentage += 0.1305;
				tokenXTotal += 136;//was 140
			}
			//tokenXPercentage = 0.020;
			//tokenYPercentage += 0.145;
			tokenXTotal = 0;
			tokenYTotal += 110;//was 115
		}
		
		x = 0;
		y = 0;
		
		gameTurns = 1;
	}
	
	private void LoadContent(){
		try
        {
            URL gameBoardBackGroundImgUrl = this.getClass().getResource("/connect_four/resources/images/GameBoardBackGround3.png");
            gameBoardBackgroundImg = ImageIO.read(gameBoardBackGroundImgUrl);
            
            URL gameBoardImgUrl = this.getClass().getResource("/connect_four/resources/images/GameBoard4.png");
            gameBoardImg = ImageIO.read(gameBoardImgUrl);
            gameBoardImgWidth = gameBoardImg.getWidth();
            gameBoardImgHeight = gameBoardImg.getHeight();
        }
        catch (IOException ex) {
            Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public void Draw(Graphics2D g2d)
    {
		g2d.drawImage(gameBoardBackgroundImg, x, y, Framework.frameWidth, Framework.frameHeight, null);
        for(int i = 0; i < tokens.length; i++){
			for(int j = 0; j < tokens[i].length; j++){
				Token singleToken = tokens[i][j];
				if(!singleToken.isAvailable()){
					singleToken.Draw(g2d);
				}
			}
        }
        g2d.drawImage(gameBoardImg, x, y, Framework.frameWidth, Framework.frameHeight, null);
        //testing game over cases
        /**if(isBlueVictory){
        	g2d.drawImage(gameBoardBlueVictoryImg, x, y, Framework.frameWidth, Framework.frameHeight, null);
        }
        else if(isRedVictory){
        	g2d.drawImage(gameBoardRedVictoryImg, x, y, Framework.frameWidth, Framework.frameHeight, null);
        }*/
    }

	public void PrepareToken(Token token){
		if (result != null)
			return; // Game is over
			
        if (token.isAvailable()) {
        	token.prepareToken(playerTurn);

        	if(playerTurn == 'B')
        		playerTurn = 'R';
  			else if(playerTurn == 'R')
  				playerTurn = 'B';
           
            result = isConsecutiveFour(tokens);

            if (result != null) {
            	if(playerTurn == 'B'){
	  				blueVictory = true;
	  				Framework.gameState = Framework.GameState.GAMEOVER;
            	}
  			  	else if(playerTurn == 'R'){
  			  		redVictory = true;
  			  		Framework.gameState = Framework.GameState.GAMEOVER;
  			  	}
            }
		}
	}
	
	public void Update()
    {
		/** AI for computer player
		 * 
		 * Going to leave out diagonal check for AI for now
		 * Also leaving out connect 2 check for now
		 * Need to prioritize winning connect 3 over blocking connect 3
		 * 
		 */
		if(playerTurn == 'R' && Framework.gameState == Framework.GameState.PLAYING_VS_AI){
			
			//place random token
			if(gameTurns == 1){
				Random rnd = new Random();
				int i = 5;
				int j = rnd.nextInt(7);
				if(tokens[i][j].isAvailable()){
					PrepareToken(tokens[i][j]);
					gameTurns++;
				}
			}
			
			//Either win the game or block player from winning
			else if(isConnect3(tokens) != null){
				int[] location = isConnect3(tokens);
				int i = location[0];
				int j = location[1];
				PrepareToken(tokens[i][j]);
				gameTurns++;
			}
			
			//Attacking Computer
			/**else if (isConnect2('R')){
				//add to tokens
			}
			
			//Defending Computer
			else if (isConnect2('B')){
				//block player
			}*/
			
			else{
				//add to singular token
				//or place random token
				for(int i = tokens.length-1; i >= 0; i--){
					for(int j = tokens[i].length-1; j >= 0; j--){
						
						if(tokens[i][j].isAvailable()){
							PrepareToken(tokens[i][j]);
							gameTurns++;
							break;
						}
					}
					if(playerTurn == 'B'){
						break;
					}
				}				
				/**
				 * Place random token placement here
				 * In order to do that have to keep track of which rows and columns are filled
				 *  then increment the base row / column value by 1 if it is filled
				 */
			}
		}
    }
	
	/**
	 * Getters and Setters
	 */
	public boolean isBlueVictory(){
		return blueVictory;
	}
	
	public void setBlueVictory(boolean blueVictory){
		this.blueVictory = blueVictory;
	}
	
	public void setRedVictory(boolean redVictory){
		this.redVictory = redVictory;
	}
	
	public boolean isRedVictory(){
		return redVictory;
	}

	public Token[][] getTokens() {
		return tokens;
	}
	
	public Token getTokens(int i, int j){
		return tokens[i][j];
	}
	
	
	/**
	 * Code From Exercise18_34 for determining if there are 4 in a row
	 */
	
	public static int[][] isConsecutiveFour(Token[][] cells) {
	    char[][] values = new char[cells.length][cells[0].length];

	    for (int i = 0; i < cells.length; i++)
	      for (int j = 0; j < cells[i].length; j++)
	        values[i][j] = cells[i][j].getDisc();

	    return isConsecutiveFour(values);
	  }

	 
	  public static int[][] isConsecutiveFour(char[][] values) {
	    int numberOfRows = values.length;
	    int numberOfColumns = values[0].length;

	    // Check rows
	    for (int i = 0; i < numberOfRows; i++) {
	      if (isConsecutiveFour(values[i]) != null) {
	        int[][] result = new int[4][2];
	        result[0][0] = result[1][0] = result[2][0] = result[3][0] = i;
	        int k = isConsecutiveFour(values[i]);

	        result[0][1] = k; result[1][1] = k + 1;
	        result[2][1] = k + 2; result[3][1] = k + 3;
	  	        return result;

	      }
	    }

	    // Check columns
	    for (int j = 0; j < numberOfColumns; j++) {
	      char[] column = new char[numberOfRows];
	      
	      // Get a column into an array
	      for (int i = 0; i < numberOfRows; i++)
	        column[i] = values[i][j];
	     
	      if (isConsecutiveFour(column) != null) {
	        int[][] result = new int[4][2];
	        result[0][1] = result[1][1] = result[2][1] = result[3][1] = j;
	        int k = isConsecutiveFour(column);

	        result[0][0] = k; result[1][0] = k + 1;
	        result[2][0] = k + 2; result[3][0] = k + 3;
	  
	        return result;       
	      }
	    }
	       
	    // Check major diagonal (lower part)  
	    for (int i = 0; i < numberOfRows - 3; i++) {
	      int numberOfElementsInDiagonal
	        = Math.min(numberOfRows - i, numberOfColumns);    
	      char[] diagonal = new char[numberOfElementsInDiagonal];

	      for (int k = 0; k < numberOfElementsInDiagonal; k++)
	        diagonal[k] = values[k + i][k];
	     
	      if (isConsecutiveFour(diagonal) != null) {
	        int[][] result = new int[4][2];
	        int k = isConsecutiveFour(diagonal);       

	        result[0][0] = k + i;

	        result[1][0] = k + 1 + i;

	        result[2][0] = k + 2 + i;

	        result[3][0] = k + 3 + i;

	        result[0][1] = k;

	        result[1][1] = k + 1;

	        result[2][1] = k + 2;

	        result[3][1] = k + 3;  

	        return result;       
	      }
	    }
	   
	    // Check major diagonal (upper part)
	    for (int j = 1; j < numberOfColumns - 3; j++) {
	      int numberOfElementsInDiagonal
	        = Math.min(numberOfColumns - j, numberOfRows);    
	      char[] diagonal = new char[numberOfElementsInDiagonal];

	      for (int k = 0; k < numberOfElementsInDiagonal; k++)
	        diagonal[k] = values[k][k + j];
	     
	      if (isConsecutiveFour(diagonal) != null) {
	        int[][] result = new int[4][2];
	        int k = isConsecutiveFour(diagonal);       

	        result[0][0] = k;

	        result[1][0] = k + 1;

	        result[2][0] = k + 2;

	        result[3][0] = k + 3;

	        result[0][1] = k + j;

	        result[1][1] = k + 1 + j;

	        result[2][1] = k + 2 + j;

	        result[3][1] = k + 3 + j;  

	        return result;       
	      }
	    }
	 
	    // Check sub-diagonal (left part)
	    for (int j = 3; j < numberOfColumns; j++) {
	      int numberOfElementsInDiagonal
	        = Math.min(j + 1, numberOfRows);    
	      char[] diagonal = new char[numberOfElementsInDiagonal];
	     
	      for (int k = 0; k < numberOfElementsInDiagonal; k++)
	        diagonal[k] = values[k][j - k];
	    
	      if (isConsecutiveFour(diagonal) != null) {
	        int[][] result = new int[4][2];
	        int k = isConsecutiveFour(diagonal);       

	        result[0][0] = k;

	        result[1][0] = k + 1;

	        result[2][0] = k + 2;

	        result[3][0] = k + 3;

	        result[0][1] = j - k;

	        result[1][1] = j - k - 1;

	        result[2][1] = j - k - 2;

	        result[3][1] = j - k - 3;  

	        return result;       
	      }
	    }
	   
	    // Check sub-diagonal (right part)
	    for (int i = 1; i < numberOfRows - 3; i++) {
	      int numberOfElementsInDiagonal
	        = Math.min(numberOfRows - i, numberOfColumns);    
	      char[] diagonal = new char[numberOfElementsInDiagonal];
	   
	      for (int k = 0; k < numberOfElementsInDiagonal; k++)
	        diagonal[k] = values[k + i][numberOfColumns - k - 1];
	   
	      if (isConsecutiveFour(diagonal) != null) {
	        int[][] result = new int[4][2];
	        int k = isConsecutiveFour(diagonal);       

	        result[0][0] = k + i;

	        result[1][0] = k + i + 1;

	        result[2][0] = k + i + 2;

	        result[3][0] = k + i + 3;

	        result[0][1] = numberOfColumns - k - 1;

	        result[1][1] = numberOfColumns - (k + 1) - 1;

	        result[2][1] = numberOfColumns - (k + 2) - 1;

	        result[3][1] = numberOfColumns - (k + 3) - 1;  

	        return result;       
	      }
	    }
	   
	    return null;
	  }

	 
	  public static Integer isConsecutiveFour(char[] values) {   
	    for (int i = 0; i < values.length - 3; i++) {
	      boolean isEqual = true;       
	      for (int j = i; j < i + 3; j++) {
	        if (values[j] == ' ' || values[j] != values[j + 1]) {
	          isEqual = false;
	          break;
	        }
	      }
	    
	      if (isEqual) return new Integer(i);

	    }
	   
	    return null;
	  }
	  
	  
	  /**
	  * Code to aid AI decision process
	  * 3 Tokens in a row
	  */
	  
	  //isConnect3 returns the location [i,j] where the AI should place its token
	  public int[] isConnect3(Token[][] tokens){
		  char[][] values = new char[tokens.length][tokens[0].length];

		    for (int i = 0; i < tokens.length; i++)
		      for (int j = 0; j < tokens[i].length; j++)
		        values[i][j] = tokens[i][j].getDisc();

		    return isConnect3(values);
	  }
	  
	  public int[] isConnect3(char[][] values){
		int numberOfRows = values.length;
	    int numberOfColumns = values[0].length;
	    int[] tokenLocation = new int[2]; //where the AI should place it's token

	    // Check rows
	    for (int i = 0; i < numberOfRows; i++) {
	      if (isConnect3(values[i]) != null) {
	        int[][] result = new int[3][2];
	        result[0][0] = result[1][0] = result[2][0] = tokenLocation[0] = i;
	        int k = isConnect3(values[i]);
	        
	        tokenLocation[1] = k+3;
	        
	        try{
	        	if(tokens[tokenLocation[0]][tokenLocation[1]].isAvailable())
		        	return tokenLocation;
	        } catch (IndexOutOfBoundsException e){ // checks if the value of k+3 is outside the board, then checks other side of k
	        	tokenLocation[1] = k-1;
	        	continue;
	        }
	        finally{
	        	try{
	        		if(tokens[tokenLocation[0]][tokenLocation[1]].isAvailable()){
	    	        	return tokenLocation;
	        		}
	        	} catch (IndexOutOfBoundsException e){
		        	continue;
	        	}
	        }
	      }
	    }

	    // Check columns
	    for (int j = 0; j < numberOfColumns; j++) {
	      char[] column = new char[numberOfRows];
	      
	      // Get a column into an array
	      for (int i = 0; i < numberOfRows; i++)
	        column[i] = values[i][j];
	     
	      if (isConnect3(column) != null) {
	        int[][] result = new int[3][2];
	        result[0][1] = result[1][1] = result[2][1] = tokenLocation[1] = j;
	        int k = isConnect3(column);
	        
	        tokenLocation[0] = k-1;

	        if(tokens[tokenLocation[0]][tokenLocation[1]].isAvailable())
	        	return tokenLocation;
	        else
	        	continue;
	      }
	    }
	       
	    // Check major diagonal (lower part)  
	    /**for (int i = 0; i < numberOfRows - 3; i++) {
	      int numberOfElementsInDiagonal
	        = Math.min(numberOfRows - i, numberOfColumns);    
	      char[] diagonal = new char[numberOfElementsInDiagonal];

	      for (int k = 0; k < numberOfElementsInDiagonal; k++)
	        diagonal[k] = values[k + i][k];
	     
	      if (isConnect3(diagonal) != null) {
	        int[][] result = new int[4][2];
	        int k = isConnect3(diagonal);       

	        result[0][0] = k + i;

	        result[1][0] = k + 1 + i;

	        result[2][0] = k + 2 + i;

	        result[3][0] = k + 3 + i;

	        result[0][1] = k;

	        result[1][1] = k + 1;

	        result[2][1] = k + 2;

	        result[3][1] = k + 3;  

	        return tokenLocation;       
	      }
	    }
	   
	    // Check major diagonal (upper part)
	    for (int j = 1; j < numberOfColumns - 3; j++) {
	      int numberOfElementsInDiagonal
	        = Math.min(numberOfColumns - j, numberOfRows);    
	      char[] diagonal = new char[numberOfElementsInDiagonal];

	      for (int k = 0; k < numberOfElementsInDiagonal; k++)
	        diagonal[k] = values[k][k + j];
	     
	      if (isConnect3(diagonal) != null) {
	        int[][] result = new int[4][2];
	        int k = isConnect3(diagonal);       

	        result[0][0] = k;

	        result[1][0] = k + 1;

	        result[2][0] = k + 2;

	        result[3][0] = k + 3;

	        result[0][1] = k + j;

	        result[1][1] = k + 1 + j;

	        result[2][1] = k + 2 + j;

	        result[3][1] = k + 3 + j;  

	        return tokenLocation;       
	      }
	    }
	 
	    // Check sub-diagonal (left part)
	    for (int j = 3; j < numberOfColumns; j++) {
	      int numberOfElementsInDiagonal
	        = Math.min(j + 1, numberOfRows);    
	      char[] diagonal = new char[numberOfElementsInDiagonal];
	     
	      for (int k = 0; k < numberOfElementsInDiagonal; k++)
	        diagonal[k] = values[k][j - k];
	    
	      if (isConnect3(diagonal) != null) {
	        int[][] result = new int[4][2];
	        int k = isConnect3(diagonal);       

	        result[0][0] = k;

	        result[1][0] = k + 1;

	        result[2][0] = k + 2;

	        result[3][0] = k + 3;

	        result[0][1] = j - k;

	        result[1][1] = j - k - 1;

	        result[2][1] = j - k - 2;

	        result[3][1] = j - k - 3;  

	        return tokenLocation;       
	      }
	    }
	   
	    // Check sub-diagonal (right part)
	    for (int i = 1; i < numberOfRows - 3; i++) {
	      int numberOfElementsInDiagonal
	        = Math.min(numberOfRows - i, numberOfColumns);    
	      char[] diagonal = new char[numberOfElementsInDiagonal];
	   
	      for (int k = 0; k < numberOfElementsInDiagonal; k++)
	        diagonal[k] = values[k + i][numberOfColumns - k - 1];
	   
	      if (isConnect3(diagonal) != null) {
	        int[][] result = new int[4][2];
	        int k = isConnect3(diagonal);       

	        result[0][0] = k + i;

	        result[1][0] = k + i + 1;

	        result[2][0] = k + i + 2;

	        result[3][0] = k + i + 3;

	        result[0][1] = numberOfColumns - k - 1;

	        result[1][1] = numberOfColumns - (k + 1) - 1;

	        result[2][1] = numberOfColumns - (k + 2) - 1;

	        result[3][1] = numberOfColumns - (k + 3) - 1;  

	        return tokenLocation;       
	      }
	    }*/
	   
	    return null;
	  }
	  
	  public static Integer isConnect3(char[] values) {   
		    for (int i = 0; i < values.length - 2; i++) {
		      boolean isEqual = true;       
		      for (int j = i; j < i + 2; j++) {
		        if (values[j] == ' ' || values[j] != values[j + 1]) {
		          isEqual = false;
		          break;
		        }
		      }
		    
		      if (isEqual) return new Integer(i);

		    }
		   
		    return null;
		  }
	  
	  
	  /**
	  * Code to aid AI decision process
	  * 2 Tokens in a row
	  */
	  
	  //public static boolean isConnect2(char player){
		  //return false;
	  //}
	
}