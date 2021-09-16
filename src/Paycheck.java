import java.util.Scanner;


public class Paycheck {

	User user;
	Day startDate;
	Day endDate;
	double gross, net, percentage, totalHours;
	
	public Paycheck(User u) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter the first date of the pay period for this check:" );
		startDate = Main.getDate(user);
		
		System.out.println("Enter the last date of the pay period for this check: ");
		endDate = Main.getDate(user);
		
		user = u;
		
		totalHours = user.workDays.getTotalHours(startDate, endDate);
		System.out.println("You worked " + totalHours + " hours during this pay period");
		
		gross = user.workDays.getGrossPay(startDate, endDate, user.payrate);
		
		System.out.println("Your gross pay should be $" + gross + " for this pay period");
		
		System.out.println("Estimate for net income: $" + user.checkList.averageNetPercentage * gross);
		
		System.out.println("Enter your actual net income: ");
		net = scanner.nextDouble();
		
		percentage = net/gross;
		
		System.out.println("Your net pay is " + percentage*100 + "% of your gross pay!");
	}
	
	public Paycheck(User u, Day s, Day e, double g, double n, double p, double t)
	{
		user = u;
		startDate = s;
		endDate = e;
		gross = g;
		net = n;
		percentage = p;
		totalHours = t;
	}

}
