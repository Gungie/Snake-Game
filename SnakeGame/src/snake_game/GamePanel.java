package snake_game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	
	char direction = 'R'; // R(ight), L(eft), U(p), D(own)
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(this.SCREEN_WIDTH, this.SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(this.DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		
		if(running) {
			//makes a grid to show unit size
			for(int i = 0; i < this.SCREEN_HEIGHT/this.UNIT_SIZE; i++) {
				g.drawLine(i*this.UNIT_SIZE, 0, i*this.UNIT_SIZE, this.SCREEN_HEIGHT);
				g.drawLine(0, i*this.UNIT_SIZE, this.SCREEN_WIDTH, i*this.UNIT_SIZE);
			}
			//end grid
			
			//apple
			g.setColor(Color.red);
			g.fillOval(appleX, this.appleY, this.UNIT_SIZE, this.UNIT_SIZE);
			//apple
			
			//snake colors
			for(int i = 0; i < this.bodyParts; i++) {
				if(i == 0) {//snake head
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], this.UNIT_SIZE, this.UNIT_SIZE);
				}
				else {//snake body
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], this.UNIT_SIZE, this.UNIT_SIZE);
				}
			}
			//end
			
			//score text
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score:"+applesEaten, (this.SCREEN_WIDTH - metrics.stringWidth("Score:"+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	
	public void newApple() {
		this.appleX = random.nextInt((int)(this.SCREEN_WIDTH/this.UNIT_SIZE))*this.UNIT_SIZE;
		this.appleY = random.nextInt((int)(this.SCREEN_HEIGHT/this.UNIT_SIZE))*this.UNIT_SIZE;
	}
	
	public void move() {
		//moving the body of the snake
		for(int i = this.bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		//end
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - this.UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + this.UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - this.UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + this.UNIT_SIZE;
			break;
			
		}
	}
	
	public void checkApple() {
		//check if the head touches apple (eat)
		if((x[0] == appleX) && (y[0] == appleY)) {
			this.bodyParts++;
			this.applesEaten++;
			newApple();
		}
		//end
	}
	
	public void checkCollisions() {
		//checks if head collides with his body
		for(int i = this.bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				this.running = false;
			}
		}
		//end
		
		//check if head touches left border
		if(x[0] < 0)
			this.running = false;
		
		//check if head touches right border
		if(x[0] > this.SCREEN_WIDTH)
			this.running = false;
		
		//check if head touches top border
		if(y[0] < 0)
			this.running = false;
		
		//check if head touches bottom border
		if(y[0] > this.SCREEN_HEIGHT)
			this.running = false;
		
		if(!running)
			timer.stop();
	}
	
	public void gameOver(Graphics g) {
		//score text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score:"+applesEaten, (this.SCREEN_WIDTH - metrics1.stringWidth("Score:"+applesEaten))/2, g.getFont().getSize());
		
		//Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont()); //lining text in the centre of the screen
		g.drawString("Game Over", (this.SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, this.SCREEN_HEIGHT/2); // 2 and 3 parameter places text in the center of the screen
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			this.move();
			this.checkApple();
			this.checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override 
		public void keyPressed(KeyEvent e) {
			//snake controls
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R')
					direction = 'L';
				break;
				
			case KeyEvent.VK_RIGHT:
				if(direction != 'L')
					direction = 'R';
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D')
					direction = 'U';
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U')
					direction = 'D';
				break;
			}
			//end
		}
	}

}
