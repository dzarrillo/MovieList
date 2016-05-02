
package com.dzartek.movielist.datamodel.pojo_moviedetails;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Releases {

    @SerializedName("countries")
    @Expose
    private List<Country> countries = new ArrayList<Country>();

    /**
     * 
     * @return
     *     The countries
     */
    public List<Country> getCountries() {
        return countries;
    }

    /**
     * 
     * @param countries
     *     The countries
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

}
