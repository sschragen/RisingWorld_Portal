package sra.risingworld.utils;

public class ID3 
{
	private char[] id = new char[3];
	
	public ID3 ()
	{
		this.id[0] = '0';
		this.id[1] = '0';
		this.id[2] = '0';
		//set("000");
		
	}
	
	public ID3 (String s)
	{
		s += "   ";
		this.id[0] = s.charAt(0);
		this.id[1] = s.charAt(1);
		this.id[2] = s.charAt(2);
	}
	
	public ID3 (int i)
	{
		String s = String.format("%03d", i);
		this.id[0] = s.charAt(0);
		this.id[1] = s.charAt(1);
		this.id[2] = s.charAt(2);
	}
	
	public void set (String s)
	{
		s = s + "   "; // safety
		this.id[0] = s.charAt(0);
		this.id[1] = s.charAt(1);
		this.id[2] = s.charAt(2);
	}
	
	public char[] getDigits()
	{
		return id;
	}
	
	public char getDigit(int digitNo)
	{
		return this.id[digitNo];
	}
	
	public void setDigit(int digitNo,char digit)
	{
		id[digitNo] = digit;
	}
	
	private String Ziffer(char c)
	{
		String s = "";
		if (c >= '0' & c <= '9')
			s += c;
		return s;
	};
	
	public String toString()
	{
		
		return Ziffer(id[0]) + Ziffer(id[1]) + Ziffer(id[2]);
	}
	
	public int toInt()
	{
		try
		{
			return Integer.parseInt(toString());
		}
		catch (NumberFormatException nfe)
		{
			return -1;
		}
	}
	
	public boolean isValid ()
	{
		try
		{
			Integer.parseInt(toString());
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}
	
	
}
