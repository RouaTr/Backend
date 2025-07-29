import Services.FilenetDocServiceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FilenetTestMain {
    public static void main(String[] args) {
        try {
            // Liste des chemins vers les fichiers PDF que tu veux uploader
            String[] paths = {
                    "C:/Users/Dell/Downloads/demandeStage.pdf",
                    "C:/Users/Dell/Downloads/candidature_mastere_isitcom.pdf",
                    "C:/Users/Dell/Downloads/identité.jpg",
                    "C:/Users/Dell/Downloads/article_MEDIVIH.pdf",
            };

            List<MultipartFile> files = new ArrayList<>();
            List<String> titles = new ArrayList<>();

            for (String path : paths) {
                InputStream inputStream = new FileInputStream(path);
                String fileName = path.substring(path.lastIndexOf('/') + 1);

                MultipartFile multipartFile = new MockMultipartFile(
                        "file",
                        fileName,
                        "application/pdf",
                        inputStream
                );

                files.add(multipartFile);
                titles.add("Titre de " + fileName);
            }

            FilenetDocServiceImpl service = new FilenetDocServiceImpl();
            List<String> fileNetIds = service.uploadToFileNet(files, titles);

            for (String id : fileNetIds) {
                System.out.println("Document enregistré avec ID : " + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
