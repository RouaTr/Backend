package Repository;

import com.backend.project.Entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository  extends JpaRepository<Document, Long> {
}
