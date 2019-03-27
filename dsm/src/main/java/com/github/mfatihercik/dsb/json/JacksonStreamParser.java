package com.github.mfatihercik.dsb.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mfatihercik.dsb.expression.ExpressionResolver;
import com.github.mfatihercik.dsb.function.FunctionFactory;
import com.github.mfatihercik.dsb.typeconverter.TypeConverterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class JacksonStreamParser extends JsonStreamParser {

    public JacksonStreamParser(FunctionFactory functionFactory, ExpressionResolver expressionResolver, ObjectMapper objectMapper, Class<?> resultType, TypeConverterFactory typeConverterFactory) {
        super(functionFactory, expressionResolver, objectMapper, resultType, typeConverterFactory);
    }

    @Override
    protected void parseInputStream(Reader reader) {

        JsonFactory factory = new JsonFactory();

        try {
            JsonParser parser = factory.createParser(reader);
            processParser(parser);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void parseInputStream(InputStream inputStream) {
        JsonFactory factory = new JsonFactory();


        JsonParser parser;
        try {
            parser = factory.createParser(inputStream);
            processParser(parser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void processParser(JsonParser parser) throws IOException {
        while (parser.nextToken() != null) {

            JsonToken rawEvent = parser.currentToken();

            switch (rawEvent) {
                case START_ARRAY:
                    String qName = parser.currentName();
                    if (qName != null) {
                        parentTagAdd(qName);
                        break;
                    }
                    startObject(null);
                    break;
                case START_OBJECT:
                    startObject(parser.currentName());
                    break;

                case END_ARRAY:
                    qName = parser.currentName();
                    if (qName != null) {
                        parentTagRemove(qName);
                        break;
                    }
                    endObject(null);
                    break;
                case END_OBJECT:
                    endObject(parser.currentName());
                    break;

                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_NUMBER_FLOAT:
                case VALUE_NUMBER_INT:
                case VALUE_STRING:
                case VALUE_TRUE:
                    endValue(parser.getCurrentName(), parser.getValueAsString());
                    break;
                default:
                    break;
            }

        }
    }

}
