package com.semwal.amit.bioscope.models;

import java.util.List;

/**
 * @author
 */
public class ApiResult<T> {
    private int page;
    private List<T> results;
    private int total_results;
    private int total_pages;

    public List<T> getResults() {
        return results;
    }

}
