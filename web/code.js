/* jshint esversion: 6 */
window.onload

/* This code will trigger a search when enter is pressed */
var input = document.getElementById("searchbox");
input.addEventListener("keyup", function(event) {
  if (event.key === 'Enter') {
    event.preventDefault();
    document.getElementById("searchbutton").click();
  }
});

document.getElementById('searchbutton').onclick =  () => {
    var loader = document.getElementById("loader");
    document.getElementById("responsesize").innerHTML ="";
    loader.classList.add("show")
    fetch("/search?q=" + document.getElementById('searchbox').value)
        .then((response) => response.json())
        .then((data) => {   
            loader.classList.remove("show")
            var websiteString = data.length==1 ? "website" : "websites";
            document.getElementById("responsesize").innerHTML ="<p>" + data.length + " " + websiteString + " found</p>";
            document.getElementById("responsesize").style = "color: darkgreen;"
            let results = data.map((page) =>
                `<li><a href="${page.url}">${page.title}</a></li>`)
                .join("\n");
            document.getElementById("urllist").innerHTML = `<ul>${results}</ul>`;
            
        }).catch((err) => console.log("Sorry, no results found " + err));

        document.getElementById("urllist").innerHTML ="";
        document.getElementById("responsesize").style = "color: crimson;";
}

/*AutoComplete*/
/* import data from local txt file */
  var stringData = $.ajax({
    mode: 'cors',
    url: "autocomplete.txt",
    async: false
  }).responseText;

/* split values by new line */
var stringArray = stringData.split("\n");
// Using Array.filter to get unique values
const useFilter = stringArray => {
    return stringArray.filter((value, index, self) => {
      return self.indexOf(value) === index;
    });
  };
  var result = useFilter(stringArray);