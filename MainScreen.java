/**
 * File Name: Main
 * Programmer: Inaam Azeezur-Rahman, Edlyn Li, Nevin D'Souza, Noam McCready
 * Date: August 25, 2020
 * Description: This program runs the game of our snake program.
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Game GUI
class Game extends JPanel implements KeyListener, ActionListener {

    // leaderboard
    ArrayList<Integer> scores = new ArrayList<Integer>();
    String str = "";
    int counter = 1;

    JFrame leaderboard = new JFrame();
    JTextPane txtBoard = new JTextPane();

    // snake heads
    ImageIcon snakeUP = new ImageIcon("snakeUp.png");
    ImageIcon snakeDOWN = new ImageIcon("snakeDown.png");
    ImageIcon snakeRIGHT = new ImageIcon("snakeRight.png");
    ImageIcon snakeLEFT = new ImageIcon("snakeLeft.png");

    // snake body
    ImageIcon snakeBODY = new ImageIcon("snakeBody.png");

    // snake length
    int[] snakeX = new int[750];
    int[] snakeY = new int[750];

    // snake game title
    ImageIcon title = new ImageIcon("banner.png");

    // mouse click directions
    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;

    // other variables
    int snakeLength = 3;
    int moves = 0;
    int score = 0;
    javax.swing.Timer timer;
    int delay = 100;

    // game is happening or game has ended
    boolean game = true;

    // food positions
    int[] foodxpos = {25, 50, 75, 100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525,
        550, 575, 600, 625, 650, 675, 700, 725, 750, 775, 800, 825, 850};
    int[] foodypos = {75, 100, 125, 175, 200, 225, 250, 275, 300, 325, 350, 375, 400, 425, 450, 475, 500, 525};

    int randxpos = (int) (Math.random() * 31);
    int randypos = (int) (Math.random() * 18);

    /**
     * starts timer and adds KeyListener
     */
    public Game() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new javax.swing.Timer(delay, this);
        timer.start();
    }

    /**
     * visuals, food controls, and determines game over
     * @param g Graphics
     */
    public void paint(Graphics g) {
        super.paintComponent(g);

        // score text
        // src: 
        // https://examples.javacodegeeks.com/desktop-java/awt/draw-text-example/
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.setColor(Color.GREEN);
        g.drawString("Score: " + score, 50, 50);

        // title image
        title.paintIcon(this, g, 340, 20);

        // game border
        g.setColor(Color.GREEN);
        g.drawRect(24, 10, 851, 591);
        // game background
        g.setColor(Color.BLACK);
        g.fillRect(25, 75, 850, 525);

        // spawn snake
        // initial snake location, head
        if (moves == 0) {
            snakeX[2] = 50;
            snakeX[1] = 75;
            snakeX[0] = 100;

            snakeY[2] = 100;
            snakeY[1] = 100;
            snakeY[0] = 100;

            snakeRIGHT.paintIcon(this, g, snakeX[0], snakeY[0]);
        }

        for (int i = 0; i < snakeLength; i++) {
            if (i == 0 && up) {
                snakeUP.paintIcon(this, g, snakeX[i], snakeY[i]);
            }
            if (i == 0 && down) {
                snakeDOWN.paintIcon(this, g, snakeX[i], snakeY[i]);
            }
            if (i == 0 && right) {
                snakeRIGHT.paintIcon(this, g, snakeX[i], snakeY[i]);
            }
            if (i == 0 && left) {
                snakeLEFT.paintIcon(this, g, snakeX[i], snakeY[i]);
            }
            if (i != 0) {
                snakeBODY.paintIcon(this, g, snakeX[i], snakeY[i]);
            }
        }

        // food
        ImageIcon foodimage = new ImageIcon("food.png");
        
        // if snake eats food
        if ((foodxpos[randxpos] == snakeX[0]) && foodypos[randypos] == snakeY[0]) {
            snakeLength++;
            score++;
            randxpos = (int) (Math.random() * 33);
            randypos = (int) (Math.random() * 18);

            // randomize apple location until not on snake
            for (int i = 0; i < snakeLength; i++) {
                while (randxpos == snakeX[i] && randypos == snakeY[i]) {
                    randxpos = (int) (Math.random() * 33);
                    randypos = (int) (Math.random() * 18);
                }
            }
        }
        
        // paint apple on screen
        foodimage.paintIcon(this, g, foodxpos[randxpos], foodypos[randypos]);

        // determine if game over
        for (int i = 1; i < snakeLength; i++) {
            if (snakeY[i] == snakeY[0] && snakeX[i] == snakeX[0]) {
                game = false;
                right = false;
                left = false;
                up = false;
                down = false;
                moves = 0;
                scores.add(score);
                Collections.sort(scores);
                Collections.reverse(scores);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(275, 200, 400, 150);
                g.fillRect(275, 200, 400, 150);
                g.setColor(Color.WHITE);
                g.drawString("Game Over", 300, 225);
                g.setColor(Color.WHITE);
                g.drawString("Click Enter to play again", 300, 275);
                g.setColor(Color.WHITE);
                g.drawString("Press tab to open leaderboards", 300, 325);

            }
        }
        g.dispose();
    }

    /**
     * snake direction key ActionEvent
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (right) {
            for (int r = snakeLength - 1; r >= 0; r--) {
                snakeY[r + 1] = snakeY[r];
            }
            for (int r = snakeLength; r >= 0; r--) {
                if (r == 0) {
                    snakeX[r] = snakeX[r] + 25;
                } else {
                    snakeX[r] = snakeX[r - 1];
                }
                if (snakeX[r] > 850) {
                    snakeX[r] = 25;
                }
            }
            repaint();
        }
        if (left) {
            for (int r = snakeLength - 1; r >= 0; r--) {
                snakeY[r + 1] = snakeY[r];
            }
            for (int r = snakeLength; r >= 0; r--) {
                if (r == 0) {
                    snakeX[r] = snakeX[r] - 25;
                } else {
                    snakeX[r] = snakeX[r - 1];
                }
                if (snakeX[r] < 25) {
                    snakeX[r] = 850;
                }
            }
            repaint();
        }
        if (up) {
            for (int r = snakeLength - 1; r >= 0; r--) {
                snakeX[r + 1] = snakeX[r];
            }
            for (int r = snakeLength; r >= 0; r--) {
                if (r == 0) {
                    snakeY[r] = snakeY[r] - 25;
                } else {
                    snakeY[r] = snakeY[r - 1];
                }
                if (snakeY[r] < 75) {
                    snakeY[r] = 575;
                }
            }
            repaint();
        }
        if (down) {
            for (int r = snakeLength - 1; r >= 0; r--) {
                snakeX[r + 1] = snakeX[r];
            }
            for (int r = snakeLength; r >= 0; r--) {
                if (r == 0) {
                    snakeY[r] = snakeY[r] + 25;
                } else {
                    snakeY[r] = snakeY[r - 1];
                }
                if (snakeY[r] > 575) {
                    snakeY[r] = 75;
                }
            }
            repaint();
        }
    }

    /**
     * snake direction and game over KeyEvents
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (game) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moves++;
                right = true;
                if (!left) {
                    right = true;
                } else {
                    right = false;
                    left = true;
                }
                up = false;
                down = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                moves++;
                left = true;
                if (!right) {
                    left = true;
                } else {
                    left = false;
                    right = true;
                }
                up = false;
                down = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                moves++;
                up = true;
                if (!down) {
                    up = true;
                } else {
                    up = false;
                    down = true;
                }
                left = false;
                right = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                moves++;
                down = true;
                if (!up) {
                    down = true;
                } else {
                    down = false;
                    up = true;
                }
                left = false;
                right = false;
            }
        } else {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                game = true;
                str = "";
                score = 0;
                // reset snake location and length
                snakeLength = 3;
                snakeX[2] = 50;
                snakeX[1] = 75;
                snakeX[0] = 100;

                snakeY[2] = 100;
                snakeY[1] = 100;
                snakeY[0] = 100;

                repaint();
                leaderboard.dispose();
            }
            if (e.getKeyCode() == KeyEvent.VK_TAB) {
                str = "";
                
                // open leaderboard
                txtBoard.setFont(new Font("SansSerif", Font.BOLD, 18));
                txtBoard.setPreferredSize(new Dimension(100, 100));
                txtBoard.setEditable(false);
                leaderboard.setBounds(10, 10, 300, 315);
                leaderboard.setBackground(Color.DARK_GRAY);
                leaderboard.setResizable(true);

                leaderboard.setVisible(true);
                
                // src:
                // https://stackoverflow.com/questions/1944446/close-one-jframe-without-closing-another
                leaderboard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                leaderboard.add(txtBoard);

                for (int i = 0; i < scores.size(); i++) {
                    if (i < 10) {
                        str += i + 1 + ". " + scores.get(i) + "\n";
                    }
                }
                txtBoard.setText("Leaderboard\n" + str);

                // src:
                // https://stackoverflow.com/questions/24407190/center-alignment-of-a-word-in-jtextpane
                StyledDocument doc = txtBoard.getStyledDocument();
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(0, doc.getLength(), center, false);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}

}
