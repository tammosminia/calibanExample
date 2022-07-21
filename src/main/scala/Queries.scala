import zio.RIO

case class Queries(cats: () => RIO[CatRepo, List[Cat]])