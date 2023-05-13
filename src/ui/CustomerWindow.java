package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.persistence.NoResultException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import model.Address;
import model.Customer;

public class CustomerWindow extends JFrame {

	private JTextField nameField, phoneField, emailField, streetField, cityField, stateField, zipCodeField;
	private Customer currentCustomer;
	
	public CustomerWindow() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 250));
        setTitle("Customer");
		
		nameField = new JTextField(20);
		phoneField= new JTextField(20);
		emailField= new JTextField(20);
		streetField= new JTextField(20);
		cityField = new JTextField(20);
		stateField = new JTextField(20);
		zipCodeField = new JTextField(20);
		
		JPanel fieldsPanel = new JPanel(new GridLayout(2, 1, 20, 0));
		
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.add(new JLabel("Name"), BorderLayout.NORTH);
		namePanel.add(nameField, BorderLayout.SOUTH);
		fieldsPanel.add(namePanel);
		
		JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 0, 20));
		
		JPanel phonePanel = new JPanel(new BorderLayout());
		phonePanel.add(new JLabel("Phone"), BorderLayout.NORTH);
		phonePanel.add(phoneField, BorderLayout.SOUTH);
		bottomPanel.add(phonePanel);
		
		JPanel emailPanel = new JPanel(new BorderLayout());
		emailPanel.add(new JLabel("Email"), BorderLayout.NORTH);
		emailPanel.add(emailField, BorderLayout.SOUTH);
		bottomPanel.add(emailPanel);
		
		fieldsPanel.add(bottomPanel);
		
		
		JPanel addressPanel = new JPanel(new GridLayout(2, 2, 20, 0));
		
		addressPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Address", TitledBorder.LEFT, TitledBorder.TOP));
		
		JPanel streetPanel = new JPanel(new BorderLayout());
		streetPanel.add(new JLabel("Street"), BorderLayout.NORTH);
		streetPanel.add(streetField, BorderLayout.SOUTH);
		addressPanel.add(streetPanel);
		
		JPanel cityPanel = new JPanel(new BorderLayout());
		cityPanel.add(new JLabel("City"), BorderLayout.NORTH);
		cityPanel.add(cityField, BorderLayout.SOUTH);
		addressPanel.add(cityPanel);
		
		JPanel statePanel = new JPanel(new BorderLayout());
		statePanel.add(new JLabel("State"), BorderLayout.NORTH);
		statePanel.add(stateField, BorderLayout.SOUTH);
		addressPanel.add(statePanel);
		
		JPanel zipCodePanel = new JPanel(new BorderLayout());
		zipCodePanel.add(new JLabel("Zip Code"), BorderLayout.NORTH);
		zipCodePanel.add(zipCodeField, BorderLayout.SOUTH);
		addressPanel.add(zipCodePanel);
		
		add(fieldsPanel);
		add(addressPanel);
		
		JButton search = new JButton("Search");
		search.addActionListener(e -> {
			SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			String name = nameField.getText().trim();
			
			Query query = session.createQuery("FROM Customer c LEFT JOIN FETCH c.address WHERE c.name = :name");
			
			query.setParameter("name", name);
			
			Customer customer;
			try {
				customer = (Customer) query.getSingleResult();
			} catch (NoResultException ex) {
				JOptionPane.showMessageDialog(this, "No records found!", "Error", JOptionPane.ERROR_MESSAGE);
				session.getTransaction().rollback();
				return;
			}
			
			phoneField.setText(customer.getPhone());
			emailField.setText(customer.getEmail());
			
			Address address = customer.getAddress();
			
			streetField.setText(address.getStreet());
			cityField.setText(address.getCity());
			stateField.setText(address.getState());
			zipCodeField.setText(address.getZipCode() + "");
			
			currentCustomer = customer;
			
			session.getTransaction().commit();
		});
		
		JButton add = new JButton("Add");
		add.addActionListener(e -> {
			SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			String name = nameField.getText().trim();
			String phone = phoneField.getText().trim();
			String email = emailField.getText().trim();
			String street = streetField.getText().trim();
			String city = cityField.getText().trim();
			String state = stateField.getText().trim();
			String zipCode = zipCodeField.getText().trim();
			
			int zipCodeInt = 0;
			try {
				zipCodeInt = Integer.parseInt(zipCode);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
				session.getTransaction().rollback();
				return;
			}
			
			if (name.isBlank() || phone.isBlank() || email.isBlank() || street.isBlank() || city.isBlank() || state.isBlank()) {
				JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
				session.getTransaction().rollback();
				return;
			}
			
			Address a = new Address(street, city, state, zipCodeInt);
			
			session.save(a);
			
			Customer c = new Customer(name, phone, email, a);
			
			session.save(c);
			JOptionPane.showMessageDialog(this, "Customer successfully included", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			session.getTransaction().commit();
			currentCustomer = c;
		});
		
		JButton update = new JButton("Update");
		update.addActionListener(e -> {
			if (currentCustomer == null) return;
			
			SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			String name = nameField.getText().trim();
			String phone = phoneField.getText().trim();
			String email = emailField.getText().trim();
			String street = streetField.getText().trim();
			String city = cityField.getText().trim();
			String state = stateField.getText().trim();
			String zipCode = zipCodeField.getText().trim();
			
			int zipCodeInt = 0;
			try {
				zipCodeInt = Integer.parseInt(zipCode);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
				session.getTransaction().rollback();
				return;
			}
			
			if (name.isBlank() || phone.isBlank() || email.isBlank() || street.isBlank() || city.isBlank() || state.isBlank()) {
				JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
				session.getTransaction().rollback();
				return;
			}
			
			currentCustomer.setName(name);
			currentCustomer.setPhone(phone);
			currentCustomer.setEmail(email);
			
			Address a = currentCustomer.getAddress();
			
			a.setStreet(street);
			a.setCity(city);
			a.setState(state);
			a.setZipCode(zipCodeInt);
			
			session.update(a);
			
			session.update(currentCustomer);
			JOptionPane.showMessageDialog(this, "Customer successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			session.getTransaction().commit();
		});
		
		JButton delete = new JButton("Delete");
		delete.addActionListener(e -> {
			if (currentCustomer == null) return;
			
			SessionFactory sessionFactory =  HibernateSessionFactory.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			session.delete(currentCustomer);
			JOptionPane.showMessageDialog(this, "Customer successfully deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
			
			session.getTransaction().commit();
			
			currentCustomer = null;
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(search);
		buttonPanel.add(add);
		buttonPanel.add(update);
		buttonPanel.add(delete);
		
		add(buttonPanel);
		
        setVisible(true);
        pack();
	}

	
	
}
