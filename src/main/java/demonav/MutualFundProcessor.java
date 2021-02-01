package demonav;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MutualFundProcessor {

	public MutualFundProcessor() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		JSONObject jsonObject = new JSONObject();
		Scanner sc = new Scanner(System.in);
		System.out.println("Period of Investment: ");
		int periodOfInvestment = sc.nextInt();
		System.out.print("Horizon: ");
		sc.nextInt();
		sc.close();
		LocalDate date = LocalDate.now().minusDays(1l);
		String previousDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		System.out.println(previousDate);

		try {
			URL url = new URL("https://api.mfapi.in/mf/102885");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			int response = connection.getResponseCode();

			if (response == 200) {
				String values = "";
				Scanner scanner = new Scanner(url.openStream());
				while (scanner.hasNext()) {
					values += scanner.nextLine();
				}
				JSONParser jsonParser = new JSONParser();
				JSONArray jsonArray = new JSONArray();
				jsonObject = (JSONObject) jsonParser.parse(values);
				jsonArray = (JSONArray) jsonObject.get("data");
				HashMap<String, String> dateAndNav = new HashMap<String, String>();
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					System.out.println(obj.get("date").toString());
					dateAndNav.put(obj.get("date").toString(), obj.get("nav").toString());
				}
				System.out.println(dateAndNav.size());
				LocalDate startDate = date.minusYears(periodOfInvestment);
				while(!dateAndNav.containsKey(startDate.toString())) {
					startDate = startDate.minusDays(1l);
				}
				
				LocalDate endDate = date;
				double startNav = Double.parseDouble(dateAndNav.get(startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
				double endNav = Double.parseDouble(dateAndNav.get(endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).toString());
				double returns = Math.pow((endNav / startNav), 1 / periodOfInvestment);
				System.out.println(returns);

				scanner.close();
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
