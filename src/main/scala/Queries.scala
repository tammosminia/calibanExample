import zio.RIO

case class Queries(cats: Unit => RIO[CatRepo, List[Cat]])