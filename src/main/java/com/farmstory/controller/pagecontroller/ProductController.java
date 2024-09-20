package com.farmstory.controller.pagecontroller;

import com.farmstory.requestdto.product.GetProductDto;
import com.farmstory.responsedto.product.GetProductRespDto;
import com.farmstory.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public ModelAndView getProducts(
            @RequestParam(value = "section") String section,
            @RequestParam(value = "type") String type,
            @RequestParam(value="page", defaultValue="0") int page
            ) {
        Page<GetProductDto> products3 = productService.selectPageProduct(page);
        if(type.equals("all")){
            ModelAndView mav = new ModelAndView();
            mav.setViewName("pages/product/product_list");
            mav.addObject("section", section);
            mav.addObject("type", type);
            Page<GetProductDto> products = productService.selectPageProduct(page);
            long totalCntType = productService.countProductsByType("과일");
            long totalCntType2 = productService.countProductsByType("야채");
            long totalCntType3 = productService.countProductsByType("신선야채");

            mav.addObject("products", products);
            mav.addObject("currentPage", page);
            mav.addObject("totalPage", Math.ceil((double)products.getTotalElements()/10));
            mav.addObject("totalCnt", products.getTotalElements());
            mav.addObject("totalCntType", totalCntType);
            mav.addObject("totalCntType2", totalCntType2);
            mav.addObject("totalCntType3", totalCntType3);
            mav.addObject("products3",products3.getTotalElements());
            return mav;
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pages/product/product_list");
        mav.addObject("section", section);
        mav.addObject("type", type);

        long totalCntType = productService.countProductsByType("과일");
        long totalCntType2 = productService.countProductsByType("야채");
        long totalCntType3 = productService.countProductsByType("신선야채");

        Page<GetProductDto> products = productService.selectPageProducts(page,type);

        mav.addObject("products", products);
        mav.addObject("currentPage", page);
        mav.addObject("totalPage", Math.ceil((double)products.getTotalElements()/10));
        mav.addObject("totalCnt", products.getTotalElements());
        mav.addObject("totalCntType", totalCntType);
        mav.addObject("totalCntType2", totalCntType2);
        mav.addObject("totalCntType3", totalCntType3);
        mav.addObject("products3",products3.getTotalElements());

        return mav;
    }


    @GetMapping("/product")
    public ModelAndView getProduct(@RequestParam Long productIdx) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("productIdx", productIdx);
        mav.addObject("section", "product");
        GetProductRespDto product = productService.selectProduct(productIdx);

        mav.setViewName("pages/product/product_view");
        mav.addObject("product",product);
        return mav;
    }


}
