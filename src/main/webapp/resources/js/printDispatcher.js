function print() {
    if (!validate()) {
        return false;
    }
    var document = {};
    var array = $('form').serializeArray();
    for (var i = 0; i < array.length; i++) {
        document[array[i].name] = array[i].value;
    }
    var doc = JSON.stringify(document);
    $.ajax({
        type: "POST",
        url: "controller",
        data: {
            action: "add",
            doc: doc
        }
    }).done(function (data) {
        var s = JSON.parse(data);
        $('#result').html(s.result);
        if (s.documents !== null) {
            createTable(s.documents);
        }
        $('form')[0].reset();
    });
}


function resumePrinting() {
    $.ajax({
        type: "POST",
        url: "controller",
        data: {
            action: "continue"
        }
    }).done(function (data) {
        $('#toPrint').hide();
        $('#printed').hide();
        $('#result').html(data);
    });
}

function getAverageTime() {
    $.ajax({
        url: "controller",
        method: "POST",
        data: {
            action: "getAverageTime"
        }
    }).done(function (data) {
        $('#result').html(data);
    });
}

function cancel(documentId) {
    $.ajax({
        url: "controller",
        method: "POST",
        data: {
            action: "remove",
            id: documentId
        }
    }).done(function (data) {
        var documents = JSON.parse(data);
        createTable(documents);
    });
}

function createTable(documents) {
    if (documents.length === 0) {
        $('#toPrint').html('');
        $('#result').append('<p> There are no documents left</p>');
    } else {
        var table = '<caption style="text-align: center; color: black">Documents left to print:</caption>' +
            '<thead >' +
            '<tr>' +
            '<th style="text-align: center">№</th>' +
            '<th style="text-align: center">NAME</th>' +
            '<th style="text-align: center">TYPE</th>' +
            '<th style="text-align: center">ACTIONS</th>' +
            '</tr>' +
            '</thead>' +
            '<tbody>';
        var a = 1;
        for (var i = 0; i !== documents.length; i++) {
            table += '<tr id = ' + a + '>' +
                '<td>' + a + '</td>' +
                '<td>' + documents[i].name + '</td>' +
                '<td>' + documents[i].documentType + '</td>' +
                '<td><button type="button" class="btn btn-primary" onclick="cancel(' + (a-1) + ')">Remove</button></td>' +
                '</tr>';
            a++;
        }
        table += '</tbody>';
        $('#toPrint').html(table);
    }
}

function getDocumentsToPrint() {
    $.ajax({
        url: "controller",
        method: "POST",
        data: {
            action: "stopPrinter"
        }
    }).done(function (data) {
        $('#result').html('Printer stopped');
        $('#toPrint').show();
        var documents = JSON.parse(data);
        createTable(documents);
    });
}

function getPrinted() {
    var sort = $("[name='sortedBy']").val();
    $.ajax({
        url: "controller",
        method: "POST",
        data: {
            action: "getSorted",
            sortedBy: sort
        }
    }).done(function (data) {
        var documents = JSON.parse(data);
        var table = '<caption style="text-align: center; color: black">Printed documents:</caption>' +
            '<thead>' +
            '<tr style="text-align: center; color: black">' +
            '<th style="text-align: center">№</th>' +
            '<th style="text-align: center">NAME</th>' +
            '<th style="text-align: center">TYPE</th>' +
            '</tr>' +
            '</thead>' +
            '<tbody>';
        if (documents.length === 0) {
            $('#printed').html('');
            $('#result').html('There are no printed documents');
        } else {
            var a = 1;
            for (var i = 0; i !== documents.length; i++) {
                table += '<tr>'
                    + '<td>' + a + '</td>'
                    + '<td>' + documents[i].name + '</td>'
                    + '<td>' + documents[i].documentType + '</td>'
                    + '</tr>';
                a++;
            }
            table += '</tbody>';
            $('#printed').show();
            $('#printed').html(table);
        }
    });
}

function validate() {
    var isEmpty = true;
    var names = $('div [name]');
    for (var i = 0; i !== names.length; i++) {
        var input = names[i];
        if (input.value === '') {
            alert($(input).attr('placeholder'));
            isEmpty = false;
        }
    }
    return isEmpty;
}