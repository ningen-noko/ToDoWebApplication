package org.github.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // для получения страниц
@ControllerAdvice // не явл контроллером
public class GlobalExceptionHandler implements ErrorController {

    @RequestMapping("/error")
    public String redirectToErrorPage(HttpServletResponse response) {
        return switch (HttpStatus.valueOf(response.getStatus())) {
            case FORBIDDEN -> "redirect:/error/403";
            case NOT_FOUND -> "redirect:/error/404";
            default -> "redirect:/error/500";
        };
    }

    // по url /error уже есть котнтроллер по умолчанию, чтобы его убрать наследуем от интерфейса ErrorController
    @RequestMapping("/error/500")
    public String getCommonErrorPage() {
        return "public/error/common-error-page";
    }
    @RequestMapping("/error/403")
    public String getForbiddenErrorPage() {
        return "public/error/forbidden-error-page";
    }
    @RequestMapping("/error/404")
    public String getNotFoundErrorPage() {
        return "public/error/not-found-error-page";
    }

    @ExceptionHandler(Throwable.class)
    public String handleThrowable(Throwable throwable) {
        return "redirect:/error/500";
    }
}
