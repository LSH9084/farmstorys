package com.farmstory.service.user;

import com.farmstory.config.SecurityConfig;
import com.farmstory.entity.board.BoardEntity;
import com.farmstory.entity.user.UserAddressEntity;
import com.farmstory.entity.user.UserEntity;
import com.farmstory.entity.user.UserPointDetailEntity;
import com.farmstory.entity.user.UserPointEntity;
import com.farmstory.repository.board.BoardRepository;
import com.farmstory.repository.user.*;
import com.farmstory.requestdto.user.*;
import com.farmstory.responsedto.user.GetMypageUserPointRespDto;
import com.farmstory.responsedto.user.GetUserAllInfoDto;
import com.farmstory.responsedto.user.GetUsersRespDto;
import com.farmstory.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserScheduleRepository userScheduleRepository;
    private final UserPointRepository userPointRepository;
    private final UserPointDetailRepository userPointDetailRepository;
    private final EmailService emailService;
    private final SecurityConfig securityConfig;
    private final BoardRepository boardRepository;

    public String deleteUser(DeleteUserReqDto request) {
        request.getUserIdx().forEach(v->{
            userRepository.delete(UserEntity.builder().userIdx(Long.parseLong(v)).build());
        });

        return "SU";
    }


    @Transactional
    public void insertUser(SignupUserDto user, SignupUserAddressDto address) {

        UserEntity userEntity = user.dtoToEntity(passwordEncoder);
        UserEntity newUser = userRepository.save(userEntity);
        Long userIdx =newUser.getUserIdx();


        UserPointEntity userPointEntity = UserPointEntity.builder()
                .userIdx(userIdx)
                .userPoint(new BigDecimal(0))
                .build();

        userPointRepository.save(userPointEntity);

        UserPointDetailEntity userPointDetailEntity = UserPointDetailEntity.builder()
                .point(userPointEntity)
                .savePoint(new BigDecimal(1000))
                .usePoint(BigDecimal.ZERO)
                .detail("회원가입 축하 포인트")
                .build();

        userPointDetailRepository.save(userPointDetailEntity);

        Optional<UserPointEntity> optUserPoint = userPointRepository.findByUserIdx(userIdx);

        List<UserPointDetailEntity> userPointDetails = userPointDetailRepository.findAllByPoint(optUserPoint.get());

        BigDecimal total = userPointDetails.stream()
                .map(detail -> detail.getSavePoint().subtract(detail.getUsePoint())) // savePoint에서 usePoint를 뺌
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 결과를 모두 더함


        UserPointEntity newUserPoint = UserPointEntity.builder()
                .userPoint(total)
                .userIdx(userIdx)
                .pointIdx(optUserPoint.get().getPointIdx())
                .build();

        userPointRepository.save(newUserPoint);

        UserAddressEntity userAddressEntity = address.toEntity(userIdx);

        userAddressRepository.save(userAddressEntity);
    }

    public Page<GetUsersRespDto> selectUsers(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<UserEntity> users = userRepository.findAll(pageable);

        List<UserEntity> userList = userRepository.findAll();
        int total = userList.size();
        List<GetUsersRespDto> dto = userList.stream()
                .map(UserEntity::toDto)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                }))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dto.size());
        List<GetUsersRespDto> pageContent = dto.subList(start, end);


        return new PageImpl<>(pageContent, pageable, total);
    }

    @Transactional
    public String putUserRole(PutUserRoleReqDto dto) {
        Optional<UserEntity> optUser = userRepository.findById(dto.getUserIdx());
        if(optUser.isPresent()) {
            int updatedRows = userRepository.updateUserRole(dto.getUserRole(), dto.getUserIdx());
            if (updatedRows > 0) {
                return "SU";
            }
            return "FAIL";
        }


        return "FAIL";
    }

    public GetUserAllInfoDto selectUser(Long userIdx) {
        Optional<UserEntity> optUser = userRepository.findById(userIdx);
        UserAddressEntity userAddressEntity = userAddressRepository.findByUserIdx(userIdx);
        Optional<UserPointEntity> userPointEntity = userPointRepository.findByUserIdx(userIdx);

        GetUserAllInfoDto user = new GetUserAllInfoDto(optUser.get(), userAddressEntity, userPointEntity.get());

        return user;
    }

    public String checkId(String userId) {
        Optional<UserEntity> optUser = userRepository.findByUserId(userId);

        if (optUser.isEmpty()) {
            return "SU";
        }



        return "DBI";
    }

    public String checkNick(String userNick) {
        Optional<UserEntity> optUser = userRepository.findByUserNick(userNick);
        if (optUser.isEmpty()) {
            return "SU";
        }

        return "DBN";
    }

    public String sendEmail(String userEmail) {
        Optional<UserEntity> optUser = userRepository.findByUserEmail(userEmail);
        if (optUser.isPresent()) {
            return "DBE";
        }


        String code = emailService.generateRandomCode();

        emailService.sendEmail(userEmail,"회원가입 인증 이메일발송",code);
        return code;

    }

    public String checkPwd(String pwd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (securityConfig.passwordEncoder().matches(pwd, userEntity.getUserPwd())) {
            return "SU"; // 성공
        }

        return "FA";
    }

    public GetUserAllInfoDto selectMypageUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<UserEntity> optUser = userRepository.findById(userEntity.getUserIdx());
        UserAddressEntity userAddressEntity = userAddressRepository.findByUserIdx(userEntity.getUserIdx());

        GetUserAllInfoDto user = new GetUserAllInfoDto(optUser.get(),userAddressEntity);

        return user;
    }

    public String updateUserPwd(String pwd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (securityConfig.passwordEncoder().matches(pwd, userEntity.getUserPwd())) {
            return "DUP";
        }

        UserEntity newUserEntity = UserEntity.builder()
                .userPwd(passwordEncoder.encode(pwd))
                .userHp(userEntity.getUserHp())
                .userIdx(userEntity.getUserIdx())
                .userName(userEntity.getUserName())
                .userCreateAt(userEntity.getUserCreateAt())
                .userRole(userEntity.getUserRole())
                .userId(userEntity.getUserId())
                .userNick(userEntity.getUserNick())
                .userEmail(userEntity.getUserEmail())
                .build();
        userRepository.save(newUserEntity);

        return "SU";
    }

    @Transactional
    public void updateUser(PutMypageUserReqDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserEntity.UserEntityBuilder userEntityBuilder = UserEntity.builder();
        if(request.getUserName()!= null && !request.getUserName().isEmpty()){
            userEntityBuilder.userName(request.getUserName());
        } else {
            userEntityBuilder.userName(userEntity.getUserName());
        }
        if(request.getUserEmail()!= null && !request.getUserEmail().isEmpty()){
            userEntityBuilder.userEmail(request.getUserEmail());
        } else {
            userEntityBuilder.userEmail(userEntity.getUserEmail());
        }
        if(request.getUserNick()!= null && !request.getUserNick().isEmpty()){
            userEntityBuilder.userNick(request.getUserNick());
        } else {
            userEntityBuilder.userNick(userEntity.getUserNick());
        }
        if(request.getUserHp()!= null && !request.getUserHp().isEmpty()){
            userEntityBuilder.userHp(request.getUserHp());
        } else {
            userEntityBuilder.userHp(userEntity.getUserHp());
        }
        userEntityBuilder.userCreateAt(userEntity.getUserCreateAt());
        userEntityBuilder.userPwd(userEntity.getUserPwd());
        userEntityBuilder.userRole(userEntity.getUserRole());
        userEntityBuilder.userId(userEntity.getUserId());
        userEntityBuilder.userIdx(userEntity.getUserIdx());

        userRepository.save(userEntityBuilder.build());
        UserAddressEntity userAddressEntity = userAddressRepository.findByUserIdx(userEntity.getUserIdx());
        UserAddressEntity.UserAddressEntityBuilder userAddressEntityBuilder = UserAddressEntity.builder();

        if(request.getAddrZone()!= null && !request.getAddrZone().isEmpty()){
            userAddressEntityBuilder.addrZone(request.getAddrZone());
        } else {
            userAddressEntityBuilder.addrZone(userAddressEntity.getAddrZone());
        }
        if(request.getAddr()!= null && !request.getAddr().isEmpty()){
            userAddressEntityBuilder.addr(request.getAddr());
        } else {
            userAddressEntityBuilder.addr(userAddressEntity.getAddr());
        }
        if(request.getAddrDetail()!= null && !request.getAddrDetail().isEmpty()){
            userAddressEntityBuilder.addrDetail(request.getAddrDetail());
        } else {
            userAddressEntityBuilder.addrDetail(userAddressEntity.getAddrDetail());
        }
        userAddressEntityBuilder.userIdx(userAddressEntity.getUserIdx());
        userAddressEntityBuilder.addrIdx(userAddressEntity.getAddrIdx());

        userAddressRepository.save(userAddressEntityBuilder.build());

    }

//    @Transactional
//    public void deleteMypageUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//
//        UserEntity userEntity = userRepository.findByUserId(username)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        userAddressRepository.deleteByUserIdx(userEntity.getUserIdx());
//        userRepository.delete(userEntity);
//
//    }

    @Transactional
    public void deleteUserAndLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 유저 주소 및 유저 삭제
        userAddressRepository.deleteByUserIdx(userEntity.getUserIdx());
        userRepository.delete(userEntity);

        // SecurityContextHolder에서 인증 정보 삭제
        SecurityContextHolder.clearContext();

        // 세션 무효화 (로그아웃 처리)
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션을 무효화하여 로그아웃 효과
        }

    }

    @Transactional
    public Page<GetMypageUserPointRespDto> selectMypageUserPoint(int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Pageable pageable = PageRequest.of(page, 10);

        Optional<UserPointEntity> optUserPoint = userPointRepository.findByUserIdx(userEntity.getUserIdx());

        List<UserPointDetailEntity> pageUserPointDetail = userPointDetailRepository.findAllByPoint(optUserPoint.get());
        int total = pageUserPointDetail.size();
        List<GetMypageUserPointRespDto> pointDtos = pageUserPointDetail.stream()
                .map(v -> v.toGetMypageUserPointRestDto())
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                }))
                .toList();

        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), 10);

        // 역순으로 정렬된 리스트로 새로운 Page 객체 생성
        int start = (int) newPageable.getOffset();
        int end = Math.min((start + newPageable.getPageSize()), pointDtos.size());
        List<GetMypageUserPointRespDto> pageContent = pointDtos.subList(start, end);


        return new PageImpl<>(pageContent, newPageable, total);
    }

    public BigDecimal selectPoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<UserPointEntity> optUserPoint = userPointRepository.findByUserIdx(userEntity.getUserIdx());

        return optUserPoint.get().getUserPoint();
    }

    public List<GetMypageUserPointRespDto> selectMypageUserAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<UserPointEntity> optUserPoint = userPointRepository.findByUserIdx(userEntity.getUserIdx());

        List<UserPointDetailEntity> pageUserPointDetail = userPointDetailRepository.findAllByPoint(optUserPoint.get());
        List<GetMypageUserPointRespDto> pointDtos = pageUserPointDetail.stream()
                .map(v -> v.toGetMypageUserPointRestDto())
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                }))
                .toList();

        return pointDtos;
    }

    public String checkRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(userEntity.getUserRole().equals("admin")){
            return "true";
        }

        return "false";
    }

    public String checkBoardUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

       BoardEntity boardEntity = boardRepository.findByUser(userEntity);



        return "false";
    }

}
