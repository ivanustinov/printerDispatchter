package dispatcher.entities;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 13.10.2019
 */
public class Document {
    private String name;
    private DocumentType documentType;

    public Document(String name, DocumentType documentType) {
        this.name = name;
        this.documentType = documentType;
    }

    public Document() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

}
