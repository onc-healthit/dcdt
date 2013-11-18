window.onload = function (event) {
    var banner = document.querySelector("div#bannerLeft"), bannerImg = banner.querySelector("img");
    
    banner.appendChild(document.createTextNode(bannerImg.alt));
};
