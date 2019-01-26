package task1;



public class Deal {

	private int sum;
	
	private int numberOffice;
	
	private int numDeal;
	
	private String date;
	

	
	
	public Deal(String date, int numberOffice, int numDeal, int sum) {
		this.sum = sum;
		this.numberOffice = numberOffice;
		this.date = date;
		this.numDeal=numDeal;
	}

	
	public static Deal getDeal(String s){
		String[] values=s.split("  ");
		return new Deal(values[0], Integer.parseInt(values[2]), Integer.parseInt(values[3]),Integer.parseInt(values[4]));
	}

	@Override
	public String toString() {
		return date+"  "+numberOffice+"  "+numDeal+"  "+sum;
	}


	
	public String getDate(){
		return date;
	}
	
	public int getNumberOffice(){
		return numberOffice;
	}
	
	public int getSum(){
		return sum;
	}
}
