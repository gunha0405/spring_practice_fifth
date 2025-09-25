package com.example.comment.controller;

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
import org.springframework.web.server.ResponseStatusException;

import com.example.comment.model.Comment;
import com.example.comment.model.dto.CommentForm;
import com.example.comment.service.CommentService;
import com.example.user.model.CustomUserDetails;
import com.example.user.model.SiteUser;
import com.example.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/question/{id}")
    public String createQuestionComment(CommentForm commentForm, Model model,
                                        @PathVariable("id") Long id) {
        model.addAttribute("actionUrl", "/comment/create/question/" + id);
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/answer/{id}")
    public String createAnswerComment(CommentForm commentForm, Model model,
                                      @PathVariable("id") Long id) {
        model.addAttribute("actionUrl", "/comment/create/answer/" + id);
        return "comment_form";
    }

    @PostMapping("/create/question/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createCommentForQuestion(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal CustomUserDetails userDetails,
                                           @Valid CommentForm form,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        String customerId = userDetails.getCustomerId();
        SiteUser user = this.userService.getUser(userDetails.getUsername());
        commentService.createForQuestion(id, customerId, user, form.getContent());

        return "redirect:/question/detail/" + id;
    }

    @PostMapping("/create/answer/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createCommentForAnswer(@PathVariable("id") Long id,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @Valid CommentForm form,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        SiteUser user = this.userService.getUser(userDetails.getUsername());
        commentService.createForAnswer(id, user, form.getContent());

        Long questionId = commentService.getAnswerQuestionId(id);
        return "redirect:/question/detail/" + questionId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyComment(CommentForm commentForm,
                                @PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Comment c = this.commentService.getComment(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        if (!c.getAuthor().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        commentForm.setContent(c.getContent());
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentForm commentForm,
                                BindingResult bindingResult,
                                @PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }

        Comment c = this.commentService.getComment(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        if (!c.getAuthor().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        c = this.commentService.modify(c, commentForm.getContent());
        return "redirect:/question/detail/" + c.getQuestionId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Comment c = this.commentService.getComment(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        if (!c.getAuthor().getUsername().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.commentService.delete(c);
        return "redirect:/question/detail/" + c.getQuestionId();
    }
}
