package connect_four;

import java.awt.Graphics2D;
import java.awt.Point;

import connect_four.Framework.GameState;

/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 *         Cory Carpenter
 */

public class Game {

    private GameBoard gameBoard;

    private GameState gameMode;
    

    public Game(GameState gameMode)
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        this.gameMode = gameMode;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
            }
        };
        
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        gameBoard = new GameBoard();
        
        //Determines if playing vs AI or Player
        if(gameMode == Framework.GameState.PLAYING)
        	Framework.gameState = Framework.GameState.PLAYING;
        else
        	Framework.gameState = Framework.GameState.PLAYING_VS_AI;
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
    	
    }
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
    	gameBoard.Update();
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
    	switch(Framework.gameState){
    		case PLAYING:
    			gameBoard.Draw(g2d);
    		break;
    		case PLAYING_VS_AI:
    			gameBoard.Draw(g2d);
    		break;
    		case OPTIONS:
    			gameBoard.Draw(g2d);
    		break;
    		case GAMEOVER:
    			gameBoard.Draw(g2d);
    	}
    }

    
	/**
	 * Getters and Setters
	 */
    public GameBoard getGameBoard(){
    	return this.gameBoard;
    }
    
    public GameState getGameMode(){
    	return this.gameMode;
    }
}
