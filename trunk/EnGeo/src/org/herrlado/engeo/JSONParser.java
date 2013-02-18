package org.herrlado.engeo;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONParser {
	static JSONArray jArray;

	private static final String TAG_WORD = "Word";
	private static final String TAG_TEXT = "Text";

	public static String GetStringFromJson(JSONArray array){
		jArray = array;
		String fullText = "";

		for(int i = 0; i < jArray.length(); i++){
			JSONObject c;
			try {
				c = jArray.getJSONObject(i);
				String word = c.getString(TAG_WORD);
				String text = c.getString(TAG_TEXT);

				fullText += word + " - " + text.replace("\n", " ") + "\n\n";
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return fullText;
	}
}
