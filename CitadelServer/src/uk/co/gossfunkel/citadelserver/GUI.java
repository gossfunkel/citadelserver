package uk.co.gossfunkel.citadelserver;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	public static String title = "citadelserver";
	
	private final Server server;
	private final Game game;

	private JLabel one;
	private JTextField txtOne;
	private JButton submitTxtOne;
	private JTextArea txtPane;
	private JScrollPane txtScrollPane;
	
	// screen dimensions (16:9) etc
	private static int width = 600;
	private static int height = 650;
	
	public GUI(final Server server, final Game game) {
		this.server = server;
		this.game = game;
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);	
		setTitle(title);
		Container cp = getContentPane();
		cp.setLayout(null);
		
		// currently not resisable
		setResizable(false);
		
		// Running label
		one = new JLabel("Game not running");
        one.setBounds(10, 10, 90, 21);
        add(one);

        // Text field. Send if hit enter
        txtOne = new JTextField();
        txtOne.setBounds(105, 10, 400, 21);
        txtOne.addKeyListener(new KeyListener() {
        	Boolean enterPressed = false;
			@Override
			public void keyPressed(KeyEvent arg0) {
				if ((arg0.getKeyCode() == KeyEvent.VK_ENTER) && !enterPressed) {
					server.parseInput(txtOne.getText());
					txtOne.setText("");
					enterPressed = true;
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {enterPressed = false;}
			@Override
			public void keyTyped(KeyEvent e) {}
        });
        add(txtOne);
        
        // Send Button
        submitTxtOne = new JButton("send");
        submitTxtOne.setBounds(510, 10, 80, 21);
        submitTxtOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				server.parseInput(txtOne.getText());
				txtOne.setText("");
			}
        });
        add(submitTxtOne);

        // Output text pane
        txtPane = new JTextArea(10, 40);
        txtPane.setEditable(false);
        txtPane.setFont(new Font("Courier", Font.PLAIN, 12));  
        txtScrollPane = new JScrollPane(txtPane);
        txtScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtScrollPane.setPreferredSize(new Dimension(width-20, height-20));
        txtScrollPane.setMinimumSize(new Dimension(10, 10));
        txtScrollPane.setBounds(10, 40, width-20, height-75);
        cp.add(txtScrollPane, BorderLayout.CENTER);
		
		pack();
		validate();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void run() {
		requestFocus();
	}
	
	public void stop() {
		dispose();
	}
	
	/* send output to outputBox
	 * 
	 */
	public void add(String str) {
		txtPane.append(str.trim() + "\n");
	}
	
	public void setOne(String str) {
		one.setText(str);
	}

}
