import java.util.ArrayList;
import java.util.Scanner;

public class DayList {
	
	ArrayList<Day> list;
	
	public DayList() {
		list = new ArrayList<Day>();
	}
	
	public void addDay(Day d)
	{
		if(list.size() == 0)
		{
			list.add(d);
			return;
		}
		
		if(!d.before(list.get(list.size()-1)))
		{
			list.add(d);
			return;
		}
		
		if(d.before(list.get(0)))
		{
			list.add(0, d);
			return;
		}
		
		for(int i = list.size()-1; i > 0; i--)
		{
			if(!d.before(list.get(i)))
			{
				list.add(i+1, d);
				return;
			}
		}
	}
	
	public String save()
	{
		//System.out.println("running save function, there are " + list.size() + " elements to save");
		String s = "dl::\n";
		for(int i = 0; i < list.size(); i++)
		{
			//System.out.println("Printing info for day " + list.get(i).id + ", there are " + list.get(i).hours + "hours on this day.");
			s += "|" + list.get(i).id + list.get(i).week + ";" + list.get(i).hours + "_";
			//System.out.println("String so far: " + s);
		}
		s += "?";
		return s;
	}
	
	public void load(String s)
	{
		System.out.println("Loading days");
		int i = 0;
		int j = 0;
		while(s.charAt(i) != '?')
		{
			//System.out.println("Scanning " + s.charAt(i));
			if(s.charAt(i) == '|')
			{
				String sid = s.substring(i+1, i+9);
				//System.out.println(sid);
				i += 9;
				j = i;
				
				while(s.charAt(j) != ';')
				{
					j++;
				}
				Day d = new Day(sid, Integer.parseInt(s.substring(i, j)));
				addDay(d);
				//System.out.println("Day created with id " + d.id + ", and week number " + d.week);
				i = j+1;
				j=i;
				
				while(s.charAt(j) != '_')
				{
					j++;
				}
				d.addHours(Double.parseDouble(s.substring(i, j)));
				i = j+1;
				j = i;
			}
			else
			{
				i++;
				j++;
			}
		}
	}
	
	public double getTotalHours(Day start, Day end)
	{
		double hours = 0;
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).id >= start.id && list.get(i).id <= end.id)
			{
				hours += list.get(i).hours;
				//System.out.println("Adding " + list.get(i).hours + " hours for " + list.get(i).id);
			}
			else if(list.get(i).id > end.id)
				break;
		}
		return hours;
	}

	public Day getWeek(Day start) // given the first day of a week, this function returns the last day of that same week
	{
		int w = 0;
		//Day lastDay = null;
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).id >= start.id)
			{
				w = list.get(i).week;
				while(list.get(i).week == w && i < list.size()-1)
				{
					i++;
				}
				if(i == list.size()-1)
					return list.get(i-1);
				else
					return list.get(i-1);
			}
		}
		return list.get(list.size()-1);
		/*if(lastDay == null)
			return list.get(list.size()-1);
		else
			return lastDay;*/
		
	}
	
	public double getGrossPay(Day start, Day end, double payrate)
	{
		double regHours = 0.0;
		double otHours = 0.0;
		Day startWeek = start;
		
		while(startWeek.before(end))
		{
			//System.out.println("adding hours up.");
			Day endWeek = getWeek(startWeek);
			double h = getTotalHours(startWeek, endWeek);
			//System.out.println("Hours for week from " + startWeek.id + " to " + endWeek.id + ": " + h);
			if(h > 40.0)
			{
				regHours += 40.0;
				otHours += h - 40.0;
			}
			else
			{
				regHours += h;
			}
			startWeek = getNextDay(endWeek);
		}
		
		System.out.println("Regular: " + regHours + ", Overtime: " + otHours);
		
		return (regHours * payrate) + (otHours * 1.5 * payrate);
		
	}
	
	public Day getNextDay(Day d)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i).id == d.id)
			{
				if(i == list.size()-1)
					return list.get(i);
				else
					return list.get(i+1);
			}
		}
		return list.get(list.size()-1);
	}
	
	public double recall(int n, User u)
	{
		double pay = 0;
		if(n > list.size())
			n = list.size();
		for(int c = list.size() - n; c < list.size(); c++)
		{
			pay += list.get(c).display(u);
		}
		return pay;
		
		/*for(int c = 1; c <= n; c++)
		{
			if(list.size()-c >= 0)
			{
				list.get(list.size()-c).display(u);
			}
			else
			{
				break;
			}
		}*/
	}
	
	public double recallSince(Day d, User u)
	{
		double p = 0;
		for(int i = getDayIndex(d); i < list.size(); i++)
		{
			p += list.get(i).display(u);
		}
		return p;
	}
	
	public void editDay(Day d)
	{
		Scanner s = new Scanner(System.in);
		Day dayToEdit = getDay(d);
		if(dayToEdit == null)
		{
			System.out.println("No matching date found in your records.");
		}
		else
		{
			System.out.println("How many hours did you work on this day?");
			dayToEdit.setHours(s.nextDouble());
		}
	}
	
	/**
	 * This method takes a day as input, then returns the day in this list that has the same date
	 */
	public Day getDay(Day d)
	{
		for(int i = list.size()-1; i > 0; i--)
		{
			if(d.id == list.get(i).id)
			{
				return list.get(i);
			}
		}
		return null;
	}
	
	private int getDayIndex(Day d)
	{
		if(!d.before(list.get(list.size()-1)) && !d.equals(list.get(list.size()-1)))
			return list.size();
		for(int i = list.size()-1; i >= 0; i--)
		{
			if(d.id == list.get(i).id)
			{
				return i;
			}
			else if(d.id > list.get(i).id && i != list.size()-1)
			{
				return i+1;
			}
		}
		return 0;
	}
	
	public double getHoursSoFar(Day d)
	{
		double hoursSoFar = 0;
		for(int i = list.size()-1; i > 0; i--)
		{
			if(list.get(i).week == d.week && list.get(i).before(d))
			{
				hoursSoFar += list.get(i).hours;
			}
			if(list.get(i).week < d.week)
			{
				break;
			}
		}
		return hoursSoFar;
	}
}
