package sra.risingworld.utils;

import java.util.HashSet; 
import java.util.Random;

public class UID3 {
	
	private HashSet<Integer> UID3_Table = new HashSet<>();
	private Random rand = new Random();
	
	public boolean isFull ()
	{
		return (UID3_Table.size() == 1000);
	}
	
	public boolean isUsed (int number)
	{		
		return UID3_Table.contains(number);
	}
	public boolean isUsed (ID3 number)
	{
		return isUsed (number.toInt());
	}
	
	public boolean add (int number)
	{
		return UID3_Table.add(number);
	}
	
	public boolean remove (int number)
	{
		return UID3_Table.remove(number);
	}
	
	public int Random () {
		int randomNumber = rand.nextInt(1000);
		if ( UID3_Table.contains(randomNumber) )
		{
			randomNumber = nextInteger(randomNumber);
		}
		add(randomNumber);
		return (randomNumber);
	}
	
	private Integer nextInteger(Integer randomNumber)
	{
		Integer newNumber = randomNumber+1;
		while (UID3_Table.contains(newNumber))
		{
			newNumber++;
			if (newNumber > 999)
				newNumber = 0;
		}		
		return newNumber;
	}
	
	static public ID3 getID3 (Integer number)
	{
		return new ID3(number);
	}

}
