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




var availableTags =  [
"United States",
"the",
"united",
"states",
"of",
"america",
"usa",
"commonly",
"referred",
"to",
"as",
"us",
"or",
"is",
"a",
"federal",
"republic",
"composed",
"district",
"five",
"major",
"selfgoverning",
"territories",
"and",
"various",
"possessionsfn",
"contiguous",
"are",
"in",
"central",
"north",
"between",
"canada",
"mexico",
"with",
"state",
"alaska",
"northwestern",
"part",
"hawaii",
"comprising",
"an",
"archipelago",
"midpacific",
"scattered",
"about",
"pacific",
"ocean",
"caribbean",
"sea",
"at",
"million",
"square",
"miles",
"km",
"over",
"people",
"worlds",
"third",
"largest",
"country",
"by",
"total",
"area",
"fourth",
"land",
"areafn",
"most",
"populous",
"it",
"one",
"ethnically",
"diverse",
"multicultural",
"nations",
"product",
"largescale",
"immigration",
"from",
"many",
"other",
"countries",
"geography",
"climate",
"also",
"extremely",
"home",
"wide",
"variety",
"wildlife",
"*PAGE:https://en.wikipedia.org/wiki/Denmark",
"Denmark",
"denmark",
"listenidnmrk",
"danish",
"danmark",
"dnm",
"listen",
"scandinavian",
"europe",
"southernmost",
"smallest",
"nordic",
"southwest",
"sweden",
"south",
"norwayn",
"bordered",
"germany",
"kingdom",
"denmarkn",
"sovereign",
"that",
"comprises",
"propern",
"two",
"autonomous",
"constituent",
"atlantic",
"faroe",
"islands",
"greenland",
"has",
"kilometres",
"sq",
"mi",
"population",
"consists",
"peninsula",
"jutland",
"named",
"which",
"around",
"inhabited",
"zealand",
"featuring",
"capital",
"city",
"copenhagen",
"characterised",
"flat",
"arable",
"sandy",
"coasts",
"low",
"elevation",
"temperate",
"world",
"happiness",
"report",
"tied",
"happiest",
"*PAGE:https://en.wikipedia.org/wiki/Japan",
"Japan",
"japan",
"island",
"east",
"asia",
"located",
"lies",
"china",
"korea",
"russia",
"stretching",
"okhotsk",
"taiwan",
"kanji",
"make",
"up",
"japans",
"name",
"mean",
"sun",
"origin",
"often",
"called",
"rising",
"*PAGE:https://en.wikipedia.org/wiki/Copenhagen",
"Copenhagen",
"ipa",
"ˌkoʊpənˈheɪɡən",
"ˈkuːpənhɑːɡən",
"københavn",
"kʰøbmˈhɑʊˀn",
"this",
"sound",
"populated",
"municipal",
"larger",
"urban",
"january",
"metropolitan",
"just",
"inhabitants",
"situated",
"on",
"eastern",
"coast",
"another",
"small",
"portion",
"amager",
"separated",
"malmö",
"strait",
"Øresund",
"*PAGE:https://en.wikipedia.org/wiki/University_of_Copenhagen",
"University of Copenhagen",
"university",
"ucph",
"københavns",
"universitet",
"oldest",
"research",
"institution",
"founded",
"studium",
"generale",
"second",
"for",
"higher",
"education",
"scandinavia",
"after",
"uppsala",
"undergraduate",
"students",
"postgraduate",
"doctoral",
"employees",
"four",
"campuses",
"headquarters",
"courses",
"taught",
"however",
"offered",
"english",
"few",
"german",
"several",
"thousands",
"foreign",
"half",
"whom",
"come",
"*PAGE:https://en.wikipedia.org/wiki/IT_University_of_Copenhagen",
"IT University of Copenhagen",
"globally",
"oriented",
"independent",
"was",
"established",
"time",
"ithøjskolen",
"when",
"new",
"law",
"passed",
"officially",
"appointed",
"twelfth",
"therefore",
"changed",
"its",
"ituniversitetet",
"i",
  ];




