import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** GUI component for Click-a-Dot game board. Maintains and visualizes game state and responds to
 * mouse inputs. Allows programmatic control of game settings and property access to game state. */
public class PaintGameComponent extends JPanel implements MouseListener {
    /** text in text box */
    private String text= null;
    public boolean firstRound= true;

    public ArrayList<JLabel> smudge_list= null;

    /** Construct a new GameComponent with default settings. */
    public PaintGameComponent() {
        // This component reacts to mouse events.
        addMouseListener(this);

        // Set a recommended size for the game board (prevents it from
        // disappearing when frame is packed).
        setPreferredSize(new Dimension(480, 360));
    }

    /** Start a new game using current settings. Progress from any previous or ongoing game is
     * reset. Request a repaint. */
    public void startGame(String s, JFrame frame, JLabel image_icon) {
        if (!firstRound) {

            image_icon.setVisible(true);
            if (smudge_list != null) {

                for (JLabel stk : smudge_list) {
                    frame.remove(stk);
                }
            }
            repaint();
        }
        firstRound= false;
        image_icon.setVisible(false);
        smudge_list= paint(s, frame);
        // System.out.println(smudge_list.size());
        for (JLabel stk : smudge_list) {
            frame.add(stk);
            // stk.setVisible(true);
            // repaint();
        }
        JPanel dummy= new JPanel();
        dummy.setVisible(false);
        frame.add(dummy);
        repaint();

    }

    /** Set layout to null */
    @Override
    public void paintComponent(Graphics g) {
        super.setLayout(null);

    }

    // method to interpret text and "paint".
    public ArrayList<JLabel> paint(String txt, JFrame frame) {
        text= txt.toLowerCase();    // text is all lowercase to simplify comparisons
        if (text.length() > 100) {
            // System.out.print("more than 100 characters");
            BufferedImage tiredMartyBuffered;
            try {
                tiredMartyBuffered= ImageIO
                    .read(new File("images/tired_marty.png"));
                // System.out.print("made marty image");
            } catch (IOException e) {
                throw new IllegalArgumentException(
                    "Can't find input file : " + e.toString());
            }
            JLabel tiredMartyImg= new JLabel(
                new ImageIcon(tiredMartyBuffered.getScaledInstance(500, 500,
                    java.awt.Image.SCALE_SMOOTH)));
            tiredMartyImg.setBounds(30, 30, 500, 500);
            tiredMartyImg.repaint();
            JLabel dummy= new JLabel();
            ImageIcon tiredmartydummy= new ImageIcon(
                new ImageIcon("images/tired_marty.png").getImage()
                    .getScaledInstance(500, 500, java.awt.Image.SCALE_SMOOTH));
            ArrayList<JLabel> tiredMarty= new ArrayList<>();
            tiredMarty.add(tiredMartyImg);
            // System.out.print("tired marty added");
            return tiredMarty;
        } else {
            return paintLetters(frame);
        }
    }

    public ArrayList<JLabel> paintLetters(JFrame frame) {
        ArrayList<JLabel> alist= new ArrayList<>();
        for (int i= 0; i < text.length(); i++ ) {
            char tempChar= text.charAt(i);
            Stroke stk= new Stroke();
            if (Character.isLetter(tempChar)) {
                alist.add(stk.makeStroke(
                    "images/brush" + Character.toUpperCase(tempChar) + ".png",
                    frame));

            }

            // ***********need to account for special chars/other

        }
        return alist;
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    /** Represents a stroke to be drawn. Maintains current position and size. Able to paint
     * itself. */
    private static class Stroke {
        /** x-coordinate of current center position [px]. */
        int x;

        /** y-coordinate of current center position [px]. */
        int y;

        /** Radius of circular dot representing the target [px]. */
        int radius;

        /** Generate random numbers to use when choosing new location/size/rotation. */
        private Random rng= new Random();

        /** Paint stroke on provided Graphics `g` with given radius and coordinates */
        JLabel makeStroke(String str, JFrame frame) {
            try {
                BufferedImage myPicture= ImageIO.read(new File(str));
                newCoord(frame.getWidth(), frame.getHeight());
                JLabel picLabel= new JLabel(new ImageIcon(
                    myPicture.getScaledInstance(radius, radius,
                        java.awt.Image.SCALE_SMOOTH)));
                picLabel.setBounds(x, y, radius, radius);
                picLabel.repaint();
                return picLabel;
            } catch (IOException e) {
                throw new IllegalArgumentException(
                    "Can't find input file : " + e.toString());
            }

        }

        /** Move stroke to a random location and reset "hit" state. xMax and yMax are the
         * (inclusive) upper bounds of the x- and y-coordinates of the bounding box of the new
         * position [px]; lower bound is 0 (inclusive). */
        void newCoord(int xMax, int yMax) {
            x= rng.nextInt(xMax - 160) + 15;
            y= rng.nextInt(yMax - 180) + 30;
            radius= rng.nextInt(yMax / 8) + 150;
        }
    }
}