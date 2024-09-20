package com.farmstory.controller.pagecontroller;

import com.farmstory.responsedto.board.GetBoardsRespDto;
import com.farmstory.service.board.BoardService;
import com.farmstory.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/boards")
    public ModelAndView getBoards(
            @RequestParam String section,
            @RequestParam String type,
            @RequestParam(value = "page", defaultValue = "0") int page
    ){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pages/board/board_list");
        mav.addObject("section", section);
        mav.addObject("type", type);

        Page<GetBoardsRespDto> boards = boardService.selectBoards(page,section,type);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        String admin = userService.checkRole();
//        String owner = userService.checkBoardUser();
        if(boards!=null){
            mav.addObject("boards", boards);
            mav.addObject("page", page);
            mav.addObject("currentPage", page);
            mav.addObject("totalPage", Math.ceil((double)boards.getTotalElements()/10));
            mav.addObject("admin", admin);
            mav.addObject("type", type);
            mav.addObject("section", section);
        } else {
            mav.addObject("page", 0);
            mav.addObject("currentPage", 0);
            mav.addObject("totalPage", 0);
            mav.addObject("admin", admin);
            mav.addObject("type", type);
            mav.addObject("section", section);
        }

        return mav;
    }
}
