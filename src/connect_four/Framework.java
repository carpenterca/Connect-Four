package connect_four;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;


/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 *         Cory Carpenter
 */

public class Framework extends Canvas {
    
	//Not sure why serializable needs this
	private static final long serialVersionUID = 1L;
	
	/**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 16;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    /**
     * Possible states of the game
     * Removed DESTROYED
     */
    public static enum GameState{VISUALIZING, STARTING, MAIN_MENU, GAME_CONTENT_LOADING, PLAYING, PLAYING_VS_AI, OPTIONS, PAUSED, GAMEOVER}
    
    /**
     * Current state of the game
     */
    public static GameState gameState;
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;
    
    
    /**
     * Image for menu.
     */
    //Main Menu Images
    private BufferedImage gamingGroveImg;
    private BufferedImage titleScreenImg;
    
    private BufferedImage vsAIButtonImg;
    private BufferedImage vsAIButtonImgMouseOver;
    private JButton vsAIButton;
    
    private BufferedImage vsPlayerButtonImg;
    private BufferedImage vsPlayerButtonImgMouseOver;
    private JButton vsPlayerButton;
    
    private BufferedImage exitGameButtonImg;
    private JButton exitGameButton;
    
    private int vsAIButtonWidth;
    private int vsAIButtonHeight;
    private int vsPlayerButtonWidth;
    private int vsPlayerButtonHeight;
    private int exitGameButtonWidth;
    private int exitGameButtonHeight;
    
    //In Game Menu Images
    private BufferedImage inGameMenuImg;
    
    private BufferedImage restartButtonImg;
    private JButton restartButton;
    
    private BufferedImage quitButtonImg;
    private JButton quitButton;
    
    private int restartButtonWidth;
    private int restartButtonHeight;
    private int quitButtonWidth;
    private int quitButtonHeight;
    
    //Game Over Menu Images
    private BufferedImage playAgainButtonImg;
    private JButton playAgainButton;
    private BufferedImage quitButton2Img;
    private JButton quitButton2;
    
    private int playAgainButtonWidth;
    private int playAgainButtonHeight;
    private int quitButton2Width;
    private int quitButton2Height;
    
	int mouseX;
	int mouseY;
    
    
    public Framework ()
    {
        super();
        
        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class.
     */
    private void Initialize()
    {
    	
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class.
     */
    private void LoadContent()
    {
        try
        {
        	//Main Menu Objects    	
        	URL gamingGroveImgUrl = this.getClass().getResource("/connect_four/resources/images/GamingGrove.png");
            gamingGroveImg = ImageIO.read(gamingGroveImgUrl);
            
            URL titleScreenImgUrl = this.getClass().getResource("/connect_four/resources/images/GameTitle.png");
            titleScreenImg = ImageIO.read(titleScreenImgUrl);
            
            URL vsPlayerButtonImgUrl = this.getClass().getResource("/connect_four/resources/images/VS_Player_Button.png");
            vsPlayerButtonImg = ImageIO.read(vsPlayerButtonImgUrl);
            
            URL vsPlayerButtonImgMouseOverUrl = this.getClass().getResource("/connect_four/resources/images/VS_Player_Button_Glow.png");
            vsPlayerButtonImgMouseOver = ImageIO.read(vsPlayerButtonImgMouseOverUrl);
            
            URL vsAIButtonImgUrl = this.getClass().getResource("/connect_four/resources/images/VS_AI_Button.png");
            vsAIButtonImg = ImageIO.read(vsAIButtonImgUrl);
            
            URL vsAIButtonImgMouseOverUrl = this.getClass().getResource("/connect_four/resources/images/VS_AI_Button_Glow.png");
            vsAIButtonImgMouseOver = ImageIO.read(vsAIButtonImgMouseOverUrl);
            
            URL exitGameButtonUrl = this.getClass().getResource("/connect_four/resources/images/Exit_Button.png");
            exitGameButtonImg = ImageIO.read(exitGameButtonUrl);
            
            
            vsAIButtonWidth = (int)(frameWidth * 0.65);
            vsAIButtonHeight = (int)(frameHeight * 0.60);
            vsPlayerButtonWidth = (int)(frameWidth * 0.02);
            vsPlayerButtonHeight = (int)(frameHeight * 0.60);
            exitGameButtonWidth = (int)(frameWidth * 0.45);
            exitGameButtonHeight = (int)(frameHeight * 0.75);
            
            vsAIButton = new JButton(new ImageIcon(vsAIButtonImg));
            vsAIButton.setBorder(BorderFactory.createEmptyBorder());
            vsAIButton.setContentAreaFilled(false);
            vsAIButton.setSize(vsAIButtonImg.getWidth(), vsAIButtonImg.getHeight());
            vsAIButton.setLocation(vsAIButtonWidth, vsAIButtonHeight);
            
            vsAIButton.addMouseListener(new MouseListener() {
            	  @Override
            	  public void mouseClicked(MouseEvent e) {
            	    newGame(GameState.PLAYING_VS_AI);
            	  }

				@Override
				public void mouseEntered(MouseEvent arg0) {
					vsAIButton.setIcon(new ImageIcon(vsAIButtonImgMouseOver));
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					vsAIButton.setIcon(new ImageIcon(vsAIButtonImg));
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
            	});
            
            vsPlayerButton = new JButton(new ImageIcon(vsPlayerButtonImg));
            vsPlayerButton.setBorder(BorderFactory.createEmptyBorder());
            vsPlayerButton.setContentAreaFilled(false);
            vsPlayerButton.setSize(vsPlayerButtonImg.getWidth(), vsPlayerButtonImg.getHeight());
            vsPlayerButton.setLocation(vsPlayerButtonWidth, vsPlayerButtonHeight);
            
            vsPlayerButton.addMouseListener(new MouseListener() {
            	@Override
          	  	public void mouseClicked(MouseEvent e) {
            		newGame(GameState.PLAYING);
          	  	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					vsPlayerButton.setIcon(new ImageIcon(vsPlayerButtonImgMouseOver));
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					vsPlayerButton.setIcon(new ImageIcon(vsPlayerButtonImg));
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
          	});
            
            //Does not allow for in game menu to open during first run if exit button is in game
            // removed for now
    	    //For some reason need to reset focus to the pannel to detect keyboard input
            /**exitGameButton = new JButton(new ImageIcon(exitGameButtonImg));
            exitGameButton.setBorder(BorderFactory.createEmptyBorder());
            exitGameButton.setContentAreaFilled(false);
            exitGameButton.setSize(exitGameButtonImg.getWidth(), exitGameButtonImg.getHeight());
            exitGameButton.setLocation(exitGameButtonWidth, exitGameButtonHeight);
            
            exitGameButton.addMouseListener(new MouseListener() {
            	@Override
          	  	public void mouseClicked(MouseEvent e) {
            		System.exit(0);
          	  	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					//vsPlayerButton.setIcon(new ImageIcon(vsPlayerButtonImgMouseOver));
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					//vsPlayerButton.setIcon(new ImageIcon(vsPlayerButtonImg));
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
          	});*/
            
            //InGame Menu Objects
            /**
             * Buttons were causing an issue, switching their codes around worked
             * restartButton is the quit button
             * quitButton is the restart button
             */
            URL inGameMenuImgUrl = this.getClass().getResource("/connect_four/resources/images/InGameOptions.png");
            inGameMenuImg = ImageIO.read(inGameMenuImgUrl);
            
            URL restartButtonImgUrl = this.getClass().getResource("/connect_four/resources/images/Restart_Button.png");
            restartButtonImg = ImageIO.read(restartButtonImgUrl);
            
            URL quitButtonImgUrl = this.getClass().getResource("/connect_four/resources/images/Quit_Button.png");
            quitButtonImg = ImageIO.read(quitButtonImgUrl);
            
			restartButtonWidth = (int)(Framework.frameWidth * 0.320);
			restartButtonHeight = (int)(Framework.frameHeight * 0.200);
			quitButtonWidth = (int)(Framework.frameWidth * 0.360);
			quitButtonHeight = (int)(Framework.frameHeight * 0.600);
			
			restartButton = new JButton(new ImageIcon(quitButtonImg));
            restartButton.setBorder(BorderFactory.createEmptyBorder());
            restartButton.setContentAreaFilled(false);
            restartButton.setSize(quitButtonImg.getWidth(), quitButtonImg.getHeight());
            restartButton.setLocation(quitButtonWidth, quitButtonHeight);
            
            restartButton.addMouseListener(new MouseListener() {
          	  	@Override
          	  	public void mouseClicked(MouseEvent e) {
          	  		leaveMenu();
          	  		gameState = GameState.STARTING;
          	  	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
          	});
            
            quitButton = new JButton(new ImageIcon(restartButtonImg));
            quitButton.setBorder(BorderFactory.createEmptyBorder());
            quitButton.setContentAreaFilled(false);
            quitButton.setSize(restartButtonImg.getWidth(), restartButtonImg.getHeight());
            quitButton.setLocation(restartButtonWidth, restartButtonHeight);
            
            quitButton.addMouseListener(new MouseListener() {
          	  	@Override
          	  	public void mouseClicked(MouseEvent e) {
          	  		leaveMenu();
          	  		newGame(game.getGameMode());
          	  	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
          	});
            
            
            //Game Over Menu
            /**
             * Buttons were causing an issue, switching their codes around worked
             * playAgainButton is the quit button
             * quitButton2 is the play again button
             */
            URL playAgainButtonImgUrl = this.getClass().getResource("/connect_four/resources/images/Play_Again_Button.png");
            playAgainButtonImg = ImageIO.read(playAgainButtonImgUrl);
            
            URL QuitButton2ImgUrl = this.getClass().getResource("/connect_four/resources/images/Quit_Button2.png");
            quitButton2Img = ImageIO.read(QuitButton2ImgUrl);
            
            //resize
            playAgainButtonWidth = (int)(Framework.frameWidth * 0.320);
            playAgainButtonHeight = (int)(Framework.frameHeight * 0.200);
            quitButton2Width = (int)(Framework.frameWidth * 0.360);
            quitButton2Height =  (int)(Framework.frameHeight * 0.600);
            
            playAgainButton = new JButton(new ImageIcon(quitButton2Img));
            playAgainButton.setBorder(BorderFactory.createEmptyBorder());
            playAgainButton.setContentAreaFilled(false);
            playAgainButton.setSize(quitButton2Img.getWidth(), quitButton2Img.getHeight());
            playAgainButton.setLocation(quitButton2Width, quitButton2Height);
            
            playAgainButton.addMouseListener(new MouseListener() {
          	  	@Override
          	  	public void mouseClicked(MouseEvent e) {
          	  		leaveGameOver();
          	  		gameState = GameState.STARTING;
          	  	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
          	});
            
            quitButton2 = new JButton(new ImageIcon(playAgainButtonImg));
            quitButton2.setBorder(BorderFactory.createEmptyBorder());
            quitButton2.setContentAreaFilled(false);
            quitButton2.setSize(playAgainButtonImg.getWidth(), playAgainButtonImg.getHeight());
            quitButton2.setLocation(playAgainButtonWidth, playAgainButtonHeight);
            
            quitButton2.addMouseListener(new MouseListener() {
          	  	@Override
          	  	public void mouseClicked(MouseEvent e) {
          	  		leaveGameOver();
          	  		newGame(game.getGameMode());
          	  	}

				@Override
				public void mouseEntered(MouseEvent arg0) {
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
          	});
            
			//Add MainMenu Objects
            add(vsPlayerButton);
            add(vsAIButton);
            //add(exitGameButton);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
            	case VISUALIZING:
            		// On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px). 
            		// So we wait one second for the window/frame to be set to its correct size. Just in case we
            		// also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
            		// so that we although get approximately size.
            		if(this.getWidth() > 1 && visualizingTime > secInNanosec)
            		{
            			frameWidth = this.getWidth();
            			frameHeight = this.getHeight();

            			// When we get size of frame we change status.
            			gameState = GameState.STARTING;
            		}
            		else
            		{
            			visualizingTime += System.nanoTime() - lastVisualizingTime;
            			lastVisualizingTime = System.nanoTime();
            		}
            	break;
                case STARTING:
                    // Sets variables and objects.
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();
                    
                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                break;
                case MAIN_MENU:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
                break;
                case PLAYING_VS_AI:
                	//copied from PLAYING
                	gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
                break;
                case OPTIONS:
                    //...
                break;
                case PAUSED:
                	//...
                break;
                case GAMEOVER:
                    //...
                break;
            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
        	case VISUALIZING:
        		//...
        	break;
            case STARTING:
            	//...
            break;
            case MAIN_MENU:
            	g2d.drawImage(gamingGroveImg, 0, 0, frameWidth, frameHeight, null);         	
            	//get the program to pause and display the logo image for about 2-3 seconds
            	//then move on to the next image
                g2d.drawImage(titleScreenImg, 0, 0, frameWidth, frameHeight, null);
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
            case PLAYING:
                game.Draw(g2d, mousePosition());
            break;
            case PLAYING_VS_AI:
            	game.Draw(g2d, mousePosition());
            break;
            case OPTIONS:
            	game.Draw(g2d, mousePosition());
            	g2d.drawImage(inGameMenuImg, (int)(Framework.frameWidth * 0.250), (int)(Framework.frameHeight * 0.185), null);
            break;
            case PAUSED:
            	//...
            break;
            case GAMEOVER:
                //game.DrawGameOver(g2d, mousePosition(), gameTime);
            	game.Draw(g2d, mousePosition());
            	if(game.getGameBoard().isBlueVictory() || game.getGameBoard().isRedVictory()){
            		game.getGameBoard().setBlueVictory(false);
            		game.getGameBoard().setRedVictory(false);
            		goToGameOver();
            	}
            break;
        }
    }
    
    /**
     * Starts new game.
     */
    private void newGame(GameState gameMode)
    {
    	if(this.gameState == GameState.MAIN_MENU){
    		remove(vsAIButton);
    		remove(vsPlayerButton);
    		//remove(exitGameButton);
    	}
    	
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game(gameMode);
    }
    
    /**
     * Goes to the menu screen while in a game to exit match, close game, or change options
     */
    private void goToMenu(){
    	gameState = GameState.OPTIONS;
    	//game.InGameMenu();
    	add(restartButton);
        add(quitButton);
    }
    
    private void leaveMenu(){
    	//game.OutOfMenu();
    	remove(restartButton);
    	remove(quitButton);
    }
    
    private void goToGameOver(){
    	add(playAgainButton);
    	add(quitButton2);
    }
    
    private void leaveGameOver(){
    	remove(playAgainButton);
    	remove(quitButton2);
    }
    
    /**
     * If the column was clicked it will place a token in that column
     */
    private void columnClicked(int clickedColumn){
		for(int row = 5, column = clickedColumn; row >= 0; row--){
			Token currentTokenToCheck = game.getGameBoard().getTokens(row, column);
			if(currentTokenToCheck.isAvailable()){
				game.getGameBoard().PrepareToken(currentTokenToCheck);
				break;
			}
		}
    }
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        switch (gameState)
        {
            case MAIN_MENU:
            	//...
            break;
            case PLAYING:
            	if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            		goToMenu();
            break;
            case PLAYING_VS_AI:
            	if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            		goToMenu();
            break;
            case OPTIONS:
            	if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            		leaveMenu();
            		if(game.getGameMode() == GameState.PLAYING)
            			gameState = GameState.PLAYING;
            		else if(game.getGameMode() == GameState.PLAYING_VS_AI)
            			gameState = GameState.PLAYING_VS_AI;
            	}
            break;
            case PAUSED:
            	//...
            break;
            case GAMEOVER:
                //...
            break;
        }
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
    	switch (gameState)
        {
            case MAIN_MENU:
            	//...
            break;
            case PLAYING:
            	if(e.getButton() == MouseEvent.BUTTON1){
            		mouseX = e.getX();
            		mouseY = e.getY();

            		//If intend to allow game resolution change need to alter the pixel variables for mouse position
            		if(mouseX >= 25 && mouseX <= 110 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(0);
            		}
            		else if(mouseX >= 145 && mouseX <= 255 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(1);
            		}
            		else if(mouseX >= 275 && mouseX <= 385 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(2);
            		}
            		else if(mouseX >= 410 && mouseX <= 525 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(3);
            		}
            		else if(mouseX >= 550 && mouseX <= 665 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(4);
            		}
            		else if(mouseX >= 685 && mouseX <= 805 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(5);
            		}
            		else if(mouseX >= 820 && mouseX <= 930 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(6);
            		}
            	}
            break;
            case PLAYING_VS_AI:
            	//copied from PLAYING
            	if(e.getButton() == MouseEvent.BUTTON1){
            		mouseX = e.getX();
            		mouseY = e.getY();

            		if(mouseX >= 25 && mouseX <= 110 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(0);
            		}
            		else if(mouseX >= 145 && mouseX <= 255 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(1);
            		}
            		else if(mouseX >= 275 && mouseX <= 385 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(2);
            		}
            		else if(mouseX >= 410 && mouseX <= 525 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(3);
            		}
            		else if(mouseX >= 550 && mouseX <= 665 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(4);
            		}
            		else if(mouseX >= 685 && mouseX <= 805 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(5);
            		}
            		else if(mouseX >= 820 && mouseX <= 930 && mouseY >= 10 && mouseY <= 640){
            			columnClicked(6);
            		}
            	}
            break;
            case OPTIONS:
            	//...
            break;
            case PAUSED:
            	//...
            break;
            case GAMEOVER:
            	//...
            break;
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e){
    	switch (gameState)
        {
            case MAIN_MENU:
            	//...
            break;
            case PLAYING:
            	//...
            break;
            case PLAYING_VS_AI:
            	//...
            break;
            case OPTIONS:
            	//...
            break;
            case PAUSED:
            	//...
            break;
            case GAMEOVER:
            	//...
            break;
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e){
    	switch (gameState)
        {
            case MAIN_MENU:
            	//...
            break;
            case PLAYING:
            	//...
            break;
            case PLAYING_VS_AI:
            	//...
            break;
            case OPTIONS:
            	//...
            break;
            case PAUSED:
            	//...
            break;
            case GAMEOVER:
            	//...
            break;
        }
    }
}
