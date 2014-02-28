package soBeSorted;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartGui {
	static JFrame frame = new JFrame("POS");
	static JPanel mainMenu = new JPanel();
	static JPanel holder = new JPanel();
	static JButton regButton = new JButton("Register");
	static JButton todayButton = new JButton("Today's Stats");
	static JButton sevenButton = new JButton("Seven day Stats");
	static JButton quitButton = new JButton("Quit");
	
	static JTextField text = new JTextField();
	
	static JPanel regPanel = new JPanel();
	static JPanel todayPanel = new JPanel();
	static JPanel sevenPanel = new JPanel();
	
	static Double[] prices;
	static List<Double> currentSalePrices = new ArrayList<Double>();
	
	
	static DateFormat f = new SimpleDateFormat("MMM_dd_yyyy");
	public static void main(String[] args) {
		
		AL al = new AL();
		mainMenu.setLayout(new GridBagLayout());
		Dimension d = sevenButton.getSize();
		regButton.addActionListener(al);
		todayButton.addActionListener(al);
		sevenButton.addActionListener(al);
		quitButton.addActionListener(al);
		
		regPanel = buildReg();

			
		addButtons();
		frame.getContentPane().add(mainMenu);
		frame.pack();
		frame.setVisible(true);
		
		changeScreen(regPanel);
	}
	/**
	 * this method re-makes the mainMenu with all its buttons.
	 * In order to change screens the whole main must be wiped then remade with the switch panel.
	 */
	public static void addButtons(){
		GridBagConstraints g = new GridBagConstraints();
		g.ipadx = 10;
		g.ipady = 10;
		//g.insets = new Insets(1, 1, 1, 1);
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 1;
		g.weighty = 1;
		g.anchor = GridBagConstraints.WEST;
		mainMenu.add(regButton, g);
		g.gridy++;
		mainMenu.add(todayButton, g);
		g.gridy++;
		mainMenu.add(sevenButton, g);
		g.gridy++;
		mainMenu.add(quitButton, g);
		g.gridy = 0;
		g.gridx = 1;
		g.gridheight = 4;
	}
	/**
	 * This method constructs a JPanel that will be the Register interface.
	 * It will constist of a Heading, a text field to input data,
	 * two labels to display that data and then a button to print the receipt
	 * @return
	 * the finished register Panel
	 */
	public static JPanel buildReg(){
		JPanel reg = new JPanel();
		JLabel label = new JLabel("The Register");
		
		JLabel taxName = new JLabel("Tax");
		JLabel saleName = new JLabel("Total");
		final JLabel taxValue = new JLabel("$0.00");
		final JLabel saleValue = new JLabel("$0.00");
		
		final JButton print = new JButton("Print Receipt");
		text = new JTextField();
		
		text.setToolTipText("Enter the price of the item, enter 0 after last item has been entered");
		text.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("hit enter");
				JTextField source = (JTextField)e.getSource();
				String data = source.getText();
				//System.out.println(data);
				if(data != null){
					boolean worked = false;
					Double price = 0.0;
					try{
						price = Double.parseDouble(data);
						worked = true;
					}catch(Exception ignored){
						//System.out.println("not a number");
					}
					if(worked && price != 0.0){
						//System.out.println("adding price to list");
						currentSalePrices.add(price);
					}
					if(worked && price == 0.0){
						JOptionPane.showMessageDialog(new JFrame("Successful Sale"), "A successful transaction has been processed.  You can now print a receipt.");
						prices = new Double[currentSalePrices.size()];
						prices = currentSalePrices
								.toArray(prices);
						currentSalePrices.clear();
						Double subTotal = total(prices);
						Double tax = subTotal * .07;
						Double grand = subTotal + tax;
						taxValue.setText(String.format("$%.2f",tax));
						saleValue.setText(String.format("$%.2f",grand));
						
						metaSave(String.format("%.2f_%.2f", tax, grand),
								f.format(getPastDate(0)));
					}
					
				}
				source.setText("");
			}
		});
		print.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(prices != null){
					ReceiptPrinter printer = new ReceiptPrinter();
					printer.setPrinterPrices(prices);
					Double grandTotal = total(prices);
					grandTotal *= 1.07;
					printer.printReceipt();
					
				}else{
					JOptionPane.showMessageDialog(new JFrame("Print Failed"), "There were no item to print a receipt for");
				}
			}
			
		});
		reg.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.ipadx = 10; g.ipady = 10;
		g.gridx = 0; g.gridy = 0;
		g.weightx = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		reg.add(label,g);
		g.gridy = 1;
		//g.gridx = 2;
		reg.add(text, g);
		
		g.gridy = 2;
		reg.add(taxName, g);
		g.gridx = 1;
		reg.add(saleName, g);
		g.gridx = 0; g.gridy = 3;
		reg.add(taxValue, g);
		g.gridx = 1;
		reg.add(saleValue, g);
		g.gridy = 4;
		reg.add(print,g);
		
		return reg;
	}
	/**
	 * Builds today's Statistics Panel.  Comprised of a heading with
	 *  a Table that holds the statistical values for both the taxes and the sales
	 * @return
	 * The finished JPanel
	 */
	public static JPanel buildToday(){
		JLabel header = new JLabel("Today's Statistics");
		JLabel taxCol = new JLabel("Tax");
		JLabel saleCol = new JLabel("Sale");
		JLabel meanRow = new JLabel("Mean");
		JLabel varRow = new JLabel("Variance");
		JLabel stdRow = new JLabel("Standard Deviation");
		JLabel totalRow = new JLabel("Totals");
		
		JPanel today = new JPanel();
		today.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.ipadx = 10; g.ipady = 10;
		g.gridx = 0; g.gridy = 0;
		g.gridx = 1;
		today.add(header,g);
		g.gridy = 1;
		today.add(taxCol,g);
		g.gridx = 2;
		today.add(saleCol,g);
		g.gridx = 0; g.gridy = 2;
		today.add(meanRow,g);
		g.gridy = 3;
		today.add(varRow, g);
		g.gridy++;
		today.add(stdRow,g);
		g.gridy++;
		today.add(totalRow, g);
		g.gridx = 1; g.gridy = 2;
		
		String[] dataStringForm = retrieveByLine(f.format(getPastDate(0)));
		Double[] dataSale = new Double[dataStringForm.length];
		Double[] dataTax = new Double[dataStringForm.length];
		Double[] tempData = new Double[2];
		for(int i = 0; i < dataStringForm.length; i++){
			tempData = parseData(dataStringForm[i]);
			dataTax[i] = tempData[0];
			dataSale[i] = tempData[1];
		}
		
		Double taxMean, taxVar, taxStdDv;
		Double salesMean, salesVar, salesStdDv;
		
		taxMean = getMean(dataTax);
		taxVar = getVar(taxMean, dataTax);
		taxStdDv = getStdDv(taxVar);
		
		salesMean = getMean(dataSale);
		salesVar = getVar(salesMean, dataSale);
		salesStdDv = getStdDv(salesVar);
		
		Double salesTot = 0.0;
		for(Double d:dataSale){
			salesTot += d;
		}
		
		Double taxTot = 0.0;
		for(Double d:dataTax){
			taxTot += d;
		}
		
		JLabel taxMeanL = new JLabel(String.format("$%.2f",taxMean));
		JLabel taxVarL = new JLabel(String.format("$%.2f",taxVar));
		JLabel taxStdL = new JLabel(String.format("$%.2f",taxStdDv));
		JLabel taxTotL = new JLabel(String.format("$%.2f",taxTot));
		
		JLabel salesMeanL = new JLabel(String.format("$%.2f",salesMean));
		JLabel salesVarL = new JLabel(String.format("$%.2f",salesVar));
		JLabel salesStdL = new JLabel(String.format("$%.2f",salesStdDv));
		JLabel salesTotL = new JLabel(String.format("$%.2f",salesTot));
		
		g.gridx = 1; g.gridy = 1;
		g.gridy++;
		today.add(taxMeanL,g);
		g.gridy++;
		today.add(taxVarL,g);
		g.gridy++;
		today.add(taxStdL,g);
		g.gridy++;
		today.add(taxTotL,g);
		
		g.gridx = 2; g.gridy = 1;
		g.gridy++;
		today.add(salesMeanL,g);
		g.gridy++;
		today.add(salesVarL,g);
		g.gridy++;
		today.add(salesStdL,g);
		g.gridy++;
		today.add(salesTotL,g);
		return today;
	}
	/**
	 * Very similar to the today statistics method.
	 *  The only difference is in the list of data 
	 *  that this method uses for the stats.
	 * @return
	 * The finished JPanel for Seven Day Stats
	 */
	public static JPanel buildSevenDay(){
		JLabel header = new JLabel("Today's Statistics");
		JLabel taxCol = new JLabel("Tax");
		JLabel saleCol = new JLabel("Sale");
		JLabel meanRow = new JLabel("Mean");
		JLabel varRow = new JLabel("Variance");
		JLabel stdRow = new JLabel("Standard Deviation");
		JLabel totalRow = new JLabel("Totals");
		
		JPanel today = new JPanel();
		today.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.ipadx = 10; g.ipady = 10;
		g.gridx = 0; g.gridy = 0;
		g.gridx = 1;
		today.add(header,g);
		g.gridy = 1;
		today.add(taxCol,g);
		g.gridx = 2;
		today.add(saleCol,g);
		g.gridx = 0; g.gridy = 2;
		today.add(meanRow,g);
		g.gridy = 3;
		today.add(varRow, g);
		g.gridy++;
		today.add(stdRow,g);
		g.gridy++;
		today.add(totalRow, g);
		g.gridx = 1; g.gridy = 2;
		
		//this is the part that differs
		Double[] dataSale = getTheMetaData();
		Double[] dataTax = new Double[dataSale.length];
		
		for(int i = 0; i < dataSale.length; i++){
			dataTax[i] = getTaxes(dataSale[i]);
		}
		
		Double taxMean, taxVar, taxStdDv;
		Double salesMean, salesVar, salesStdDv;
		
		taxMean = getMean(dataTax);
		taxVar = getVar(taxMean, dataTax);
		taxStdDv = getStdDv(taxVar);
		
		salesMean = getMean(dataSale);
		salesVar = getVar(salesMean, dataSale);
		salesStdDv = getStdDv(salesVar);
		
		Double salesTot = 0.0;
		for(Double d:dataSale){
			salesTot += d;
		}
		
		Double taxTot = 0.0;
		for(Double d:dataTax){
			taxTot += d;
		}
		
		JLabel taxMeanL = new JLabel(String.format("$%.2f",taxMean));
		JLabel taxVarL = new JLabel(String.format("$%.2f",taxVar));
		JLabel taxStdL = new JLabel(String.format("$%.2f",taxStdDv));
		JLabel taxTotL = new JLabel(String.format("$%.2f",taxTot));
		
		JLabel salesMeanL = new JLabel(String.format("$%.2f",salesMean));
		JLabel salesVarL = new JLabel(String.format("$%.2f",salesVar));
		JLabel salesStdL = new JLabel(String.format("$%.2f",salesStdDv));
		JLabel salesTotL = new JLabel(String.format("$%.2f",salesTot));
		
		g.gridx = 1; g.gridy = 1;
		g.gridy++;
		today.add(taxMeanL,g);
		g.gridy++;
		today.add(taxVarL,g);
		g.gridy++;
		today.add(taxStdL,g);
		g.gridy++;
		today.add(taxTotL,g);
		
		g.gridx = 2; g.gridy = 1;
		g.gridy++;
		today.add(salesMeanL,g);
		g.gridy++;
		today.add(salesVarL,g);
		g.gridy++;
		today.add(salesStdL,g);
		g.gridy++;
		today.add(salesTotL,g);
		return today;
	}
	/**
	 * this method whips the content Pane of the mainMenu Panel,
	 *  then whips clean the main menu Pane
	 *  so that it can rebuild the main menu a different side Panel,
	 *   either the register, one day stats or seven day stats
	 * @param newScreen
	 * the panel that mainMenu will contain next
	 */
	public static void changeScreen(JPanel newScreen){
		frame.getContentPane().removeAll();
		frame.getContentPane().invalidate();
		
		mainMenu.removeAll();
		addButtons();
		GridBagConstraints g = new GridBagConstraints();
		g.ipadx = 10; g.ipady = 10;
		g.gridx = 1; g.gridy = 0;
		g.gridheight = 4;
		mainMenu.add(newScreen,g);
		
		frame.getContentPane().add(mainMenu);
		frame.getContentPane().revalidate();
		frame.pack();
		if(text.isShowing()){
			text.grabFocus();
			//System.out.println("focusing");
		}
		//frame.validate();
	}
	/**
	 * Calculates the mean from an array of doubles
	 * @param data
	 * the list of Doubles
	 * @return
	 * the mean
	 */
	public static Double getMean(Double[] data){
		Double total = total(data);
		return total / data.length;
	}
	/**
	 * Calculaltes the taxes from a sale given the grand Total
	 * @param total
	 * the grand total of a sale
	 * @return
	 * the sales tax
	 */
	public static Double getTaxes(Double total) {
		return total * .07 / 1.07;
	}
	/**
	 * A complex method that retrieves the sales data from the past 7 days.
	 * This method then compiles a list of all their grand totals into a large
	 * array
	 * @return
	 * a large array with 7 days worth of sales data
	 */
	public static Double[] getTheMetaData(){
		//retrieving each day from file and converting it into a double array
		//containing each sale's grand total
		
		String[] oneString = retrieveByLine(f.format(getPastDate(0)));
		Double[] oneDouble = new Double[oneString.length];
		oneDouble = parseArrayOfStrings(oneString);
		
		String[] twoString = retrieveByLine(f.format(getPastDate(1)));
		Double[] twoDouble = new Double[twoString.length];
		twoDouble = parseArrayOfStrings(twoString);
		
		String[] threeString = retrieveByLine(f.format(getPastDate(2)));
		Double[] threeDouble = new Double[threeString.length];
		threeDouble = parseArrayOfStrings(threeString);
		
		String[] fourString = retrieveByLine(f.format(getPastDate(3)));
		Double[] fourDouble = new Double[fourString.length];
		fourDouble = parseArrayOfStrings(fourString);
		
		String[] fiveString = retrieveByLine(f.format(getPastDate(4)));
		Double[] fiveDouble = new Double[fiveString.length];
		fiveDouble = parseArrayOfStrings(fiveString);
		
		String[] sixString = retrieveByLine(f.format(getPastDate(5)));
		Double[] sixDouble = new Double[sixString.length];
		sixDouble = parseArrayOfStrings(sixString);
		
		String[] sevenString = retrieveByLine(f.format(getPastDate(6)));
		Double[] sevenDouble = new Double[sevenString.length];
		sevenDouble = parseArrayOfStrings(sevenString);
		
		//each day's number of sales
		int l1 = oneDouble.length, l2 = twoDouble.length, l3 = threeDouble.length, l4 = fourDouble.length, l5 = fiveDouble.length, l6 = sixDouble.length, l7 = sevenDouble.length;
		//the running total for number of sales, starts from to today and goes backwards
		int c1 = l1, c2 = c1+l2, c3 = c2+l3, c4 = c3+l4, c5 = c4+l5, c6=c5+l6, c7 = c6+l7;
		
		Double[] allData = new Double[c7];
		for(int i = 0; i < c1; i++){
			allData[i] = oneDouble[i];
		}
		for(int i = c1; i < c2; i++){
			allData[i] = twoDouble[i-c1];
			//the minus is so that the day array starts at 0 while
			//the large array starts after the first set of data
		}
		for(int i = c2; i < c3; i++){
			allData[i] = threeDouble[i-c2];
		}
		for(int i = c3; i < c4; i++){
			allData[i] = fourDouble[i-c3];
		}
		for(int i = c4; i < c5; i++){
			allData[i] = fiveDouble[i-c4];
		}
		for(int i = c5; i < c6; i++){
			allData[i] = sixDouble[i-c5];
		}
		for(int i = c6; i < c7; i++){
			allData[i] = sevenDouble[i-c6];
		}
		
		return allData;
	}
	/**
	 * 
	 * @param amount
	 * number of days backwards in time
	 * @return
	 * A data object of the requested day
	 */
	public static Date getPastDate(int amount) {
		Calendar c = Calendar.getInstance();
		c.roll(Calendar.DAY_OF_YEAR, -amount);
		return c.getTime();
	}
	/**
	 * Takes a variance and square roots it to get the Standard Deviation
	 * @param var
	 * the variance
	 * @return
	 * Standard Deviation
	 */
	public static Double getStdDv(Double var){
		Double stdDev = Math.pow(var, .5);
		return stdDev;
	}
	/**
	 * from a list of data and its mean this method calculates the variance
	 * @param mean
	 * the mean of the data
	 * @param data
	 * the list of doubles
	 * @return
	 * the variance
	 */
	public static Double getVar(Double mean, Double[] data){
		Double total = 0.0;
		for(int i = 0; i < data.length; i++){
			total+=Math.pow((data[i]-mean),2);
		}
		total = (total/((Double)((double)data.length)));
		return total;
	}
	/**
	 * Appends a file or creates a new file(if this is the first save of the day)
	 * 
	 * @param data
	 * the string to be saved to file
	 * @param fileName
	 * the location and name of the save file
	 */
	static void metaSave(String data, String fileName) {
		String[] currentData = retrieveByLine(fileName);
		String[] newData;
		if (currentData == null) {
			newData = new String[1];
			newData[0] = data;
			//System.out.println("New existing file");
		} else {
			newData = new String[currentData.length + 1];
			for (int i = 0; i < currentData.length; i++) {
				newData[i] = currentData[i];
			}
			newData[currentData.length] = data;
			//System.out.println("Adding to the end");
		}
	
		saveByLine(newData, fileName);
	
	}
	/**
	 * 
	 * @param dataString
	 * takes an array of strings each in the format <tt>tax_grandTotal</tt>
	 * for each element.  Each element represents an individual sale.
	 * @return
	 * returns an array of each sale's grand total
	 */
	public static Double[] parseArrayOfStrings(String[] dataString){
		Double[] dataDouble = new Double[dataString.length];
		Double tempData;
		for(int i = 0; i < dataString.length; i++){
			tempData = parseData(dataString[i])[1];
			dataDouble[i] = tempData;
		}
		return dataDouble;
	}
	/**
	 * 
	 * @param data
	 * takes a single string of data in the form <tt>tax_grandTotal</tt> 
	 *
	 * @return
	 *  a double array of length 2 that contains tax and grandTotal respectively
	 */
	public static Double[] parseData(String data) {
		/*split makes the single string into an array
		* of strings split at each in instance of "_"
		* each element does not include the "_"
		*/
		String[] dati = data.split("_");
		
		Double[] doubles = new Double[2];
		doubles[0] = Double.parseDouble(dati[0]);
		doubles[1] = Double.parseDouble(dati[1]);
		return doubles;
	}
	/**
	 * Reads a file of sales and returns an array with
	 *  elements each containing each containing a single sail
	 *  in the format <p>
	 *  <tt>tax_grandTotal</tt>
	 * @param fileName
	 * name/location of the file to be read
	 * @return
	 * the array of data from the file
	 */
	public static String[] retrieveByLine(String fileName) {
		File dataFile = new File("P:/" + fileName + ".txt");
		BufferedReader in;
		List<String> data = new ArrayList();
		//checks to see if the file that it is trying to reach actually is there
		
		if (dataFile.exists()) {
			try {
				in = new BufferedReader(new FileReader(dataFile));
				
				String currentLine = null;//temp storage
				
				//while the current read line still exists
				while ((currentLine = in.readLine()) != null) {
					data.add(currentLine);
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//convert the data list into a data array
		String[] dataArray = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			dataArray[i] = data.get(i);
		}
		return dataArray;
	}
	/**
	 * Saves a String array to file with each element taking its own line
	 * @param data
	 * the string array to be saved
	 * @param fileName
	 * the file' name/location
	 */
	static void saveByLine(String[] data, String fileName) {
		File saveFile = new File("P:/" + fileName + ".txt");
		if (!saveFile.exists()) {// this part checks for if the file already exists
			//System.out.println("Trying to create new file");
			try {
				saveFile.createNewFile();
				//System.out.println("File successfully created.");

			} catch (IOException e) {
				//System.out.println("File not created");
				e.printStackTrace();
			}
		}

		// this part writes the list of primes to the file
		try {
			// Buffered writer writes data
			BufferedWriter output = new BufferedWriter(new FileWriter(saveFile));
			// it is an object that iterates over the prime number list
			for (int i = 0; i < data.length; i++) {
				output.write(data[i]);
				output.newLine();
			}
			// closing the
			output.close();
		} catch (Exception e) {
			// if anything goes wrong print the source
			e.printStackTrace();
		}
	}
	/**
	 * A simpole method to get the total of a double array
	 * @param data
	 * the double array to be totaled
	 * @return
	 * the total of the double array
	 */
	public static Double total(Double[] data){
		Double total = 0.0;
		for(Double d: data){
			total += d;
		}
		return total;
	}
	/**
	 * 
	 * This action listener is in charge of the main Menu buttons, it controls
	 * which button switches to which JPanel.
	 * 
	 * This also handles quitting the program with the quit button
	 * @author 14_thaumersen
	 *
	 */
	private static class AL implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton)e.getSource();
			JPanel switchTo = new JPanel();
			
			switch(source.getText()){
			

			case "Today's Stats":
				todayPanel = buildToday();
				switchTo = todayPanel;
				//System.out.println("today");
				break;
			case "Seven day Stats":
				sevenPanel = buildSevenDay();
				switchTo = sevenPanel;
				//System.out.println("Seven");
				break;
			case "Register":
				switchTo = regPanel;
				//System.out.println("Register");
				break;
			case "Quit":
				//System.out.println("Quit");
				frame.dispose();
				break;
			}
			changeScreen(switchTo);
			frame.validate();
			
		}
	}
	/**
	 * an inner class that implements Printable, it itselfs in printable and
	 * @author 14_thaumersen
	 *
	 */
	private static class ReceiptPrinter implements Printable {
		Double[] printerPrices;
		Double printerGrandTotal;
		Double printerTotal;
		Double tax;

		
		public ReceiptPrinter() {
		}

		// methods
		/**
		 * Sets this instance of printer to a specific array of prices,
		 * from which it calculates the sub-total and tax and Grand total
		 * @param newPrinterPrices
		 * the array of prices to be printed
		 */
		public void setPrinterPrices(Double[] newPrinterPrices) {
			this.printerPrices = newPrinterPrices;
			Double total = 0.0;
			for(Double d: newPrinterPrices){
				total += d;
			}
			this.printerTotal = total;
			this.tax = total * .07;
			this.printerGrandTotal = total * 1.07;
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
			for (int i = 0; i < this.printerPrices.length; i++) {
				g.drawString(String.format("Item %d: %.2f", i + 1,
						this.printerPrices[i]), 100, 20 * i + 100);
			}
			g.drawString(String.format("SubTotal: %.2f", this.printerTotal),
					100, this.printerPrices.length * 20 + 120);
			g.drawString(String.format("Tax: 7.0 Total Tax: %.2f", this.tax),
					100, this.printerPrices.length * 20 + 140);
			g.drawString(
					String.format("Grandtotal: %.2f", this.printerGrandTotal),
					100, this.printerPrices.length * 20 + 160);

			/* tell the caller that this page is part of the printed document */
			return PAGE_EXISTS;
		}
		/**
		 * this is the method that gets called by an outside class, 
		 * this method creates the PrinterJob that sets the printing in motion
		 */
		public void printReceipt() {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(this);//this is 
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
