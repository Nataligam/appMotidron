package com.example.app.motidron.Model.DTO;

import java.sql.Blob;

public class ReconocimientoDTO {

    private int id;
    private String latitud,longitud,api_cloud,msg;
    byte[] src;

    public ReconocimientoDTO(byte[] src, String latitud, String longitud, String msg, String api) {
        this.src = src;
        this.latitud = latitud;
        this.longitud = longitud;
        this.msg = msg;
        this.api_cloud = api;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getSrc() {
        return src;
    }

    public void setSrc(byte[] src) {
        this.src = src;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getApi_cloud() {
        return api_cloud;
    }

    public void setApi_cloud(String api_cloud) {
        this.api_cloud = api_cloud;
    }


}
