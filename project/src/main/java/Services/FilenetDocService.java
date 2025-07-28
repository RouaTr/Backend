package Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FilenetDocService {
    String uploadToFileNet(MultipartFile file, String title) throws Exception;
    InputStream downloadDocumentFromFileNet(String documentId) throws Exception;

    String getMimeType(String documentId) throws Exception; }

