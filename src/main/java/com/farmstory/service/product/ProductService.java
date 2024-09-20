package com.farmstory.service.product;

import com.farmstory.entity.product.ProductEntity;
import com.farmstory.entity.product.ProductFileEntity;
import com.farmstory.repository.product.ProductFileRepository;
import com.farmstory.repository.product.ProductRepository;
import com.farmstory.repository.product.ProductSnapShotRepository;
import com.farmstory.requestdto.product.DeleteProductReqDto;
import com.farmstory.requestdto.product.GetProductDto;
import com.farmstory.requestdto.product.PostProductDto;
import com.farmstory.responsedto.product.GetProductRespDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductFileRepository productFileRepository;
    private final ProductSnapShotRepository productSnapShotRepository;

    @Transactional
    public String insertProduct(PostProductDto product, String list, String basic, String description) {

        ProductEntity productEntity = product.toEntity();
        ProductEntity productEntity2 = productRepository.save(productEntity);
 
        if(list!=null && !list.isEmpty()) {
            String path = list.substring(0,26);
            String correctedPath = path.replace("\\", "/");
            ProductFileEntity productFileEntity = ProductFileEntity.builder()
                    .prod(productEntity2)
                    .prodFileName(list.substring(20))
                    .prodFilePath("/file/")
                    .prodFileType("list")
                    .build();

            productFileRepository.save(productFileEntity);
        }
        if(basic!=null && !basic.isEmpty()) {
            ProductFileEntity productFileEntity = ProductFileEntity.builder()
                    .prod(productEntity2)
                    .prodFileName(basic.substring(20))
                    .prodFilePath("/file/")
                    .prodFileType("basic")
                    .build();

            productFileRepository.save(productFileEntity);
        }
        if(description!=null && !description.isEmpty()) {
            ProductFileEntity productFileEntity = ProductFileEntity.builder()
                    .prod(productEntity2)
                    .prodFileName(description.substring(20))
                    .prodFilePath("/file/")
                    .prodFileType("description")
                    .build();

            productFileRepository.save(productFileEntity);
        }

        return "SU";
    }



    public Page<GetProductDto> selectPageProduct(int page){
        Pageable pageable = PageRequest.of(page, 10);

        List<ProductEntity> productLists = productRepository.findAll();

        int total = productLists.size();

        List<GetProductDto> listDtos = productLists.stream()
                .map(ProductEntity::toDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                }))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), listDtos.size());
        List<GetProductDto> pageContent = listDtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, total);
    }

    public Page<GetProductDto> selectPageProducts(int page,String type){
        Pageable pageable = PageRequest.of(page, 10);

        List<ProductEntity> products = productRepository.findAllByProdType(type);

        int total = products.size();

        List<GetProductDto> productDtos = products.stream()
                .map(ProductEntity::toDto) // ProductEntity의 toDto 메서드를 사용
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                }))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productDtos.size());
        List<GetProductDto> pageContent = productDtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, total);
    }





    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        // 파일 업로드 디렉토리가 존재하는지 확인하고 없으면 생성합니다
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory", e);
            }
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store empty file");
        }
        String originalFilename = file.getOriginalFilename();

        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String randomFilename = UUID.randomUUID().toString() + extension;
        // 파일이 저장될 경로를 생성합니다
        Path path = Paths.get(uploadDir).resolve(randomFilename);

        // 파일을 지정된 경로에 저장합니다
        Files.copy(file.getInputStream(), path);

        return path.toString();
    }

    public long countProductsByType(String type) {
        return productRepository.countByType(type);
    }

    @Transactional
    public String deleteProduct(DeleteProductReqDto request) {
        request.getProdIdx().forEach(v->{
            Optional<ProductEntity> optProduct = productRepository.findById(Long.parseLong(v));
            productSnapShotRepository.save(optProduct.get().toProductSnapShotEntity());

            productRepository.delete(ProductEntity.builder().prodIdx(Long.parseLong(v)).build());
        });

        return "SU";
    }

    public GetProductRespDto selectProduct(Integer productIdx) {
        ProductEntity prod = productRepository.findById((long)productIdx).get();
        GetProductRespDto product = prod.getProductRespDto();
        return product;
    }
}
