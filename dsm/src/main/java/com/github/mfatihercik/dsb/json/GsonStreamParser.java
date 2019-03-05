package com.github.mfatihercik.dsb.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.expression.ExpressionResolver;
import com.github.mfatihercik.dsb.function.FunctionFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class GsonStreamParser extends JsonStreamParser {


    public GsonStreamParser(FunctionFactory functionFactory, ExpressionResolver expressionResolver, ObjectMapper objectMapper, Class<?> resultType) {
        super(functionFactory, expressionResolver, objectMapper, resultType);
    }

    @Override
    protected void parseInputStream(Reader reader) {


        try {
            JsonReader parser = new JsonReader(reader);
            processParser(parser);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void parseInputStream(InputStream inputStream) {


        try {
            JsonReader parser = new JsonReader(new InputStreamReader(inputStream));
            processParser(parser);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String[] pushName(String[] pathNames, int stackSize, String name) {
        if (stackSize == pathNames.length) {
            String[] newPathNames = new String[stackSize * 2];
            System.arraycopy(pathNames, 0, newPathNames, 0, stackSize);
            pathNames = newPathNames;
        }
        pathNames[stackSize] = name;
        return pathNames;
    }

    private void processParser(JsonReader parser) throws IOException {

        int stackSize = 0;
        String[] pathNames = new String[32];


        while (true) {

            JsonToken rawEvent = parser.peek();

            switch (rawEvent) {
                case BEGIN_ARRAY:
                    parser.beginArray();
                    String qName = pathNames[stackSize];
                    stackSize++;
                    if (qName != null) {
                        parentTagAdd(qName);
                        break;
                    }
                    startObject(qName);
                    break;
                case BEGIN_OBJECT:
                    parser.beginObject();
                    startObject(pathNames[stackSize]);

                    stackSize++;
                    break;

                case END_ARRAY:
                    parser.endArray();
                    pathNames[stackSize] = null;
                    stackSize--;
                    qName = pathNames[stackSize];

                    if (qName != null) {
                        parentTagRemove(qName);
                        break;
                    }
                    endObject(qName);
                    break;
                case END_OBJECT:

                    parser.endObject();
                    pathNames[stackSize] = null;
                    stackSize--;
                    qName = pathNames[stackSize];

                    endObject(qName);
                    break;
                case NAME:
                    pathNames = pushName(pathNames, stackSize, parser.nextName());
                    break;

                case STRING:
                case NUMBER:
                case BOOLEAN:
                    endValue(pathNames[stackSize], parser.nextString());
                    break;
                case NULL:
                    parser.nextNull();
                    endValue(pathNames[stackSize], null);
                    break;
                case END_DOCUMENT:
                    return;
                default:
                    break;
            }

        }
    }


}
