import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 public class Breakout extends JFrame{
 public Breakout(){
 this.setTitle("Manuel's Breakout");
 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }
 public static void main (String args[]){
 Breakout myWindow = new Breakout();
 myWindow.setSize(1000,650 );
 myWindow.setVisible(true);
 }
 }
 */

public class Breakout {
    static JFrame frame = new JFrame("Breakout!");
    JPanel panel = new JPanel();
    static int WWidth = 1000, WHeight = 650;
    Timer repaintTimer;
    Timer modTimer;
    static ImageIcon img;
    boolean On = true;
    static int fps = 0;
    static int bspeed = 0;
    static boolean plusLength = false;
    static int frames = 0;
    
    
    Breakout(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setSize(WWidth, WHeight);
        
        JLabel MyName = new JLabel("<html><br><br><br><br>Brick Breaker</html>", JLabel.CENTER);
        MyName.setFont(new Font("Gill Sans", Font.PLAIN, 45));
        MyName.setForeground(Color.RED);
        MyName.setAlignmentX(frame.CENTER_ALIGNMENT);
        
        
        JLabel WaterlooID = new JLabel("20529146", JLabel.CENTER);
        WaterlooID.setFont(new Font("Gill Sans", Font.ITALIC, 35));
        WaterlooID.setForeground(Color.gray);
        WaterlooID.setAlignmentX(frame.CENTER_ALIGNMENT);
        
        JLabel info = new JLabel("Click button to start! Press 'p' for powerup, 'q to quit'", JLabel.CENTER);
        info.setFont(new Font("Gill Sans", Font.ITALIC, 35));
        info.setForeground(Color.gray);
        info.setAlignmentX(frame.CENTER_ALIGNMENT);
        
        JButton button = new JButton("START!");
        
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentY(Component.HEIGHT);
        
        HandlerClass handler = new HandlerClass();
        button.addActionListener(handler);
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(MyName);
        panel.add(info);
        panel.add(button);
        panel.setOpaque(false);
        
        frame.add(panel);
        frame.setResizable(true);
        frame.setSize(1000, 650);
        frame.setVisible(true);
        panel.setVisible(true);
        //        mainPanel pan = new mainPanel();
        //        frame.add(pan);
        //        frame.setVisible(true);
    }
    
    class HandlerClass implements ActionListener{
        public void actionPerformed(ActionEvent e){
            GameBoard game = new GameBoard();
            frame.setLayout(new BorderLayout());
            frame.add(game, BorderLayout.CENTER);
            panel.setVisible(false);
            //frame.pack();
            game.setFocusable(true);
            game.requestFocusInWindow();
            frame.setVisible(true);
        }
    }
    
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                fps = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
            try {
                bspeed = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[1] + " must be an integer.");
                System.exit(1);
            }
        }
        if (bspeed == 0){
            bspeed = 3;
        }
        if (fps == 0){
            fps = 60;
        }
        
        Breakout b = new Breakout();
        
    }
    
    public class GameBoard extends JPanel {
        Ball ball;
        Block[][] blocks;
        Paddle paddle;
        ImageIcon img = null;
        int Score = 0;
        
        public class KeyEv extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        paddle.mv = -2;
                        break;
                    case KeyEvent.VK_RIGHT:
                        paddle.mv = 2;
                        break;
                    case KeyEvent.VK_P:
                        frames = 0;
                        plusLength = true;
                        break;
                    case KeyEvent.VK_Q:
                        System.exit(0);
                        break;
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        paddle.mv = 0;
                        break;
                    case KeyEvent.VK_RIGHT:
                        paddle.mv = 0;
                        break;
                }
            }
        }
        
        
        public GameBoard(){
            setBackground(Color.black);
            setOpaque(true);
            paddle = new Paddle();
            ball = new Ball(13);
            blocks = new Block[5][10];
            makeblocks();
            addKeyListener(new KeyEv());
            
            ActionListener modifier = new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    boolean allBroken = true;
                    for(int i = 0; i < 5; ++i){
                        for(int j = 0; j < 10; ++j){
                            if(blocks[i][j].broken == false){
                                allBroken = false;
                                break;
                            }
                        }
                    }
                    if(allBroken){
                        modTimer.stop();
                        img = new ImageIcon(getClass().getResource("win.png"));
                    }
                    
                    else if(ball.life <= 0) {
                        modTimer.stop();
                        img = new ImageIcon(getClass().getResource("lose.png"));
                    }
                    
                    else {
                        ball.ballupdate();
                        paddle.move(paddle.mv);
                    }
                }
            };
            
            ActionListener repainter = new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    frames++;
                    repaint();
                }
            };
            
            modTimer = new Timer(bspeed, modifier);
            modTimer.start();
            repaintTimer = new Timer(1000/fps, repainter);
            repaintTimer.start();
            
        }
        
        class Paddle{
            int width = 190, height = 14, x = 405, y = 580, mv = 0;
            
            public Paddle() {
            }
            
            public void move(int mv) {
                if((x > 0 || mv > 0) && (x+width < frame.getWidth() || mv < 0)) {
                    x = x + mv;
                }
            }
            
            public void draw(Graphics g){
                g.setColor(Color.blue);
                g.fillRect(x, y, width, height);
            }
        }
        
        class Block{
            int x, y, width, height;
            Color color;
            boolean broken;
            public Block(int val1, int val2, Color col){
                x = val1;
                y = val2;
                color = col;
                width = 97;
                height = 40;
                broken = false;
            }
            
            public void draw(Graphics g){
                g.setColor(color);
                g.fillRect(x, y, width, height);
            }
        }
        
        class Ball{
            double xdir, ydir, xchange, ychange;
            int radius, life;
            
            public Ball(int r){
                radius = r;
                xdir = paddle.x + paddle.width/2;
                ydir = paddle.y - radius;
                xchange = 1;
                ychange = -1;
                life = 3;
            }
            
            public void reset(){
                xdir = paddle.x + paddle.width/2;
                ydir = paddle.y - (radius*2);
                xchange = 1;
                ychange = -1;
            }
            
            public void ballupdate(){
                double xpos = xdir + xchange;
                double ypos = ydir + ychange;
                xdir = xpos;
                ydir = ypos;
                int left, right, top, bottom, middleX, middleY;
                
                if(xpos+ball.radius*2 >= frame.getWidth()){
                    xchange = -1 * Math.abs(xchange);
                }
                
                if(xpos <= 0){
                    xchange = Math.abs(xchange);
                }
                
                if(ypos <= 0){
                    ychange = Math.abs(ychange);
                }
                
                if(((xpos + ball.radius * 2 < paddle.x) || (xpos > (paddle.x + paddle.width)))
                   && (ypos > paddle.y + paddle.height)){
                    life -= 1;
                    reset();
                }
                
                if((xpos + ball.radius >= paddle.x) && (xpos + ball.radius <= paddle.x + paddle.width) && ((ypos + ball.radius * 2) >= paddle.y)){
                    if(ychange >= 0) {
                        ychange = -ychange;
                    }
                    ydir = paddle.y - ball.radius * 2 - 1;
                }
                
                for(int i = 0; i < 5; ++i){
                    for(int j = 0; j < 10; ++j){
                        if(!blocks[i][j].broken) {
                            left = blocks[i][j].x;
                            right = left + blocks[i][j].width;
                            top = blocks[i][j].y;
                            bottom = top + blocks[i][j].height;
                            middleX = (left + right) / 2;
                            middleY = (top + bottom) / 2;
                            
                            // Hit the bottom and top side
                            if ((xpos + ball.radius * 2 >= left) && (xpos <= right)) {
                                if(ychange < 0 && ypos <= bottom && ypos > middleY) {
                                    // Hit the bottom of the brick
                                    ychange = Math.abs(ychange);
                                    blocks[i][j].broken = true;
                                    Score += Math.abs(xchange) + Math.abs(ychange);
                                    break;
                                } else if(ychange > 0 && ypos + ball.radius * 2 >= top && ypos + ball.radius * 2 < middleY){
                                    // Hit the top of the brick
                                    ychange = -1 * Math.abs(ychange);
                                    blocks[i][j].broken = true;
                                    Score += Math.abs(xchange) + Math.abs(ychange);
                                    break;
                                }
                            }
                            
                            // Hit the left and right side
                            if ((ypos + ball.radius * 2 >= top) && (ypos <= bottom)) {
                                if(xchange > 0 && xpos + ball.radius * 2 >= left && xpos < middleX){
                                    // Hit the left side of brick
                                    xchange = -1 * Math.abs(xchange);
                                    blocks[i][j].broken = true;
                                    Score += Math.abs(xchange) + Math.abs(ychange);
                                    break;
                                } else if(xchange < 0 && xpos <= right && xpos > middleX){
                                    //Hit the right side of brick
                                    xchange = Math.abs(xchange);
                                    blocks[i][j].broken = true;
                                    Score += Math.abs(xchange) + Math.abs(ychange);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            public void draw(Graphics g){
                g.setColor(Color.white);
                g.fillOval((int)xdir, (int)ydir, radius*2, radius*2);
            }
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            ball.draw(g2);
            
            if (frames % fps * 10 == 0) {
                frames = 0;
                plusLength = false;
            }
            
            
            paddle.width = (int)(frame.getWidth()*0.19);
            if (plusLength) paddle.width *= 1.5;
            paddle.height = (int)(frame.getHeight()*0.0215);
            if (paddle.x > frame.getWidth() - paddle.width){
                paddle.x = frame.getWidth() - paddle.width;
            }
            if(paddle.x < 0){
                paddle.x = 0;
            }
            
            paddle.y = frame.getHeight() - paddle.height - (int)(frame.getHeight()*0.1);
            paddle.draw(g2);
            
            editblocks();
            for(int i = 0; i < 5; ++i) {
                for(int j = 0; j < 10; ++j) {
                    if(!blocks[i][j].broken) {
                        blocks[i][j].draw(g2);
                    }
                }
            }
            if(img != null){
                int x = (this.getWidth() - img.getIconWidth())/2;
                int y = (this.getHeight() - img.getIconHeight())/2;
                g2.drawImage(img.getImage(),x, y, img.getIconWidth(), img.getIconHeight(), null);
                repaintTimer.stop();
            }
            
            for(int i = 0; i < ball.life; ++i){
                g.setColor(Color.white);
                ball.radius = (int)Math.sqrt(0.0002*frame.getHeight()*frame.getWidth());
                g.fillOval((int)(frame.getWidth()*0.91) + i*(ball.radius+(int)(frame.getWidth()*0.015)), (int)(frame.getHeight()*0.9), ball.radius*2, ball.radius*2);
            }
            
            g.setFont(new Font("Gill Sans", Font.PLAIN, (int)(frame.getHeight()*0.03846)));
            g.drawString("Score: " + Integer.toString(Score), 20, frame.getHeight() - 40);
        }
        
        public void editblocks(){
            for (int i = 0; i < 5; ++i){
                for(int j = 0; j < 10; ++j) {
                    int xpos = j * frame.getWidth()/10;
                    int ypos = i * frame.getHeight()/15 + (int)(frame.getHeight()*0.11);
                    blocks[i][j].x = xpos;
                    blocks[i][j].width = frame.getWidth()/10 - 3;
                    blocks[i][j].y = ypos;
                    blocks[i][j].height = frame.getHeight()/15 - 3;
                }
            }
        }
        
        public void makeblocks() {
            for (int i = 0; i < 5; ++i){
                for(int j = 0; j < 10; ++j) {
                    int xpos = j * 100;
                    int ypos = i * 43 + 60;
                    if(i == 0){
                        blocks[i][j] = new Block(xpos, ypos, Color.red);
                    }
                    else if(i == 1){
                        blocks[i][j] = new Block(xpos, ypos, Color.ORANGE);
                    }
                    else if(i == 2){
                        blocks[i][j] = new Block(xpos, ypos, Color.YELLOW);
                    }
                    else if(i == 3){
                        blocks[i][j] = new Block(xpos, ypos, Color.GREEN);
                    }
                    else{
                        blocks[i][j] = new Block(xpos, ypos, Color.BLUE);
                    }
                    
                }
            }
        }
        
    }
    
}

