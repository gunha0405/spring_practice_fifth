package com.example.answer.controller;

import java.security.Principal;

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
import org.springframework.web.server.ResponseStatusException;

import com.example.answer.model.Answer;
import com.example.answer.model.dto.AnswerForm;
import com.example.answer.service.AnswerService;
import com.example.question.model.BaseQuestion;
import com.example.question.service.QuestionService;
import com.example.user.model.CustomUserDetails;
import com.example.user.model.SiteUser;
import com.example.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model,
                               @PathVariable("id") Long id,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        String customerId = userDetails.getCustomerId();
        BaseQuestion question = this.questionService.getQuestion(id, customerId);
        SiteUser siteUser = this.userService.getUser(userDetails.getUsername());

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        Answer answer = this.answerService.create(
                id,
                customerId,          // questionType 대체용 (A/B 구분)
                answerForm.getContent(),
                siteUser
        );

        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestionId(), answer.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm,
                               @PathVariable("id") Long id,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return "answer_form";
        }

        Answer answer = this.answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.answerService.modify(answer, answerForm.getContent());

        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestionId(), answer.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Long id,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Answer answer = this.answerService.getAnswer(id);

        if (!answer.getAuthor().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.answerService.delete(answer);

        return String.format("redirect:/question/detail/%s", answer.getQuestionId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(@PathVariable("id") Long id,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(userDetails.getUsername());

        this.answerService.vote(answer, siteUser);

        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestionId(), answer.getId());
    }
}
