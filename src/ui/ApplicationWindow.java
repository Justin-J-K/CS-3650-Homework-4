package ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JButton;

public class ApplicationWindow extends JFrame {

	public static void main(String[] args) {
		new ApplicationWindow();
	}
	
	public ApplicationWindow() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(200, 100));
        setTitle("Application");
        
        JButton order = new JButton("Orders");
        order.addActionListener(e -> {
        	new OrderWindow();
        });
        
        JButton customer = new JButton("Customers");
        customer.addActionListener(e -> {
        	new CustomerWindow();
        });
        
        add(order);
        add(customer);
        
        pack();
        
        setVisible(true);
	}
	
}
