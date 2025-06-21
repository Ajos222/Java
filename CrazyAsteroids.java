import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CrazyAsteroids extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private int playerX = 250;
    private int playerY = 400;
    private final int PLAYER_SIZE = 35;
    private final int SPEED = 6;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    private ArrayList<Rectangle> obstacles = new ArrayList<>();
    private Random rand = new Random();
    private int score = 0;
    private int hiscore = 0;

    private boolean inGame = false;

    public CrazyAsteroids() {
        loadHiScore();

        JFrame frame = new JFrame("🚀 Vesmírna misia: Vyhni sa asteroidom");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addKeyListener(this);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Vesmírne pozadie
        GradientPaint bg = new GradientPaint(0, 0, Color.BLACK, 0, getHeight(), new Color(20, 20, 40));
        g2.setPaint(bg);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (!inGame) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("🚀 VESMÍRNA MISIA", 170, 150);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Stlač [SPACE] pre začatie hry", 200, 220);
            g.drawString("HiScore: " + hiscore, 250, 260);
            return;
        }

        // 🚀 Vesmírna loď - trojuholník
        g.setColor(new Color(0, 255, 200));
        int[] xPoints = {playerX + PLAYER_SIZE / 2, playerX, playerX + PLAYER_SIZE};
        int[] yPoints = {playerY, playerY + PLAYER_SIZE, playerY + PLAYER_SIZE};
        g.fillPolygon(xPoints, yPoints, 3);

        // ☄️ Asteroidy - rôzne ovály
        for (Rectangle r : obstacles) {
            Color asteroidColor = new Color(120 + rand.nextInt(50), 100 + rand.nextInt(50), 90);
            g.setColor(asteroidColor);
            g.fillOval(r.x, r.y, r.width, r.height);
        }

        // Skóre
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Skóre: " + score, 10, 20);
        g.drawString("HiScore: " + hiscore, 470, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!inGame) return;

        // Pohyb hráča
        if (leftPressed && playerX > 0) playerX -= SPEED;
        if (rightPressed && playerX < getWidth() - PLAYER_SIZE) playerX += SPEED;
        if (upPressed && playerY > 0) playerY -= SPEED;
        if (downPressed && playerY < getHeight() - PLAYER_SIZE) playerY += SPEED;

        // Pridávanie asteroidov
        if (rand.nextInt(15) == 0) {
            int size = 30 + rand.nextInt(20);
            obstacles.add(new Rectangle(rand.nextInt(getWidth() - size), 0, size, size));
        }

        // Pohyb a kolízie
        Iterator<Rectangle> it = obstacles.iterator();
        while (it.hasNext()) {
            Rectangle r = it.next();
            r.y += 4;

            if (r.y > getHeight()) {
                it.remove();
                score++;
            }

            Rectangle playerRect = new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
            if (r.intersects(playerRect)) {
                endGame();
            }
        }

        repaint();
    }

    private void startGame() {
        score = 0;
        playerX = 250;
        playerY = 400;
        obstacles.clear();
        inGame = true;
    }

    private void endGame() {
        inGame = false;
        if (score > hiscore) {
            hiscore = score;
            saveHiScore();
        }
        repaint();
    }

    private void saveHiScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("hiscore.txt"))) {
            writer.write(String.valueOf(hiscore));
        } catch (IOException e) {
            System.err.println("Nepodarilo sa uložiť HiScore.");
        }
    }

    private void loadHiScore() {
        File f = new File("hiscore.txt");
        if (f.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String line = reader.readLine();
                hiscore = Integer.parseInt(line.trim());
            } catch (IOException | NumberFormatException e) {
                hiscore = 0;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (!inGame && code == KeyEvent.VK_SPACE) {
            startGame();
        }

        if (code == KeyEvent.VK_LEFT) leftPressed = true;
        if (code == KeyEvent.VK_RIGHT) rightPressed = true;
        if (code == KeyEvent.VK_UP) upPressed = true;
        if (code == KeyEvent.VK_DOWN) downPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_RIGHT) rightPressed = false;
        if (code == KeyEvent.VK_UP) upPressed = false;
        if (code == KeyEvent.VK_DOWN) downPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new CrazyAsteroids();
    }
}
