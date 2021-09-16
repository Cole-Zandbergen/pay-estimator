import java.util.ArrayList;


public class PaycheckList {
	
	ArrayList<Paycheck> list;
	double averageNetPercentage;
	double percentageSum;
	ArrayList<Double> percentages;
	
	public PaycheckList() {
		list = new ArrayList<Paycheck>();
		percentages = new ArrayList<Double>();
		averageNetPercentage = 1.0;
		percentageSum = 0;
	}
	
	public void addCheck(Paycheck p)
	{
		list.add(p);
		percentages.add(p.percentage);
		percentageSum += p.percentage;
		averageNetPercentage = percentageSum / list.size();
	}
	
	public String save()
	{
		String s = "pl::\n";
		{
			for(int i = 0; i < list.size(); i++)
			{
				s += "#" + list.get(i).startDate.id;
				s += ")" + list.get(i).endDate.id;
				s += "$" + list.get(i).gross;
				s += "%" + list.get(i).net;
				s += "&" + list.get(i).percentage;
				s += "*" + list.get(i).totalHours;
			}
			s += ">";
			return s;
		}
	}
	
	public void load(String s, User u)
	{
		String startDateId = "";
		String endDateId = "";
		double gross = 0;
		double net = 0;
		double percentage = 0;
		double totalHours = 0;
		int i = 0;
		while(s.charAt(i) != '>')
		{
			
			if(s.charAt(i) == '#')
			{
				startDateId = getStringBetween(s, i+1, ')');
			}
			else if(s.charAt(i) == ')')
			{
				endDateId = getStringBetween(s, i+1, '$');
			}
			else if(s.charAt(i) == '$')
			{
				gross = Double.parseDouble(getStringBetween(s, i+1, '%'));
			}
			else if(s.charAt(i) == '%')
			{
				net = Double.parseDouble(getStringBetween(s, i+1, '&'));
			}
			else if(s.charAt(i) == '&')
			{
				percentage = Double.parseDouble(getStringBetween(s, i+1, '*'));
			}
			else if(s.charAt(i) == '*')
			{
				if(getStringBetween(s, i+1, '#') == null)
				{
					totalHours = Double.parseDouble(getStringBetween(s, i+1, '>'));
					addCheck(new Paycheck(u, new Day(startDateId, 0), new Day(endDateId, 0), gross, net, percentage, totalHours));
					break;
				}
				else
				{
					totalHours = Double.parseDouble(getStringBetween(s, i+1, '#'));
					addCheck(new Paycheck(u, new Day(startDateId, 0), new Day(endDateId, 0), gross, net, percentage, totalHours));
				}
			}
			i++;
		}
	}
	
	private String getStringBetween(String main, int index, char b)
	{
		int i = index;
		while(main.charAt(i) != b && i < main.length()-1)
		{
			i++;
		}
		if(main.charAt(i) == b)
		{
			return main.substring(index, i);
		}
		else
		{
			//System.out.println("");
			return null;
		}
	}
	
	public double getPercent(Day d)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(d.before(list.get(i).endDate))
			{
				if(!d.before(list.get(i).startDate))
				{
					return list.get(i).percentage;
				}
			}
			if(d.equals(list.get(i).startDate) || d.equals(list.get(i).endDate))
			{
				return list.get(i).percentage;
			}
		}
		return averageNetPercentage;
	}
}
