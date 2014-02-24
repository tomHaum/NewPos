package soBeSorted;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JavaTesting {
	public static void main(String[] args){
		Date d = new Date();
		d.setDate(1);
		Calendar c = Calendar.getInstance();
		c.roll(Calendar.DAY_OF_YEAR, -90);
		//d.setTime(System.currentTimeMillis());
		DateFormat f = new SimpleDateFormat("MMM_dd_yyyy");
		System.out.println(f.format(c.getTime()));
		
	}
	public Date getPastDate(int amount){
		Calendar c = Calendar.getInstance();
		c.roll(Calendar.DAY_OF_YEAR, -amount);
		return c.getTime();
	}
}
