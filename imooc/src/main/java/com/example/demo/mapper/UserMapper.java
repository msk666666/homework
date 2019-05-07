package com.example.demo.mapper;

import com.example.demo.domain.MobileUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

     MobileUser getUserByPhone(String nickname);


    void addUser(MobileUser mobileUser);

    void updateLoginInfo(MobileUser user);

}
