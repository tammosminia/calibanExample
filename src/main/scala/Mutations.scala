import zio.RIO

case class Mutations(addCat: Cat => RIO[CatRepo, Cat])