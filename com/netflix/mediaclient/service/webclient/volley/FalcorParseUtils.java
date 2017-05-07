// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.webclient.volley;

import com.netflix.mediaclient.Log;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

public class FalcorParseUtils
{
    private static final String FIELD_PATH = "path";
    private static final String FIELD_VALUE = "value";
    private static final String METHOD_TYPE_CALL = "call";
    private static final String METHOD_TYPE_GET = "get";
    private static final String PARAM_NAME_CALLPATH = "callPath";
    private static final String PARAM_NAME_PARAM = "param";
    private static final String PARAM_NAME_PATH = "path";
    private static final Gson gson;
    
    static {
        gson = new Gson();
    }
    
    public static String buildEmptyValueMessage() {
        return new String("{\"value\":{},\"paths\":[[\"topGenres\",\"null\"]]}");
    }
    
    public static String buildErrorMessage() {
        return new String("{\"value\":{},\"error\":{\"message\":\"For input string: null\",\"pql\":[\"topGenre\",[\"null\"]]}}");
    }
    
    public static String buildInnerErrorMessage() {
        return new String("{ \"value\": {},  \"error\": { \"innerErrors\": [{\"message\": \"Not authorized.\",\"pql\": [\"topGenre\", [\"7462\"]],\"unauthorized\": true }, {\"message\": \"Not authorized.\",\"pql\": [\"topGenre\", [\"7462\"]], \"unauthorized\": true }]}}");
    }
    
    public static String buildMapErrorMessage() {
        return new String("");
    }
    
    public static String buildNotAuthorizedMessage() {
        return new String("{\"value\":{},\"error\":{\"message\":\"Not authorized.\",\"pql\":[\"topGenre\",[\"null\"]],\"unauthorized\":true}}");
    }
    
    public static String buildNullPointerMessage() {
        return new String("{\"value\":{},\"error\":{\"innerErrors\":[{\"error\":\"java.lang.NullPointerException\"},{\"error\":\"java.lang.NullPointerException\"}]}}");
    }
    
    public static boolean containsErrors(final JsonObject jsonObject) {
        return jsonObject.has("error") || jsonObject.has("innerErrors");
    }
    
    public static JsonObject getDataObj(final String s, final String s2) throws FalcorParseException, FalcorServerException {
        JsonObject asJsonObject;
        try {
            asJsonObject = new JsonParser().parse(s2).getAsJsonObject();
            if (containsErrors(asJsonObject)) {
                throw new FalcorServerException(getErrorMessage(asJsonObject));
            }
        }
        catch (Exception ex) {
            Log.v(s, "String response to parse = " + s2);
            throw new FalcorParseException("Error in creating JsonObject", ex);
        }
        return asJsonObject.getAsJsonObject("value");
    }
    
    public static String getErrorMessage(JsonObject asJsonObject) {
        if (containsErrors(asJsonObject)) {
            asJsonObject = asJsonObject.getAsJsonObject("error");
            if (asJsonObject.has("message")) {
                return asJsonObject.get("message").toString();
            }
            if (asJsonObject.has("innerErrors")) {
                return asJsonObject.get("innerErrors").toString();
            }
        }
        return null;
    }
    
    public static Gson getGson() {
        return FalcorParseUtils.gson;
    }
    
    public static String getIdFromPath(final String s, final JsonObject jsonObject) throws FalcorParseException, FalcorServerException {
        try {
            return jsonObject.getAsJsonArray("path").get(1).getAsString();
        }
        catch (Exception ex) {
            Log.v(s, "String jsonObj to parse = " + jsonObject.toString());
            throw new FalcorParseException("Missing path/id in jsonObj", ex);
        }
    }
    
    public static String getMethodNameCall() {
        return "call";
    }
    
    public static String getMethodNameGet() {
        return "get";
    }
    
    public static String getParamNameCallPath() {
        return "callPath";
    }
    
    public static String getParamNameParam() {
        return "param";
    }
    
    public static String getParamNamePath() {
        return "path";
    }
    
    public static <T> T getPropertyObject(final JsonObject jsonObject, final String s, final Class<T> clazz) {
        if (jsonObject.has(s)) {
            return FalcorParseUtils.gson.fromJson(jsonObject.get(s), clazz);
        }
        return null;
    }
    
    public static boolean isEmpty(final JsonObject jsonObject) {
        return jsonObject.isJsonNull() || jsonObject.toString().equals("{}");
    }
    
    public static boolean isMapCacheError(final String s) {
        return s.contains("cache miss") || s.contains("mapgetcachedlistclient failed") || s.contains("cachemiss");
    }
    
    public static boolean isMapError(final String s) {
        return s.contains("map error");
    }
    
    public static boolean isNotAuthorized(final String s) {
        return s.contains("Not authorized") || s.contains("unauthorized");
    }
    
    public static boolean isNullPointerException(final String s) {
        return s.contains("NullPointerException");
    }
    
    public static boolean isWrongState(final String s) {
        return s.contains("wrong state");
    }
}