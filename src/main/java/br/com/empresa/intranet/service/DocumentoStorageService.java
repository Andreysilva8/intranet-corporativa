package br.com.empresa.intranet.service;

import br.com.empresa.intranet.exception.ArquivoStorageException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentoStorageService {

    private final Path diretorioDocumentos;

    public DocumentoStorageService(@Value("${app.storage.documents-path}") String documentsPath) {
        this.diretorioDocumentos = Paths.get(documentsPath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void inicializar() {
        try {
            Files.createDirectories(diretorioDocumentos);
        } catch (IOException ex) {
            throw new ArquivoStorageException("Nao foi possivel criar o diretorio de documentos", ex);
        }
    }

    public String armazenar(MultipartFile arquivo) {
        validarArquivo(arquivo);

        String nomeOriginal = limparNomeOriginal(arquivo);
        String nomeArmazenado = UUID.randomUUID() + extrairExtensao(nomeOriginal);
        Path destino = resolverComSeguranca(nomeArmazenado);

        try (InputStream inputStream = arquivo.getInputStream()) {
            Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
            return nomeArmazenado;
        } catch (IOException ex) {
            throw new ArquivoStorageException("Nao foi possivel armazenar o arquivo", ex);
        }
    }

    public Resource carregarComoResource(String nomeArquivoArmazenado) {
        Path arquivo = resolverComSeguranca(nomeArquivoArmazenado);

        try {
            Resource resource = new UrlResource(arquivo.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new ArquivoStorageException("Arquivo nao encontrado ou sem permissao de leitura");
        } catch (MalformedURLException ex) {
            throw new ArquivoStorageException("Caminho de arquivo invalido", ex);
        }
    }

    public void excluir(String nomeArquivoArmazenado) {
        Path arquivo = resolverComSeguranca(nomeArquivoArmazenado);

        try {
            Files.deleteIfExists(arquivo);
        } catch (IOException ex) {
            throw new ArquivoStorageException("Nao foi possivel excluir o arquivo", ex);
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoStorageException("Arquivo vazio ou nao informado");
        }
    }

    private String limparNomeOriginal(MultipartFile arquivo) {
        String nomeOriginal = Objects.requireNonNullElse(arquivo.getOriginalFilename(), "documento");
        String nomeLimpo = StringUtils.cleanPath(nomeOriginal);

        if (!StringUtils.hasText(nomeLimpo) || nomeLimpo.contains("..")) {
            throw new ArquivoStorageException("Nome de arquivo invalido");
        }

        return nomeLimpo;
    }

    private String extrairExtensao(String nomeArquivo) {
        int indicePonto = nomeArquivo.lastIndexOf('.');
        if (indicePonto < 0 || indicePonto == nomeArquivo.length() - 1) {
            return "";
        }
        return nomeArquivo.substring(indicePonto).toLowerCase();
    }

    private Path resolverComSeguranca(String nomeArquivo) {
        Path caminho = diretorioDocumentos.resolve(nomeArquivo).normalize();
        if (!caminho.startsWith(diretorioDocumentos)) {
            throw new ArquivoStorageException("Tentativa de acesso fora do diretorio de documentos");
        }
        return caminho;
    }
}
