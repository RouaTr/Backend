package Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FilenetDocService {
    //arraylist
    List<String> uploadToFileNet(List<MultipartFile> files, List<String> titles) throws Exception;
    InputStream downloadDocumentFromFileNet(String documentId) throws Exception;

    String getMimeType(String documentId) throws Exception; }

