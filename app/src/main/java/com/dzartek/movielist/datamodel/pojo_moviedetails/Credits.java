
package com.dzartek.movielist.datamodel.pojo_moviedetails;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Credits {

    @SerializedName("cast")
    @Expose
    private List<Cast> cast = new ArrayList<Cast>();
    @SerializedName("crew")
    @Expose
    private List<Crew> crew = new ArrayList<Crew>();

    /**
     * 
     * @return
     *     The cast
     */
    public List<Cast> getCast() {
        return cast;
    }

    /**
     * 
     * @param cast
     *     The cast
     */
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    /**
     * 
     * @return
     *     The crew
     */
    public List<Crew> getCrew() {
        return crew;
    }

    /**
     * 
     * @param crew
     *     The crew
     */
    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

}
