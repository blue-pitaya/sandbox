package example.extlibs.myslick

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.io.StdIn

class Users2(tag: Tag) extends Table[(Int, String, String)](tag, "users") {
  def id = column[Int]("id", O.PrimaryKey)
  def login = column[String]("login")
  def password = column[String]("password")

  override def * = (id, login, password)
}

//requires postgres istance
object Slick0 extends App {
  val users2 = TableQuery[Users2]
  // *** BETTER CREATE TABLES USING PLAIN SQL ***
  //val setup = DBIO.seq(
  //  (users2.schema).createIfNotExists
  //)
  //db.run(setup)
  val db = Database.forConfig("mydb")

  val setup2 = DBIO.seq(
    users2.map(c => (c.login, c.password)) += ("user", "pass"),
    users2.map(c => (c.login, c.password)) += ("user2", "pass2")
  )
  db.run(setup2)

  val q = for (c <- users2) yield c.login
  val a = q.result
  val f: Future[Seq[String]] = db.run(a)

  f.onComplete {
    case Success(s) => println(s"Result: $s")
    case Failure(t) => t.printStackTrace()
  }
}
