package com.inventry.inventry.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Products {
    @Id
    private long pCode;
    private String pName;
    private float pWeight;
    private float pPrice;
    private String pBrandName;

    @Lob
    private String pDescription;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category categoryId;

    // Getters and Setters
    public long getpCode() {
        return pCode;
    }

    public void setpCode(long pCode) {
        this.pCode = pCode;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public float getpWeight() {
        return pWeight;
    }

    public void setpWeight(float pWeight) {
        this.pWeight = pWeight;
    }

    public float getpPrice() {
        return pPrice;
    }

    public void setpPrice(float pPrice) {
        this.pPrice = pPrice;
    }

    public String getpBrandName() {
        return pBrandName;
    }

    public void setpBrandName(String pBrandName) {
        this.pBrandName = pBrandName;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }
}
