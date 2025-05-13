package com.example.app_bun;
import androidx.room.Dao;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MargarinaDao {
    @Insert
    void insert(MargarinaEntity margarina);

    @Query("SELECT * FROM margarine")
    List<MargarinaEntity> getAll();

    @Query("SELECT * FROM margarine WHERE nume = :nume")
    MargarinaEntity findByNume(String nume);

    @Query("SELECT * FROM margarine WHERE numarCalorii BETWEEN :min AND :max")
    List<MargarinaEntity> getByCaloriiInterval(int min, int max);

    @Query("DELETE FROM margarine WHERE numarCalorii > :valoare")
    int deleteCaloriiMaiMariDecat(int valoare);

    @Query("UPDATE margarine SET numarCalorii = numarCalorii + 1 WHERE nume LIKE :litera || '%'")
    void incrementCaloriiForNamesStartingWith(String litera);
}