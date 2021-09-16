
public class Day {
	
	int date, month, year;
	double hours, gross;
	String sid = "";
	int id;
	User user;
	int week;
	
	
	public Day(int d, int m, int y, int w) {
		week = w;
		date = d;
		month = m;
		year = y;
		sid += String.valueOf(year);
		if(month < 10)
		{
			sid += "0" + String.valueOf(month);
		}
		else
		{
			sid += String.valueOf(month);
		}
		if(date < 10)
		{
			sid += "0" + String.valueOf(date);
		}
		else
		{
			sid += date;
		}
		id = Integer.parseInt(sid);
		//System.out.println("New Day created with ID "+ id);
		hours = 0;
	}
	
	public Day(String i, int w)
	{
		week = w;
		sid = i;
		id = Integer.parseInt(i);
		year = Integer.parseInt(i.substring(0, 4));
		month = Integer.parseInt(i.substring(4, 6));
		date = Integer.parseInt(i.substring(6));
		hours = 0;
	}
	
	public void addHours(double h)
	{
		hours += h;
	}
	
	public double getHours()
	{
		return hours;
	}
	
	public void setHours(double h)
	{
		hours = h;
	}
	
	public void setGross(double g)
	{
		gross = g;
	}
	
	public boolean before(Day D)
	{
		if(id < D.id)
			return true;
		else 
			return false;
	}
	
	public boolean equals(Day d)
	{
		return id == d.id;	
	}
	
	public double display(User u)
	{
		double hoursSoFar = u.workDays.getHoursSoFar(this);
		double gross = Main.getDayPay(hoursSoFar, hours, u.payrate);
		//double net = gross * u.checkList.averageNetPercentage;
		double percent = u.checkList.getPercent(this);
		double net = gross * percent;
		
		System.out.println("Date: " + month + "/" + date + "/" + year + " - hours worked: " + hours + " -- gross wages: $" + Main.round(gross) + " -- estimated net wages: $" + Main.round(net));
		
		return net;
	}
}