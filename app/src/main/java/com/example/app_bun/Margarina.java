package com.example.app_bun;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class Margarina implements Parcelable {

    public enum TipMarg {
        VEGANĂ, LIGHT, CLASICĂ, CU_UNT
    }

    private String nume;
    private boolean contineAlergeni;
    private int numarCalorii;
    private float rating;
    private TipMarg tip;
    private Date dataExpirare;

    public Margarina(String nume, boolean contineAlergeni, int numarCalorii, float rating, TipMarg tip,Date dataExpirare) {
        this.nume = nume;
        this.contineAlergeni = contineAlergeni;
        this.numarCalorii = numarCalorii;
        this.rating = rating;
        this.tip = tip;
        this.dataExpirare=dataExpirare;
    }

    protected Margarina(Parcel in) {
        nume = in.readString();
        contineAlergeni = in.readByte() != 0;
        numarCalorii = in.readInt();
        rating = in.readFloat();
        tip = Margarina.TipMarg.valueOf(in.readString());
        long dateMillis = in.readLong();
        dataExpirare = (dateMillis == -1L) ? null : new Date(dateMillis);
    }

    public static final Creator<Margarina> CREATOR = new Creator<Margarina>() {
        @Override
        public Margarina createFromParcel(Parcel in) {
            return new Margarina(in);
        }

        @Override
        public Margarina[] newArray(int size) {
            return new Margarina[size];
        }
    };

    public String getNume() { return nume; }

    public boolean isContineAlergeni() { return contineAlergeni; }
    public int getNumarCalorii() { return numarCalorii; }
    public float getRating() { return rating; }
    public TipMarg getTip() { return tip; }
    public Date getDataExpirare() { return dataExpirare; }
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nume);
        dest.writeByte((byte) (contineAlergeni ? 1 : 0));
        dest.writeInt(numarCalorii);
        dest.writeFloat(rating);
        dest.writeString(tip.name());
        dest.writeLong(dataExpirare != null ? dataExpirare.getTime() : -1L);
    }
}
