package com.example.megusta;
/** Assignment: Concluding assignment
 * Campus: Ashdod
 * Author1: Ilan Kroter, ID: 323294843
 * Author2: Noy Nir, ID: 207993940
 **/

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Immutable object of the item sold in the store.
 * There is currently no implementation of changing the object, best to create a new one.
 */
public class Item {
    private String id;
    private String name;
    private String category;
    private String item_photo;
    private boolean rent;
    private int price;
    private String location;
    private String phone;
    private String email;
    private String date;
    private String user_name;

    /**
     * Unused parametric constructor
     * @param id String Firebase ID
     * @param name String name of item
     * @param category String the category it belongs to
     * @param item_photo String the URL of the photo
     * @param rent Boolean is the item for rent?
     * @param price int the price of the item.
     * @param location the location the item is sold in.
     * @param phone the phone of the item owner.
     * @param email the email of the owner.
     * @param date the date posted.
     * @param user_name the user name of the item owner.
     */
    public Item(String id, String name, String category, String item_photo, boolean rent,
                int price, String location, String phone, String email, String date,String user_name) {
        this.id=id;
        this.name = name;
        this.category = category;
        this.item_photo = item_photo;
        this.rent = rent;
        this.price = price;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.date = date;
        this.user_name=user_name;
    }

    /**
     * COnstructor for item from Firebase document.
     * @param document from firebase.
     * @throws NullPointerException if any field is null.
     */
    public Item(DocumentSnapshot document) throws NullPointerException {
        this.id=document.getId();
        this.name = (String)document.getData().get("item_name");
        this.category = (String)document.getData().get("item_category");
        this.item_photo = (String)document.getData().get("item_photo");
        this.rent = (boolean)document.getData().get("rent");
        this.price = Integer.parseInt((String)document.getData().get("price"));
        this.location = (String)document.getData().get("location");
        this.phone = (String)document.getData().get("phone");
        this.email = (String)document.getData().get("email");
        this.date = (String)document.getData().get("date");
        this.user_name=(String)document.getData().get("user_name");
    }

    /**
     * @return the key of the item generated in firebase.
     */
    public String getId() {
        return id;
    }

    /**
     * @return get the item name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return get the category.
     */

    public String getCategory() {
        return category;
    }

    /**
     * @return get the photo URL.
     */
    public String getItem_photo() {
        return item_photo;
    }

    /**
     * @return get if it's rental or not.
     */
    public boolean isRent() {
        return rent;
    }
    /**
     * @return get the price of the item.
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return get the location of the item.
     */
    public String getLocation() {
        return location;
    }
    /**
     * @return get the phone number of the seller.
     */
    public String getPhone() {
        return phone;
    }
    /**
     * @return get the date the item was posted in.
     */
    public String getDate() {
        return date;
    }

    /**
     * @return get the user name of the poster.
     */
    public String getUser_name() {
        return user_name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", item_photo='" + item_photo + '\'' +
                ", rent=" + rent +
                ", price=" + price +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
