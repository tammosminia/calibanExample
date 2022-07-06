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

}
