import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {

	public static void main(String[] args) {
		User currentUser;
		
		Scanner scanner  = new Scanner(System.in);
		System.out.println("Welcome\nEnter your name:");
		String name = scanner.next();
		
		currentUser = loadUser(name);
		
		System.out.println("Your current user is " + currentUser.name);
		
		boolean active = true;
		
		while(active)
		{
			System.out.println("Enter a command, or enter 'help' to display a list of possible commands");
			String input = scanner.next();
			/**
			 * ADD COMMAND
			 */
			if(input.equals("add"))
			{
				System.out.println("Is this the first work day of a new week? (y/n)");
				if(yesNo(scanner.next()))
					currentUser.newWeek();
				Day d = getDate(currentUser);
				System.out.println("Enter the amount of hours worked on this day:");
				double hours = scanner.nextDouble();
				d.addHours(hours);
				double todaysPay = getDayPay(currentUser.hoursSoFar, hours, currentUser.payrate);
				d.setGross(todaysPay);
				System.out.println("Your gross pay today is $" + round(todaysPay));
				System.out.println("Your estimated net pay for today is $" + round(currentUser.checkList.averageNetPercentage * todaysPay));
				currentUser.hoursSoFar += hours;
				currentUser.workDays.addDay(d);
				
			}
			/*************************************************************************/
			
			else if(input.equals("save"))
			{
				if(currentUser.save())
					System.out.println("Saved successfully");
				else
					System.out.println("error");
			}
			
			else if(input.equals("payday"))
			{
				currentUser.checkList.addCheck(new Paycheck(currentUser));
			}
			
			else if(input.equals("recall"))
			{
				System.out.println("How many days would you like to recall?");
				int n = scanner.nextInt();
				double netPay = currentUser.workDays.recall(n, currentUser);
				System.out.println("Your total estimated net pay from the past " + n + " work days is $" + round(netPay));
			}
			
			else if(input.equals("edit"))
			{
				currentUser.workDays.editDay(getDate(currentUser));
			}
			
			else if(input.equals("exit"))
			{
				active = false;
			}
			
			else if(input.equals("sofar"))
			{
				double h = currentUser.hoursSoFar;
				double p = getDayPay(0, h, currentUser.payrate);
				System.out.println("You have worked " + h + " hours so far this week.");
				System.out.println("Your gross pay so far this week is $" + round(p));
				System.out.println("Your estimated net pay so far this week is $" + round(p * currentUser.checkList.averageNetPercentage));
			}
			
			else if(input.equals("percentage"))
			{
				System.out.println("Your average net percentage is " + round(currentUser.checkList.averageNetPercentage * 100) + "%");
			}
			
			else if(input.equals("recallsince"))
			{
				System.out.println("Enter a date to recall back to: ");
				Day d = getDate(currentUser);
				double netPay = currentUser.workDays.recallSince(d, currentUser);
				System.out.println("Your estimated net pay since " + d.month + "/" + d.date + "/" + d.year + " is $" + round(netPay));
			}
			
			input = "";
		}
		
		scanner.close();
		
	}
	
	public static Day getDate(User u)
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Month: ");
		int month = scanner.nextInt();
		
		System.out.println("Day: ");
		int day = scanner.nextInt();
		
		System.out.println("Year: ");
		int year = scanner.nextInt();
		
		
		if(u == null)
			return new Day(day, month, year, 0);
		else
			return new Day(day, month, year, u.week);
	}
	public static User loadUser(String n)
	{
		String filename = "src/saveFiles/" + n;
		File f = new File(filename);
		if(f.isFile())
		{
			//System.out.println(f.getAbsolutePath());
			//This is where we load the user's file
			Scanner fileScanner;
			try {
				fileScanner = new Scanner(f);
				String name = fileScanner.nextLine();
				//System.out.println(name);
				double payrate = Double.parseDouble(fileScanner.nextLine());
				//System.out.println(payrate);
				int week = Integer.parseInt(fileScanner.nextLine());
				Day startOfWeek = new Day(fileScanner.nextLine(), week);
				double hoursSoFar = Double.parseDouble(fileScanner.nextLine());
				User u = new User(name, payrate, week, startOfWeek, hoursSoFar);
				
				if(fileScanner.nextLine().equals("dl::"))
				{
					u.workDays.load(fileScanner.nextLine());
				}
				
				if(fileScanner.nextLine().equals("pl::"))
				{
					u.checkList.load(fileScanner.nextLine(), u);
				}
				
				return u;
			}
			 catch (FileNotFoundException e) {
				System.out.println("file not found?");
				return null;
			}
			
			
		}
		else
		{
			return createUser(n);
		}
	}
	
	public static User createUser(String name)
	{
		Scanner s = new Scanner(System.in);
		System.out.println("What is your payrate?");
		double p = s.nextDouble();
		
		System.out.println("Please enter today's date:");
		Day currentDate = getDate(null);
		
		User u = new User(name, p, 0, currentDate, 0);
		
		if(u.save())
			System.out.println("User file created successfully");
		else
			System.out.println("There was an error creating the file.");
		
		return u;
	}
	
	public static boolean yesNo(String s)
	{
		if(s.equals("Y") || s.equals("y"))
			return true;
		else
			return false;
	}
	
	public static double getDayPay(double hoursSoFar, double hours, double payrate)
	{
		if(hoursSoFar < 40)
		{
			if(hoursSoFar + hours < 40)
			{
				return hours * payrate;
			}
			else
			{
				double reg = 40 - hoursSoFar;
				double ot = hours - reg;
				return (reg * payrate) + (ot * 1.5 * payrate);
			}
		}
		else
		{
			return round(hours * 1.5 * payrate);
		}
	}
	
	public static double round(double value)
	{
		return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

}
