package com.cangyao;
import javax.swing.JFrame;

public class GameFrame extends JFrame{
  GameFrame() {
    
    this.add(new GamePanel());
    this.setTitle("Snake Game");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.pack(); // this will set the size of the frame to the preferred size of the components it contains
    this.setVisible(true);
    this.setLocationRelativeTo(null); // this will center the frame on the screen

  }
}
