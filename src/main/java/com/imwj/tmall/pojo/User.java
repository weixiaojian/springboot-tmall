package com.imwj.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @author langao_q
 * @create 2019-12-02 16:12
 */
@Data
@Entity
@Table(name = "user")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Transient
    private String anonymousName;

    public String getAnonymousName(){
        if(null != anonymousName){
            return anonymousName;
        }
        if(null == name){
            anonymousName = null;
        }else if(name.length() <= 1){
            anonymousName = "*";
        }else if(name.length() == 2){
            anonymousName = name.substring(0,1) + "*";
        }else{
            char[] chars = name.toCharArray();
            for(int i=1; i < chars.length-1; i++){
                chars[i] = '*';
                anonymousName = new String(chars);
            }
        }
        return anonymousName;
    }

}
