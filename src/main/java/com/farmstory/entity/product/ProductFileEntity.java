package com.farmstory.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_file")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ProductFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_file_idx")
    private Long prodFileIdx;

    @Column(name = "prod_file_type", nullable = false, length = 20)
    private String prodFileType;

    @Column(name = "prod_file_name", nullable = false, length = 255)
    private String prodFileName;

    @Column(name = "prod_file_path", nullable = false, length = 255)
    private String prodFilePath;

    @ManyToOne
    @JoinColumn(name = "prod_idx")
    private ProductEntity prod;

//    public GetProductRespDto getProduct() {
//
//        return GetProductRespDto.builder()
//                .prodIdx(prod.getProdIdx())
//                .prodDelivery(prod.getProdDelivery())
//                .prodName(prod.getProdName())
//                .prodSavePoint(prod.getProdSavePoint())
//                .prodPrice(prod.getProdPrice())
//                .prodDiscount(prod.getProdDiscount())
//                .prodEtc(prod.getProdEtc())
//                .build();
//    }
}
