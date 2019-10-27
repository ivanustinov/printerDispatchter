package dispatcher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dispatcher.entities.Document;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * //TODO add comments.
 *
 * @author Ivan Ustinov(ivanustinov1985@yandex.ru)
 * @version 1.0
 * @since 13.10.2019
 */
public class Service {

    private final static Service INSTANCE = new Service();

    private BlockingQueue<Document> documentsToPrint = new LinkedBlockingQueue<>();

    private List<Document> documentsPrinted = new ArrayList<>();

    private List<Document> documentsBuffer = new ArrayList<>();

    private Map<String, Consumer<HttpServletRequest>> actions = new HashMap<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    private volatile Boolean isPrinting = true;


    private Service() {
        init();
        actions.put("add", addDocument());
        actions.put("remove", removeDocument());
        actions.put("stopPrinter", stopPrinting());
        actions.put("getSorted", getSorted());
        actions.put("getAverageTime", getAverageTime());
        actions.put("continue", continuePrinting());
    }

    private void init() {
        Thread printing = new Thread(() -> {
            try {
                while (true) {
                    if (!isPrinting) {
                        synchronized (this) {
                            wait();
                        }
                    }
                    Document document = documentsToPrint.take();
                    documentsPrinted.add(document);
                    long time = System.currentTimeMillis();
                    Thread.sleep(document.getDocumentType().getPrintDuration());
                    System.out.println("End printing " + document.getName() + ", duration: " + (System.currentTimeMillis() - time));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        printing.start();
    }

    private Consumer<HttpServletRequest> getSorted() {
        return request -> {
            String sortedBy = request.getParameter("sortedBy");
            if ("documentType".equals(sortedBy)) {
                documentsPrinted.sort(Comparator.comparing(document -> document.getDocumentType().getName()));
            } else if ("printDuration".equals(sortedBy)) {
                documentsPrinted.sort(Comparator.comparing(document -> document.getDocumentType().getPrintDuration()));
            } else if ("paperSize".equals(sortedBy)) {
                documentsPrinted.sort(Comparator.comparing(document -> document.getDocumentType().getPaperSize()));
            }
            try {
                String printed = objectMapper.writeValueAsString(documentsPrinted);
                request.setAttribute("result", printed);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public void doAction(String action, HttpServletRequest request) {
        actions.get(action).accept(request);
    }


    private Consumer<HttpServletRequest> addDocument() {
        return request -> {
            String json = request.getParameter("doc");
            Document document = null;
            try {
                document = objectMapper.readValue(json, Document.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (document != null) {
                if (isPrinting) {
                    documentsToPrint.offer(document);
                    request.setAttribute("result", "{\"result\":\"Document " + document.getName() + " was added to print\", \"documents\":null }");
                } else {
                    try {
                        documentsBuffer.add(document);
                        String documents = objectMapper.writeValueAsString(documentsBuffer);
                        request.setAttribute("result", "{\"result\":\"Document " + document.getName() + " was added to print\", \"documents\":" + documents + "}");
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private Consumer<HttpServletRequest> removeDocument() {
        return request -> {
            int id = Integer.parseInt(request.getParameter("id"));
            documentsBuffer.remove(id);
            try {
                String documents = objectMapper.writeValueAsString(documentsBuffer);
                request.setAttribute("result", documents);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        };
    }

    private Consumer<HttpServletRequest> getAverageTime() {
        return request -> {
            int count = 0;
            int totalTime = 0;
            for (Document document : documentsPrinted) {
                count++;
                totalTime += document.getDocumentType().getPrintDuration();
            }
            int result = totalTime / 1000 / count;
            request.setAttribute("result", "The average time of printing each document is: " + result + " sc");
        };
    }

    private Consumer<HttpServletRequest> stopPrinting() {
        return request -> {
            try {
                isPrinting = false;
                documentsBuffer.addAll(documentsToPrint);
                documentsToPrint.clear();
                String json = objectMapper.writeValueAsString(documentsBuffer);
                request.setAttribute("result", json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private Consumer<HttpServletRequest> continuePrinting() {
        return request -> {
            isPrinting = true;
            documentsToPrint.addAll(documentsBuffer);
            documentsBuffer.clear();
            synchronized (this) {
                notify();
            }
            request.setAttribute("result", "Resume printing");
        };
    }


    public static Service getInstanse() {
        return INSTANCE;
    }

}
