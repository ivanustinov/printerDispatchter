package dispatcher.entities;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 13.10.2019
 */
public enum DocumentType {

    A1("A1", 40000, 594 * 841),
    A2("A2", 20000, 420 * 594),
    A3("A3", 10000, 297 * 420),
    A4("A4", 5000, 210 * 297);

    private String name;
    private Integer printDuration;
    private Integer paperSize;

    DocumentType(String name, Integer printDuration, Integer paperSize) {
        this.name = name;
        this.printDuration = printDuration;
        this.paperSize = paperSize;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getPrintDuration() {
        return printDuration;
    }

    public void setPrintDuration(Integer printDuration) {
        this.printDuration = printDuration;
    }


    public Integer getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(Integer paperSize) {
        this.paperSize = paperSize;
    }

}
