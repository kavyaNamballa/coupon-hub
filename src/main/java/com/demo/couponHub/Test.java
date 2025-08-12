package com.demo.couponHub;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import com.api.jsonata4java.Expression;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Experimental;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.JsonSurferJackson;
import org.jsfr.json.SurfingConfiguration;

import java.io.IOException;
import java.io.StringReader;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        String json = "[\n" +
                "    {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Store A\",\n" +
                "        \"departments\": [\n" +
                "            {\n" +
                "                \"id\": 10,\n" +
                "                \"name\": \"Electronics\",\n" +
                "                \"products\": [\n" +
                "                    {\"id\": 101, \"name\": \"TV\", \"in_stock\": true},\n" +
                "                    {\"id\": 102, \"name\": \"Laptop\", \"in_stock\": false}\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"Store B\",\n" +
                "        \"departments\": [\n" +
                "            {\n" +
                "                \"id\": 12,\n" +
                "                \"name\": \"Grocery\",\n" +
                "                \"products\": [\n" +
                "                    {\"id\": 301, \"name\": \"Milk\", \"in_stock\": false},\n" +
                "                    {\"id\": 302, \"name\": \"Eggs\", \"in_stock\": true}\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        Object jsonObject = JSON.parse(json);
        Object res = JSONPath.eval(jsonObject, "$[?(@..products[?(@.id==102)])].name");

//        JsonSurfer surfer = JsonSurferJackson.INSTANCE;
//        SurfingConfiguration config = surfer.configBuilder()
//                .bind("$[?(@.departments[?(@.products[?(@.id==102)])])].name", (value, context) -> {
//                    System.out.println("Matched store: " + value);
//                }).build();
//
//        surfer.surf(new StringReader(json), config);

        String jsonataExpr = "$filter($, function($store) { " +
                "$count($filter($store.departments[].products[], function($p) { $p.id = 102 })) > 0" +
                " }).name";
        //[$[departments[products[id=102]]]].name
        Expression expression = Expression.jsonata(jsonataExpr);
        JsonNode result =  expression.evaluate(new ObjectMapper().readTree(json));
        System.out.println(result.toString());

    }
}
