package com.example.appclima.Dto;


/**
 * Created by ravi on 20/02/18.
 */

public class Historico {
    public static final String TABLE_NAME = "Historico";


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ICONO = "icono";
    public static final String COLUMN_TEMPERATURA = "temperatura";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_COORDENADAS = "coordenadas";


    private int id;
    private String icono;
    private String temperatura;
    private String summary;
    private String time;
    private String location;
    private String coordenadas;

    public Historico() {
    }

    public Historico(int id, String icono, String temperatura, String summary,
     String time, String location , String coordenadas) {
        this.id = id;
        this.icono = icono;
        this.temperatura = temperatura;
        this.summary = summary;
        this.time = time;
        this.location = location;
        this.coordenadas = coordenadas;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getIcono() {
        return icono;
    }
    public void setIcono(String icono){
        this.icono = icono;
    }

    public String getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoordenadas() {
        return coordenadas;
    }
    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

}
