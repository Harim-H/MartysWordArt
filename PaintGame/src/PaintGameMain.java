import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/** Main class for painting game. Creates window with canvas, textbox for input, and
 * submit/regenerate button */
public class PaintGameMain extends JFrame {
    /** Start the application. */
    public static void main(String[] args) {
        // Creation of window occurs on Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    /** Create application window. Window title is "Placeholder title" Game board is in center of
     * window, expands to fill window size Start button is at bottom */

    private static void createAndShowGUI() {
        // Create frame.
        JFrame frame= new JFrame("Marty's WordArt");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create and add game board.
        PaintGameComponent game= new PaintGameComponent();

        // Create and add start button.
        JButton startButton= new JButton("Start");
        startButton.setFont(startButton.getFont().deriveFont(20.0f));
        // Add `startButton` to bottom of frame.
        frame.add(startButton, BorderLayout.PAGE_END);

        JTextArea inTextBox= new JTextArea("type your message here!");
        frame.add(inTextBox, BorderLayout.PAGE_START);

        BufferedImage martyBuffered;
        try {
            martyBuffered= ImageIO
                .read(new File("images/marty_the_artist.jpeg"));
        } catch (IOException e) {
            throw new IllegalArgumentException(
                "Can't find input file : " + e.toString());
        }
        JLabel marty= new JLabel(
            new ImageIcon(martyBuffered.getScaledInstance(500, 500,
                java.awt.Image.SCALE_SMOOTH)));

        frame.add(marty);

        // JMenuItem saveItem= new JMenuItem("Save painting");
        JMenuItem exitItem= new JMenuItem("Exit");
        // TODO 14: Add a menu bar with a "File" menu to the frame. The
        // menu items `saveItem` and `exitItem` should be accessible under the
        // "File" menu. See the Menu tutorial [1] for example code you can adapt.
        // You do not need to add the mnemonics, keyboard shortcuts, or hover over
        // descriptions shown in that tutorial.
        // [1] https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
        JMenuBar menubar= new JMenuBar();
        JMenu menu= new JMenu("File");
        menubar.add(menu);
        // menu.add(saveItem);
        menu.add(exitItem);
        frame.setJMenuBar(menubar);
        startButton.addActionListener(e -> {
            game.startGame(inTextBox.getText(), frame, marty);

        });
        // When "Exit" menu item is activated, dispose of the JFrame.
        exitItem.addActionListener((ActionEvent e) -> frame.dispose());
        // saveItem.addActionListener((ActionEvent e) -> frame.dispose());

        // Stop game when window is closed to ensure that game background tasks
        // do not hold up application shutdown.
        // Use an anonymous subclass of WindowAdapter to avoid having to handle
        // other window events.
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {}
        });

// Compute ideal window size and show window.
        frame.pack();
        frame.setVisible(true);

    }

}