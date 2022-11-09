package com.mohammedalmajhali.aladhkar;

public class Dekr {
    private int number;
    private String dhekrText, virtueOfDhekrText;
    int id;



    public Dekr(int id, int number, String dhekrText, String virtueOfDhekrText) {
        this.number = number;
        this.dhekrText = dhekrText;
        this.virtueOfDhekrText = virtueOfDhekrText;
        this.id = id;
    }

    public String getVirtueOfDhekrText() {
        return virtueOfDhekrText;
    }

    public void setVirtueOfDhekrText(String virtueOfDhekrText) {
        this.virtueOfDhekrText = virtueOfDhekrText;
    }

    public int getNumber() {
        return number;
    }
    public String getDhekrText() {
        return dhekrText;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setDhekrText(String dhekrText) {
        this.dhekrText = dhekrText;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
