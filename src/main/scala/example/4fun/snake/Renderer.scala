package example.`4fun`.snake

object Renderer {
  implicit class StringToCharMap(s: String) {
    def toCharMap = Renderer.toCharMap2(s)
  }

  def toCharMap1(text: String): Map[Vec2d, Char] = {
    def parseLine(line: String, rowNum: Int) = {
      line
        .foldLeft[(Map[Vec2d, Char], Int)]((Map(), 0)) { (acc, ch) =>
          val (mp, col) = acc
          val nextCol = col + 1
          val nextMap =
            if (ch.isWhitespace)
              mp
            else
              mp + (Vec2d(col, rowNum) -> ch)

          (nextMap, nextCol)
        }
        ._1
    }

    text
      .split('\n')
      .zipWithIndex
      .map {
        _ match {
          case (line, idx) => parseLine(line, idx)
        }
      }
      .foldLeft(Map[Vec2d, Char]())((acc, mp) => acc ++ mp)
  }

  def toCharMap2(text: String): Map[Vec2d, Char] = {
    def parseChar(txt: String, col: Int = 0, row: Int = 0, res: Map[Vec2d, Char] = Map()): Map[Vec2d, Char] = {
      val currentChar = txt.take(1)
      val restText = txt.drop(1)
      currentChar match {
        case ""   => res
        case "\n" => parseChar(restText, 0, row + 1, res)
        case str =>
          val ch = str(0)
          val nextRes = if (ch.isWhitespace) res else res + (Vec2d(col, row) -> ch)
          parseChar(restText, col + 1, row, nextRes)
      }
    }

    parseChar(text)
  }

  def toText(charMap: Map[Vec2d, Char]): String = "xd"
}
