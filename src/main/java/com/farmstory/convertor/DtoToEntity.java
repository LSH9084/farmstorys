//package com.farmstory.convertor;
//
//import com.farmstory.entity.user.UserEntity;
//import com.farmstory.requestdto.TestUserRequestDto;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DtoToEntity {
//    public UserEntity convertToUser(TestUserRequestDto dto) {
//
//        return new UserEntity(
//                dto.getUser_idx(),
//                dto.getUser_id(), // uid
//                dto.getUser_email(), // email
//                dto.getUser_name(),
//                dto.getUser_pwd(),
//                dto.getUser_nick(),
//                dto.getUser_hp()
//        );
//    }
//
//}
