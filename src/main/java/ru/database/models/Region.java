package ru.database.models;

public class Region {
    private String region;
    private int count;

    public Region(String region, int count) {
        this.region = region;
        this.count = count;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Region{" +
                "region='" + region + '\'' +
                ", count=" + count +
                '}';
    }
}
