// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson.internal.bind;

import com.google.gson.JsonNull;
import java.util.Iterator;
import java.util.Map;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonToken;
import java.util.ArrayList;
import com.google.gson.JsonElement;
import java.util.List;
import java.io.Reader;
import com.google.gson.stream.JsonReader;

public final class JsonTreeReader extends JsonReader
{
    private static final Object SENTINEL_CLOSED;
    private static final Reader UNREADABLE_READER;
    private final List<Object> stack;
    
    static {
        UNREADABLE_READER = new JsonTreeReader$1();
        SENTINEL_CLOSED = new Object();
    }
    
    public JsonTreeReader(final JsonElement jsonElement) {
        super(JsonTreeReader.UNREADABLE_READER);
        (this.stack = new ArrayList<Object>()).add(jsonElement);
    }
    
    private void expect(final JsonToken jsonToken) {
        if (this.peek() != jsonToken) {
            throw new IllegalStateException("Expected " + jsonToken + " but was " + this.peek());
        }
    }
    
    private Object peekStack() {
        return this.stack.get(this.stack.size() - 1);
    }
    
    private Object popStack() {
        return this.stack.remove(this.stack.size() - 1);
    }
    
    @Override
    public void beginArray() {
        this.expect(JsonToken.BEGIN_ARRAY);
        this.stack.add(((JsonArray)this.peekStack()).iterator());
    }
    
    @Override
    public void beginObject() {
        this.expect(JsonToken.BEGIN_OBJECT);
        this.stack.add(((JsonObject)this.peekStack()).entrySet().iterator());
    }
    
    @Override
    public void close() {
        this.stack.clear();
        this.stack.add(JsonTreeReader.SENTINEL_CLOSED);
    }
    
    @Override
    public void endArray() {
        this.expect(JsonToken.END_ARRAY);
        this.popStack();
        this.popStack();
    }
    
    @Override
    public void endObject() {
        this.expect(JsonToken.END_OBJECT);
        this.popStack();
        this.popStack();
    }
    
    @Override
    public boolean hasNext() {
        final JsonToken peek = this.peek();
        return peek != JsonToken.END_OBJECT && peek != JsonToken.END_ARRAY;
    }
    
    @Override
    public boolean nextBoolean() {
        this.expect(JsonToken.BOOLEAN);
        return ((JsonPrimitive)this.popStack()).getAsBoolean();
    }
    
    @Override
    public double nextDouble() {
        final JsonToken peek = this.peek();
        if (peek != JsonToken.NUMBER && peek != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + peek);
        }
        final double asDouble = ((JsonPrimitive)this.peekStack()).getAsDouble();
        if (!this.isLenient() && (Double.isNaN(asDouble) || Double.isInfinite(asDouble))) {
            throw new NumberFormatException("JSON forbids NaN and infinities: " + asDouble);
        }
        this.popStack();
        return asDouble;
    }
    
    @Override
    public int nextInt() {
        final JsonToken peek = this.peek();
        if (peek != JsonToken.NUMBER && peek != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + peek);
        }
        final int asInt = ((JsonPrimitive)this.peekStack()).getAsInt();
        this.popStack();
        return asInt;
    }
    
    @Override
    public long nextLong() {
        final JsonToken peek = this.peek();
        if (peek != JsonToken.NUMBER && peek != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + peek);
        }
        final long asLong = ((JsonPrimitive)this.peekStack()).getAsLong();
        this.popStack();
        return asLong;
    }
    
    @Override
    public String nextName() {
        this.expect(JsonToken.NAME);
        final Map.Entry<K, Object> entry = ((Iterator)this.peekStack()).next();
        this.stack.add(entry.getValue());
        return (String)entry.getKey();
    }
    
    @Override
    public void nextNull() {
        this.expect(JsonToken.NULL);
        this.popStack();
    }
    
    @Override
    public String nextString() {
        final JsonToken peek = this.peek();
        if (peek != JsonToken.STRING && peek != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + peek);
        }
        return ((JsonPrimitive)this.popStack()).getAsString();
    }
    
    @Override
    public JsonToken peek() {
        if (this.stack.isEmpty()) {
            return JsonToken.END_DOCUMENT;
        }
        final Object peekStack = this.peekStack();
        if (peekStack instanceof Iterator) {
            final boolean b = this.stack.get(this.stack.size() - 2) instanceof JsonObject;
            final Iterator<Object> iterator = (Iterator<Object>)peekStack;
            if (iterator.hasNext()) {
                if (b) {
                    return JsonToken.NAME;
                }
                this.stack.add(iterator.next());
                return this.peek();
            }
            else {
                if (b) {
                    return JsonToken.END_OBJECT;
                }
                return JsonToken.END_ARRAY;
            }
        }
        else {
            if (peekStack instanceof JsonObject) {
                return JsonToken.BEGIN_OBJECT;
            }
            if (peekStack instanceof JsonArray) {
                return JsonToken.BEGIN_ARRAY;
            }
            if (peekStack instanceof JsonPrimitive) {
                final JsonPrimitive jsonPrimitive = (JsonPrimitive)peekStack;
                if (jsonPrimitive.isString()) {
                    return JsonToken.STRING;
                }
                if (jsonPrimitive.isBoolean()) {
                    return JsonToken.BOOLEAN;
                }
                if (jsonPrimitive.isNumber()) {
                    return JsonToken.NUMBER;
                }
                throw new AssertionError();
            }
            else {
                if (peekStack instanceof JsonNull) {
                    return JsonToken.NULL;
                }
                if (peekStack == JsonTreeReader.SENTINEL_CLOSED) {
                    throw new IllegalStateException("JsonReader is closed");
                }
                throw new AssertionError();
            }
        }
    }
    
    public void promoteNameToValue() {
        this.expect(JsonToken.NAME);
        final Map.Entry<K, Object> entry = ((Iterator)this.peekStack()).next();
        this.stack.add(entry.getValue());
        this.stack.add(new JsonPrimitive((String)entry.getKey()));
    }
    
    @Override
    public void skipValue() {
        if (this.peek() == JsonToken.NAME) {
            this.nextName();
            return;
        }
        this.popStack();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
