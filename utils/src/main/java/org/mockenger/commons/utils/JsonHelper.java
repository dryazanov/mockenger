package org.mockenger.commons.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompare;

import static org.skyscreamer.jsonassert.JSONCompareMode.LENIENT;

/**
 * @author Dmitry Ryazanov
 */
public class JsonHelper {

    public static boolean hasDifferences(final String json1, final String json2) throws JSONException {
		return JSONCompare.compareJSON(new JSONObject(json1), new JSONObject(json2), LENIENT).failed();
	}
}
