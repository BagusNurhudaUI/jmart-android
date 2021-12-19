package com.BagusJmartMH.model;

/**
 * merupakan model yang digunakan utnuk melakukan inisialisai dalam class product
 * terdapat funtion untuk mengembalikkan nilai parameter name, weight, dll
 */
public class Product extends Serializable{
    public int accountId;
    public double discount;
    public double price;
    public byte shipmentPlans;
    public String name;
    public int weight;
    public boolean conditionUsed;
    public ProductCategory category;

    public String toString() {
        return  "Name : " + this.name + "\nWeight : " + this.weight +
                "\nconditionUsed : " + this.conditionUsed + "\nprice : " +
                this.price + "\ncategory : " + this.category + "\ndiscount : " +
                this.discount + "\naccountId : " + this.accountId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}