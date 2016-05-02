
package com.dzartek.movielist.datamodel.pojo_moviedetails;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Country {

    @SerializedName("certification")
    @Expose
    private String certification;
    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("primary")
    @Expose
    private boolean primary;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    /**
     * 
     * @return
     *     The certification
     */
    public String getCertification() {
        return certification;
    }

    /**
     * 
     * @param certification
     *     The certification
     */
    public void setCertification(String certification) {
        this.certification = certification;
    }

    /**
     * 
     * @return
     *     The iso31661
     */
    public String getIso31661() {
        return iso31661;
    }

    /**
     * 
     * @param iso31661
     *     The iso_3166_1
     */
    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    /**
     * 
     * @return
     *     The primary
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * 
     * @param primary
     *     The primary
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * 
     * @return
     *     The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * 
     * @param releaseDate
     *     The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
