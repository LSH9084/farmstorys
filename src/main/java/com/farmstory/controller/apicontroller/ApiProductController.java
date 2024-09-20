package com.farmstory.controller.apicontroller;

import com.farmstory.requestdto.product.GetProductDto;
import com.farmstory.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ApiProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProductType(
            @RequestParam(value = "section") String section,
            @RequestParam(value = "type") String type,
            @RequestParam(value="page", defaultValue="0") int page
    ){
        if(type.equals("all")){
            Page<GetProductDto> products = productService.selectPageProduct(page);

            // 결과를 Map에 담아 반환
            Map<String, Object> response = new HashMap<>();
            response.put("section", section);
            response.put("type", type);
            response.put("products", products.getContent()); // DTO 리스트
            response.put("currentPage", page);
            response.put("totalPage", Math.ceil((double) products.getTotalElements() / 10)); // 페이지 수
            response.put("totalCnt", products.getTotalElements());
            return ResponseEntity.ok(response);
        }
            Page<GetProductDto> products = productService.selectPageProducts(page,type);
            Map<String, Object> response = new HashMap<>();
            response.put("section", section);
            response.put("type", type);
            response.put("products", products.getContent()); // DTO 리스트
            response.put("currentPage", page);
            response.put("totalPage", Math.ceil((double) products.getTotalElements() / 10)); // 페이지 수
            response.put("totalCnt", products.getTotalElements());


        return ResponseEntity.ok(response);

    }
}
