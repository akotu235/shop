package io.github.akotu235.shop.exceptions.handler;


import io.github.akotu235.shop.exceptions.AccessDeniedException;
import io.github.akotu235.shop.exceptions.AppException;
import io.github.akotu235.shop.exceptions.InvalidAddressException;
import io.github.akotu235.shop.result.Result;
import io.github.akotu235.shop.service.theme.ThemeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ResultExceptionHandler {
    private final ThemeService themeService;

    public ResultExceptionHandler(ThemeService themeService) {
        this.themeService = themeService;
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