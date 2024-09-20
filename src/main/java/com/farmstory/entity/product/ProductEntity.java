package com.farmstory.entity.product;

import com.farmstory.entity.order.OrderItemEntity;
import com.farmstory.requestdto.product.GetProductDto;
import com.farmstory.responsedto.product.GetProductFileRespDto;
import com.farmstory.responsedto.product.GetProductRespDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_idx")
    private Long prodIdx;

    @Column(name = "prod_name", nullable = false, length = 40)
    private String prodName;

    @Column(name = "prod_type", nullable = false, length = 20)
    private String prodType;

    @Column(name = "prod_delivery", nullable = false)
    private BigDecimal prodDelivery;

    @Column(name = "prod_price", nullable = false)
    private BigDecimal prodPrice;

    @Column(name = "prod_discount", nullable = false)
    private BigDecimal prodDiscount;

    @Column(name = "prod_save_point", nullable = false)
    private BigDecimal prodSavePoint;

    @Column(name = "prod_stock", nullable = false)
    private Integer prodStock;

    @Column(name = "prod_create_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp prodCreateAt;

    @Column(name = "prod_etc", nullable = false)
    private String prodEtc;

    @OneToMany(mappedBy = "prod", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ProductFileEntity> productFiles;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<OrderItemEntity> orderItems;

    public GetProductDto toDto (){
        ProductFileEntity selectedFile = productFiles.stream()
                .filter(file -> file.getProdFileType().equals("list"))
                .findFirst()
                .orElse(null);

        String prodFileName = selectedFile != null ? selectedFile.getProdFileName() : "empty.png";
        String prodFilePath = "/file/";

        return GetProductDto.builder()
                .prodFileName(prodFileName)
                .prodFilePath(prodFilePath)
                .prodIdx(prodIdx)
                .prodName(prodName)
                .prodType(prodType)
                .prodDelivery(prodDelivery)
                .prodPrice(prodPrice)
                .prodDiscount(prodDiscount)
                .prodSavePoint(prodSavePoint)
                .prodStock(prodStock)
                .prodCreateAt(prodCreateAt)
                .prodEtc(prodEtc)
                .build();
    }

    public GetProductRespDto getProductRespDto(){
        List<GetProductFileRespDto> lists = new ArrayList<>();
        lists.addAll(productFiles.stream()
                .filter(v -> v.getProdFileType().equals("basic"))
                .map(v -> new GetProductFileRespDto(v.getProdFileName(), v.getProdFilePath(), v.getProdFileType()))  // 변환 로직 추가
                .collect(Collectors.toList()));
        lists.addAll(productFiles.stream()
                .filter(v -> v.getProdFileType().equals("description"))
                .map(v -> new GetProductFileRespDto(v.getProdFileName(), v.getProdFilePath(), v.getProdFileType()))  // 변환 로직 추가
                .collect(Collectors.toList()));
      return GetProductRespDto.builder()
              .files(lists)
              .prodIdx(prodIdx)
              .prodName(prodName)
              .prodEtc(prodEtc)
              .prodStock(prodStock)
              .prodDiscount(prodDiscount)
              .prodSavePoint(prodSavePoint)
              .prodDelivery(prodDelivery)
              .prodPrice(prodPrice)
              .prodCreateAt(prodCreateAt)
              .build();
    };

    public ProductSnapShotEntity toProductSnapShotEntity(){
        ProductFileEntity selectedFile = productFiles.stream()
                .filter(file -> file.getProdFileType().equals("list"))
                .findFirst()
                .orElse(null);

        String prodFileName = selectedFile != null ? selectedFile.getProdFileName() : "empty.png";
        String prodFilePath = "/file/";

        return ProductSnapShotEntity.builder()
                .snapshotIdx(prodIdx)
                .snapshotName(prodName)
                .snapshotType(prodType)
                .snapshotPrice(prodPrice)
                .snapshotDiscount(prodDiscount)
                .snapshotSavePoint(prodSavePoint)
                .snapshotDelivery(prodDelivery)
                .snapshotCreateAt(prodCreateAt)
                .snapshotEtc(prodEtc)
                .snapshotStock(prodStock)
                .snapshotFileName(prodFileName)
                .snapshotFilePath(prodFilePath)
                .build();
    }

    public EventSnapShotEntity toEventSnapShotEntity(){

        return EventSnapShotEntity.builder()
                .eventIdx(prodIdx)
                .eventName(prodName)
                .eventType(prodType)
                .eventPrice(prodPrice)
                .eventDiscount(prodDiscount)
                .eventSavePoint(prodSavePoint)
                .eventDelivery(prodDelivery)
                .eventCreateAt(prodCreateAt)
                .eventEtc(prodEtc)
                .eventStock(prodStock)
                .build();
    }

    public ProductEntity forUpdate(){
        return ProductEntity.builder()
                .prodCreateAt(prodCreateAt)
                .prodEtc(prodEtc)
                .prodDiscount(new BigDecimal(30))
                .prodSavePoint(BigDecimal.ZERO)
                .prodDelivery(prodDelivery)
                .prodPrice(prodPrice)
                .prodStock(prodStock)
                .prodType(prodType)
                .prodName(prodName)
                .prodIdx(prodIdx)
                .build();
    }
}
