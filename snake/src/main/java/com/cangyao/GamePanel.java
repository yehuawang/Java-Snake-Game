package com.cangyao;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

  static final int SCREEN_WIDTH = 600;
  static final int SCREEN_HEIGHT = 600;
  static final int UNIT_SIZE = 25;
  static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

  static final int DELAY = 100;

  // holding all x and y cordinates of the snake.
  final int x[] = new int[GAME_UNITS];
  final int y[] = new int[GAME_UNITS];

  // initial number of body blocks of the snake, food eaten,.. some basic properties of the snake.
  int bodyLength = 6;
  int foodEaten = 0;
  int foodX; // x coordinate of the food
  int foodY; // y coordinate of the food
  char direction = 'R'; // initial direction of the snake, R->right, L->left, U->up, D->down
  boolean running = false; // game is running or not

  Timer timer;
  Random random;

  // constructor
  GamePanel() {
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdaptor());
    startGame();
  }

  public void startGame() {
    initializeSnakePosition();
    newFood();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    
    if (running) {
      drawInGame(g);
      drawScore(g);
    }
    else {
      drawGameOver(g);;
    }
    
  }

  // draw the game scene while in game
  public void drawInGame(Graphics g) {
    // helper code to show the grid for development purposes 
    // g.setColor(Color.GRAY);
    // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++ ) {
    //   g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
    // }
    // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
    //   g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
    // }

    // drawing food
    g.setColor(Color.RED);
    g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

    // drawthe snake with cool gradient.
    Graphics2D g2d = (Graphics2D) g;
    for (int i = 0; i < bodyLength; i++) {
      int alpha = 255 - (i * (255 / bodyLength));
      alpha = Math.max(alpha, 50);
      g2d.setColor(new Color(0, 255, 0, alpha));
      g2d.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
    }
  }

  // draw the start scene before starting new game
  public void drawStartNewGame(Graphics g) {

  }

  // draw the game over scene after player died
  public void drawGameOver(Graphics g) {
    g.setColor(Color.RED);
    try (InputStream is = getClass().getResourceAsStream("/fonts/KodeMono.ttf")) {
      if (is == null) {
        System.out.println("font not found");
      }
      Font kodeMono = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(75f);
      // System.out.println("font loaded");
      // GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      // ge.registerFont(kodeMono);
      g.setFont(kodeMono);
    } catch (Exception e) {
      e.printStackTrace();
      g.setFont(new Font("Arial", Font.BOLD, 75));
    }
    FontMetrics metrics = getFontMetrics(g.getFont());
    g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 3);
    try (InputStream is = getClass().getResourceAsStream("/fonts/KodeMono.ttf")) {
      if (is == null) {
        System.out.println("font not found");
      }
      Font kodeMono = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(40f);
      // System.out.println("font loaded");
      // GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      // ge.registerFont(kodeMono);
      g.setFont(kodeMono);
    } catch (Exception e) {
      e.printStackTrace();
      g.setFont(new Font("Arial", Font.BOLD, 40));
    }
    FontMetrics metrics_score = getFontMetrics(g.getFont());
    g.drawString("SCORE: " + foodEaten, (SCREEN_WIDTH - metrics_score.stringWidth("SCORE: " + foodEaten)) / 2, SCREEN_HEIGHT / 3 + g.getFont().getSize() + 30);
  }

  public void drawScore(Graphics g) {
    g.setColor(Color.WHITE);
    try (InputStream is = getClass().getResourceAsStream("/fonts/KodeMono.ttf")) {
      if (is == null) {
        System.out.println("font not found");
      }
      Font kodeMono = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
      // System.out.println("font loaded");
      // GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      // ge.registerFont(kodeMono);
      g.setFont(kodeMono);
    } catch (Exception e) {
      e.printStackTrace();
      g.setFont(new Font("Arial", Font.BOLD, 20));
    }
    FontMetrics metrics = getFontMetrics(g.getFont());
    g.drawString("SCORE: " + foodEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + foodEaten)) / 2, g.getFont().getSize());
  }

  public void initializeSnakePosition() {

    // initialize position of the snake head
    x[0] = SCREEN_WIDTH / 2;
    y[0] = SCREEN_HEIGHT / 2;

    // initialize the rest of snake body part
    for (int i = 1; i < bodyLength; i++) {
      x[i] = x[0] - i * UNIT_SIZE;
      y[i] = y[0];
    }
  }

  public void newFood() {
    boolean foodOnSnake = false;
  
    do {
      foodX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
      foodY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
      // check if food is still on part of the snake
      for (int i = 0; i < bodyLength; i++) {
        if (foodX == x[i] && foodY == y[i]) {
          foodOnSnake = true;
          break;
        }
      }
    } while (foodOnSnake);
  }

  public void move() {

    // loop through all body parts of the snake, shifting the body parts
    for (int i = bodyLength; i > 0; i--) {
      x[i] = x[i - 1];
      y[i] = y[i - 1];
    }

    // change direction
    switch(direction) {
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'U':
        y[0] = y[0] - UNIT_SIZE;
    }

  }

  public void checkIfEatFood() {
    if (x[0] == foodX && y[0] == foodY) {
      bodyLength++;
      foodEaten++;
      newFood();
    }
  }

  public void checkCollisions() {
    for (int i = bodyLength; i > 0; i--) {
      // collide with body
      if (x[0] == x[i] && y[0] == y[i]) {
        running = false;
      }
      // collide with border
      else if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
        running = false;
      }
    }
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      checkIfEatFood();
      checkCollisions();
    }
    repaint();

  }

  public class MyKeyAdaptor extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        // W/UP
        case KeyEvent.VK_W:
        case KeyEvent.VK_UP:
          if (direction != 'D') { // Forbid 180 degree turns
              direction = 'U';
          }
          break;
            
        // A/LEFT
        case KeyEvent.VK_A:
        case KeyEvent.VK_LEFT:
          if (direction != 'R') {
            direction = 'L';
          }
          break;
        
        // S/DOWN
        case KeyEvent.VK_S:
        case KeyEvent.VK_DOWN:
          if (direction != 'U') {
              direction = 'D';
          }
          break;

        // D/RIGHT
        case KeyEvent.VK_D:
        case KeyEvent.VK_RIGHT:
          if (direction != 'L') {
              direction = 'R';
          }
          break;
    }
    }
  }
}
