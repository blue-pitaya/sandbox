package example.`4fun`.snake

import Renderer.StringToCharMap

object Boards {
  val default: Map[Vec2d, Char] = """
  |##########
  |#        #
  |#        #
  |#        #
  |#        #
  |#        #
  |#        #
  |#        #
  |#        #
  |#        #
  |#        #
  |##########
  """.stripMargin.trim().toCharMap
}
