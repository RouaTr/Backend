package Services;

import org.springframework.web.multipart.MultipartFile;
import javax.security.auth.Subject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.util.UserContext;
import com.filenet.api.core.ObjectStore;
import org.springframework.stereotype.Service;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;

@Service
public class FilenetDocServiceImpl implements FilenetDocService{

    @Override
    public List<String> uploadToFileNet(List<MultipartFile> files, List<String> titles) throws Exception {
        List<String> documentIds = new ArrayList<>();

        Connection conn = Factory.Connection.getConnection("http://192.168.56.101:9080/wsi/FNCEWS40MTOM/");
        Subject subject = UserContext.createSubject(conn, "GCD Administrator", "P@ssw0rd", null);
        UserContext.get().pushSubject(subject);

        try {
            Domain domain = Factory.Domain.fetchInstance(conn, null, null);
            ObjectStore os = Factory.ObjectStore.fetchInstance(domain, "DemoObjectStore", null);

            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String title = titles.get(i);

                Document doc = Factory.Document.createInstance(os, ClassNames.DOCUMENT);
                doc.getProperties().putValue("DocumentTitle", title);
                doc.set_MimeType(file.getContentType());

                ContentTransfer ct = Factory.ContentTransfer.createInstance();
                InputStream inputStream = file.getInputStream();
                ct.setCaptureSource(inputStream);
                ContentElementList contentList = Factory.ContentElement.createList();
                contentList.add(ct);
                doc.set_ContentElements(contentList);

                doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
                doc.save(RefreshMode.REFRESH);

                documentIds.add(doc.get_Id().toString());
            }
        } finally {
            UserContext.get().popSubject();
        }

        return documentIds;
    }

    @Override
    public InputStream downloadDocumentFromFileNet(String documentId) throws Exception {
        Connection conn = Factory.Connection.getConnection("http://192.168.56.101:9080/wsi/FNCEWS40MTOM/");
        Subject subject = UserContext.createSubject(conn, "GCD Administrator", "P@ssw0rd", null);
        UserContext uc = UserContext.get();
        uc.pushSubject(subject);

        try {
            Domain domain = Factory.Domain.fetchInstance(conn, null, null);
            ObjectStore os = Factory.ObjectStore.fetchInstance(domain, "DemoObjectStore", null);

            Document doc = Factory.Document.fetchInstance(os, documentId, null);
            ContentElementList contentList = doc.get_ContentElements();

            if (contentList == null || contentList.isEmpty()) {
                throw new Exception("Aucun contenu trouvé pour le document ID " + documentId);
            }

            ContentTransfer ct = (ContentTransfer) contentList.get(0);
            return ct.accessContentStream();

        } catch (Exception e) {
            throw e;
        } finally {
            uc.popSubject();
        }
    }
    @Override
    public String getMimeType(String documentId) throws Exception {
        Connection conn = Factory.Connection.getConnection("http://192.168.56.101:9080/wsi/FNCEWS40MTOM/");
        Subject subject = UserContext.createSubject(conn, "GCD Administrator", "P@ssw0rd", null);
        UserContext.get().pushSubject(subject);

        Domain domain = Factory.Domain.fetchInstance(conn, null, null);
        ObjectStore os = Factory.ObjectStore.fetchInstance(domain, "DemoObjectStore", null);

        Document doc = Factory.Document.fetchInstance(os, documentId, null);
        String mimeType = doc.get_MimeType();

        UserContext.get().popSubject();

        return mimeType;
    }
}
