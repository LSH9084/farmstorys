package com.farmstory.service.user;

//import com.farmstory.entity.product.EventSnapShotEntity;

import com.farmstory.entity.product.EventSnapShotEntity;
import com.farmstory.entity.product.ProductEntity;
import com.farmstory.entity.user.AdminScheduleEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.entity.user.UserScheduleEntity;
import com.farmstory.repository.product.EventSnapShotRepository;
import com.farmstory.repository.product.ProductRepository;
import com.farmstory.repository.user.AdminScheduleRepository;
import com.farmstory.repository.user.UserRepository;
import com.farmstory.repository.user.UserScheduleRepository;
import com.farmstory.requestdto.user.DeleteScheduleReqDto;
import com.farmstory.requestdto.user.PostAdminScheduleReqDto;
import com.farmstory.requestdto.user.PostScheduleReqDto;
import com.farmstory.responsedto.user.GetAdminScheduleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserScheduleService {
    private final UserScheduleRepository userScheduleRepository;
    private final UserRepository userRepository;
    private final AdminScheduleRepository adminScheduleRepository;
    private final ProductRepository productRepository;
    private final EventSnapShotRepository eventSnapShotRepository;

    public String insertSchedule(PostScheduleReqDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 사용자 이름(일반적으로 username or email)


        UserEntity user = UserEntity.builder().userId(username).build();
        Optional<UserEntity> userIdx = userRepository.findByUserId(user.getUserId());
        System.out.println(userIdx.get().getUserIdx());
        dto.setUserIdx(userIdx.get().getUserIdx());

        UserScheduleEntity userScheduleEntity = dto.toEntity();
        userScheduleRepository.save(userScheduleEntity);
        return "SU";
    } // 일정 추가하기

    @Transactional
    public List<PostScheduleReqDto> selectSchedules() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 사용자 이름(일반적으로 username or email)

        UserEntity user = UserEntity.builder().userId(username).build();
        Optional<UserEntity> userIdx = userRepository.findByUserId(user.getUserId());


        if (userIdx.isPresent()) {
            List<UserScheduleEntity> schedules = userScheduleRepository.findAllByUserIdx(userIdx.get().getUserIdx());

            // DTO로 변환된 리스트를 반환
            return schedules.stream()
                    .map(UserScheduleEntity::toPostScheduleReqDto) // DTO로 변환
                    .collect(Collectors.toList());
        } else {
            // 사용자 엔티티를 찾지 못한 경우 빈 리스트 반환
            return Collections.emptyList();
        }
    } // 달력 화면 출력

    @Transactional
    public String deleteSchedule(DeleteScheduleReqDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<UserScheduleEntity> scheduleEntities = userScheduleRepository.findAllByUserIdx(userEntity.getUserIdx());
        List<Long> idxs = scheduleEntities.stream()
                .map(v-> v.forDelete(request))
                .toList();

        Optional<Long> idx = idxs.stream().filter(v -> v != 0)
                .findFirst();

        if(idx.isEmpty()){
            return "EMS";
        }
        userScheduleRepository.deleteById(idx.get());

        return "SU";
    }

    public List<GetAdminScheduleRespDto> selectAdminSchedules() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<AdminScheduleEntity> adminSchedules = adminScheduleRepository.findAll();
        if(adminSchedules.isEmpty()){
            return null;
        }

        List<GetAdminScheduleRespDto> respDtos = adminSchedules.stream()
                .map(v->v.toGetAdminScheduleRespDto())
                .toList();

        return respDtos;
    }

    public String insertAdminSchedule(PostAdminScheduleReqDto request) {

        AdminScheduleEntity adminScheduleEntity = request.toAdminScheduleEntity();

        adminScheduleRepository.save(adminScheduleEntity);

        return "SU";
    }

    public String deleteAdminSchedule(DeleteScheduleReqDto request) {
        List<AdminScheduleEntity> adminScheduleEntities = adminScheduleRepository.findAll();

        List<Long> idxs = adminScheduleEntities.stream()
                .map(v-> v.forDelete(request))
                .toList();

        Optional<Long> idx = idxs.stream().filter(v -> v != 0)
                .findFirst();

        if(idx.isEmpty()){
            return "EMS";
        }
        adminScheduleRepository.deleteById(idx.get());

        return "SU";
    }

    @Transactional
    public String selectYear(LocalDate dbToday) {

        String year = String.valueOf(dbToday.getYear());
        String month = String.valueOf(dbToday.getMonthValue());
        String day = String.valueOf(dbToday.getDayOfMonth());

        System.out.println(year);
        System.out.println(month);
        System.out.println(day);

        List<AdminScheduleEntity> adminScheduleEntities = adminScheduleRepository.findAllByText("30% 초특가 Event");
        if(adminScheduleEntities.isEmpty()){
            return "false";
        }
        List<String> results = adminScheduleEntities.stream()
                .map(v->v.forEvent(year,month,day))
                .toList();

        String result = results.stream()
                        .filter(v-> v.equals("true"))
                        .findFirst()
                        .orElse("false");

        if(result.equals("true")){
            List<EventSnapShotEntity> existingSnapShots = eventSnapShotRepository.findAll();
            if (!existingSnapShots.isEmpty()) {
                return "true";
            }
            List<ProductEntity> productEntities = productRepository.findAll();
            List<EventSnapShotEntity> eventSnapShotEntities = productEntities.stream()
                    .map(v->v.toEventSnapShotEntity())
                    .toList();
            eventSnapShotRepository.saveAll(eventSnapShotEntities);

            List<ProductEntity> newProductEntities = productEntities.stream()
                    .map(v->v.forUpdate())
                    .toList();

            productRepository.saveAll(newProductEntities);
        } else {
            List<EventSnapShotEntity> eventSnapShotEntities = eventSnapShotRepository.findAll();
            if(eventSnapShotEntities.isEmpty()){
                return "false";
            }
            List<ProductEntity> productEntities = eventSnapShotEntities.stream()
                    .map(v->v.forEvent())
                    .toList();

            productRepository.saveAll(productEntities);
            eventSnapShotRepository.deleteAll(eventSnapShotEntities);
        }

        return result;
    }
}
