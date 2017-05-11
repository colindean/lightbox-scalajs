# Lightbox-ScalaJS

This script dynamically creates a lightbox based on
[Tympanus CSS3 Lightbox Demo #3](https://tympanus.net/Tutorials/CSS3Lightbox/index3.html).

See `index-dev.html` for an example of the HTML structure to use. Include the JavaScript in your page
and it will find the divs with class of `image_set`. It will use the contents then to create the rest of
 the lightbox infrastructure.

## Using

Get a release from the [releases page](https://github.com/colindean/lightbox-scalajs/releases) and copy it locally. Include something like this in your HTML, probably at the bottom:

    <script src="lightbox-scala-js-opt.js" async></script>

then look at `style.css` to get the styles and `index-dev.html` to see the HTML
structure of the `image_set` divs.

## History

[Brigette Lefever](https://github.com/brareme) wanted a lightbox for her website, a single page, static HTML
site without having to manually maintain a large amount of HTML. She was OK with including JavaScript but was
not quite comfortable enough yet with the language to code it up herself. I jumped at the opportunity to try
Scala.js for a project!


