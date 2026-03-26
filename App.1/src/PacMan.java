import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {
    // --- İÇ SINIF: BLOCK ---
    class Block {
        int x, y, width, height, startX, startY, speed;
        Image image;
        char direction = 'U';
        int velocityX = 0, velocityY = 0;
        boolean isScared = false;

        Block(Image image, int x, int y, int width, int height, int speed) {
            this.image = image;
            this.x = x; this.y = y;
            this.width = width; this.height = height;
            this.startX = x; this.startY = y;
            this.speed = speed;
        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            int currentSpeed = isScared ? speed / 2 : speed;
            if (this.direction == 'U') { this.velocityX = 0; this.velocityY = -currentSpeed; }
            else if (this.direction == 'D') { this.velocityX = 0; this.velocityY = currentSpeed; }
            else if (this.direction == 'L') { this.velocityX = -currentSpeed; this.velocityY = 0; }
            else if (this.direction == 'R') { this.velocityX = currentSpeed; this.velocityY = 0; }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
            this.isScared = false;
        }
    }

    // --- DEĞİŞKENLER ---
    private int rowCount = 21, columnCount = 32, tileSize = 32;
    private int boardWidth = columnCount * tileSize, boardHeight = rowCount * tileSize;
    private Image wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, scaredGhostImage, jumpScare;
    private Image pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;

    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            "XS             b              SX",
            "X  XXXXXX  XXXXXX  XXXXXX  XXXXX",
            "X  XXXXXX  XXXXXX  XXXXXX  XXXXX",
            "X                              X",
            "X      XX XX        XX XX      X",
            "X    XXXXXXXXX    XXXXXXXXX    X",
            "X   XXXXXXXXXXX  XXXXXXXXXXX   X",
            "X   XXXXXXXXXXX  XXXXXXXXXXX   X",
            "X    XXXXXXXXX    XXXXXXXXX    X",
            "X      XXXXX   bpo  XXXXX      X",
            "X        XXX   rrr  XXX        X",
            "X         X     P    X         X",
            "X                              X",
            "X  XXXX  XXXXXXXXXXXXXX  XXXX  X",
            "X  XXXX  XXXXXXXXXXXXXX  XXXX  X",
            "X                              X",
            "X  XXXXXX  XXXXXX  XXXXXX  XXXXX",
            "X  XXXXXX  XXXXXX  XXXXXX  XXXXX",
            "X                p            SX",
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls = new HashSet<>(), foods = new HashSet<>(), ghosts = new HashSet<>(), superFoods = new HashSet<>();
    HashSet<Point> superFoodLocations = new HashSet<>();
    Block pacman;
    Timer gameLoop, powerTimer;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0, lives = 3;
    boolean gameOver = false;
    boolean isJumpScareActive = false;
    boolean hasJumpScared = false; // 2000 puan jumpscare'i için bayrak

    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // Görselleri yükle
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        scaredGhostImage = new ImageIcon(getClass().getResource("./Eerie Halloween Ghost PNG Collection_ Spooky Pumpkins & Witches Cauldron.png")).getImage();
        jumpScare = new ImageIcon(getClass().getResource("./de6d10592d5a91864e4c91fbc62f63a8.jpg")).getImage();

        loadMap();
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    public void loadMap() {
        walls.clear(); foods.clear(); ghosts.clear(); superFoods.clear(); superFoodLocations.clear();
        for (int r = 0; r < rowCount; r++) {
            String row = tileMap[r];
            for (int c = 0; c < columnCount; c++) {
                char tileChar = row.charAt(c);
                int x = c * tileSize, y = r * tileSize;

                if (tileChar == 'X') walls.add(new Block(wallImage, x, y, tileSize, tileSize, 0));
                else if (tileChar == 'b') ghosts.add(new Block(blueGhostImage, x, y, tileSize, tileSize, tileSize / 4));
                else if (tileChar == 'o') ghosts.add(new Block(orangeGhostImage, x, y, tileSize, tileSize, tileSize / 4));
                else if (tileChar == 'p') ghosts.add(new Block(pinkGhostImage, x, y, tileSize, tileSize, tileSize / 4));
                else if (tileChar == 'r') ghosts.add(new Block(redGhostImage, x, y, tileSize, tileSize, tileSize / 4));
                else if (tileChar == 'P') pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize, tileSize / 2);
                else if (tileChar == ' ') foods.add(new Block(null, x + 14, y + 14, 4, 4, 0));
                else if (tileChar == 'S') superFoodLocations.add(new Point(x + 10, y + 10));
            }
        }
        resetGhostMovement();
    }

    private void resetGhostMovement() {
        for (Block ghost : ghosts) {
            ghost.direction = directions[random.nextInt(4)];
            ghost.updateVelocity();
        }
    }

    public void activatePowerUp() {
        for (Block ghost : ghosts) {
            ghost.isScared = true;
            ghost.updateVelocity();
        }
        if (powerTimer != null) powerTimer.stop();
        powerTimer = new Timer(10000, e -> {
            for (Block ghost : ghosts) {
                ghost.isScared = false;
                ghost.updateVelocity();
            }
        });
        powerTimer.setRepeats(false);
        powerTimer.start();
    }

    public void move() {
        if (gameOver || isJumpScareActive) return;

        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        for (Block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                if (ghost.isScared) {
                    ghost.reset();
                    score += 200;
                } else {
                    lives--;
                    if (lives <= 0) { gameOver = true; return; }
                    resetPositions();
                }
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    ghost.updateDirection(directions[random.nextInt(4)]);
                }
            }
        }

        // Yem yeme
        foods.removeIf(food -> collision(pacman, food) && (score += 10) > 0);

        // --- 1000 PUAN SÜPER YEM TETİKLEYİCİ ---
        if (score >= 1000 && !superFoodLocations.isEmpty()) {
            for (Point p : superFoodLocations) {
                superFoods.add(new Block(null, p.x, p.y, 16, 16, 0)); // Boyutu 16 yaptık (Daha büyük)
            }
            superFoodLocations.clear();
        }

        // Süper yem yeme
        superFoods.removeIf(sf -> {
            if (collision(pacman, sf)) {
                score += 50;
                activatePowerUp();
                return true;
            }
            return false;
        });

        // --- 2000 PUAN JUMPSCARE TETİKLEYİCİ ---
        if (false && score >= 2000 && !hasJumpScared) {
            hasJumpScared = false;
            isJumpScareActive = false;
            gameLoop.stop();
            Timer t = new Timer(2000, e -> {
                isJumpScareActive = false;
                gameLoop.start();
            });
            t.setRepeats(false);
            t.start();
        }

        // Harita yenileme (Tüm yemler biterse)
        if (foods.isEmpty() && superFoods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0; pacman.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            ghost.updateDirection(directions[random.nextInt(4)]);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (Block wall : walls) g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);

        g2d.setColor(Color.WHITE);
        for (Block food : foods) g.fillRect(food.x, food.y, food.width, food.height);

        g2d.setColor(Color.YELLOW);
        for (Block sf : superFoods) g2d.fillOval(sf.x, sf.y, sf.width, sf.height);

        for (Block ghost : ghosts) {
            if(ghost.isScared){
            int increasedSize=16;
        int newW =ghost.width+increasedSize;
        int newH=ghost.height+increasedSize;

        int offsetX=ghost.x-(increasedSize/2);
        int offsetY= ghost.y-(increasedSize/2);

        g2d.drawImage(scaredGhostImage,offsetX,offsetY,newW,newH,null);}
            else{
                g2d.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
            }
        }


        g2d.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        // UI
        if (!gameOver) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(10, 10, 180, 70, 15, 15);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
            g2d.drawString("SKOR:" + score, 25, 35);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("CANLAR:", 25, 60);
            for (int i = 0; i < lives; i++) {
                g2d.setColor(Color.RED);
                g2d.fillOval(90 + (i * 25), 48, 15, 15);
            }
        }

        if (gameOver) {
            g2d.setColor(new Color(50, 50, 50, 230));
            g2d.fillRoundRect(boardWidth / 2 - 150, boardHeight / 2 - 100, 300, 200, 20, 20);
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            g2d.drawString("OYUN BİTTİ", boardWidth / 2 - 75, boardHeight / 2 - 40);
        }

        if (false && isJumpScareActive) {
            g2d.drawImage(jumpScare, 0, 0, boardWidth, boardHeight, null);
        }
    }

    @Override public void actionPerformed(ActionEvent e) { move(); repaint(); }
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            lives = 3; score = 0; gameOver = false; hasJumpScared = false;
            loadMap(); resetPositions(); gameLoop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) pacman.updateDirection('U');
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) pacman.updateDirection('D');
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) pacman.updateDirection('L');
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) pacman.updateDirection('R');

        if (pacman.direction == 'U') pacman.image = pacmanUpImage;
        else if (pacman.direction == 'D') pacman.image = pacmanDownImage;
        else if (pacman.direction == 'L') pacman.image = pacmanLeftImage;
        else if (pacman.direction == 'R') pacman.image = pacmanRightImage;
    }
}