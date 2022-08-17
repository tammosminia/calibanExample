import Cat.{Color, Pattern}
import zio.{RIO, Task, UIO, ZIO}

class CatRepo {
  //in memory database of cats. not threadsafe or reasonable
  var catDb: List[Cat] = List(Cat("Koffie", Color("grey"), Pattern.Stripes), Cat("Minoes", Color("brown"), Pattern.Plain))

  def getAllCats: Task[List[Cat]] = ZIO.succeed(catDb)

  def similar(cat: Cat): Task[List[Cat]] = Task {
    catDb.filter(x => x != cat && x.color == cat.color)
  }

  def addCat(cat: Cat): Task[Cat] = Task {
    catDb = cat :: catDb
    cat
  }
}

/** some convenience methods to use in the API */
object CatRepo {
  def getAllCats: RIO[CatRepo, List[Cat]] = ZIO.environmentWithZIO[CatRepo](_.get.getAllCats)

  def similar(cat: Cat): RIO[CatRepo, List[Cat]] = ZIO.environmentWithZIO[CatRepo](_.get.similar(cat))

  def addCat(cat: Cat): RIO[CatRepo, Cat] = ZIO.environmentWithZIO[CatRepo](_.get.addCat(cat))
}
