package br.com.budismo.nikkyojers.data;

/**
 * Created by marcio.ikeda on 29/01/2018.
 */

public class User {

    public String name;
    public String photoUrl;
    public String provider;

    public User() {

    }

    public User(String name, String photoUrl, String provider) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.provider = provider;
    }

}
