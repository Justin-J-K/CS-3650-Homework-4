package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import model.Order;
import model.Customer;

public class OrderWindow extends JFrame {

	private JTextField numberField, dateField, priceField;
	private JComboBox customerField, itemField;
	private Order currentOrder;
	
	public OrderWindow() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 250));
        setTitle("Order");
        
        numberField = new JTextField(20);
        dateField = new JTextField(20);
        priceField = new JTextField(20);
        customerField = new JComboBox();
        itemField = new JComboBox();
        
        itemField.addItem("Caesar Salad");
        itemField.addItem("Greek Salad");
        itemField.addItem("Cobb Salad");
        
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 1, 20, 0));
        
        JPanel panel1 = new JPanel(new GridLayout(1, 2, 0, 40));
		
		JPanel numberPanel = new JPanel(new BorderLayout());
		numberPanel.add(new JLabel("Number"), BorderLayout.NORTH);
		numberPanel.add(numberField, BorderLayout.SOUTH);
		panel1.add(numberPanel);
		
		JPanel datePanel = new JPanel(new BorderLayout());
		datePanel.add(new JLabel("Date"), BorderLayout.NORTH);
		datePanel.add(dateField, BorderLayout.SOUTH);
		panel1.add(datePanel);
		
		fieldsPanel.add(panel1);
		
		JPanel customerPanel = new JPanel(new BorderLayout());
		customerPanel.add(new JLabel("Customer"), BorderLayout.NORTH);
		customerPanel.add(customerField, BorderLayout.SOUTH);
		fieldsPanel.add(customerPanel);
		
		JPanel panel2 = new JPanel(new GridLayout(1, 2, 0, 20));
		
		JPanel itemPanel = new JPanel(new BorderLayout());
		itemPanel.add(new JLabel("Item"), BorderLayout.NORTH);
		itemPanel.add(itemField, BorderLayout.SOUTH);
		panel2.add(itemPanel);
		
		JPanel pricePanel = new JPanel(new BorderLayout());
		pricePanel.add(new JLabel("Price"), BorderLayout.NORTH);
		pricePanel.add(priceField, BorderLayout.SOUTH);
		panel2.add(pricePanel);
		
		fieldsPanel.add(panel2);
		
		add(fieldsPanel);
		
        
        JButton search = new JButton("Search");
		search.addActionListener(e -> {
			SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			String number = numberField.getText().trim();
			
			int num;
			
			try {
				num = Integer.parseInt(number);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Order order = session.get(Order.class, num);
			
			if (order == null) {
				JOptionPane.showMessageDialog(this, "No records found!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			dateField.setText(new SimpleDateFormat("MM/dd/yyyy").format(order.getDate()));
			priceField.setText(NumberFormat.getCurrencyInstance().format(order.getPrice()));
			itemField.setSelectedItem(order.getItem());
			customerField.setSelectedItem(order.getCustomer());
			
			currentOrder = order;
			
			session.getTransaction().commit();
		});
		
		JButton add = new JButton("Add");
		add.addActionListener(e -> {
			SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			String number = numberField.getText().trim();
			String date = dateField.getText().trim();
			String price = priceField.getText().trim();
			Customer customer = (Customer) customerField.getSelectedItem();
			String item = (String) itemField.getSelectedItem();
			
			int numberInt = 0;
			try {
				numberInt = Integer.parseInt(number);
			} catch (NumberFormatException ex) {
				session.getTransaction().rollback();
				JOptionPane.showMessageDialog(this, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			double priceDouble = 0.0;
			try {
				priceDouble = Double.parseDouble(price);
			} catch (NumberFormatException ex) {
				session.getTransaction().rollback();
				JOptionPane.showMessageDialog(this, "Invalid price!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Date dateDate = null;
			try {
				dateDate = new SimpleDateFormat("MM/dd/yyyy").parse(date);
			} catch (ParseException ex) {
				session.getTransaction().rollback();
				JOptionPane.showMessageDialog(this, "Invalid date!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Order o = new Order(dateDate, item, priceDouble, customer);
			o.setNumber(numberInt);
			
			session.save(o);
			JOptionPane.showMessageDialog(this, "Order successfully included", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			session.getTransaction().commit();
			currentOrder = o;
		});
		
		JButton update = new JButton("Update");
		update.addActionListener(e -> {
			if (currentOrder == null) return;
			
			SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			String number = numberField.getText().trim();
			String date = dateField.getText().trim();
			String price = priceField.getText().trim();
			Customer customer = (Customer) customerField.getSelectedItem();
			String item = (String) itemField.getSelectedItem();
			
			int numberInt = 0;
			try {
				numberInt = Integer.parseInt(number);
			} catch (NumberFormatException ex) {
				session.getTransaction().rollback();
				JOptionPane.showMessageDialog(this, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			double priceDouble = 0.0;
			try {
				priceDouble = Double.parseDouble(price);
			} catch (NumberFormatException ex) {
				session.getTransaction().rollback();
				JOptionPane.showMessageDialog(this, "Invalid price!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Date dateDate = null;
			try {
				dateDate = new SimpleDateFormat("MM/dd/yyyy").parse(date);
			} catch (ParseException ex) {
				session.getTransaction().rollback();
				JOptionPane.showMessageDialog(this, "Invalid date!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			currentOrder.setDate(dateDate);
			currentOrder.setItem(item);
			currentOrder.setNumber(numberInt);
			currentOrder.setPrice(priceDouble);
			currentOrder.setCustomer(customer);
			
			session.update(currentOrder);
			JOptionPane.showMessageDialog(this, "Order successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			session.getTransaction().commit();
		});
		
		JButton delete = new JButton("Delete");
		delete.addActionListener(e -> {
			if (currentOrder == null) return;
			
			SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			session.delete(currentOrder);
			JOptionPane.showMessageDialog(this, "Order successfully deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			session.getTransaction().commit();
			
			currentOrder = null;
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(search);
		buttonPanel.add(add);
		buttonPanel.add(update);
		buttonPanel.add(delete);
		
		add(buttonPanel);
        
		addCustomers();
		
        setVisible(true);
        pack();
        
        
	}
	
	private void addCustomers() {
		SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		
		Query<Customer> q = session.createQuery("from Customer", Customer.class);
		List<Customer> list = q.getResultList();
		
		for (Customer c : list) {
			customerField.addItem(c);
		}
		
		session.getTransaction().commit();
	}
	
}
