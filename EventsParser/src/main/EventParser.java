package main;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventParser {
	
	public Event getEvent(String id, String link, String city, String tag, String age, ArrayList<String> users) {
		Event event = new Event();
		String content = getContentFromPage(link);
		
		String dirtyValue = "";
		
		//get id
		try {
			event.setID(id);
		} catch (Throwable e) {}
		
		//get name
		try {
			dirtyValue = find("<meta content=\".*?\" name=\"relap-title\" />", content);
			String name = find("\".*?\"", dirtyValue);
			name = name.substring(1, name.length() - 1);
			name = name.replace("&quot;", "\"");
			name = name.replace("&nbsp;", " ");
			event.setNAME(name);
		} catch (Throwable e) {}
		
		//get date
		try {
			dirtyValue = find("<div class=\"platform-header-date platform-header-date-column\">.*?</div>", content);
			String date = find(">.*?<", dirtyValue);
			date = date.substring(1, date.length() - 1);
			date = date.replace("&quot;", "\"");
			date = date.replace("&nbsp;", " ");
			event.setDATE(date);
		} catch (Throwable e) {}
		
		//get city
		try {
			event.setCITY(city);
		} catch (Throwable e) {}
		
		//get tag
		try {
			event.setTAG(tag);
		} catch (Throwable e) {}
		
		//get cost
		try {
			dirtyValue = find("<div class=\"price\" itemprop=\"offers\" itemscope=\"true\" itemtype=\"http://schema.org/Offer\">.*?<span class='rouble-icon'>", content);
			String cost = find(">.*?<", dirtyValue);
			cost = cost.substring(1, cost.length() - 1);
			cost = cost.replace("&quot;", "\"");
			cost = cost.replace("&nbsp;", " ");
			cost = cost.replace(" ", "");
			event.setCOST(cost);
		} catch (Throwable e) {}
		
		//get age ?
		try {
			event.setAGE(age);
		} catch (Throwable e) {}
		
		//get owner
		try {
			dirtyValue = find("<div class=\"platform-info-card-name\">.*?</div>", content);
			String owner = find(">.*?<", dirtyValue);
			owner = owner.substring(1, owner.length() - 1);
			owner = owner.replace("&quot;", "\"");
			owner = owner.replace("&nbsp;", " ");
			event.setOWNER(owner);
		} catch (Throwable e) {}
		
		//get link
		try {
			event.setLINK(link);
		} catch (Throwable e) {}
		
		//get users
		try {
			event.setUSERS(users);
		} catch (Throwable e) {}
		
		return event;
	}
	
	private String getContentFromPage(String link) {
		String content = null;
		URLConnection connection = null;
		try {
		  connection =  new URL(link).openConnection();
		  Scanner scanner = new Scanner(connection.getInputStream());
		  scanner.useDelimiter("\\Z");
		  content = scanner.next();
		}catch ( Exception ex ) {
		    ex.printStackTrace();
		}
		return content;
	}

	private String find(String pattern, String content) {
		Pattern pt = Pattern.compile(pattern);
	    Matcher mt = pt.matcher(content);
	    if (mt.find()) {
	        return mt.group(0);
	    } else {
	        return "";
	    }
	}

}
