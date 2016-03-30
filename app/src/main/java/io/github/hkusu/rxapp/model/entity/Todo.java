package io.github.hkusu.rxapp.model.entity;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class Todo {
    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    public String text;

    public static Todo_Relation relation(OrmaDatabase orma) {
        return orma.relationOfTodo().orderByIdDesc();
    }
}