package com.eudycontreras.weathersense.utilities.miscellaneous;

/**
 * Created by Eudy Contreras on 9/10/2016.
 */
public class QueryUtility {

    public static String select(String table, String field, String value){
        String query = "SELECT * FROM " + table + " WHERE " + field + " = " + "'"+value+"'";
        return query;
    }
    public static String select(String table, String[] fields, String[] values){
        String query = "SELECT * FROM " + table + " WHERE ";
        for(int i = 0; i<fields.length; i++){
            query+= fields[i] + " = '" +values[i]+"'";
            if(i < fields.length-1){
                query+= " AND ";
            }
        }
        return query;
    }
    public static String select(String table, String field, int value){
        String query = "SELECT * FROM " + table + " WHERE " + field + " = " +value;
        return query;
    }
    public static String select(String table, String field, char value){
        String query = "SELECT * FROM " + table + " WHERE " + field + " = " + "'"+value+"'";
        return query;
    }
    public static String[] fields(String... fields){
        return fields;
    }
    public static String[] values(String... values){
        return values;
    }
}
