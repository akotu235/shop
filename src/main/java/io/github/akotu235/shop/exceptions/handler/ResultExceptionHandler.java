package io.github.akotu235.shop.exceptions.handler;


import io.github.akotu235.shop.exceptions.AccessDeniedException;
import io.github.akotu235.shop.exceptions.AppException;
import io.github.akotu235.shop.exceptions.ImageNotFoundException;
import io.github.akotu235.shop.exceptions.InvalidAddressException;
import io.github.akotu235.shop.result.Result;
import io.github.akotu235.shop.service.theme.ThemeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@ControllerAdvice
public class ResultExceptionHandler {
    private final ThemeService themeService;
    private final TemplateEngine templateEngine;

    public ResultExceptionHandler(ThemeService themeService, TemplateEngine templateEngine) {
        this.themeService = themeService;
        this.templateEngine = templateEngine;
    }

    @ExceptionHandler(InvalidAddressException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleInvalidAddressException(AppException ex, HttpServletRequest request) {
        return getFailureResultModelAndView(ex, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessException(AppException ex, HttpServletRequest request) {
        return getFailureResultModelAndView(ex, request);
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleAppException(AppException ex, HttpServletRequest request) {
        return getFailureResultModelAndView(ex, request);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleImageNotFoundException(HttpServletRequest request, Locale locale) {
        Context context = new Context(locale);

        context.setVariable("theme", themeService.getThemeFromCookie(request));

        String svgContent = templateEngine.process("fragments/default-image", context);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("image/svg+xml"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"not-found.svg\"")
                .body(svgContent);
    }

    private ModelAndView getFailureResultModelAndView(AppException ex, HttpServletRequest request) {
        Result<?> result = new Result<>(false, null, ex.getMessage(), ex.getArgs());
        ModelAndView modelAndView = new ModelAndView("result");
        modelAndView.addObject("result", result);
        getTheme(modelAndView, request);
        return modelAndView;
    }

    private void getTheme(ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView.addObject("theme", themeService.getThemeFromCookie(request));
    }
}