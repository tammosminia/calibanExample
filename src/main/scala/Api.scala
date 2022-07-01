import Api.Queries
import caliban.{AkkaHttpAdapter, CalibanError, GraphQL, GraphQLInterpreter, RootResolver}
import zio.ZIO
import sttp.tapir.json.circe._

case class Cat(name: String)

class Api {
  implicit val runtime = zio.Runtime.default

  def route = {
    val i = runtime.unsafeRun(createInterpreter)
    AkkaHttpAdapter.makeHttpService(i)
  }

  def createInterpreter: ZIO[Any, CalibanError.ValidationError, GraphQLInterpreter[Any, CalibanError]] = {
    GraphQL.graphQL[Any, Queries, Unit, Unit](
      RootResolver(
        Queries(
          () => getAllCats
        )
      )
    ).interpreter
  }

  def getAllCats: List[Cat] = List(Cat("Koffie"), Cat("Minoes"))
}

object Api {
  case class Queries(cats: () => List[Cat])
}
