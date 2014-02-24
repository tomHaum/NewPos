package soBeSorted;
import java.awt.GridLayout;

import gui.InventoryRow;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

public class Main {
//school comment	
public static void main(String[] args) {
		JFrame myFrame = new JFrame("testing");
		JPanel content = (JPanel) myFrame.getContentPane();

		JPanel subScroller = new JPanel();

		subScroller.setLayout(new GridLayout(0, 1));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby", "Fitzgerald", "F-Scott", "123456789012", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		subScroller.add(new JSeparator(JSeparator.HORIZONTAL));
		subScroller.add(new InventoryRow(new Book(10.00, "Great Gatsby 	", "Fitzgerald", "F-Scott", "1234567890123", 10)));
		
		// scroller.add(subScroller);
		JScrollPane scroller = new JScrollPane(subScroller);
		JPanel inventory = new JPanel();
		inventory.setLayout(new GridLayout(0,3));
		content.add(scroller);
		myFrame.pack();
		myFrame.setVisible(true);

	}
}
