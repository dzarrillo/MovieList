
package com.dzartek.movielist.datamodel.pojo_moviedetails;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Reviews {

    @SerializedName("page")
    @Expose
    private long page;
    @SerializedName("results")
    @Expose
    private List<Result_> results = new ArrayList<Result_>();
    @SerializedName("total_pages")
    @Expose
    private long totalPages;
    @SerializedName("total_results")
    @Expose
    private long totalResults;

    /**
     * 
     * @return
     *     The page
     */
    public long getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The page
     */
    public void setPage(long page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<Result_> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<Result_> results) {
        this.results = results;
    }

    /**
     * 
     * @return
     *     The totalPages
     */
    public long getTotalPages() {
        return totalPages;
    }

    /**
     * 
     * @param totalPages
     *     The total_pages
     */
    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 
     * @return
     *     The totalResults
     */
    public long getTotalResults() {
        return totalResults;
    }

    /**
     * 
     * @param totalResults
     *     The total_results
     */
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

}
