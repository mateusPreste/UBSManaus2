package example.android.ubsmanaus.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Countries implements Serializable {
    @SerializedName("name")
    public String name;

    @SerializedName("subregion")
    public String region;

    @SerializedName("population")
    public String population;

    public String flag;

    public long id;

    public Countries(String name, String subregion, String population, String flag) {
        this.name = name;
        this.region = subregion;
        this.population = population;
        this.flag = flag+".png";
    }

    @Override
    public String toString() {
        return "Country{" +
                " name='" + this.name+ '\'' +
                " region='" + this.region + '\'' +
                '}';
    }

    public long getId() {return this.id;}

    public String getName() {
        return this.name;
    }

    public String getRegion() {
        return this.region;
    }

    public String getPopulation() {
        return this.population;
    }

    public String getFlag() {
        return this.flag;
    }

}
