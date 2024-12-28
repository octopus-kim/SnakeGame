package ssafy.prj6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int SCREEN_SIZE = 400; // 화면 크기 (px)
    private final int UNIT_SIZE = 20;   // 격자 크기 (px)
    private final int UNIT_COUNT = (SCREEN_SIZE * SCREEN_SIZE) / (UNIT_SIZE * UNIT_SIZE); // 격자 갯수 (20 x 20)
    private final int DELAY = 150;      // 타이머 딜레이
    private final int[] x = new int[UNIT_COUNT]; // 뱀의 x 좌표
    private final int[] y = new int[UNIT_COUNT]; // 뱀의 y 좌표
    private int snakeBodyLength = 3;          // 뱀의 초기 길이
    private int foodX;                  // 먹이 x 좌표
    private int foodY;                  // 먹이 y 좌표
    private int score = 0;              // 점수
    private char direction = 'R';       // 초기 방향: 오른쪽
    private boolean running = false;    // 게임 실행 여부
    private Timer timer;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        spawnFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(SCREEN_SIZE / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(SCREEN_SIZE / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = snakeBodyLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] -= UNIT_SIZE; // 위쪽
            case 'D' -> y[0] += UNIT_SIZE; // 아래쪽
            case 'L' -> x[0] -= UNIT_SIZE; // 왼쪽
            case 'R' -> x[0] += UNIT_SIZE; // 오른쪽
        }
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            snakeBodyLength++;
            score++;
            spawnFood();
        }
    }

    public void checkCollision() {
        // 자기 자신과 충돌
        for (int i = snakeBodyLength; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // 벽과 충돌
        if (x[0] < 0 || x[0] >= SCREEN_SIZE || y[0] < 0 || y[0] >= SCREEN_SIZE) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // 격자 그리기
            g.setColor(Color.GRAY);
            for (int i = 0; i < SCREEN_SIZE / UNIT_SIZE; i++) {
                for (int j = 0; j < SCREEN_SIZE / UNIT_SIZE; j++) {
                    g.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    g.setColor(Color.BLACK); // 격자 선 색
                    g.drawRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    g.setColor(Color.GRAY); // 격자 배경 색
                }
            }

            // 먹이 그리기
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // 뱀 그리기
            for (int i = 0; i < snakeBodyLength; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // 머리
                } else {
                    g.setColor(Color.LIGHT_GRAY); // 몸통
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // 점수 표시
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_SIZE - metrics.stringWidth("Game Over")) / 2, SCREEN_SIZE / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, (SCREEN_SIZE - metrics.stringWidth("Score: " + score)) / 2, SCREEN_SIZE / 2 + 40);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') direction = 'L';
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') direction = 'R';
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') direction = 'U';
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') direction = 'D';
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();

        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

