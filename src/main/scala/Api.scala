import Api._
import Cat.Color
import caliban.Value.StringValue
import caliban.schema.{ArgBuilder, Schema}
import caliban.{AkkaHttpAdapter, CalibanError, GraphQL, GraphQLInterpreter, RootResolver}
import zio.{RIO, Runtime, ULayer, ZIO, ZLayer}
import sttp.tapir.json.circe._

class Api {
  val catRepoLayer: ULayer[CatRepo] = ZLayer.succeed(new CatRepo())
  implicit val runtime: Runtime[CatRepo] = zio.Runtime.unsafeFromLayer(catRepoLayer)
  val i = runtime.unsafeRun(createInterpreter)

  def route = {
    AkkaHttpAdapter.makeHttpService(i)
  }

  def createInterpreter: ZIO[CatRepo, CalibanError.ValidationError, GraphQLInterpreter[CatRepo, CalibanError]] = {
    import ApiSchema._
    implicit val colorSchema: Schema[Any, Color] = scalarSchema[Color]("CatColor", Some("the color of the cat"), None, c => StringValue(c.s))
    implicit val catSchema: Schema[CatRepo, Cat] = obj[CatRepo, Cat]("Cat")(
      implicit ft =>
        List(
          field("name")(_.name),
          field("color")(_.color),
          field("tail")(_.tail),
          field("speed")(cat => CatRepo.getSpeed(cat.name))
        )
    )
    implicit val queriesSchema: Schema[CatRepo, Queries] = obj[CatRepo, Queries]("Queries", Some("manually made queries"))(
      implicit ft =>
        List(
          field[Queries]("cats")(_.cats) //(functionSchema(ArgBuilder.unit, unitSchema, effectSchema(Cat.gqlSchema)), ft)
        )
    )
    GraphQL.graphQL[CatRepo, Queries, Unit, Unit](
      RootResolver(
        Queries(
          _ => CatRepo.getAllCats
        )
      )
    ).interpreter
  }

}

object Api {
  case class Queries(
                      cats: Unit => RIO[CatRepo, List[Cat]]
                    )
}
