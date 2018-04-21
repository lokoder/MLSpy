$(document).ready(function() {
    
    $("#frmsearch").submit(function(event) { 
        
        $.ajax({
            method: 'POST',
            url: 'Search',
            timeout: 1000*120,
            data: {
                keyword: $("input[name=keyword]").val()
            }
        }).done(function(data) {
            
            console.log(data);
            
            var json = $.parseJSON(data);
            
            var table = $('<table>').attr('id', 'result').addClass('display').appendTo('body');
            var thead = $('<thead>').appendTo(table);
             
                var tr = $('<tr>').append(
                    $('<th>').text("id"),
                    $('<th>').text("nome"),
                    $('<th>').text("preco"),
                    $('<th>').text("vendidos"),
                    //$('<th>').text("link"),
                    $('<th>').text("nomeVendedor")
                ).appendTo(thead);            
        
            var tbody = $('<tbody>').appendTo(table);
            
            $.each(json, function(index, item) {
                var tr = $('<tr>').append(
                    $('<td>').text(item.id),
                    $('<td>').append($('<a>', {href: item.link}).text(item.nome)), 
                    $('<td>').text(item.preco),
                    $('<td>').text(item.qtdeVendidos),
                    //$('<td>').text(item.link.substring(0,50)),
                    $('<td>').text(item.nomeVendedor)
                ).appendTo(table); 
        
                console.log(tr);
            });
            
            $(table).DataTable({
                'language': {
                    'decimal': ",",
                    'thousands': "."
                }
            });
            
        }).fail(function(data) {
            console.log("Erro em frmsearch.submit: " + data);
        });
        
        event.preventDefault();
    });

});
