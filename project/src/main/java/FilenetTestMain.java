import Services.FilenetDocServiceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

public class FilenetTestMain {
    public static void main(String[] args) {
        try {
            String filePath = "C:/Users/Dell/Downloads/demandeStage.pdf"; // ← à adapter
            InputStream inputStream = new FileInputStream(filePath);


            MultipartFile multipartFile = new MockMultipartFile(
                    "file",
                    "demandeStage.pdf",
                    "application/pdf",
                    inputStream
            );


            FilenetDocServiceImpl service = new FilenetDocServiceImpl();


            String fileNetId = service.uploadToFileNet(multipartFile, "Test Document");


            System.out.println("Document enregistré dans FileNet avec ID : " + fileNetId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
