//This is the API Key: 88fd0b04-a4d2-47c1-9ff8-1e2dc5e0158b
package com.company;
import java.lang.reflect.Array;
import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		String[] arr = input.split(" ");

		String time = arr[2];
		String airCode = arr[1];
		String winds = arr[3];
		String vis = arr[4];
		int phenoLength=0;
		int oktaLength = 0;
		String tempDew;
		String altSetting;

		HashMap<String, String> oktas= new HashMap<>();
		oktas.put("SKC", "Clear Sky");
		oktas.put("FEW", "Few: 1-2 Oktas Cloud Coverage");
		oktas.put("SCT", "Scattered: 3-4 Oktas Cloud Coverage");
		oktas.put("BKN", "Broken: 5-7 Oktas Cloud Coverage");
		oktas.put("OVC", "Overcast: All 8 Oktas covered");

		//Sample METAR: METAR CYFB 141000Z 31003KT 15SM -SN DZ OVC026 BKN010 M11/M14 A2971 RMK SC8 SLP067=
		for (int i=5; i<arr.length-1; i++) {
			while (1==1) {
				if (arr[i].length()<3) {
					phenoLength++;
					i++;
				}
				else if (!oktas.containsKey(arr[i].substring(0, 3))) {
					phenoLength++;
					i++;
				}
				else {
					break;
				}
			}
			while (oktas.containsKey(arr[i].substring(0, 3))) {
				oktaLength++;
				i++;
			}
			break;
		}
		String[] phenomena = new String[phenoLength];
		String[] oktaArr = new String[oktaLength];

		for (int i=5; i<5+phenoLength; i++) {
			phenomena[i-5] = arr[i];
		}
		for (int i=5+phenoLength; i<5+phenoLength+oktaLength; i++){
			oktaArr[i-phenoLength-5] = arr[i];
		}
		tempDew = arr[5+phenoLength+oktaLength];
		altSetting = arr[6+phenoLength+oktaLength];

		String remarks="";
		for (int i=7+phenoLength+oktaLength; i<arr.length; i++) {
			remarks+=arr[i];
			remarks+=" ";
		}
		System.out.println(parseTime(time));
		System.out.println(parseWinds(winds));
		System.out.println(parseVis(vis));
		for (int i=0; i<phenoLength; i++) {
			System.out.println(parseWeather(phenomena[i]));
		}
		for (int i=0; i<oktaLength; i++) {
			System.out.println(parseOktas(oktaArr[i], oktas));
		}
		System.out.println(parseTempDew(tempDew));
		System.out.println(parseAlt(altSetting));
		parseRemarks(remarks);

	}
	public static String parseTime(String time) {
		String day = time.substring(0, 2);
		String zuluTime = time.substring(2, 6);
		String timeOutput = "Time of Reporting: "+"Day "+day+" of month, on "+zuluTime+" UTC (Greenwich Mean Time)";
		return timeOutput;
	}
	public static String parseCode(String code) {
		return code;
	}
	public static String parseWinds(String wind) {
		String output = "";
		if (wind.substring(0, 5).equals("00000")) {
			output+="Winds Calm";
		}
		else if (wind.length()==7){
			String speeds = wind.substring(3, 5);
			String heading = wind.substring(0, 3);
			output += "Winds coming from "+heading+" degrees with an intensity of "+ speeds+" Nautical Miles an hour (Knots)";
		}
		else {
			String speed1 = wind.substring(3,5);
			String speed2 = wind.substring(6, 8);
			String heading = wind.substring(0, 3);
			output+="Winds coming from "+heading+" degrees with an intensity of "+speed1+" Knots gusting to "+speed2+" Knots";
		}
		return output;
	}
	public static String parseVis(String vis) {
		String visibility = vis.substring(0, 2);
		String output = "Visibility of "+visibility+" Statute Miles";
		return output;
	}
	public static String parseWeather(String phen) {
		String finalOutput = "";
		HashMap<String, String> phenom = new HashMap<String, String>();
		phenom.put("DS", "Duststorm");
		phenom.put("SS", "Sandstorm");
		phenom.put("FG", "Fog");
		phenom.put("SH", "Shower");
		phenom.put("PO", "Dust Swirls");
		phenom.put("BLDU", "Blowing Dust");
		phenom.put("BLSA", "Blowing Sand");
		phenom.put("BLSN", "Blowing Snow");
		phenom.put("FC", "Tornado/Funnel Cloud");
		phenom.put("DZ", "Drizzle");
		phenom.put("RA", "Rain");
		phenom.put("SN", "Snow");
		phenom.put("SG", "Snow Grains");
		phenom.put("PL", "Ice Pellets");
		phenom.put("GR", "Hail");
		phenom.put("IC", "Ice Crystals");
		phenom.put("UP", "Unknown Precipitation");
		phenom.put("HZ", "Haze");
		phenom.put("FU", "Smoke");
		phenom.put("SA", "Sand");
		phenom.put("DU", "Dust");
		phenom.put("BR", "Mist");
		phenom.put("VA", "Volcanic Ash");
		String toParse = phen;
		String furtherParsing;
		String specificPhenom;
		if (toParse.charAt(0)=='-') {
			furtherParsing = toParse.substring(1);
			specificPhenom  = "Light "+phenom.get(furtherParsing);
		}
		else if (toParse.charAt(0)=='+') {
			furtherParsing = toParse.substring(1);
			specificPhenom = "Heavy "+phenom.get(furtherParsing);
		}
		else {
			furtherParsing = toParse;
			specificPhenom = phenom.get(furtherParsing);
		}
		finalOutput+=specificPhenom+" ";
		return finalOutput;
	}
	public static String parseOktas(String okta, HashMap oktas) {
		String descriptor = okta.substring(0, 3);
		String description = "";
		description+=oktas.get(descriptor);
		int height = Integer.parseInt(okta.substring(3,6));
		height*=100;
		return "The clouds are: "+description+" at a height of "+height+" feet AGL";
	}
	public static String parseTempDew(String tempDew) {
		int breakIndex=0;
		for (int i=0; i<tempDew.length(); i++) {
			if (tempDew.charAt(i)=='/') {
				breakIndex = i;
			}
		}
		String temp = tempDew.substring(0, breakIndex);
		String dew = tempDew.substring(breakIndex+1);
		if (temp.charAt(0)=='M') {
			temp = "-"+temp.substring(1, 3);
		}

		if (dew.charAt(0)=='M') {
			dew = "-"+dew.substring(1, 3);
		}
		return "The field temperature is "+temp+" degrees Celsius, and the dew point is "+dew+" degrees Celsius.";
	}
	public static String parseAlt(String alt) {
		alt = alt.substring(1);
		double altInt = Integer.parseInt(alt);
		altInt/=100;
		return "The altimeter setting is "+altInt+" inches of mercury";
	}
	public static void parseRemarks(String rmk) {
		HashMap<String, String> cloudTypes = new HashMap<>();
		cloudTypes.put("CI", "Cirrus");
		cloudTypes.put("CS", "Cirrostratus");
		cloudTypes.put("AC", "Altocumulus");
		cloudTypes.put("CU", "Cumulus");
		cloudTypes.put("NS", "Nimbostratus");
		cloudTypes.put("SC", "Stratocumulus");
		cloudTypes.put("CB", "Cumulonimbus");
		cloudTypes.put("TCU", "Towering Cumulus");
		cloudTypes.put("AS", "Altostratus");
		cloudTypes.put("SF", "Stratus Fractus");
		cloudTypes.put("ST", "Stratus");
		cloudTypes.put("CC", "Cirrocumulus");
		cloudTypes.put("CF", "Cumulus Fractus");

		String clouds = "";
		String rmkArr[] = rmk.split(" ");
		if (cloudTypes.containsKey(rmkArr[1].substring(0, 2))) {
			clouds = rmkArr[1];
		}
		String[] furtherParsed=clouds.split("(?<=\\G.{3})");
		for (int i=0; i<furtherParsed.length; i++) {
			System.out.println(cloudTypes.get(furtherParsed[i].substring(0, 2))+"-type Cloud covers "+furtherParsed[i].substring(2)+" oktas of the sky");
		}
		String seaPres = rmkArr[2].substring(3, 6);
			if (seaPres.charAt(0)=='0'||seaPres.charAt(0)=='1') {
				seaPres = "10" + seaPres;
				double newSeaPres = Integer.parseInt(seaPres);
				newSeaPres /= 10;
				System.out.println("Sea Level Pressure: " + newSeaPres + " Hectopascals/Millibars");
			}
			else {
				System.out.println(" Sea Level Pressure: "+seaPres+" Hectopascals/Millibars");
			}
	}
}




