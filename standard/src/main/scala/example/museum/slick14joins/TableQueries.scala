package example.museum.slick14joins

import slick.lifted.TableQuery

object TableQueries {
  val items = TableQuery[Items]
  val itemInstances = TableQuery[ItemInstances]
  val heroes = TableQuery[Heroes]
}
