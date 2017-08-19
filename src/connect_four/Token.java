package connect_four;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Each token to be placed on the game board.
 * 
 * @author Cory Carpenter
 */

public class Token {
	
	private int x;

	private int y;
	
	private char disc; //Can be: ' ', 'R', or 'B'
	
	private boolean isAvailable;
	
	private BufferedImage redTokenImg;
	
	private BufferedImage blueTokenImg;
    
    public int tokenImgWidth;
    
    public int tokenImgHeight;
	
	
	public Token(){
		Initialize();
        LoadContent();
	}
	
	public Token(int x, int y){
		Initialize();
		this.x = x;
		this.y = y;
		LoadContent();
	}
	
	private void Initialize(){
		this.disc = ' ';
		this.x = 0;
		this.y = 0;
		this.isAvailable = true;
	}
	
	private void LoadContent(){
		try{
			URL blueTokenImgURL = this.getClass().getResource("/connect_four/resources/images/BlueToken3.png");
			blueTokenImg = ImageIO.read(blueTokenImgURL);
			
			tokenImgWidth = blueTokenImg.getWidth();
			tokenImgHeight = blueTokenImg.getHeight();
			
			URL redTokenImgURL = this.getClass().getResource("/connect_four/resources/images/RedToken3.png");
			redTokenImg = ImageIO.read(redTokenImgURL);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public void Draw(Graphics2D g2d){
		if(disc == 'B')
			g2d.drawImage(blueTokenImg, x, y, null);
		else if(disc == 'R')
			g2d.drawImage(redTokenImg, x, y, null);
		
	}
	
	public void prepareToken(char playerTurn){
		this.isAvailable = false;
		if(playerTurn == 'B'){
			disc = 'B';
		}
		else{
			disc = 'R';
		}
	}
	
	
	/** 
	 * Getters and Setters 
	 */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public char getDisc() {
		return this.disc;
	}

	public void setDisc(char disc) {
		if(disc == 'B' || disc == 'R' || disc == ' ')
			this.disc = disc;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
}