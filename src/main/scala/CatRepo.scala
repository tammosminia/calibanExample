import Cat.{Color, Tail}
import zio.{RIO, Task, UIO, ZIO}

class CatRepo {
  //in memory database of cats. not threadsafe or reasonable
  var catDb: List[Cat] = List(Cat("Koffie", Color("grey"), Tail(Color("black"))), Cat("Minoes", Color("brown"), Tail(Color("White"))))

  def getAllCats: Task[List[Cat]] = ZIO.succeed(catDb)

  def getSpeed(name: String): UIO[Int] = ZIO.succeed(name.length)
}

object CatRepo {
  def getAllCats: RIO[CatRepo, List[Cat]] = ZIO.environmentWithZIO[CatRepo](_.get.getAllCats)

  def getSpeed(name: String): RIO[CatRepo, Int] = ZIO.environmentWithZIO[CatRepo](_.get.getSpeed(name))
}