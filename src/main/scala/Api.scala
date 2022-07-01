import Api._
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
    //    implicit val queriesSchema: Schema[CatRepo, Queries] = ApiSchema.genMacro[Queries].schema
    implicit val queriesSchema: Schema[CatRepo, Queries] = obj[CatRepo, Queries]("Queries", Some("manually made queries"))(
      implicit ft =>
        List(
          field[Queries]("cat")(_.cat)(functionSchema[CatRepo, CatRepo, Unit, RIO[CatRepo, Cat]](ArgBuilder.unit, unitSchema, effectSchema[CatRepo, CatRepo, CatRepo, Throwable, Cat](Cat.gqlSchema)), ft)
        )
    )
    GraphQL.graphQL[CatRepo, Queries, Unit, Unit](
      RootResolver(
        Queries(
          Unit => ZIO.succeed(Cat("anders")) //CatRepo.getAllCats
        )
      )
    ).interpreter
  }

}

object Api {
  case class Queries(cat: Unit => RIO[CatRepo, Cat])
}
