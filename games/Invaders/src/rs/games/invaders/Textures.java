package rs.games.invaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Textures extends JPanel {
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(new Color(212, 212, 212));
    g2d.drawRect(10, 15, 90, 60);

    BufferedImage bimage1 = null;

    URL url1 = ClassLoader.getSystemResource("compas.gif");

    try {
      bimage1 = ImageIO.read(url1);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    Rectangle rect1 = new Rectangle(0, 0, bimage1.getWidth(), bimage1.getHeight());
    TexturePaint texture1 = new TexturePaint(bimage1, rect1);

    g2d.setPaint(texture1);
    g2d.fillRect(10, 15, 90, 60);
  }

  public static void main(String[] args) {
    Textures rects = new Textures();
    JFrame frame = new JFrame("Textures");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(rects);
    frame.setSize(360, 210);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
