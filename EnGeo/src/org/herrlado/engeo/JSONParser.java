package org.herrlado.engeo;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONParser {
	static JSONArray jArray;
	static HashMap<String, String> map;

	private static final String TAG_WORD = "Word";
	private static final String TAG_TEXT = "Text";

	public static String GetStringFromJson(String url){
		jArray = GetTranslateGE.getFromTranslateGe(url);
		map = new HashMap<String, String>();		

		for(int i = 0; i < jArray.length(); i++){
			JSONObject c;
			try {
				c = jArray.getJSONObject(i);
				String word = c.getString(TAG_WORD);
				String text = c.getString(TAG_TEXT);
				
				map.put(word, text);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		String text = "";
		
		for(Map.Entry<String, String> entry : map.entrySet()){
			String word = entry.getKey();
			String value = entry.getValue();
			
			text += word + " - " + value + "\n";
		}
		return text;
	}
}
