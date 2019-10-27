<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>printDispatcher</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="resources/js/printDispatcher.js"></script>
</head>
<body style="text-align: center; margin-top: 50px">
<fieldset>
    <div class="container">
        <h2>Printer Dispather</h2>
        <p>Insert name and select type of the document before printing</p>
        <form id="myForm" class="form-inline">
            <label for="name">Document Name:</label>
            <input class="form-control" id="name" name="name" placeholder="Enter document name">
            <label for="documentType">DocumentType:</label>
            <select class="form-control" name="documentType" id="documentType">
                <option selected="selected" value="A4">A4</option>
                <option value="A3">A3</option>
                <option value="A2">A2</option>
                <option value="A1">A1</option>
            </select>
            <button type="button" class="btn btn-primary" onclick="print()">Print</button>
        </form>
    </div>
    <div class="container" style=" margin-top: 20px">
        <button type="button" class="btn btn-primary" onclick="getDocumentsToPrint()">STOP PRINTING</button>
        <button type="button" class="btn btn-primary" onclick="resumePrinting()">CONTINUE</button>
    </div>
    <div style="margin-top: 10px" align="center" id="result"></div>
    <div class="container">
        <table class="table table-bordered" style="text-align: center" id="toPrint"></table>
    </div>
    <div class="form-inline" style=" margin-top: 20px">
        <button type="button" class="btn btn-primary" onclick="getPrinted()">GetPrinted</button>
        <label for="sortedBy">SortedBy:</label>
        <select class="form-control" name="sortedBy" id="sortedBy" onchange="getPrinted()">
            <option selected="selected" value="Order">Print Order</option>
            <option value="printDuration">Print Duration</option>
            <option value="paperSize">Paper Size</option>
            <option value="documentType">Document Type</option>
        </select>
        <button type="button" class="btn btn-primary" onclick="getAverageTime()">GetAverageTime</button>
    </div>
    <div class="container">
        <table class="table table-bordered" style="text-align: center" id="printed"></table>
    </div>
    <br/>
</fieldset>
</body>
</html>
