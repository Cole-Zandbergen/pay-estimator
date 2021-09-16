import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class User {
	
	String name, filename;
	double payrate;
	DayList workDays;
	PaycheckList checkList;
	int week;
	Day startOfWeek;
	double hoursSoFar;
	
	public User(String n, double p, int w, Day s, double h) {
		name = n;
		payrate = p;
		week = w;
		startOfWeek = s;
		hoursSoFar = h;
		
		workDays = new DayList();
		checkList = new PaycheckList();
	}
	
	public boolean save()
	{
		String filename = "src/saveFiles/" + name;
		try {
			FileWriter f = new FileWriter(filename);
			f.write(name + "\n" + payrate + "\n" + week + "\n");
			f.write(startOfWeek.id + "\n");
			f.write(hoursSoFar + "\n");
			f.write(workDays.save() + "\n");
			f.write(checkList.save() + "\n");
			f.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void newWeek()
	{
		week++;
		hoursSoFar = 0;
	} 

}
