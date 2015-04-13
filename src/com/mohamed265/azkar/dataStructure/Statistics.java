package com.mohamed265.azkar.dataStructure;

public class Statistics {

	public String date;
	public int numberOFZekr;

	public String toString() {
		return 
				 numberOFZekr + " " + date + (numberOFZekr < 11 && numberOFZekr != 0 ? " ãÑÇÊ" : " ãÑÉ");
	}
}
