package soBeSorted;

import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class Receipt {
	double[] prices;

	public static void main(String[] args) {
		DateFormat f = new SimpleDateFormat("MMM_dd_yyyy");
		Double[] prices = getPrices();
		Double grandTotal = calcPrice(prices);
		Double taxes = getTaxes(grandTotal);
		System.out.println(grandTotal);
		metaSave(String.format("%.2f_%.2f", taxes, grandTotal),
				f.format(getPastDate(0)));

		ReceiptPrinter printer = new ReceiptPrinter();
		printer.setPrinterPrices(prices);
		printer.setPrinterGrandTotal(grandTotal);
		printer.printReceipt();

	}

	public static Date getPastDate(int amount) {
		Calendar c = Calendar.getInstance();
		c.roll(Calendar.DAY_OF_YEAR, -amount);
		return c.getTime();
	}

	public static Double[] getPrices() {
		Double input = (double) 0;
		ArrayList<Double> price = new ArrayList<Double>();
		Scanner in = new Scanner(System.in);
		do {
			System.out.println("Enter Price of book:");
			input = in.nextDouble();
			price.add(input);
			System.out.println(input);
		} while (!input.equals(0.0));
		in.close();
		Double[] priceArray = new Double[price.size()];
		priceArray = price.toArray(priceArray);
		return priceArray;
	}

	public static Double calcPrice(Double[] price) {
		Double total = (double) 0;
		for (Double p : price) {
			total += p;
		}
		Double taxes = 0.07;
		taxes *= total;
		Double grand = taxes + total;
		return grand;
	}

	/**
	 * This method appends one string to a file that may already exist. This
	 * method uses the save() method inside itself
	 * 
	 * @param data
	 *            String that is to be saved
	 * @param fileName
	 *            String is the name of the file to be saved at
	 */
	static void metaSave(String data, String fileName) {
		String[] currentData = retrieve(fileName);
		String[] newData;
		if (currentData == null) {
			newData = new String[1];
			newData[0] = data;
		} else {
			newData = new String[currentData.length + 1];
			for (int i = 0; i < currentData.length; i++) {
				newData[i] = currentData[i];
			}
			newData[currentData.length] = data;
		}

		save(newData, fileName);

	}

	/**
	 * Saves a String array to a given file location inside the directory
	 * <p>
	 * <tt>C:/POSSave/***.txt</tt>
	 * 
	 * @param data
	 *            the string array that should be saved
	 * @param fileName
	 */
	static void save(String[] data, String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream("C:/"
					+ "data" + ".txt");

			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(data);

			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Saving Data");
		}

	}

	/**
	 * 
	 * @param fileName
	 * @return the string array held at that file name
	 */
	static String[] retrieve(String fileName) {

		try {
			FileInputStream fis = new FileInputStream("C:/" + fileName + ".txt");

			BufferedInputStream bis = new BufferedInputStream(fis);

			DataInputStream dis = new DataInputStream(bis);

			ObjectInputStream ois = new ObjectInputStream(dis);

			String[] x = (String[]) ois.readObject();

			fis.close();

			return x;
		} catch (Exception e) {
			System.out.println("error");

			return null;
		}

	}

	public static Double getTaxes(Double total) {
		return total * .07 / 1.07;
	}

	public static class ReceiptPrinter implements Printable {
		Double[] printerPrices;
		Double printerGrandTotal;
		Double tax;

		// constructor
		public ReceiptPrinter() {
		}

		// methods

		public void setPrinterPrices(Double[] newPrinterPrices) {
			this.printerPrices = newPrinterPrices;
		}

		public void setPrinterGrandTotal(Double newPrinterGrandTotal) {
			this.printerGrandTotal = newPrinterGrandTotal;
			// this.tax = getTaxes(this.printerGrandTotal);
		}

		public int print(Graphics g, PageFormat pf, int page)
				throws PrinterException {

			if (page > 0) { /* We have only one page, and 'page' is zero-based */
				return NO_SUCH_PAGE;
			}

			/*
			 * User (0,0) is typically outside the imageable area, so we must
			 * translate by the X and Y values in the PageFormat to avoid
			 * clipping
			 */
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());

			/* Now we perform our rendering */
			g.drawString("Mendham Books", 100, 80);
			for (int i = 0; i < this.printerPrices.length - 1; i++) {
				g.drawString(String.format("Item %d: %.2f", i + 1,
						this.printerPrices[i]), 100, 20 * i + 100);
			}
			g.drawString(String.format("Tax: 7.0 Total Tax: %.2f", this.tax),
					100, this.printerPrices.length * 20 + 100);
			g.drawString(
					String.format("Grandtotal: %.2f", this.printerGrandTotal),
					100, this.printerPrices.length * 20 + 120);

			/* tell the caller that this page is part of the printed document */
			return PAGE_EXISTS;
		}

		public void printReceipt() {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(this);
			boolean ok = job.printDialog();
			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
					/* The job did not successfully complete */
				}
			}
		}
	}
}
