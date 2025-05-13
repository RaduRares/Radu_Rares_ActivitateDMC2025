package com.example.app_bun;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "margarine")
public class MargarinaEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nume;
    public boolean contineAlergeni;
    public int numarCalorii;
    public float rating;
    public String tip;
    public Date dataExpirare;
    public MargarinaEntity(String nume, boolean contineAlergeni, int numarCalorii, float rating, String tip,Date dataExpirare) {
        this.nume = nume;
        this.contineAlergeni = contineAlergeni;
        this.numarCalorii = numarCalorii;
        this.rating = rating;
        this.tip = tip;
        this.dataExpirare = dataExpirare;
    }}
