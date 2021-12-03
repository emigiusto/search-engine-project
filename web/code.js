/* jshint esversion: 6 */
document.getElementById('searchbutton').onclick =  () => {
   
    fetch("/search?q=" + document.getElementById('searchbox').value)
        .then((response) => response.json())
        .then((data) => {
        
            if (data.length == 1) {
                document.getElementById("responsesize").innerHTML =

                    "<p>" + data.length + " website found</p>";
                    document.getElementById("responsesize").style =
                    "color: darkgreen;"
            }
            else {
                document.getElementById("responsesize").innerHTML =

                    "<p>" + data.length + " websites found</p>";
                    
                    document.getElementById("responsesize").style =
                    "color: darkgreen;"
                    
            }
            let results = data.map((page) =>
                `<li><a href="${page.url}">${page.title}</a></li>`)
                .join("\n");
            document.getElementById("urllist").innerHTML = `<ul>${results}</ul>`;

        });

    if (document.getElementById("urllist").innerHTML == "") {
        document.getElementById("responsesize").innerHTML =
            "<p>Sorry, no results found</p>";
            document.getElementById("responsesize").style =
            "color: crimson;"
       
           

    }
};