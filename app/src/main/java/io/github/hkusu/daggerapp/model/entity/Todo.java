package io.github.hkusu.daggerapp.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Todo extends RealmObject {
    // プライマリキー名
    public static final String PRIMARY_KEY = "id";
    // ソートキー名
    public static final String SORT_KEY = "id";

    // カラム定義
    @PrimaryKey
    private int id;      // id
    private String text; // text

    // Realmでは引数なしのデフォルトコンストラクタをpublicで定義する必要がある
    public Todo() {
    }

    // 以降 Getter/Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}