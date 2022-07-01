import zio.{RIO, Task, ZIO}

class CatRepo {
  //in memory database of cats. not threadsafe or reasonable
  var catDb: List[Cat] = List(Cat("Koffie"), Cat("Minoes"))

  def getAllCats: Task[List[Cat]] = ZIO.succeed(catDb)
}

object CatRepo {
  def getAllCats: RIO[CatRepo, List[Cat]] = ZIO.environmentWithZIO[CatRepo](_.get.getAllCats)

}