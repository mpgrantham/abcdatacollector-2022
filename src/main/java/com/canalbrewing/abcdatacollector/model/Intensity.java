package com.canalbrewing.abcdatacollector.model;

import com.canalbrewing.abcdatacollector.resultsetmapper.annotation.DbColumn;

public class Intensity {

    @DbColumn(name = "id")
    private int id;

    @DbColumn(name = "intensity")
    private String name;

    public Intensity() {
    }

    public Intensity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}