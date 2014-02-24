package soBeSorted;

public class Book {
	double price;
	String title, authorFirst, authorLast;
	String isbn;
	int quantity;
	
	Book(Double price, String title, String authorFirst, String authorLast, String isbn, int quantity){
		this.price = price;
		this.title = title;
		this.authorFirst = authorFirst;
		this.authorLast = authorLast;
		this.isbn = isbn;
		this.quantity = quantity;
	}
	/**
	 * 
	 * @param savedData
	 * savedData is a string in the form:
	 * price|title|authorFirst|authorLast|isbn|stock
	 */
	public Book(String savedData){
		String[] data = savedData.split("|");
		this.price = Double.parseDouble(data[0]);
		this.title = data[1];
		this.authorFirst = data[2];
		this.authorLast = data[3];
		this.isbn = data[4];
		this.quantity = Integer.parseInt(data[5]);
	}
	public void changePrice(double newPrice){
		this.price = newPrice;
	}
	public void sellCopy(){
		if(quantity > 0){
			this.quantity--;
		}
	}
	public void sellCopies(int amount){
		if(this.quantity - amount >= 0){
			this.quantity-= amount;
		}
	}
	public void addCopy(){
		this.quantity++;
	}
	 public void addCopies(int amount){
		 this.quantity += amount;
		 System.out.println(amount + " added!");
		 System.out.println(this.toString());
	 }
	 public double getPrice(){
		 return this.price;
	 }
	 public String getTitle(){
		 return this.title;
	 }
	 public String getFirst(){
		 return this.authorFirst;
	 }
	 public String getLast(){
		 return this.authorLast;
	 }
	 public String getISBN(){
		 return this.isbn;
	 }
	 public int getStock(){
		 return this.quantity;
	 }
	 @Override
	 public String toString(){
		 return String.format("%.2f|%s|%s|%s|%s|%d",
				 this.price,
				 this.title,
				 this.authorFirst,
				 this.authorLast,
				 this.isbn,
				 this.quantity);
	 }
	 
}	
