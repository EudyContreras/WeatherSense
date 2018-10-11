package com.eudycontreras.weathersense.utilities.connection;

/**
 * Created by Eudy on 1/28/2017.
 */

public class ConnectionResponse {

    private ConnectionParameters parameters;
    private String result;

    public ConnectionResponse(ConnectionParameters parameters, String result){
        this.parameters = parameters;
        this.result = result;
    }

    public ConnectionParameters getParameters() {
        return parameters;
    }

    public String getResult() {
        return result;
    }

}
