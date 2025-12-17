package models;

import play.*;
import play.data.validation.*;

import java.util.*;

public class Project  {
    
    @Required
    public String name;
    
    @InFuture
    @InPast("2020-01-01")
    public Date endDate;
    
    @InPast
    @InFuture("1980-12-21")
    public Date startDate;
    
    @Required
    public Company company;
    
    public Map<String,Company> companies;
    
    private String observation;
    
    public String toString() {
        return name + " belongs to " + company;
    }
    
    public Project() {}
    
    public Project(String name) {
        this.name = name;
    }
    
    public String getObservation() {
    	return this.observation;
    }
    
    public void setObservation(String observation) {
    	this.observation = observation;
    }
}

