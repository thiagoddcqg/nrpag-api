package com.nextreleaseproblem.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

public class HtmlUtils {

    public static JSONObject convertElementToJson(Element element, JSONObject jsonObject) {
        // Check if the element has children
        if (element.children().isEmpty()) {
            // No children, add the element's text as a JSON property
            jsonObject.put(element.tagName(), element.text());
        } else {
            // Element has children, add them as a JSON array
            JSONArray jsonArray = new JSONArray();
            for (Element child : element.children()) {
                JSONObject childJsonObject = new JSONObject();
                convertElementToJson(child, childJsonObject);
                jsonArray.put(childJsonObject);
            }
            jsonObject.put(element.tagName(), jsonArray);
        }
        return jsonObject;
    }
}
