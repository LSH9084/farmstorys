package com.farmstory.entity.cart;

import com.farmstory.entity.product.ProductEntity;
import com.farmstory.entity.product.ProductFileEntity;
import com.farmstory.responsedto.cart.GetCartsRespDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_item")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_idx")
    private Long cartItemIdx;

    @Column(name = "cart_item_quantity", nullable = false)
    private Integer cartItemQuantity;

    @ManyToOne
    @JoinColumn(name = "cart_idx")
    @ToString.Exclude
    private CartEntity cart;


    @ManyToOne
    @JoinColumn(name = "prod_idx")
    private ProductEntity prod;
    

    public GetCartsRespDto toDtoGetCarts(String userName, String userHp,BigDecimal userPoint){
        String prodFileName = null;
        if(prod.getProductFiles()==null){
            prodFileName = "empty.png";
        }

        ProductFileEntity file = prod.getProductFiles()
                .stream().filter(v->v.getProdFileType().equals("list"))
                .findFirst().orElse(null);

        prodFileName = file != null ? file.getProdFileName() : "empty.png";

        BigDecimal multiPrice = prod.getProdPrice().multiply(new BigDecimal(cartItemQuantity));
        BigDecimal discoutPrice = multiPrice.multiply(prod.getProdDiscount().divide(new BigDecimal(100)));
        BigDecimal minusDiscount = discoutPrice.negate();
        BigDecimal totalPrice = multiPrice.add(minusDiscount);

        return GetCartsRespDto.builder()
                .prodType(prod.getProdType())
                .cartItemIdx(cartItemIdx)
                .cartItemQuantity(cartItemQuantity)
                .prodFileName(prodFileName)
                .prodFilePath("/file/")
                .prodPrice(prod.getProdPrice())
                .prodDiscount(prod.getProdDiscount())
                .prodPoint(prod.getProdSavePoint())
                .prodName(prod.getProdName())
                .prodTotal(multiPrice)
                .prodDelivery(prod.getProdDelivery())
                .cartIdx(cart.getCartIdx())
                .userHp(userHp)
                .userName(userName)
                .userPoint(userPoint)
                .build();
    }
}
