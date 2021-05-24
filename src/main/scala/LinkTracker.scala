import scala.collection.Seq
import scala.xml._
import scala.xml.transform._

/**
 * Link Tracking
 *
 * Given an HTML document as Scala XML:
 *
 * For each a element with an href attribute or img element with a src attribute
 * in the document if the attribute value contains "cust" but not "internal"
 * replace it with a unique numeric ID.  The IDs should start at 0 and increment
 * by 7.  If the path matches one we've already seen reuse the old ID.
 *
 * Return the updated HTML document along with a Map of IDs to paths wrapped in
 * a PathTrackerResult.
 */

// https://github.com/scala/scala-xml/wiki/Getting-started
object LinkTracker {

  private def findPaths(doc: Elem, element: String, attribute: String): Seq[String] =
    for {
      node <- doc \\ element
      elem <- node \ attribute
    } yield elem.text

  private def createMap(id: Int, seq: Seq[String]): Map[Int, String] =
    seq
      .filter(value => !value.contains("internalcustonly") && value.contains("customers"))
      .map(value => id -> value)
      .foldLeft(Map[Int, String]()){ case (map, (key, value)) => map + (key -> value)}

  private val replacePathById: RewriteRule = new RewriteRule {
    override def transform(n: Node): Seq[Node] = n match {
      case e @ <a>{_*}</a> =>
        e.asInstanceOf[Elem] % Attribute(null, "href", 0.toString, Null)
      case e @ <img>{_*}</img> if (e \@ "src") == "/img/happycustomers.jpg" =>
        e.asInstanceOf[Elem] % Attribute(null, "src", 7.toString, Null)
      case _ => n
    }
  }

  private def getEncodedPaths(aHrefDistinct: Seq[String], imgSrcDistinct: Seq[String]): Map[Int, String] =
    createMap(0, aHrefDistinct) ++ createMap(7, imgSrcDistinct)

  private def getUniquePath(ahrefDistinct: Seq[String], imgSrcDistinct: Seq[String]): Int =
    ahrefDistinct.size + imgSrcDistinct.size

  private def getTrackedDoc(doc: Elem): collection.Seq[Node] = new RuleTransformer(replacePathById).transform(doc)

  def parseDoc(doc: Elem): PathTrackerResult = {

    val ahref      = findPaths(doc, "a", "@href")
    val imgSrc     = findPaths(doc, "img", "@src")
    val totalPath  = ahref.size + imgSrc.size

    val ahrefDistinct  = ahref.distinct
    val imgSrcDistinct = imgSrc.distinct

    val uniquePath = getUniquePath(ahrefDistinct, imgSrcDistinct)
    val map        = getEncodedPaths(ahrefDistinct, imgSrcDistinct)
    val trackedDoc = getTrackedDoc(doc)

    PathTrackerResult(trackedDoc.toString(), map, uniquePath, totalPath)

  }

}

case class PathTrackerResult(trackedDoc: String, encodedPaths: Map[Int, String],
                             uniquePaths: Int, totalPaths: Int)


