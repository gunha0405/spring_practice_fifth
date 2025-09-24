package com.example.question.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.example.answer.model.Answer;
import com.example.answer.model.dto.AnswerForm;
import com.example.answer.service.AnswerService;
import com.example.question.model.BaseQuestion;
import com.example.question.model.QuestionA;
import com.example.question.model.QuestionB;
import com.example.question.model.dto.QuestionForm;
import com.example.question.service.QuestionService;
import com.example.user.model.CustomUserDetails;
import com.example.user.model.SiteUser;
import com.example.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String list(Model model,
                       @RequestParam(value="page", defaultValue="0") int page,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        String tenantId = userDetails.getCustomerId();
        Page<? extends BaseQuestion> paging = this.questionService.getList(page, tenantId);

        Map<Long, Integer> answerCounts = paging.getContent().stream()
            .collect(Collectors.toMap(
                BaseQuestion::getId,
                q -> answerService.countByQuestion(q.getId(), q.getQuestionType())
            ));

        model.addAttribute("paging", paging);
        model.addAttribute("answerCounts", answerCounts);

        return "question_list";
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("isAuthenticated()")
    public String detail(Model model,
                         @PathVariable("id") Long id,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         AnswerForm answerForm) {

        String customerId = userDetails.getCustomerId();

        BaseQuestion question = this.questionService.getQuestion(id, customerId);

        List<Answer> answers = this.answerService.getAnswersByQuestion(id, customerId);

        model.addAttribute("question", question);
        model.addAttribute("answers", answers);

        return "question_detail";
    }

    
    @GetMapping("/create")
    public String showCreateForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {
    	String customerId = userDetails.getCustomerId();
        model.addAttribute("questionForm", new QuestionForm());
        model.addAttribute("customerId", customerId); // 뷰에서 사용할 수 있도록 추가
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        SiteUser siteUser = userService.getUser(userDetails.getUsername());
        String tenantId = userDetails.getCustomerId(); // 고객사 기준 tenantId

        this.questionService.create(
                questionForm.getSubject(),
                questionForm.getContent(),
                questionForm.getKeyword(),
                questionForm.getHashtag(),
                tenantId,
                siteUser
        );
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 @PathVariable("id") Long id,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        String customerId = userDetails.getCustomerId();
        BaseQuestion question = this.questionService.getQuestion(id, customerId);

        SiteUser author = question.getAuthor();
        if (!author.getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.questionService.modify(
                question,
                questionForm.getSubject(),
                questionForm.getContent(),
                questionForm.getKeyword(),
                questionForm.getHashtag()
        );
        return String.format("redirect:/question/detail/%s", id);
    }

    @GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id") Long id,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        String customerId = userDetails.getCustomerId();
        BaseQuestion question = this.questionService.getQuestion(id, customerId);

        SiteUser author = question.getAuthor();
        if (!author.getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.questionService.delete(question);
        return "redirect:/question/list";
    }

    @GetMapping("/search")
    public String searchQuestions(@RequestParam String subject,
                                  @RequestParam String value,
                                  @RequestParam(value="page", defaultValue="0") int page,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  Model model) {

        String customerId = userDetails.getCustomerId();
        Page<? extends BaseQuestion> paging = questionService.search(subject, value, customerId, page);

        model.addAttribute("paging", paging);
        model.addAttribute("searchMode", true);
        model.addAttribute("subject", subject);
        model.addAttribute("value", value);

        return "question_list";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
    	String customerId = userDetails.getCustomerId();
    	BaseQuestion question = this.questionService.getQuestion(id, customerId);
    	SiteUser siteUser = userService.getUser(userDetails.getUsername());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
