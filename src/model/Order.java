package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@Column(name = "number")
	private int number;
	@Column(name = "date")
	private Date date;
	@Column(name = "item")
	private String item;
	@Column(name = "price")
	private double price;
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	public Order() {
		super();
	}

	public Order(Date date, String item, double price, Customer customer) {
		super();
		this.date = date;
		this.item = item;
		this.price = price;
		this.customer = customer;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}
