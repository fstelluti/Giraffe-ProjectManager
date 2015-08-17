package blackBox;

import static org.junit.Assert.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.EarnedValueAnalysisTab.EVATestingClass;

public class blackBoxGetCPI {

	
	
}

/*private double getCPI(double EV, double AC) {
	//Test if the EV and AC are negative
	if(EV < 0 || AC < 0) {
		throw new IllegalArgumentException("The earned value and/or actual cost cannot be negative");
	}
		
	// Handle possible divisions by zero
	if(AC == 0) {
		CPI = 0;
	}
	else {
		CPI = (double) EV / AC;
	}
	
	//Round the CPI to two decimal places
	CPI = CPI * 100;
	CPI = (double) ((int) CPI); // Truncate after two decimals
	CPI = CPI / 100;
	
	return CPI;
}*/