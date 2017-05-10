package cx.cad.scalajs.lightbox

import cx.cad.scalajs.lightbox.Image.{NoThumbnailAlt, NoThumbnailSrc}
import cx.cad.scalajs.lightbox.Lightbox.Names
import org.scalajs.dom
import org.scalajs.dom.ext.{PimpedNodeList => RichNodeList}
import org.scalajs.dom.html.{Anchor, Div}
import org.scalajs.dom.raw.{Event, Node}

import scala.scalajs.js.JSApp
import scalatags.JsDom
import scalatags.JsDom.all.{cls, href, _}

class Lightbox {
  val imageSets: Seq[ImageSet] = dom.document.getElementsByClassName(Names.class_imageSet).map(announceSet).map(ImageSet)

  private def announceSet(set: Node): Node = { println(s"Found an ${Names.class_imageSet}"); set }

  println("Inserting renders…")
  imageSets.map{ set => println(set); set }.foreach(_.insertRender())
}

case class ImageSet(imageSetDiv: Node) {
  lazy val images: Seq[Image] = {
    val asImages = imageSetDiv.childNodes.filter(onlyAnchors).map(Image(_))
    asTriplets(asImages).map(assignPrevNext).toSeq
  }

  private def assignPrevNext(seq: Seq[Image]): Image = {
    if(seq.size != 3) { throw new IllegalArgumentException(s"The sequence must be exactly three elements in the order: previous, current, next.")}
    seq match {
      case Seq(prev, current, next) =>
        current.previous = Some(prev)
        current.next = Some(next)
        current
    }
  }

  def asTriplets[A](seq: Seq[A]): Iterator[Seq[A]] = {
    (Seq(seq.last) ++ seq ++ Seq(seq.head)).sliding(3, 1)
  }

  private def onlyAnchors(child: Node) = { Names.tag_anchor.equals(child.nodeName) }

  def insertRender(): Node = {
    bindClicks()
    val r = render
    println(s"Appending ${r.toString}")
    imageSetDiv.appendChild(r)
  }

  def bindClicks(): Unit = {
    images.foreach { image =>
      def handler(e: Event): Boolean = {
        e.preventDefault()
        e.stopPropagation()
        dom.window.location.hash = image.idAsAttr
        false
      }
      image.anchor.addEventListener("click", handler, useCapture = true)
      val cls = dom.document.createAttribute("class")
      cls.value = "enhanced"
      image.anchor.attributes.setNamedItem(cls)
    }
  }

  def render: Node = {
    println(s"Rendering ${images.size} images…")
    div( images.map(ImageRenderer.render) ).render
  }

  override def toString: String = s"ImageSet with ${images.size} images: $images"
}
class Image(val anchor: Node,
            val id: Int,
            var previous: Option[Image] = None,
            var next: Option[Image] = None
           ) {
  val fullsize: String = anchor.attributes.getNamedItem("href").value
  val (thumbnail, title) = anchor.childNodes
    .find(theImageTag)
    .map(extractThumbnailAndTitle)
    .getOrElse((NoThumbnailSrc, NoThumbnailAlt))

  private def extractThumbnailAndTitle(imgTag: Node) = {
      println(s"Extracting thumbnail and title from an image")
      (imgTag.attributes.getNamedItem("src").value,
        imgTag.attributes.getNamedItem("alt").value)
  }

  private def theImageTag(child: Node) = {
    println(s"Evaluating anchor child ${child.nodeName}")
    Names.tag_img.equals(child.nodeName)
  }

  def idAsAttr: String = s"image-$id"

  override def toString: String = s"Image(title = $title, thumbnail = $thumbnail, fullsize = $fullsize)"
}

object Image {
  val NoThumbnailAlt = ""
  def NoThumbnailSrc: String = { dom.window.alert("No thumbnail found"); "" }
  var count = 0
  def apply(anchor: Node): Image = {
    count += 1
    new Image(anchor, count)
  }
}

object ImageRenderer {
  def render(image: Image): Div = {
    val imageDiv =
      div(cls := Names.class_overlay, id := idAttrFor(image),
        img(src := image.fullsize, alt := image.title),
        div(
          h3(image.title),
          p(image.title),
          prevLinkFor(image),
          nextLinkFor(image)
        ),
        closeLink
      )

    imageDiv.render
  }

  def idAttrFor(image: Image): String = { image.idAsAttr }

  def prevLinkFor(image: Image): JsDom.TypedTag[Anchor] = link(s"#${image.previous.map(idAttrFor).head}", Names.class_prev, s"Prev: ${image.previous.map(_.title).head}")
  def nextLinkFor(image: Image): JsDom.TypedTag[Anchor] = link(s"#${image.next.map(idAttrFor).head}", Names.class_next, s"Next: ${image.next.map(_.title).head}")
  def closeLink: JsDom.TypedTag[Anchor] = link("#page", Names.class_close, "Close")

  def link(lhref: String, lcls: String, ltext: String): JsDom.TypedTag[Anchor] = a(href := lhref, cls := lcls, ltext)

}

object Lightbox extends JSApp{
  object Names {
    val class_imageSet = "image_set"
    val class_overlay = "lb-overlay"
    val class_next = "lb-next"
    val class_prev = "lb-prev"
    val class_close = "lb-close"
    val tag_anchor = "A"
    val tag_img = "IMG"
  }
  override def main(): Unit = {
    println("Initializing lightbox…")
    dom.window.addEventListener("load", initializeLightbox)
  }

  def initializeLightbox(loadEvent: Event): Unit = {
    val lightbox = new Lightbox
  }
}