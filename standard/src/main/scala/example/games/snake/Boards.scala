package example.games.snake

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
