package br.com.empresa.intranet.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String recursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("erro", "Recurso nao encontrado");
        model.addAttribute("mensagem", ex.getMessage());
        model.addAttribute("path", request.getRequestURI());
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String acessoNegado(AccessDeniedException ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("erro", "Acesso negado");
        model.addAttribute("mensagem", ex.getMessage());
        model.addAttribute("path", request.getRequestURI());
        return "error/403";
    }

    @ExceptionHandler({RegraNegocioException.class, ArquivoStorageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String erroNegocio(RuntimeException ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("erro", "Requisicao invalida");
        model.addAttribute("mensagem", ex.getMessage());
        model.addAttribute("path", request.getRequestURI());
        return "error/400";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String erroInterno(Exception ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("erro", "Erro interno");
        model.addAttribute("mensagem", "Ocorreu um erro inesperado. Tente novamente em instantes.");
        model.addAttribute("path", request.getRequestURI());
        return "error/500";
    }
}
