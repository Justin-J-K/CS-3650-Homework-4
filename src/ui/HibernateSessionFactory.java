package ui;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import model.*;

public class HibernateSessionFactory {
	
	private static SessionFactory sessionFactory;
	
	private HibernateSessionFactory() {}
	
	public synchronized static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration()
								 .configure("hibernate.cfg.xml")
				                 .addAnnotatedClass(Order.class)
				                 .addAnnotatedClass(Customer.class)
				                 .addAnnotatedClass(Address.class)
				                 .buildSessionFactory();
		}
		
		return sessionFactory;
	}
	
}
