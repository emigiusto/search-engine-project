/* jshint esversion: 6 */


/* This code will trigger a search when enter is pressed */
var input = document.getElementById("searchbox");
input.addEventListener("keyup", function(event) {
  if (event.key === 'Enter') {
   event.preventDefault();
   document.getElementById("searchbutton").click();
  }
});

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
                    document.getElementById("toTop").style=
                    "display: block;";
                    
                    
            }
            let results = data.map((page) =>
                `<li><a href="${page.url}">${page.title}</a></li>`)
                .join("\n");
            document.getElementById("urllist").innerHTML = `<ul>${results}</ul>`;
            

        }).catch(() => console.log("Sorry, no results found"));

             document.getElementById("urllist").innerHTML ="";
                document.getElementById("responsesize").innerHTML =
                    "<p>Sorry, no results found</p>";
                    document.getElementById("responsesize").style =
                    "color: crimson;";
                    
                       
};

/*AutoComplete*/


  var stringData = $.ajax({
    mode: 'cors',
    url: "autocomplete.txt",
    async: false
 }).responseText;

//Split values of string data
var stringArray = stringData.split("\n");
// alert("Data Loaded: " + stringData);


// Using Array.filter
const useFilter = stringArray => {
    return stringArray.filter((value, index, self) => {
      return self.indexOf(value) === index;
    });
  };
   
  var result = useFilter(stringArray);
  





