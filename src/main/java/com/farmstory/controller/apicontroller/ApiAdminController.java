package com.farmstory.controller.apicontroller;

import com.farmstory.requestdto.product.DeleteProductReqDto;
import com.farmstory.requestdto.product.PostProductDto;
import com.farmstory.requestdto.user.DeleteUserReqDto;
import com.farmstory.requestdto.user.PutUserRoleReqDto;
import com.farmstory.responsedto.user.GetUserAllInfoDto;
import com.farmstory.service.product.ProductService;
import com.farmstory.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ApiAdminController {
    private final ProductService productService;
    private final UserService userService;

    @DeleteMapping("/product")
    public ResponseEntity<String> deleteProduct(
            @RequestBody DeleteProductReqDto request
    ){

        String result = productService.deleteProduct(request);
        String path = null;
        if("SU".equals(result)){
            path = "/admin/products";
        }
        return ResponseEntity.ok(path);
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(
            @RequestBody DeleteUserReqDto request
    ){
        System.out.println(request);
        String result = userService.deleteUser(request);
        String path = null;
        if("SU".equals(result)){
            path = "/admin/users";
        }
        return ResponseEntity.ok(path);
    }



    @PostMapping("/product")
    public ResponseEntity<String> postProduct(
            @RequestParam("product") String productJson,
            @RequestPart(value = "file_list", required = false) MultipartFile fileList,
            @RequestPart(value = "file_basic", required = false) MultipartFile fileBasic,
            @RequestPart(value = "file_description", required = false) MultipartFile fileDescription
            ){

        ObjectMapper objectMapper = new ObjectMapper();
        PostProductDto product;
        try {
            product = objectMapper.readValue(productJson, PostProductDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid product data");
        }
        String path1 = null;
        String path2 = null;
        String path3 = null;
        try {
            if (fileList != null && !fileList.isEmpty()) {
                path1 = productService.saveFile(fileList);
            }
            if (fileBasic != null && !fileBasic.isEmpty()) {
                path2 = productService.saveFile(fileBasic);
            }
            if (fileDescription != null && !fileDescription.isEmpty()) {
                path3 = productService.saveFile(fileDescription);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String result = productService.insertProduct(product,path1,path2,path3);
        String path = "admin/products";

        if("SU".equals(result)){
            return ResponseEntity.ok().body(path);
        }
        return ResponseEntity.badRequest().body("상품등록 실패");
    }

    @DeleteMapping("/order")
    public ResponseEntity<String> deleteOrder(
            @RequestParam Long orderIdx
    ){
        String path = "admin/orders";

        return ResponseEntity.ok().body(path);
    }

    @PutMapping("/user")
    public ResponseEntity<String> putUser(
            @RequestBody PutUserRoleReqDto dto
            ){
        String result = userService.putUserRole(dto);

        if(result.equals("SU")){
            return ResponseEntity.ok().body("SUCCESS");
        } else {
            return ResponseEntity.accepted().body("FAIL");
        }

    }
    @GetMapping("/user")
    public ResponseEntity<Map<String,Object>> getUser(
        @RequestParam Long userIdx
    ){
        GetUserAllInfoDto user = userService.selectUser(userIdx);
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);

        return ResponseEntity.ok(map);
    }
}
