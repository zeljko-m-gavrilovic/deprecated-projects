package rs.games.invaders;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CompassPanel extends JPanel implements KeyListener {

    private static final long serialVersionUID = -6580060939408626448L;
    private BufferedImage originalImage;
    private BufferedImage backgroundImage;
    private int offsetX;
    private int WIDTH = 200;
    private int HEIGHT = 50;

    public CompassPanel() {
        try {
            originalImage = ImageIO.read(new File("src/res/compas.gif"));

            offsetX = originalImage.getWidth();
            backgroundImage = new BufferedImage(3 * originalImage.getWidth(), HEIGHT, Transparency.OPAQUE);
            Graphics2D g = (Graphics2D) backgroundImage.getGraphics();
            g.setPaint(new TexturePaint(originalImage, new Rectangle(0, 0, originalImage.getWidth(), originalImage
                    .getHeight())));
            g.fillRect(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        checkUpperBounds();
        checkLowerBounds();
        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, offsetX, 0, WIDTH + offsetX, HEIGHT, this);
    }

    private void checkLowerBounds() {
        if (offsetX <= (originalImage.getWidth() - WIDTH)) {
            offsetX = (2 * originalImage.getWidth()) - WIDTH;
        }
    }

    private void checkUpperBounds() {
        if (offsetX >= (2 * originalImage.getWidth())) {
            offsetX = originalImage.getWidth();
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            increaseOffset();
            break;
        case KeyEvent.VK_RIGHT:
            decreaseOffset();
            break;
        case KeyEvent.VK_SPACE:
            offsetX = 0;
            break;
        }
        repaint();
    }

    private void increaseOffset() {
        offsetX = offsetX + 10;
    }

    private void decreaseOffset() {
        offsetX = offsetX - 10;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Compass");
        CompassPanel compassPanel = new CompassPanel();
        mainFrame.add(compassPanel);
        mainFrame.setBounds(0, 0, compassPanel.WIDTH, compassPanel.HEIGHT);
        mainFrame.setPreferredSize(new Dimension(compassPanel.WIDTH, compassPanel.HEIGHT));
        mainFrame.setVisible(true);
        mainFrame.addKeyListener(compassPanel);
    }
}