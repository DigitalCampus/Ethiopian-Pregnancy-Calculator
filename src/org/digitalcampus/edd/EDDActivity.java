package org.digitalcampus.edd;

import java.util.Arrays;
import java.util.List;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EDDActivity extends Activity {
    
	
	 	private EditText txtMonth;
	    private EditText txtDay;
	    private EditText txtYear;
	    private Chronology chron_eth = EthiopicChronology.getInstance();
	    
	    private static List<String> ETHIOPIC_MONTHS_TIGRIGNA = Arrays.asList("Meskerem", 
	    														"Tikimti", 
	    														"Hidar", 
	    														"Tahsas", 
	    														"Tiri", 
	    														"Yekatit", 
	    														"Megabit", 
	    														"Miazia", 
	    														"Gunbet", 
	    														"Sene", 
	    														"Hamle",
	    														"Nehase",
	    														"Pagumein");
	
	    private static List<String> ETHIOPIC_MONTHS_AMHARIC = Arrays.asList("Meskerem", 
																"Tekemt", 
																"Hedar", 
																"Tahsas", 
																"Ter", 
																"Yekatit", 
																"Megabit", 
																"Miazia", 
																"Genbot", 
																"Senei", 
																"Hamlei",
																"Nehassei",
																"Pagumein");
	/** Called when the activity is first created. */
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setUpListeners();
        setDate();
        updateDates();
    }
    
    public void setUpListeners(){
    	txtDay = (EditText) findViewById(R.id.daytext);
        txtMonth = (EditText) findViewById(R.id.monthtext);
        txtYear = (EditText) findViewById(R.id.yeartext);
        
        Button btnDayUp = (Button) findViewById(R.id.dayupbutton);
        
        btnDayUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(txtDay.getText().toString().equals("") || txtDay.getText().toString() == null){
					// if the year field is currently blank set to current day
					DateTime dt = new DateTime();
			    	DateTime dtEthio = dt.withChronology(chron_eth);
					txtDay.setText(String.format("%02d",dtEthio.getDayOfMonth()));
				} else {
					// before validating date, check that the fields contain valid values, otherwise just increment the day
					try {
						Integer.parseInt(txtDay.getText().toString());
						Integer.parseInt(txtYear.getText().toString());
					} catch (Exception e) {
						int newDay = Math.min(30, Integer.parseInt(txtDay.getText().toString())+1);
						txtDay.setText(String.format("%02d",newDay));
						return;
					} 
					int newDay = Math.min(30, Integer.parseInt(txtDay.getText().toString())+1);
					if(isValidDate(newDay, 
									ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString())+1, 
									Integer.parseInt(txtYear.getText().toString()))){
						txtDay.setText(String.format("%02d",newDay));
						
					}
		    		
				}
				updateDates();
			}
		});
        
        Button btnMonthUp = (Button) findViewById(R.id.monthupbutton);
        
        btnMonthUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int curr_month = ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString());
				switch (curr_month){
					case 12:
						txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(0));
						break;
					case -1:
						DateTime dt = new DateTime();
				    	DateTime dtEthio = dt.withChronology(chron_eth);
						txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(dtEthio.getMonthOfYear()-1));
						break;
					default:
						// before validating date, check that the fields contain valid values, otherwise just increment the month
						try {
							Integer.parseInt(txtDay.getText().toString());
							Integer.parseInt(txtYear.getText().toString());
						} catch (Exception e) {
							txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(curr_month+1));
							return;
						} 
						int newMonth = curr_month+2;
						if(isValidDate(Integer.parseInt(txtDay.getText().toString()), 
										newMonth, 
										Integer.parseInt(txtYear.getText().toString()))){
							txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(curr_month+1));
						} else if(isValidDate(6, 
										newMonth, 
										Integer.parseInt(txtYear.getText().toString()))){
							txtDay.setText(String.format("%02d",6));
							txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(curr_month+1));
						} else if(isValidDate(5, 
											newMonth, 
											Integer.parseInt(txtYear.getText().toString()))){
							txtDay.setText(String.format("%02d",5));
							txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(curr_month+1));
						} 
						
						break;
				}
				updateDates();
			}
		});
        
        Button btnYearUp = (Button) findViewById(R.id.yearupbutton);
        
        btnYearUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(txtYear.getText().toString().equals("") || txtYear.getText().toString() == null){
					// if field is currently blank, set to current year
					DateTime dt = new DateTime();
			    	DateTime dtEthio = dt.withChronology(chron_eth);
					txtYear.setText(String.format("%04d",dtEthio.getYear()));
				} else {
					// before validating date, check that the fields contain valid values, otherwise just increment the year
					try {
						Integer.parseInt(txtDay.getText().toString());
						Integer.parseInt(txtYear.getText().toString());
					} catch (Exception e) {
						txtYear.setText(String.format("%04d",Integer.parseInt(txtYear.getText().toString())+1));
						return;
					}
					int newYear = Integer.parseInt(txtYear.getText().toString())+1;
					// alter the other fields so it will always result in a valid date
					if(isValidDate(Integer.parseInt(txtDay.getText().toString()), 
							ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString())+1, 
									newYear)){
						txtYear.setText(String.format("%04d",newYear));
					} else if(isValidDate(5, 
							ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString())+1, 
										Integer.parseInt(txtYear.getText().toString()))){
						txtDay.setText("5");
						txtYear.setText(String.format("%04d",newYear));
					} 
				}
				updateDates();
			}
		});

        
        Button btnDayDown = (Button) findViewById(R.id.daydownbutton);
        
        btnDayDown.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(txtDay.getText().toString().equals("") || txtDay.getText().toString() == null){
					// if the year field is currently blank set to current day
					DateTime dt = new DateTime();
			    	DateTime dtEthio = dt.withChronology(chron_eth);
					txtDay.setText(String.format("%02d",dtEthio.getDayOfMonth()));
				} else {
					// before validating date, check that the fields contain valid values, otherwise just decrement the day
					try {
						Integer.parseInt(txtDay.getText().toString());
						Integer.parseInt(txtYear.getText().toString());
					} catch (Exception e) {
						int newDay = Math.min(30, Integer.parseInt(txtDay.getText().toString())-1);
						txtDay.setText(String.format("%02d",newDay));
						return;
					} 
					int newDay = Math.max(1, Integer.parseInt(txtDay.getText().toString())-1);
					if(isValidDate(newDay, 
							ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString())+1, 
									Integer.parseInt(txtYear.getText().toString()))){
						txtDay.setText(String.format("%02d",newDay));
					}
				}
				updateDates();
			}
		});
        
        Button btnMonthDown = (Button) findViewById(R.id.monthdownbutton);

        btnMonthDown.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int curr_month = ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString());
				switch (curr_month){
					case 0:
						// before validating date, check that the fields contain valid values, otherwise just increment the month
						try {
							Integer.parseInt(txtDay.getText().toString());
							Integer.parseInt(txtYear.getText().toString());
						} catch (Exception e) {
							txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(12));
							return;
						}
						int newMonth = 13;
						if(isValidDate(Integer.parseInt(txtDay.getText().toString()), 
										newMonth, 
										Integer.parseInt(txtYear.getText().toString()))){
							txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(12));
						} else if(isValidDate(6, 
										newMonth, 
										Integer.parseInt(txtYear.getText().toString()))){
							txtDay.setText(String.format("%02d",6));
							txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(12));
						} else if(isValidDate(5, 
											newMonth, 
											Integer.parseInt(txtYear.getText().toString()))){
							txtDay.setText(String.format("%02d",5));
							txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(12));
						} 
						break;
					case -1:
						DateTime dt = new DateTime();
				    	DateTime dtEthio = dt.withChronology(chron_eth);
						txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(dtEthio.getMonthOfYear()-1));
						break;
					default:
						txtMonth.setText(ETHIOPIC_MONTHS_TIGRIGNA.get(curr_month-1));
						break;
				}
				updateDates();
			}
		});

        
        Button btnYearDown = (Button) findViewById(R.id.yeardownbutton);
        
        btnYearDown.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(txtYear.getText().toString().equals("") || txtYear.getText().toString() == null){
					// if the year field is currently blank set to current year
					DateTime dt = new DateTime();
			    	DateTime dtEthio = dt.withChronology(chron_eth);
					txtYear.setText(String.format("%04d",dtEthio.getYear()));
				} else {
					// before validating date, check that the fields contain valid values, otherwise just decrement the year
					try {
						Integer.parseInt(txtDay.getText().toString());
						Integer.parseInt(txtYear.getText().toString());
					} catch (Exception e) {
						txtYear.setText(String.format("%04d",Integer.parseInt(txtYear.getText().toString())-1));
						return;
					} 
					int newYear = Math.max(1, Integer.parseInt(txtYear.getText().toString())-1);
					if(isValidDate(Integer.parseInt(txtDay.getText().toString()), 
							ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString())+1, 
										newYear)){
						txtYear.setText(String.format("%04d",newYear));
					} else if(isValidDate(5, 
							ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString())+1, 
										Integer.parseInt(txtYear.getText().toString()))){
						txtDay.setText("5");
						txtYear.setText(String.format("%04d",newYear));
					} 
				}
				updateDates();
			}
		});
    }
    
    public void updateDates(){
    	
    	// set the Gregorian version of LMD
    	
    	int ethioDay = 0;
    	int ethioMonth = 0;
    	int ethioYear = 0;
    	try {
			ethioDay = Integer.parseInt(txtDay.getText().toString());
			ethioMonth = ETHIOPIC_MONTHS_TIGRIGNA.indexOf(txtMonth.getText().toString())+1;
			ethioYear = Integer.parseInt(txtYear.getText().toString());
    	} catch (Exception e){
    		return;
    	}
    	if(isValidDate(ethioDay,ethioMonth, ethioYear)){
	    	TextView txtEDDEthio = (TextView) findViewById(R.id.eddethio);
	    	TextView txtEDDGreg = (TextView) findViewById(R.id.eddgreg);
	    	TextView txtLMDGreg  = (TextView) findViewById(R.id.lmdgreg);
	    	
	    	DateTime dt_eth = new DateTime(ethioYear, ethioMonth, ethioDay, 0, 0, 0, 0, chron_eth);
	    	
	    	// set the LMD (Greg)
	    	DateTime dtLMDGreg = dt_eth.withChronology(GregorianChronology.getInstance());
	    	DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM yyyy");
	    	String str = fmt.print(dtLMDGreg);
	    	txtLMDGreg.setText("("+str+")");
	    	
	    	// set the EDD (Ethio)
	    	DateTime edd = dt_eth.plusDays(280);
	    	txtEDDEthio.setText(String.format("%02d %s %04d",edd.getDayOfMonth(),ETHIOPIC_MONTHS_TIGRIGNA.get(edd.getMonthOfYear()-1),edd.getYear()));
	    	
	    	// set the EDD (Greg)
	    	DateTime dtGreg = edd.withChronology(GregorianChronology.getInstance());
	    	fmt = DateTimeFormat.forPattern("d MMMM yyyy");
	    	str = fmt.print(dtGreg);
	    	txtEDDGreg.setText("("+str+")");
    	}
    }
    
    public boolean isValidDate(int ethioDay, int ethioMonth, int ethioYear){
    	try {
    		DateTime dt_eth = new DateTime(ethioYear, ethioMonth, ethioDay, 0, 0, 0, 0, chron_eth);
    		return true;
    	} catch (Exception e){
    		return false;
    	}	
    }
    
    public void setDate(){
    	
    	DateTime dt = new DateTime();
    	DateTime dtEthio = dt.withChronology(chron_eth);
    	
    	String dtEthioDay = Integer.toString(dtEthio.getDayOfMonth());
    	String dtEthioMonth = ETHIOPIC_MONTHS_TIGRIGNA.get(dtEthio.getMonthOfYear()-1);
    	String dtEthioYear = Integer.toString(dtEthio.getYear());
    	
    	txtDay.setText(dtEthioDay);
    	txtMonth.setText(dtEthioMonth);
    	txtYear.setText(dtEthioYear);
    }
}