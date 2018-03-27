package com.mcustodio.tasklog.model;


import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

// Por algum motivo essa classe tem que ser Java
// Com Kotlin é gerado um erro no build :
// "Error: Type of the parameter must be a class annotated with @Entity or a collection/array of it."
// Acredito que acontece porque ele não identifica a classe genérica T como um @Entity

public interface BaseDao<T> {


    @Insert
    long insert(T record);

    @Insert
    long[] insert(List<T> records);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(T record);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] upsert(List<T> records);

    @Update
    int update(T record);

    @Update
    int update(List<T> records);

    @Delete
    int delete(T record);

    @Delete
    int delete(List<T> records);

}